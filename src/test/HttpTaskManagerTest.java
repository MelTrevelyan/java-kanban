package test;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskManager;
import server.HttpTaskServer;
import server.KVServer;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
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
        URI url = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        List<Task> tasks = List.of(task1, task2);
        List<Task> tasksFromServer = new ArrayList<>();

        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        for (JsonElement element : jsonArray) {
            tasksFromServer.add(gson.fromJson(element, Task.class));
        }

        assertEquals(tasks, tasksFromServer);
    }

    @Test
    public void endpointGetSubTasksTest() throws IOException, InterruptedException {
        SubTask stask1 = new SubTask("name1", "description1", 0, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("02.01.2023;12:00", FORMATTER), 0);
        SubTask stask2 = new SubTask("name2", "description2", 0, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("03.01.2023;12:00", FORMATTER), 0);

        taskManager.addSubTask(stask1);
        taskManager.addSubTask(stask2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        List<Task> tasks = List.of(stask1, stask2);
        List<Task> tasksFromServer = new ArrayList<>();

        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        for (JsonElement element : jsonArray) {
            tasksFromServer.add(gson.fromJson(element, SubTask.class));
        }

        assertEquals(tasks, tasksFromServer);
    }

    @Test
    public void endpointGetEpicsTest() throws IOException, InterruptedException {
        SubTask stask1 = new SubTask("name1", "description1", 0, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("02.01.2023;12:00", FORMATTER), 0);
        SubTask stask2 = new SubTask("name2", "description2", 0, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("03.01.2023;12:00", FORMATTER), 0);
        Epic epic1 = new Epic("name3", "description3", 0, Status.DONE, Duration.ZERO,
                LocalDateTime.MAX);
        Epic epic2 = new Epic("name4", "description4", 0, Status.NEW, Duration.ZERO,
                LocalDateTime.MAX);

        taskManager.addSubTask(stask1);
        taskManager.addSubTask(stask2);
        epic1.addSubTaskId(stask1.getId());
        epic1.addSubTaskId(stask2.getId());
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        List<Task> tasks = List.of(epic1, epic2);
        List<Task> tasksFromServer = new ArrayList<>();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        for (JsonElement element : jsonArray) {
            tasksFromServer.add(gson.fromJson(element, Epic.class));
        }

        assertEquals(tasks, tasksFromServer);
    }

    @Test
    public void endpointGetTaskByIdTest() throws IOException, InterruptedException {
        Task task1 = new Task("name3", "description3", 0, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("06.01.2023;12:00", FORMATTER));

        taskManager.addTask(task1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonElement jsonElement = JsonParser.parseString(response.body());
        Task taskFromServer = gson.fromJson(jsonElement, Task.class);
        assertEquals(task1, taskFromServer);

    }

    @Test
    public void endpointGetSubTaskByIdTest() throws IOException, InterruptedException {
        SubTask stask1 = new SubTask("name1", "description1", 0, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("02.01.2023;12:00", FORMATTER), 0);
        taskManager.addSubTask(stask1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonElement jsonElement = JsonParser.parseString(response.body());
        Task taskFromServer = gson.fromJson(jsonElement, SubTask.class);
        assertEquals(stask1, taskFromServer);
    }

    @Test
    public void endpointGetEpicByIdTest() throws IOException, InterruptedException {
        Epic epic1 = new Epic("name3", "description3", 0, Status.DONE, Duration.ZERO,
                LocalDateTime.MAX);
        taskManager.addEpic(epic1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonElement jsonElement = JsonParser.parseString(response.body());
        Task taskFromServer = gson.fromJson(jsonElement, Epic.class);
        assertEquals(epic1, taskFromServer);
    }

    @Test
    public void endpointGetAllTasksTest() throws IOException, InterruptedException {
        SubTask stask1 = new SubTask("name1", "description1", 0, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("02.01.2023;12:00", FORMATTER), 0);
        Task task1 = new Task("name3", "description3", 0, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("06.01.2023;12:00", FORMATTER));


        taskManager.addTask(task1);
        taskManager.addSubTask(stask1);

        List<Task> priority = taskManager.getPrioritizedTasks();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(List.of(stask1, task1), priority);
        assertEquals(gson.toJson(priority), response.body());
    }

    @Test
    public void postAddTaskTest() throws IOException, InterruptedException {
        Task task1 = new Task("name3", "description3", 0, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("06.01.2023;12:00", FORMATTER));
        Task task2 = new Task("name4", "description4", 0, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("06.01.2023;17:00", FORMATTER));

        taskManager.addTask(task1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task");
        String json = gson.toJson(task2);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(2, taskManager.getAllTasks().size());
    }

    @Test
    public void postAddSubTaskTest() throws IOException, InterruptedException {
        SubTask stask1 = new SubTask("name1", "description1", 0, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("02.01.2023;12:00", FORMATTER), 0);
        SubTask stask2 = new SubTask("name2", "description2", 0, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("03.01.2023;12:00", FORMATTER), 0);

        taskManager.addSubTask(stask1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        String json = gson.toJson(stask2);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(2, taskManager.getAllSubTasks().size());
    }

    @Test
    public void postAddEpicTest() throws IOException, InterruptedException {
        Epic epic1 = new Epic("name3", "description3", 0, Status.DONE, Duration.ZERO,
                LocalDateTime.MAX);
        Epic epic2 = new Epic("name4", "description4", 0, Status.NEW, Duration.ZERO,
                LocalDateTime.MAX);

        taskManager.addEpic(epic1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic");
        String json = gson.toJson(epic2);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(2, taskManager.getAllEpics().size());
    }

    @Test
    public void postUpdateTaskTest() throws IOException, InterruptedException {
        Task task1 = new Task("name3", "description3", 0, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("06.01.2023;12:00", FORMATTER));
        Task task2 = new Task("name4", "description4", 1, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("06.01.2023;17:00", FORMATTER));

        taskManager.addTask(task1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task");
        String json = gson.toJson(task2);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(1, taskManager.getAllTasks().size());
    }

    @Test
    public void postUpdateSubTaskTest() throws IOException, InterruptedException {
        SubTask stask1 = new SubTask("name1", "description1", 0, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("02.01.2023;12:00", FORMATTER), 0);
        SubTask stask2 = new SubTask("name2", "description2", 1, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("03.01.2023;12:00", FORMATTER), 0);

        taskManager.addSubTask(stask1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        String json = gson.toJson(stask2);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(1, taskManager.getAllSubTasks().size());

    }

    @Test
    public void postUpdateEpicTest() throws IOException, InterruptedException {
        Epic epic1 = new Epic("name3", "description3", 0, Status.DONE, Duration.ZERO,
                LocalDateTime.MAX);
        Epic epic2 = new Epic("name4", "description4", 1, Status.NEW, Duration.ZERO,
                LocalDateTime.MAX);

        taskManager.addEpic(epic1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic");
        String json = gson.toJson(epic2);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(1, taskManager.getAllEpics().size());

    }

    @Test
    public void deleteTaskTest() throws IOException, InterruptedException {
        Task task1 = new Task("name3", "description3", 0, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("06.01.2023;12:00", FORMATTER));
        Task task2 = new Task("name4", "description4", 0, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("06.01.2023;17:00", FORMATTER));

        taskManager.addTask(task1);
        taskManager.addTask(task2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(1, taskManager.getAllTasks().size());
    }

    @Test
    public void deleteSubTaskTest() throws IOException, InterruptedException {
        SubTask stask1 = new SubTask("name1", "description1", 0, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("02.01.2023;12:00", FORMATTER), 0);
        SubTask stask2 = new SubTask("name2", "description2", 1, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("03.01.2023;12:00", FORMATTER), 0);

        taskManager.addSubTask(stask1);
        taskManager.addSubTask(stask2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(1, taskManager.getAllSubTasks().size());
    }

    @Test
    public void deleteEpicTest() throws IOException, InterruptedException {
        Epic epic1 = new Epic("name3", "description3", 0, Status.DONE, Duration.ZERO,
                LocalDateTime.MAX);
        Epic epic2 = new Epic("name4", "description4", 1, Status.NEW, Duration.ZERO,
                LocalDateTime.MAX);

        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(1, taskManager.getAllEpics().size());
    }

    @Test
    public void deleteAllTasksTest() throws IOException, InterruptedException {
        SubTask stask1 = new SubTask("name1", "description1", 0, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("02.01.2023;12:00", FORMATTER), 0);
        Task task1 = new Task("name3", "description3", 0, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("06.01.2023;12:00", FORMATTER));


        taskManager.addTask(task1);
        taskManager.addSubTask(stask1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(0, taskManager.getPrioritizedTasks().size());

    }

    @Test
    public void deleteTasksTest() throws IOException, InterruptedException {
        Task task1 = new Task("name3", "description3", 0, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("06.01.2023;12:00", FORMATTER));
        Task task2 = new Task("name4", "description4", 1, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("06.01.2023;17:00", FORMATTER));

        taskManager.addTask(task1);
        taskManager.addTask(task2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(0, taskManager.getAllTasks().size());
    }

    @Test
    public void deleteSubTasksTest() throws IOException, InterruptedException {
        SubTask stask1 = new SubTask("name1", "description1", 0, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("02.01.2023;12:00", FORMATTER), 0);
        SubTask stask2 = new SubTask("name2", "description2", 1, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("03.01.2023;12:00", FORMATTER), 0);

        taskManager.addSubTask(stask1);
        taskManager.addSubTask(stask2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(0, taskManager.getAllSubTasks().size());
    }

    @Test
    public void deleteEpicsTest() throws IOException, InterruptedException {
        Epic epic1 = new Epic("name3", "description3", 0, Status.DONE, Duration.ZERO,
                LocalDateTime.MAX);
        Epic epic2 = new Epic("name4", "description4", 1, Status.NEW, Duration.ZERO,
                LocalDateTime.MAX);

        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(0, taskManager.getAllEpics().size());
    }

    @Test
    public void getEpicSubtasksTest() throws IOException, InterruptedException {
        SubTask stask1 = new SubTask("name1", "description1", 0, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("02.01.2023;12:00", FORMATTER), 0);
        SubTask stask2 = new SubTask("name2", "description2", 0, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("03.01.2023;12:00", FORMATTER), 0);
        Epic epic1 = new Epic("name3", "description3", 0, Status.DONE, Duration.ZERO,
                LocalDateTime.MAX);

        taskManager.addSubTask(stask1);
        taskManager.addSubTask(stask2);
        epic1.addSubTaskId(stask1.getId());
        epic1.addSubTaskId(stask2.getId());
        taskManager.addEpic(epic1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/epic/?id=3");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        List<Integer> subtasks = List.of(1, 2);

        assertEquals(gson.toJson(subtasks), response.body());
    }

    @Test
    public void getHistoryFromServerTest() throws IOException, InterruptedException {
        Task task1 = new Task("name3", "description3", 0, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("06.01.2023;12:00", FORMATTER));
        Task task2 = new Task("name4", "description4", 1, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("06.01.2023;17:00", FORMATTER));
        SubTask stask1 = new SubTask("name1", "description1", 0, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("02.01.2023;12:00", FORMATTER), 0);
        SubTask stask2 = new SubTask("name2", "description2", 0, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("03.01.2023;12:00", FORMATTER), 0);
        Epic epic1 = new Epic("name3", "description3", 0, Status.DONE, Duration.ZERO,
                LocalDateTime.MAX);

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addSubTask(stask1);
        taskManager.addSubTask(stask2);
        epic1.addSubTaskId(stask1.getId());
        epic1.addSubTaskId(stask2.getId());
        taskManager.addEpic(epic1);

        taskManager.getTask(1);
        taskManager.getTask(2);
        taskManager.getSubTask(3);
        taskManager.getEpic(5);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        List<Task> rightHistory = List.of(task1, task2, stask1, epic1);

        assertEquals(gson.toJson(rightHistory), response.body());
    }
}
