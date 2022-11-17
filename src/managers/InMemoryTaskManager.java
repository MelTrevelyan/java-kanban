package managers;

import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Дарья Толстоногова
 * Этот класс включает в себя основную логику работы трекера задач
 */
public class InMemoryTaskManager implements TaskManager {

    private int nextId = 1;
    private final HashMap<Integer, Task> tasksMap = new HashMap<>();
    private final HashMap<Integer, Epic> epicTasksMap = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasksMap = new HashMap<>();

    @Override
    public void addTask(Task task) {
        task.setId(nextId++);
        tasksMap.put(task.getId(), task);
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(nextId++);
        epicTasksMap.put(epic.getId(), epic);
        syncEpic(epic);
    }

    @Override
    public void addSubTask(SubTask task) {
        task.setId(nextId++);
        subTasksMap.put(task.getId(), task);
    }

    /**
     * Метод сохранения id эпика у подзадач и обновления статуса эпика;
     *
     * @param epic - эпик, который мы обновляем;
     *             Для выбора статуса используем counter;
     */
    private void syncEpic(Epic epic) {
        ArrayList<Integer> subTasks = epic.getSubTaskIds();
        ArrayList<Status> subTasksStatuses = new ArrayList<>();
        int counter = 0;
        if (subTasks.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }
        for (int taskId : subTasks) {
            SubTask subTask = subTasksMap.get(taskId);
            subTask.setEpicId(epic.getId());
            subTasksStatuses.add(subTask.getStatus());
        }
        for (int i = 0; i < subTasks.size(); i++) {
            if (subTasksStatuses.get(i) == Status.NEW) {
                counter += 0;
            } else if (subTasksStatuses.get(i) == Status.DONE) {
                counter += 1;
            } else {
                epic.setStatus(Status.IN_PROGRESS);
                return;
            }
        }
        if (counter == 0) {
            epic.setStatus(Status.NEW);
        } else if (counter == subTasks.size()) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        Epic oldEpic = epicTasksMap.get(epic.getId());
        ArrayList<Integer> oldEpicSubsIds = oldEpic.getSubTaskIds();
        epicTasksMap.put(epic.getId(), epic);
        epic.setSubTaskIds(oldEpicSubsIds);
        syncEpic(epic);
    }

    @Override
    public void updateSubTask(SubTask task) {
        SubTask oldSubTask = subTasksMap.get(task.getId());
        task.setEpicId(oldSubTask.getEpicId());
        subTasksMap.put(task.getId(), task);
        Epic epic = epicTasksMap.get(task.getEpicId());
        syncEpic(epic);
    }

    @Override
    public void updateTask(Task task) {
        tasksMap.put(task.getId(), task);
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasksMap.values());
    }

    @Override
    public ArrayList<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasksMap.values());
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epicTasksMap.values());
    }

    @Override
    public Task getTask(int taskId) {
        Managers.historyManager.add(tasksMap.get(taskId));
        return tasksMap.get(taskId);
    }

    @Override
    public Epic getEpic(int epicId) {
        Managers.historyManager.add(epicTasksMap.get(epicId));
        return epicTasksMap.get(epicId);
    }

    @Override
    public SubTask getSubTask(int SubTaskId) {
        Managers.historyManager.add(subTasksMap.get(SubTaskId));
        return subTasksMap.get(SubTaskId);
    }

    @Override
    public void removeAllTasks() {
        tasksMap.clear();
    }

    @Override
    public void removeAllEpics() {
        epicTasksMap.clear();
        removeAllSubTasks();
    }

    @Override
    public void removeAllSubTasks() {
        subTasksMap.clear();
        for (Epic epic : epicTasksMap.values()) {
            syncEpic(epic);
        }
    }

    @Override
    public void removeTask(int taskId) {
        tasksMap.remove(taskId);
    }

    @Override
    public void removeEpic(int epicId) {
        Epic epic = epicTasksMap.get(epicId);
        for (int taskId : epic.getSubTaskIds()) {
            subTasksMap.remove(taskId);
        }
        epicTasksMap.remove(epicId);
    }

    @Override
    public void removeSubTask(int subTaskId) {
        int epicId = subTasksMap.get(subTaskId).getEpicId();
        Epic epic = epicTasksMap.get(epicId);
        epic.removeTaskId(subTaskId);
        subTasksMap.remove(subTaskId);
        syncEpic(epicTasksMap.get(epicId));
    }

    @Override
    public void printSubTasksOfEpic(int epicId) {
        System.out.println("Эпик: " + epicTasksMap.get(epicId).getName() + ". Его подзадачи: ");
        for (int taskId : epicTasksMap.get(epicId).getSubTaskIds()) {
            System.out.println(subTasksMap.get(taskId).getName());
        }
    }

    @Override
    public String toString() {
        return "Managers.Manager{" +
                "tasksMap=" + tasksMap +
                ", epicTasksMap=" + epicTasksMap +
                ", subTasksMap=" + subTasksMap +
                '}';
    }
}