package test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import server.HttpTaskServer;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static tasks.Task.FORMATTER;

public class HttpTaskServerTest {

    private HttpTaskServer server;

    @BeforeEach
    public void beforeEach() throws IOException {
        server = new HttpTaskServer();
        server.start();
        SubTask stask1 = new SubTask("name1", "description1", 0, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("02.01.2023;12:00", FORMATTER), 0);
        SubTask stask2 = new SubTask("name2", "description2", 0, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("03.01.2023;12:00", FORMATTER), 0);
        Epic epic = new Epic("name3", "description3", 0, Status.DONE, Duration.ZERO,
                LocalDateTime.MAX);
        Task task1 = new Task("name3", "description3", 0, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("06.01.2023;12:00", FORMATTER));
        Task task2 = new Task("name4", "description4", 1, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("06.01.2023;17:00", FORMATTER));
        HttpTaskServer server = new HttpTaskServer();
        server.taskManager.addTask(task1);
        server.taskManager.addTask(task2);
        server.taskManager.addSubTask(stask1);
        server.taskManager.addSubTask(stask2);
        epic.addSubTaskId(stask1.getId());
        epic.addSubTaskId(stask2.getId());
        server.taskManager.addEpic(epic);
    }

    @AfterEach
    public void afterEach() {
        server.stop();
    }
}
