package test;

import exception.ManagerSaveException;
import managers.filebacked.FileBackedTasksManager;
import managers.inmemory.InMemoryTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static tasks.Task.FORMATTER;

public class FileBackedTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    File save;

    @BeforeEach
    public void beforeEach() {
        save = new File("resources/for_tests.csv");
        taskManager = new FileBackedTasksManager(save);
    }

    @AfterEach
    public void clearFile() {
        try (Writer fileWriter = new FileWriter(save)) {
            fileWriter.write("");
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
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

    @Test
    public void epicStartTimeTest() {
        super.epicStartTimeTest();
    }

    @Test
    public void setEpicDurationTest() {
        super.setEpicDurationTest();
    }

    @Test
    public void epicEndTimeTest() {
        super.epicEndTimeTest();
    }

    @Test
    public void emptyLoadFromFileTest() {
        taskManager = FileBackedTasksManager.loadFromFile(save);

        List<Task> emptyList = Collections.emptyList();

        assertEquals(emptyList, taskManager.getAllTasks());
        assertEquals(emptyList, taskManager.getAllSubTasks());
        assertEquals(emptyList, taskManager.getAllEpics());

    }

    @Test
    public void loadFromFileWithEpic() {
        Epic epic = new Epic("name", "description", 0, Status.NEW, Duration.ZERO, LocalDateTime.MAX);

        taskManager.addEpic(epic);

        taskManager = FileBackedTasksManager.loadFromFile(save);

        assertEquals(epic, taskManager.getEpic(1));
        assertEquals(1, taskManager.getAllEpics().size());
        assertEquals(1, taskManager.getHistory().size());
    }

    @Test
    public void loadFromFileWithEmptyHistory() {
        SubTask stask1 = new SubTask("name1", "description1", 0, Status.DONE, Duration.ofMinutes(60),
                LocalDateTime.parse("02.01.2023;12:00", FORMATTER), 0);
        SubTask stask2 = new SubTask("name2", "description2", 0, Status.DONE, Duration.ofMinutes(60),
                LocalDateTime.parse("03.01.2023;12:00", FORMATTER), 0);
        Epic epic = new Epic("name3", "description3", 0, Status.NEW, Duration.ZERO,
                LocalDateTime.MAX);

        taskManager.addSubTask(stask1);
        taskManager.addSubTask(stask2);
        epic.addSubTaskId(stask1.getId());
        epic.addSubTaskId(stask2.getId());
        taskManager.addEpic(epic);

        List<Integer> subTasks = List.of(stask1.getId(), stask2.getId());

        taskManager = FileBackedTasksManager.loadFromFile(save);

        assertEquals(Collections.emptyList(), taskManager.getHistory());
        assertEquals(epic, taskManager.getEpic(3));
        assertEquals(1, taskManager.getAllEpics().size());
        assertEquals(2, taskManager.getAllSubTasks().size());
        assertEquals(subTasks, epic.getSubTaskIds());
    }
}
