import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {

    private int nextId = 1;
    private HashMap<Integer, Task> tasksMap = new HashMap<>();
    private HashMap<Integer, Epic> epicTasksMap = new HashMap<>();
    private HashMap<Integer, SubTask> subTasksMap = new HashMap<>();
    private String[] statuses = {"NEW", "IN_PROGRESS", "DONE"};

    public void addTask(Task task) {
        task.setId(nextId++);
        tasksMap.put(task.getId(), task);
    }

    public void addEpic(Epic epic) {
        epic.setId(nextId++);
        epicTasksMap.put(epic.getId(), epic);
        syncEpic(epic);
    }

    public void addSubTask(SubTask task) {
        task.setId(nextId++);
        subTasksMap.put(task.getId(), task);
    }

    private void syncEpic(Epic epic) {
        ArrayList<Integer> subTasks = epic.getSubTaskIds();
        ArrayList<String> subTasksStatuses = new ArrayList<>();
        int counter = 0;
        if (subTasks.isEmpty()) {
            epic.setStatus(statuses[0]);
            return;
        }
        for (int taskId : subTasks) {
            SubTask subTask = subTasksMap.get(taskId);
            subTask.setEpicId(epic.getId());
            subTasksStatuses.add(subTask.getStatus());
        }
        for (int i = 0; i < subTasks.size(); i++) {
            if (subTasksStatuses.get(i).equals(statuses[0])) {
                counter += 0;
            } else if (subTasksStatuses.get(i).equals(statuses[2])) {
                counter += 1;
            } else {
                epic.setStatus(statuses[1]);
                return;
            }
        }
        if (counter == 0) {
            epic.setStatus(statuses[0]);
        } else if (counter == subTasks.size()) {
            epic.setStatus(statuses[2]);
        } else {
            epic.setStatus(statuses[1]);
        }
    }


    public void updateEpic(Epic epic) {
        Epic oldEpic = getEpic(epic.getId());
        ArrayList<Integer> oldEpicSubsIds = oldEpic.getSubTaskIds();
        epicTasksMap.put(epic.getId(), epic);
        epic.setSubTaskIds(oldEpicSubsIds);
        syncEpic(epic);
    }

    public void updateSubTask(SubTask task) {
        SubTask oldSubTask = getSubTask(task.getId());
        task.setEpicId(oldSubTask.getEpicId());
        subTasksMap.put(task.getId(), task);
        Epic epic = epicTasksMap.get(task.getEpicId());
        syncEpic(epic);
    }

    public void updateTask(Task task) {
        tasksMap.put(task.getId(), task);
    }

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasksMap.values());
    }

    public ArrayList<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasksMap.values());
    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epicTasksMap.values());
    }

    public Task getTask(int taskId) {
        return tasksMap.get(taskId);
    }

    public Epic getEpic(int epicId) {
        return epicTasksMap.get(epicId);
    }

    public SubTask getSubTask(int SubTaskId) {
        return subTasksMap.get(SubTaskId);
    }

    public void removeAllTasks() {
        tasksMap.clear();
    }

    public void removeAllEpics() {
        epicTasksMap.clear();
        removeAllSubTasks();
    }

    public void removeAllSubTasks() {
        subTasksMap.clear();
        for (Epic epic : epicTasksMap.values()) {
            syncEpic(epic);
        }
    }

    public void removeTask(int taskId) {
        tasksMap.remove(taskId);
    }


    public void removeEpic(int epicId) {
        Epic epic = epicTasksMap.get(epicId);
        for (int taskId : epic.getSubTaskIds()) {
            subTasksMap.remove(taskId);
        }
        epicTasksMap.remove(epicId);
    }

    public void removeSubTask(int subTaskId) {
        int epicId = subTasksMap.get(subTaskId).getEpicId();
        Epic epic = getEpic(epicId);
        epic.removeTaskId(subTaskId);
        subTasksMap.remove(subTaskId);
        syncEpic(epicTasksMap.get(epicId));
    }

    public void printSubTasksOfEpic(int epicId) {
        System.out.println("Эпик: " + epicTasksMap.get(epicId).getName() + ". Его подзадачи: ");
        for (int taskId : epicTasksMap.get(epicId).getSubTaskIds()) {
            System.out.println(subTasksMap.get(taskId).getName());
        }
    }

    @Override
    public String toString() {
        return "Manager{" +
                "tasksMap=" + tasksMap +
                ", epicTasksMap=" + epicTasksMap +
                ", subTasksMap=" + subTasksMap +
                '}';
    }
}
