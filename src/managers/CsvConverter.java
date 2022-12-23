package managers;

import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.util.List;

/**
 * Класс включает в себя набор статических методов для сохранения менеджера задач и менеджера истории в CSV файл;
 * И для их восстановления из CSV;
 */
public class CsvConverter {

    public static Task fromString(String value) {
        Status status;
        String[] split = value.split(",");
        if (split[3].equals("NEW")) {
            status = Status.NEW;
        } else if (split[3].equals("IN_PROGRESS")) {
            status = Status.IN_PROGRESS;
        } else {
            status = Status.DONE;
        }
        if (split[1].equals("SUBTASK")) {
            return new SubTask(split[2], split[4], Integer.parseInt(split[0]), status, Integer.parseInt(split[5]));
        } else if (split[1].equals("EPIC")) {
            return new Epic(split[2], split[4], Integer.parseInt(split[0]), status);
        }
        return new Task(split[2], split[4], Integer.parseInt(split[0]), status);
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
        if (task instanceof Epic) {
            taskType = TaskType.EPIC;
        } else if (task instanceof SubTask) {
            taskType = TaskType.SUBTASK;
            epicId += ((SubTask) task).getEpicId();
        } else {
            taskType = TaskType.TASK;
        }
        return String.format("%d,%s,%s,%s,%s,%s", task.getId(), taskType, task.getName(), task.getStatus(),
                task.getDescription(), epicId);
    }
}
