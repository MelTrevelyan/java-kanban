package managers;

import tasks.Task;

import java.util.List;

/**
 * Интерфейс описывает методы менеджера истории просмотра задач;
 */
public interface HistoryManager {

    /**
     * Метод добавдяет просмотренную задачу в список tasksHistory;
     *
     * @param task обозначает добавляемую задачу;
     */
    void add(Task task);

    /**
     * Метод возвращает просмотренные задачи;
     */
    List<Task> getHistory();

    /**
     * Метод удаляет задачу из истории просмотров;
     * @param id обозначает какую задачу надо удалить;
     */
    void remove(int id);
}
