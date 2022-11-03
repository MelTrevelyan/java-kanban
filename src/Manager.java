import java.util.ArrayList;
import java.util.HashMap;

public class Manager {

    protected int nextId = 1;
    protected HashMap<Integer, Task> tasksMap = new HashMap<>();
    protected HashMap<Integer, Epic> epicTasksMap = new HashMap<>();
    protected HashMap<Integer, SubTask> subTasksMap = new HashMap<>();
    String[] statuses = {"NEW", "IN_PROGRESS", "DONE"};

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
        for (int i = 0; i <subTasks.size(); i++) {
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
        Epic epic = epicTasksMap.get(task.epicId);
        syncEpic(epic);
    }

    public void updateTask(Task task) {
        tasksMap.put(task.getId(), task);
    }

    public void printAllTasks() {
        System.out.println("Список обычных задач:");
        for(Task task : tasksMap.values()) {
            System.out.println(task.name);
        }
    }

    public void printAllEpics() {
        System.out.println("Список эпиков:");
        for(Epic epic : epicTasksMap.values()) {
            System.out.println(epic.name);
        }
    }

    public void printAllSubTasks() {
        System.out.println("Список всех подзадач:");
        for(SubTask task : subTasksMap.values()) {
            System.out.println(task.name);
        }
    }

    public Task getTask(int taskId) {
        Task task = tasksMap.get(taskId);
        return task;
    }

    public Epic getEpic(int epicId) {
        Epic epic = epicTasksMap.get(epicId);
        return epic;
    }

    public SubTask getSubTask(int SubTaskId) {
        SubTask task = subTasksMap.get(SubTaskId);
        return task;
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
        for(Epic epic : epicTasksMap.values()) {
            syncEpic(epic);
        }
    }

    public void removeTask(int taskId) {
        tasksMap.remove(taskId);
    }


    public void removeEpic(int epicId) {
        Epic epic = epicTasksMap.get(epicId);
        for(int taskId : epic.getSubTaskIds()) {
            subTasksMap.remove(taskId);
        }
        epicTasksMap.remove(epicId);
    }

    public void removeSubTask(int subTaskId) {
        int epicId = subTasksMap.get(subTaskId).getEpicId();
        Epic epic = getEpic(epicId);
        epic.removeTaskId(subTaskId);
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
