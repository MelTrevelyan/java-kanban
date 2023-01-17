package managers.inmemory;

import exception.ManagerValidateException;
import managers.HistoryManager;
import managers.Managers;
import managers.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Дарья Толстоногова
 * Этот класс включает в себя основную логику работы трекера задач
 */
public class InMemoryTaskManager implements TaskManager {

    protected int nextId = 1;
    protected final HashMap<Integer, Task> tasksMap = new HashMap<>();
    protected final HashMap<Integer, Epic> epicTasksMap = new HashMap<>();
    protected final HashMap<Integer, SubTask> subTasksMap = new HashMap<>();
    protected Comparator<Task> comparator = (o1, o2) -> {
        if (o1.getStartTime().isAfter(o2.getStartTime())) {
            return 1;
        } else if (o1.equals(o2)) {
            return 0;
        }
        return -1;
    };
    protected final Set<Task> anyTypeTasks = new TreeSet<>(comparator);

    protected HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public Set<Task> getPrioritizedTasks() {
        return anyTypeTasks;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void addTask(Task task) {
        task.setId(nextId++);
        validate(task);
        tasksMap.put(task.getId(), task);
        anyTypeTasks.add(task);
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(nextId++);
        epicTasksMap.put(epic.getId(), epic);
        anyTypeTasks.add(epic);
        syncEpic(epic);
    }

    @Override
    public void addSubTask(SubTask task) {
        task.setId(nextId++);
        validate(task);
        subTasksMap.put(task.getId(), task);
        anyTypeTasks.add(task);
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
        setEpicDuration(epic);
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

    private void setEpicDuration(Epic epic) {
        ArrayList<Integer> subTasks = epic.getSubTaskIds();
        Duration epicDuration = Duration.ZERO;
        LocalDateTime earliestStartTime = LocalDateTime.MAX;
        LocalDateTime latestEndTime = LocalDateTime.MIN;
        if (!subTasks.isEmpty()) {
            for (int taskId : subTasks) {
                SubTask subTask = subTasksMap.get(taskId);
                if (subTask.getStartTime().isBefore(earliestStartTime)) {
                    earliestStartTime = subTask.getStartTime();
                }
                if (subTask.getEndTime().isAfter(latestEndTime)) {
                    latestEndTime = subTask.getEndTime();
                }
            }
            epicDuration = Duration.between(earliestStartTime, latestEndTime);
            epic.setStartTime(earliestStartTime);
            epic.setEndTime(latestEndTime);
        } else {
            epic.setStartTime(LocalDateTime.MAX);
            epic.setEndTime(LocalDateTime.MAX);
        }
        epic.setDuration(epicDuration);
    }

    private void validate(Task task) {
        List<Task> tasks = new ArrayList<>(getPrioritizedTasks());
        if (!tasks.isEmpty()) {
            for (Task listTask : tasks) {
                if (task.getStartTime().isBefore(listTask.getStartTime())
                        && task.getEndTime().isBefore(listTask.getStartTime())
                        || task.getStartTime().isAfter(listTask.getEndTime())
                        && task.getEndTime().isAfter(listTask.getEndTime())) {
                } else {
                    throw new ManagerValidateException();
                }
            }
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        Epic oldEpic = epicTasksMap.get(epic.getId());
        ArrayList<Integer> oldEpicSubsIds = oldEpic.getSubTaskIds();
        epic.setSubTaskIds(oldEpicSubsIds);
        syncEpic(epic);
        anyTypeTasks.remove(oldEpic);
        anyTypeTasks.add(epic);
        epicTasksMap.put(epic.getId(), epic);
    }

    @Override
    public void updateSubTask(SubTask task) {
        SubTask oldSubTask = subTasksMap.get(task.getId());
        task.setEpicId(oldSubTask.getEpicId());
        validate(task);
        if (oldSubTask.getEpicId() != 0) {
            Epic epic = epicTasksMap.get(task.getEpicId());
            syncEpic(epic);
        }
        anyTypeTasks.remove(oldSubTask);
        anyTypeTasks.add(task);
        subTasksMap.put(task.getId(), task);
    }

    @Override
    public void updateTask(Task task) {
        Task oldTask = tasksMap.get(task.getId());
        validate(task);
        anyTypeTasks.remove(oldTask);
        anyTypeTasks.add(task);
        tasksMap.put(task.getId(), task);
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        for (Task task : tasksMap.values()) {
            historyManager.add(task);
        }
        return new ArrayList<>(tasksMap.values());
    }

    @Override
    public ArrayList<SubTask> getAllSubTasks() {
        for (SubTask task : subTasksMap.values()) {
            historyManager.add(task);
        }
        return new ArrayList<>(subTasksMap.values());
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        for (Epic epic : epicTasksMap.values()) {
            historyManager.add(epic);
        }
        return new ArrayList<>(epicTasksMap.values());
    }

    @Override
    public Task getTask(int taskId) {
        historyManager.add(tasksMap.get(taskId));
        return tasksMap.get(taskId);
    }

    @Override
    public Epic getEpic(int epicId) {
        historyManager.add(epicTasksMap.get(epicId));
        return epicTasksMap.get(epicId);
    }

    @Override
    public SubTask getSubTask(int subTaskId) {
        historyManager.add(subTasksMap.get(subTaskId));
        return subTasksMap.get(subTaskId);
    }

    @Override
    public void removeAllTasks() {
        for (int id : tasksMap.keySet()) {
            historyManager.remove(id);
        }
        tasksMap.clear();
    }

    @Override
    public void removeAllEpics() {
        for (int epicId : epicTasksMap.keySet()) {
            historyManager.remove(epicId);
        }
        epicTasksMap.clear();
        for (int subId : subTasksMap.keySet()) {
            historyManager.remove(subId);
        }
        removeAllSubTasks();
    }

    @Override
    public void removeAllSubTasks() {
        ArrayList<Integer> emptyList = new ArrayList<>(0);
        for (int subId : subTasksMap.keySet()) {
            historyManager.remove(subId);
        }
        subTasksMap.clear();
        for (Epic epic : epicTasksMap.values()) {
            epic.setSubTaskIds(emptyList);
            syncEpic(epic);
        }
    }

    @Override
    public void removeTask(int taskId) {
        anyTypeTasks.remove(tasksMap.remove(taskId));
        historyManager.remove(taskId);
    }

    @Override
    public void removeEpic(int epicId) {
        Epic epic = epicTasksMap.get(epicId);
        anyTypeTasks.remove(epic);
        for (int taskId : epic.getSubTaskIds()) {
            anyTypeTasks.remove(subTasksMap.remove(taskId));
            historyManager.remove(taskId);
        }
        epicTasksMap.remove(epicId);
        historyManager.remove(epicId);
    }

    @Override
    public void removeSubTask(int subTaskId) {
        int epicId = subTasksMap.get(subTaskId).getEpicId();
        Epic epic = epicTasksMap.get(epicId);
        epic.removeTaskId(subTaskId);
        syncEpic(epic);
        anyTypeTasks.remove(subTasksMap.remove(subTaskId));
        historyManager.remove(subTaskId);
    }

    @Override
    public void printSubTasksOfEpic(int epicId) {
        System.out.println("Эпик: " + epicTasksMap.get(epicId).getName() + ". Его подзадачи: ");
        for (int taskId : epicTasksMap.get(epicId).getSubTaskIds()) {
            System.out.println(subTasksMap.get(taskId).getName());
        }
    }
}