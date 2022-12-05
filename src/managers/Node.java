package managers;

import java.util.Objects;

public class Node<Task> {
    protected Task task;
    protected Node<Task> nextTask;
    protected Node<Task> prevTask;

    public Node(Task task, Node<Task> prevTask, Node<Task> nextTask) {
        this.task = task;
        this.nextTask = nextTask;
        this.prevTask = prevTask;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node<?> node = (Node<?>) o;
        return Objects.equals(task, node.task) && Objects.equals(nextTask, node.nextTask)
                && Objects.equals(prevTask, node.prevTask);
    }

    @Override
    public int hashCode() {
        return Objects.hash(task, nextTask, prevTask);
    }
}
