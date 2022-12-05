package managers;

import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private Map<Integer, Node<Task>> erasingHistory = new HashMap<>(0);
    private Node<Task> head;
    private Node<Task> tail;
    private List<Task> tasksHistory = new LinkedList<>();

    @Override
    public void add(Task task) {
        tasksHistory.add(task);
        if (tasksHistory.size() > 10) {
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

    @Override
    public void remove(int id) {
        tasksHistory.remove(tasksHistory.get(id));
    }

    public void linkLast(Task task) {
        if (erasingHistory.isEmpty()) {
            Node<Task> firstNode = new Node<>(task, null, null);
            head = firstNode;
            tail = firstNode;
            erasingHistory.put(task.getId(), firstNode);
        } else {
            Node<Task> nextNode = new Node<>(task, tail, null);
            tail.nextTask = nextNode;
            erasingHistory.put(tail.task.getId(), tail);
            tail = nextNode;
            erasingHistory.put(task.getId(), nextNode);
        }
    }

    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        for (Node<Task> node : erasingHistory.values()) {
            tasks.add(node.task);
        }
        return new ArrayList<>(tasks);
    }

    public void removeNode(Node<Task> node) {
        node.prevTask.nextTask = node.nextTask;
        node.nextTask.prevTask = node.prevTask;
        erasingHistory.remove(node.task.getId());
    }
}
