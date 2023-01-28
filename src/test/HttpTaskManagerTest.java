package test;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskManager;
import server.HttpTaskServer;
import server.KVServer;
import tasks.Status;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static tasks.Task.FORMATTER;

public class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {

    private KVServer kvServer;
    private HttpTaskServer server;
    private Gson gson;

    @BeforeEach
    public void beforeEach() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        server = new HttpTaskServer();
        taskManager = (HttpTaskManager) server.getTaskManager();
        gson = new Gson();
    }

    @AfterEach
    public void afterEach() {
        server.stop();
        kvServer.stop();
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
    public void emptyLoadFromServerTest() {
        List<Task> emptyList = Collections.emptyList();

        assertEquals(emptyList, taskManager.getAllTasks());
        assertEquals(emptyList, taskManager.getAllSubTasks());
        assertEquals(emptyList, taskManager.getAllEpics());
        assertEquals(emptyList, taskManager.getPrioritizedTasks());
        assertEquals(emptyList, taskManager.getHistory());
    }

    @Test
    public void endpointGetTasksTest() throws IOException, InterruptedException {
        Task task1 = new Task("name3", "description3", 0, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("06.01.2023;12:00", FORMATTER));
        Task task2 = new Task("name4", "description4", 1, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("06.01.2023;17:00", FORMATTER));

        taskManager.addTask(task1);
        taskManager.addTask(task2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        List<String> tasks = new ArrayList<>();
        tasks.add(gson.toJson(task1));
        tasks.add(gson.toJson(task2));

        assertEquals(tasks, response.body());
    }

    @Test
    public void endpointGetSubTasksTest() {

    }

    @Test
    public void endpointGetEpicsTest() {

    }

    @Test
    public void endpointGetTaskByIdTest() {

    }

    @Test
    public void endpointGetSubTaskByIdTest() {

    }

    @Test
    public void endpointGetEpicByIdTest() {

    }

    @Test
    public void endpointGetAllTasksTest() {

    }

    @Test
    public void postAddTaskTest() {

    }

    @Test
    public void postAddSubTaskTest() {

    }

    @Test
    public void postAddEpicTest() {

    }

    @Test
    public void postUpdateTaskTest() {

    }

    @Test
    public void postUpdateSubTaskTest() {

    }

    @Test
    public void postUpdateEpicTest() {

    }

    @Test
    public void deleteTaskTest() {}

    @Test
    public void deleteSubTaskTest() {}

    @Test
    public void deleteEpicTest() {}

    @Test
    public void deleteAllTasksTest() {}

    @Test
    public void deleteTasksTest() {}

    @Test
    public void deleteSubTasksTest() {}

    @Test
    public void deleteEpicsTest() {}

    @Test
    public void getEpicSubtasksTest() {}

    @Test
    public void getHistoryTest() {}
}
