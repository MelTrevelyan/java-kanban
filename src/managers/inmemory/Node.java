package managers.inmemory;

import tasks.Task;

import java.util.Objects;

public class Node {
    protected Task task;
    protected Node nextTask;
    protected Node prevTask;

    public Node(Task task, Node prevTask, Node nextTask) {
        this.task = task;
        this.nextTask = nextTask;
        this.prevTask = prevTask;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(task, node.task) && Objects.equals(nextTask, node.nextTask)
                && Objects.equals(prevTask, node.prevTask);
    }

    @Override
    public int hashCode() {
        return Objects.hash(task, nextTask, prevTask);
    }
}
