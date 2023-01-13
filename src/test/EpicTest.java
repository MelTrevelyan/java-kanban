package test;

import managers.TaskManager;
import managers.inmemory.InMemoryTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;

class EpicTest {
/*
    private TaskManager testManager;

    @BeforeEach
    public void beforeEach() {
        testManager = new InMemoryTaskManager();
    }

    @Test
    public void epicStatusShouldBeNewWithEmptySubtasks() {
        Epic epic = new Epic("name1", "description1", 0, Status.DONE);
        testManager.addEpic(epic);
        Assertions.assertEquals(Status.NEW, testManager.getEpic(1).getStatus(), "Статус определяется" +
                " неправильно");
    }

    @Test
    public void epicStatusShouldBeNew() {
        SubTask stask1 = new SubTask("name1", "description1", 0, Status.NEW, 0);
        SubTask stask2 = new SubTask("name2", "description2", 0, Status.NEW, 0);
        Epic epic = new Epic("name3", "description3", 0, Status.DONE);

        testManager.addSubTask(stask1);
        testManager.addSubTask(stask2);
        epic.addSubTaskId(stask1.getId());
        epic.addSubTaskId(stask2.getId());
        testManager.addEpic(epic);
        Assertions.assertEquals(Status.NEW, testManager.getEpic(3).getStatus(), "Статус определяется" +
                " неправильно");
    }

    @Test
    public void epicStatusShouldBeDone() {
        SubTask stask1 = new SubTask("name1", "description1", 0, Status.DONE, 0);
        SubTask stask2 = new SubTask("name2", "description2", 0, Status.DONE, 0);
        Epic epic = new Epic("name3", "description3", 0, Status.NEW);

        testManager.addSubTask(stask1);
        testManager.addSubTask(stask2);
        epic.addSubTaskId(stask1.getId());
        epic.addSubTaskId(stask2.getId());
        testManager.addEpic(epic);
        Assertions.assertEquals(Status.DONE, testManager.getEpic(3).getStatus(), "Статус определяется" +
                " неправильно");
    }

    @Test
    public void epicStatusShouldBeInProgressWithNewAndDoneSubtasks() {
        SubTask stask1 = new SubTask("name1", "description1", 0, Status.DONE, 0);
        SubTask stask2 = new SubTask("name2", "description2", 0, Status.NEW, 0);
        Epic epic = new Epic("name3", "description3", 0, Status.NEW);

        testManager.addSubTask(stask1);
        testManager.addSubTask(stask2);
        epic.addSubTaskId(stask1.getId());
        epic.addSubTaskId(stask2.getId());
        testManager.addEpic(epic);
        Assertions.assertEquals(Status.IN_PROGRESS, testManager.getEpic(3).getStatus(), "Статус" +
                " определяется неправильно");
    }

    @Test
    public void epicStatusShouldBeInProgressWithInProgressSubtasks() {
        SubTask stask1 = new SubTask("name1", "description1", 0, Status.IN_PROGRESS, 0);
        SubTask stask2 = new SubTask("name2", "description2", 0, Status.IN_PROGRESS, 0);
        Epic epic = new Epic("name3", "description3", 0, Status.NEW);

        testManager.addSubTask(stask1);
        testManager.addSubTask(stask2);
        epic.addSubTaskId(stask1.getId());
        epic.addSubTaskId(stask2.getId());
        testManager.addEpic(epic);
        Assertions.assertEquals(Status.IN_PROGRESS, testManager.getEpic(3).getStatus(), "Статус" +
                " определяется неправильно");
    }

 */
}