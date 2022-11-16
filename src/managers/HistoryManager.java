package managers;

import tasks.Task;

import java.util.List;

public interface HistoryManager {

    /**
     * Метод добавдяет просмотренную задачу в список tasksHistory;
     *
     * @param task обозначает добавляемую задачу;
     *             После добавления задачи, проверяем размер списка;
     *             Если размер больше 10, удаляем старые задачи из начала списка;
     */
    void add(Task task);

    /**
     * Метод возвращает последние 10 просмотренных задач;
     * Или сообщает, что список пустой;
     */
    List<Task> getHistory();
}
