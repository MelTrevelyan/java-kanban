package managers;

import tasks.Task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private List<Task> tasksHistory = new LinkedList<>();

    @Override
    public void add(Task task) {
        tasksHistory.add(task);
        int count = tasksHistory.size();
        if (count > 10) {
            tasksHistory.remove(0);
        }
    }

    @Override
    public List<Task> getHistory() {
        if (tasksHistory.isEmpty()) {
            System.out.println("Вы еще не просматривали задачи");
        } else {
            System.out.println("Последние просмотренные задачи:");
        }
        return new ArrayList<>(tasksHistory);
    }
}
