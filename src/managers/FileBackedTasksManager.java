package managers;

import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
    File saving;

    public FileBackedTasksManager(File saving) {
        this.saving = saving;
    }

    private void save() {
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubTask(SubTask task) {
        super.addSubTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubTask(SubTask task) {
        super.updateSubTask(task);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return super.getAllTasks();
    }

    @Override
    public ArrayList<SubTask> getAllSubTasks() {
        return super.getAllSubTasks();
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        return super.getAllEpics();
    }

    @Override
    public Task getTask(int taskId) {
        return super.getTask(taskId);
    }

    @Override
    public Epic getEpic(int epicId) {
        return super.getEpic(epicId);
    }

    @Override
    public SubTask getSubTask(int subTaskId) {
        return super.getSubTask(subTaskId);
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    @Override
    public void removeAllSubTasks() {
        super.removeAllSubTasks();
        save();
    }

    @Override
    public void removeTask(int taskId) {
        super.removeTask(taskId);
        save();
    }

    @Override
    public void removeEpic(int epicId) {
        super.removeEpic(epicId);
        save();
    }

    @Override
    public void removeSubTask(int subTaskId) {
        super.removeSubTask(subTaskId);
        save();
    }

    @Override
    public void printSubTasksOfEpic(int epicId) {
        super.printSubTasksOfEpic(epicId);
        save();
    }

    private String toString(Task task) {
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

    private Task fromString(String value) {
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
        }
        return new Task(split[2], split[4], Integer.parseInt(split[0]), status);
    }

    public static String historyToString(HistoryManager manager) {
        StringBuilder historyIds = new StringBuilder();
        for (Task task : manager.getHistory()) {
            historyIds.append(task.getId() + ",");
        }
        historyIds.deleteCharAt(historyIds.length() - 1);
        return historyIds.toString();

    }

    public static List<Integer> historyFromString(String value) {
        String[] split = value.split(",");
        Integer[] history = new Integer[split.length];
        for (int i = 0; i < split.length; i++) {
            history[i] = Integer.parseInt(split[i]);
        }
        return List.of(history);
    }
}
