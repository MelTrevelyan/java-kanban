package managers.filebacked;

import managers.HistoryManager;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static tasks.Task.FORMATTER;

/**
 * Класс включает в себя набор статических методов для сохранения менеджера задач и менеджера истории в CSV файл;
 * И для их восстановления из CSV;
 */
public class CsvConverter {

    public static Task fromString(String value) {
        Status status;
        ArrayList<Integer> subsId = new ArrayList<>();
        String[] split = value.split(",");
        if (split[3].equals("NEW")) {
            status = Status.NEW;
        } else if (split[3].equals("IN_PROGRESS")) {
            status = Status.IN_PROGRESS;
        } else {
            status = Status.DONE;
        }
        if (split[1].equals("SUBTASK")) {
            return new SubTask(split[2], split[4], Integer.parseInt(split[0]), status,
                    Duration.ofMinutes(Long.parseLong(split[5])),
                    LocalDateTime.parse(split[6], FORMATTER), Integer.parseInt(split[7]));
        } else if (split[1].equals("EPIC")) {
            if (split.length > 7) {
                String[] subsStringId = split[7].split("/");
                for (String id : subsStringId) {
                    subsId.add(Integer.parseInt(id));
                }
            }
            Epic epic = new Epic(split[2], split[4], Integer.parseInt(split[0]), status,
                    Duration.ofMinutes(Long.parseLong(split[5])), LocalDateTime.parse(split[6], FORMATTER));
            epic.setSubTaskIds(subsId);
            epic.setEndTime(LocalDateTime.parse(split[6], FORMATTER).plus(Duration.ofMinutes(Long
                    .parseLong(split[5]))));
            return epic;
        }
        return new Task(split[2], split[4], Integer.parseInt(split[0]), status,
                Duration.ofMinutes(Long.parseLong(split[5])),
                LocalDateTime.parse(split[6], FORMATTER));
    }

    public static String historyToString(HistoryManager manager) {
        String[] historyIds = new String[manager.getHistory().size()];
        for (int i = 0; i < manager.getHistory().size(); i++) {
            Integer id = manager.getHistory().get(i).getId();
            historyIds[i] = id.toString();
        }
        return String.join(",", historyIds);
    }

    public static List<Integer> historyFromString(String value) {
        String[] split = value.split(",");
        Integer[] history = new Integer[split.length];
        for (int i = 0; i < split.length; i++) {
            history[i] = Integer.parseInt(split[i]);
        }
        return List.of(history);
    }

    public static String toString(Task task) {
        TaskType taskType;
        String epicId = "";
        StringBuilder sb = new StringBuilder();
        if (task instanceof Epic) {
            taskType = TaskType.EPIC;
            for (Integer id : ((Epic) task).getSubTaskIds()) {
                sb.append(id + "/");
            }
            epicId = sb.toString();
        } else if (task instanceof SubTask) {
            taskType = TaskType.SUBTASK;
            epicId += ((SubTask) task).getEpicId();
        } else {
            taskType = TaskType.TASK;
        }
        return String.format("%d,%s,%s,%s,%s,%d,%s,%s", task.getId(), taskType, task.getName(), task.getStatus(),
                task.getDescription(), task.getDuration().toMinutes(), task.getStartTime().format(FORMATTER), epicId);
    }
}
