package test;

import managers.inmemory.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void beforeEach() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    public void addTaskTest() {
        super.addTaskTest();
    }

    @Test
    public void addSubTaskTest() {
        super.addSubTaskTest();
    }

    @Test
    public void addEpicTest() {
        super.addEpicTest();
    }

    @Test
    public void updateTaskTest() {
        super.updateTaskTest();
    }

    @Test
    public void updateSubTaskTest() {
        super.updateSubTaskTest();
    }

    @Test
    public void updateEpicTest() {
        super.updateEpicTest();
    }

    @Test
    public void getAllTasksTest() {
        super.getAllTasksTest();
    }

    @Test
    public void getAllSubtasksTest() {
        super.getAllSubTasksTest();
    }

    @Test
    public void getAllEpicsTest() {
        super.getAllEpicsTest();
    }

    @Test
    public void getTaskTest() {
        super.getTaskTest();
    }

    @Test
    public void getEpicTest() {
        super.getEpicTest();
    }

    @Test
    public void getSubTaskTest() {
        super.getSubTaskTest();
    }

    @Test
    public void removeAllTasksTest() {
        super.removeAllTasksTest();
    }

    @Test
    public void removeAllEpicsTest() {
        super.removeAllEpicsTest();
    }

    @Test
    public void removeAllSubTasksTest() {
        super.removeAllSubTasksTest();
    }

    @Test
    public void removeTaskTest() {
        super.removeTaskTest();
    }

    @Test
    public void removeEpicTest() {
        super.removeEpicTest();
    }

    @Test
    public void removeSubTaskTest() {
        super.removeSubTaskTest();
    }

    @Test
    public void getSubTasksOfEpicTest() {
        super.getSubTasksOfEpicTest();
    }

    @Test
    public void getHistoryTest() {
        super.getHistoryTest();
    }

    @Test
    public void getPrioritizedTasksTest() {
        super.getPrioritizedTasksTest();
    }

    @Test
    public void taskValidationTest() {
        super.taskValidationTest();
    }

    @Test
    public void epicStatusShouldBeNewWithEmptySubtasks() {
        super.epicStatusShouldBeNewWithEmptySubtasks();
    }

    @Test
    public void epicStatusShouldBeNew() {
        super.epicStatusShouldBeNew();
    }

    @Test
    public void epicStatusShouldBeDone() {
        super.epicStatusShouldBeDone();
    }

    @Test
    public void epicStatusShouldBeInProgressWithNewAndDoneSubtasks() {
        super.epicStatusShouldBeInProgressWithNewAndDoneSubtasks();
    }

    @Test
    public void epicStatusShouldBeInProgressWithInProgressSubtasks() {
        super.epicStatusShouldBeInProgressWithInProgressSubtasks();
    }
}
