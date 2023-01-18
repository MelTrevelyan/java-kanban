package test;

import managers.HistoryManager;
import managers.inmemory.InMemoryHistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static tasks.Task.FORMATTER;

public class HistoryManagerTest {

    HistoryManager manager;

    @BeforeEach
    public void beforeEach() {
        manager = new InMemoryHistoryManager();
    }

    @Test
    public void addToEmptyManager() {
        Task task1 = new Task("name3", "description3", 1, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("06.01.2023;12:00", FORMATTER));

        manager.add(task1);

        assertEquals(List.of(task1), manager.getHistory());
        assertNotNull(manager.getHistory());
    }

    @Test
    public void addSeveralTasks() {
        Task task1 = new Task("name3", "description3", 1, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("06.01.2023;12:00", FORMATTER));
        Task task2 = new Task("name4", "description4", 2, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("06.01.2023;12:00", FORMATTER));

        manager.add(task1);
        manager.add(task2);

        assertEquals(List.of(task1, task2), manager.getHistory());
        assertEquals(2, manager.getHistory().size());
        assertNotNull(manager.getHistory());
    }

    @Test
    public void getHistoryWithRepeatedTasks() {
        SubTask subTask1 = new SubTask("name1", "description1", 1, Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.parse("02.01.2023;12:00", FORMATTER), 0);
        SubTask subTask2 = new SubTask("name2", "description2", 2, Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.parse("03.01.2023;12:00", FORMATTER), 0);
        Epic epic = new Epic("name3", "description3", 3, Status.NEW, Duration.ZERO,
                LocalDateTime.MAX);
        Task task1 = new Task("name4", "description4", 4, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("06.01.2023;12:00", FORMATTER));
        Task task2 = new Task("name5", "description5", 5, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("07.01.2023;12:00", FORMATTER));

        manager.add(subTask1);
        manager.add(subTask2);
        manager.add(epic);
        manager.add(task1);
        manager.add(task2);
        manager.add(subTask1);
        manager.add(subTask2);

        List<Task> rightHistory = List.of(epic, task1, task2, subTask1, subTask2);

        assertNotNull(manager.getHistory());
        assertEquals(rightHistory, manager.getHistory());
        assertEquals(5, manager.getHistory().size());
    }

    @Test
    public void removeFromBeginning() {
        SubTask subTask1 = new SubTask("name1", "description1", 1, Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.parse("02.01.2023;12:00", FORMATTER), 0);
        SubTask subTask2 = new SubTask("name2", "description2", 2, Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.parse("03.01.2023;12:00", FORMATTER), 0);
        Epic epic = new Epic("name3", "description3", 3, Status.NEW, Duration.ZERO,
                LocalDateTime.MAX);

        manager.add(subTask1);
        manager.add(subTask2);
        manager.add(epic);

        manager.remove(1);

        assertEquals(List.of(subTask2, epic), manager.getHistory());
        assertNotNull(manager.getHistory());
        assertEquals(2, manager.getHistory().size());
    }

    @Test
    public void removeFromMiddle() {
        SubTask subTask1 = new SubTask("name1", "description1", 1, Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.parse("02.01.2023;12:00", FORMATTER), 0);
        SubTask subTask2 = new SubTask("name2", "description2", 2, Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.parse("03.01.2023;12:00", FORMATTER), 0);
        Epic epic = new Epic("name3", "description3", 3, Status.NEW, Duration.ZERO,
                LocalDateTime.MAX);

        manager.add(subTask1);
        manager.add(subTask2);
        manager.add(epic);

        manager.remove(2);

        assertEquals(List.of(subTask1, epic), manager.getHistory());
        assertNotNull(manager.getHistory());
        assertEquals(2, manager.getHistory().size());
    }

    @Test
    public void removeFromEnd() {
        SubTask subTask1 = new SubTask("name1", "description1", 1, Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.parse("02.01.2023;12:00", FORMATTER), 0);
        SubTask subTask2 = new SubTask("name2", "description2", 2, Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.parse("03.01.2023;12:00", FORMATTER), 0);
        Epic epic = new Epic("name3", "description3", 3, Status.NEW, Duration.ZERO,
                LocalDateTime.MAX);

        manager.add(subTask1);
        manager.add(subTask2);
        manager.add(epic);

        manager.remove(3);

        assertEquals(List.of(subTask1, subTask2), manager.getHistory());
        assertNotNull(manager.getHistory());
        assertEquals(2, manager.getHistory().size());

    }
}
