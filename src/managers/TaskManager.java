package managers;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Интерфейс включает в себя основные методы менеджера задач;
 */
public interface TaskManager {

    /**
     * Метод устанавливает индивидуальный id обычной задаче;
     * Затем помещает её в tasksMap, где ключ - её id;
     *
     * @param task - готовая задача, поступающая извне;
     */
    void addTask(Task task);

    /**
     * Метод устанавливает индивидуальный id эпику;
     * Затем помещает его в epicTasksMap, где ключ - его id;
     *
     * @param epic - готовый эпик, поступающий извне;
     *             Устанавливаем подзадачам id их эпика;
     *             Обновляем статус эпика;
     */
    void addEpic(Epic epic);

    /**
     * Метод устанавливает индивидуальный id подзадаче;
     * Затем помещает её в subTasksMap, где ключ - её id;
     *
     * @param task - готовая подзадача, поступающая извне;
     */
    void addSubTask(SubTask task);

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
    void updateEpic(Epic epic);

    /**
     * Метод обновляет подзадачу в subTasksMap;
     * Устанавливаем новой подзадаче id эпика;
     * Заменяем подзадачу;
     * Обновляем статус эпика;
     *
     * @param task - обновлённая подзадача, существующая в subTasksMap;
     */
    void updateSubTask(SubTask task);

    /**
     * Метод обновляет задачу в tasksMap;
     *
     * @param task обновлённая задача, существующая в tasksMap;
     */
    void updateTask(Task task);

    /**
     * Метод возвращает список обычных задач;
     */
    ArrayList<Task> getAllTasks();

    /**
     * Метод возвращает список подзадач;
     */
    ArrayList<SubTask> getAllSubTasks();

    /**
     * Метод возвращает список эпиков;
     */
    ArrayList<Epic> getAllEpics();

    Task getTask(int taskId);

    Epic getEpic(int epicId);

    SubTask getSubTask(int subTaskId);

    /**
     * Метод полностью очищает обычные задачи в tasksMap;
     */
    void removeAllTasks();

    /**
     * Метод полностью очищает эпики и подзадачи в epicTasksMap и subTasksMap;
     */
    void removeAllEpics();

    /**
     * Метод полностью очищает подзадачи в subTasksMap;
     */
    void removeAllSubTasks();

    /**
     * Метод удаляет задачу по её id;
     *
     * @param taskId указывает, какую задачу удалить;
     */
    void removeTask(int taskId);

    /**
     * Метод удаляет эпик с его подзадачами;
     *
     * @param epicId указывает, какой эпик удалить;
     */
    void removeEpic(int epicId);

    /**
     * Метод удаляет подзадачу по её id;
     * Сначала удаляем у эпика id удаляемой подзадачи;
     * Затем удаляем подзадачу из subTasksMap;
     * Обновляем статус эпика;
     *
     * @param subTaskId указывает, какую подзадачу удалить;
     */
    void removeSubTask(int subTaskId);

    /**
     * Метод печатает название эпика и всех его подзадач;
     *
     * @param epicId указывает, про какой эпик нужно печатать информацию;
     */
    void printSubTasksOfEpic(int epicId);

    /**
     * Метод получения списка просмотренных задач из соответствующего менеджера истории;
     */
    List<Task> getHistory();
}
