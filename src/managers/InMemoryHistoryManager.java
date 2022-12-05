package managers;

import tasks.Task;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс включает в себя логику работы истории просмотров;
 */
public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node<Task>> history = new HashMap<>(0);
    private Node<Task> head;
    private Node<Task> tail;

    @Override
    public void add(Task task) {
        if (history.containsKey(task.getId())) {
            removeNode(history.get(task.getId()));
        }
        linkLast(task);
    }

    @Override
    public List<Task> getHistory() {
        if (history.isEmpty()) {
            System.out.println("Вы еще не просматривали задачи");
        } else {
            System.out.println("Последние просмотренные задачи:");
        }
        return getTasks();
    }

    @Override
    public void remove(int id) {
        removeNode(history.get(id));
    }

    private void linkLast(Task task) {
        if (history.isEmpty()) {
            Node<Task> firstNode = new Node<>(task, null, null);
            head = firstNode;
            tail = firstNode;
            history.put(task.getId(), firstNode);
        } else {
            Node<Task> nextNode = new Node<>(task, tail, null);
            tail.nextTask = nextNode;
            history.put(tail.task.getId(), tail);
            tail = nextNode;
            history.put(task.getId(), nextNode);
        }
    }

    private List<Task> getTasks() {
        Task[] tasks = new Task[history.size()];
        int i = 0;
        for (Node<Task> x = head; x != null; x = x.nextTask) {
            tasks[i++] = x.task;
        }
        return Arrays.asList(tasks);
    }

    private void removeNode(Node<Task> node) {
        if (node.equals(head)) {
            node.nextTask.prevTask = null;
            head = node.nextTask;
        } else if (node.equals(tail)) {
            node.prevTask.nextTask = null;
            tail = node.prevTask;
        } else {
            node.prevTask.nextTask = node.nextTask;
            node.nextTask.prevTask = node.prevTask;
        }
        history.remove(node.task.getId());
    }
}
