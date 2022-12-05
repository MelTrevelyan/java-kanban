package managers;

public class Node<Task> {
    protected Task task;
    protected Node<Task> nextTask;
    protected Node<Task> prevTask;

    public Node(Task task, Node<Task> prevTask, Node<Task> nextTask) {
        this.task = task;
        this.nextTask = nextTask;
        this.prevTask = prevTask;
    }
}
