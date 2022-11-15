package AllManagers;

import Tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private List<Task> tasksHistory = new ArrayList<>();

    @Override
    public void add(Task task) {
        tasksHistory.add(task);
    }

    /**
     * Метод возвращает последние 10 просмотренных задач;
     * Если просматривалось больше 10 задач, в tasksHistory удаляются старые;
     */
    @Override
    public List<Task> getHistory() {
        int count = tasksHistory.size();
        if (tasksHistory.isEmpty()) {
            System.out.println("Вы еще не просматривали задачи");
            return null;
        } else if (tasksHistory.size() > 10) {
            for (int i = 0; i < count - 10; i++) {
                tasksHistory.remove(i);
            }
        }
        System.out.println("Последние просмотренные задачи:");
        return tasksHistory;
    }
}
