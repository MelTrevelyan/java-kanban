package managers;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private List<Task> tasksHistory = new ArrayList<>();

    @Override
    public void add(Task task) {
        tasksHistory.add(task);
        int count = tasksHistory.size();
        if (tasksHistory.size() > 10) {
            for (int i = 0; i < count - 10; i++) {
                tasksHistory.remove(i);
            }
        }
    }

    @Override
    public List<Task> getHistory() {
        if (tasksHistory.isEmpty()) {
            System.out.println("Вы еще не просматривали задачи");
        } else {
            System.out.println("Последние просмотренные задачи:");
        }
        return tasksHistory;
    }
}
