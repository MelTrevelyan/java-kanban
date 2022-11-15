package AllManagers;

import Tasks.Epic;
import Tasks.Statuses;
import Tasks.SubTask;
import Tasks.Task;

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

    /**
     * Метод устанавливает индивидуальный id обычной задаче;
     * Затем помещает её в tasksMap, где ключ - её id;
     *
     * @param task - готовая задача, поступающая извне;
     */
    @Override
    public void addTask(Task task) {
        task.setId(nextId++);
        tasksMap.put(task.getId(), task);
    }

    /**
     * Метод устанавливает индивидуальный id эпику;
     * Затем помещает его в epicTasksMap, где ключ - его id;
     *
     * @param epic - готовый эпик, поступающий извне;
     *             Устанавливаем подзадачам id их эпика;
     *             Обновляем статус эпика;
     */
    @Override
    public void addEpic(Epic epic) {
        epic.setId(nextId++);
        epicTasksMap.put(epic.getId(), epic);
        syncEpic(epic);
    }

    /**
     * Метод устанавливает индивидуальный id подзадаче;
     * Затем помещает её в subTasksMap, где ключ - её id;
     *
     * @param task - готовая подзадача, поступающая извне;
     */
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
        ArrayList<Statuses> subTasksStatuses = new ArrayList<>();
        int counter = 0;
        if (subTasks.isEmpty()) {
            epic.setStatus(Statuses.NEW);
            return;
        }
        for (int taskId : subTasks) {
            SubTask subTask = subTasksMap.get(taskId);
            subTask.setEpicId(epic.getId());
            subTasksStatuses.add(subTask.getStatus());
        }
        for (int i = 0; i < subTasks.size(); i++) {
            if (subTasksStatuses.get(i) == Statuses.NEW) {
                counter += 0;
            } else if (subTasksStatuses.get(i) == Statuses.DONE) {
                counter += 1;
            } else {
                epic.setStatus(Statuses.IN_PROGRESS);
                return;
            }
        }
        if (counter == 0) {
            epic.setStatus(Statuses.NEW);
        } else if (counter == subTasks.size()) {
            epic.setStatus(Statuses.DONE);
        } else {
            epic.setStatus(Statuses.IN_PROGRESS);
        }
    }

    /**
     * Метод обновляет эпик в epicTasksMap;
     * По id находим старый эпик;
     * Сохраняем список его подзадач;
     * Заменяем старый эпик новым;
     * Добавляем новому эпику подзадачи старого;
     * Обновляем статус эпика;
     *
     * @param epic - изменённый эпик, существующий в epicTasksMap;
     */
    @Override
    public void updateEpic(Epic epic) {
        Epic oldEpic = epicTasksMap.get(epic.getId());
        ArrayList<Integer> oldEpicSubsIds = oldEpic.getSubTaskIds();
        epicTasksMap.put(epic.getId(), epic);
        epic.setSubTaskIds(oldEpicSubsIds);
        syncEpic(epic);
    }

    /**
     * Метод обновляет подзадачу в subTasksMap;
     * Устанавливаем новой подзадаче id эпика;
     * Заменяем подзадачу;
     * Обновляем статус эпика;
     *
     * @param task - обновлённая подзадача, существующая в subTasksMap;
     */
    @Override
    public void updateSubTask(SubTask task) {
        SubTask oldSubTask = subTasksMap.get(task.getId());
        task.setEpicId(oldSubTask.getEpicId());
        subTasksMap.put(task.getId(), task);
        Epic epic = epicTasksMap.get(task.getEpicId());
        syncEpic(epic);
    }

    /**
     * Метод обновляет задачу в tasksMap;
     *
     * @param task обновлённая задача, существующая в tasksMap;
     */
    @Override
    public void updateTask(Task task) {
        tasksMap.put(task.getId(), task);
    }

    /**
     * Метод возвращает список обычных задач;
     */
    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasksMap.values());
    }

    /**
     * Метод возвращает список подзадач;
     */
    @Override
    public ArrayList<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasksMap.values());
    }

    /**
     * Метод возвращает список эпиков;
     */
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

    /**
     * Метод полностью очищает обычные задачи в tasksMap;
     */
    @Override
    public void removeAllTasks() {
        tasksMap.clear();
    }

    /**
     * Метод полностью очищает эпики и подзадачи в epicTasksMap и subTasksMap;
     */
    @Override
    public void removeAllEpics() {
        epicTasksMap.clear();
        removeAllSubTasks();
    }

    /**
     * Метод полностью очищает подзадачи в subTasksMap;
     */
    @Override
    public void removeAllSubTasks() {
        subTasksMap.clear();
        for (Epic epic : epicTasksMap.values()) {
            syncEpic(epic);
        }
    }

    /**
     * Метод удаляет задачу по её id;
     *
     * @param taskId указывает, какую задачу удалить;
     */
    @Override
    public void removeTask(int taskId) {
        tasksMap.remove(taskId);
    }

    /**
     * Метод удаляет эпик с его подзадачами;
     *
     * @param epicId указывает, какой эпик удалить;
     */
    @Override
    public void removeEpic(int epicId) {
        Epic epic = epicTasksMap.get(epicId);
        for (int taskId : epic.getSubTaskIds()) {
            subTasksMap.remove(taskId);
        }
        epicTasksMap.remove(epicId);
    }

    /**
     * Метод удаляет подзадачу по её id;
     * Сначала удаляем у эпика id удаляемой подзадачи;
     * Затем удаляем подзадачу из subTasksMap;
     * Обновляем статус эпика;
     *
     * @param subTaskId указывает, какую подзадачу удалить;
     */
    @Override
    public void removeSubTask(int subTaskId) {
        int epicId = subTasksMap.get(subTaskId).getEpicId();
        Epic epic = epicTasksMap.get(epicId);
        epic.removeTaskId(subTaskId);
        subTasksMap.remove(subTaskId);
        syncEpic(epicTasksMap.get(epicId));
    }

    /**
     * Метод печатает название эпика и всех его подзадач;
     *
     * @param epicId указывает, про какой эпик нужно печатать информацию;
     */
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
