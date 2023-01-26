package server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import exception.ManagerSaveException;
import managers.TaskManager;
import managers.filebacked.FileBackedTasksManager;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

import static tasks.Task.FORMATTER;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final HttpServer httpServer;
    public final TaskManager taskManager;
    private File save;
    private final Gson gson;

    public HttpTaskServer() throws IOException {
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/task", new TasksHandler());
        save = new File("resources/for_server.csv");
        taskManager = new FileBackedTasksManager(save);
        gson = new Gson();
        start();
    }

    public static void main(String[] args) throws IOException {
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

    private Endpoint getEndpoint(String path, String query, String requestMethod) {

        Endpoint defaultEndpoint = Endpoint.UNKNOWN;

        switch (requestMethod) {
            case "GET":
                if (Pattern.matches("^/tasks/task$", path)) {
                    return Endpoint.GET_TASKS;
                } else if (Pattern.matches("^/tasks/subtask$", path)) {
                    return Endpoint.GET_SUBTASKS;
                } else if (Pattern.matches("^/tasks/epic$", path)) {
                    return Endpoint.GET_EPICS;
                } else if (Pattern.matches("^/tasks/$", path)) {
                    return Endpoint.GET_ALL_TASKS;
                } else if (Pattern.matches("^/tasks/task/id=\\d+$", path + query)) {
                    return Endpoint.GET_TASK_BY_ID;
                } else if (Pattern.matches("^/tasks/subtask/id=\\d+$", path + query)) {
                    return Endpoint.GET_SUBTASK_BY_ID;
                } else if (Pattern.matches("^/tasks/epic/id=\\d+$", path + query)) {
                    return Endpoint.GET_EPIC_BY_ID;
                } else if (Pattern.matches("^/tasks/subtask/epic/id=\\d+$", path + query)) {
                    return Endpoint.GET_EPIC_SUBTASKS;
                }
                break;
            case "POST":
                if (Pattern.matches("^/tasks/task$", path)) {
                    return Endpoint.POST_TASK;
                } else if (Pattern.matches("^/tasks/subtask$", path)) {
                    return Endpoint.POST_SUBTASK;
                } else if (Pattern.matches("^/tasks/epic$", path)) {
                    return Endpoint.POST_EPIC;
                }
                break;
            case "DELETE":
                if (Pattern.matches("^/tasks/task/id=\\d+$", path + query)) {
                    return Endpoint.DELETE_TASK;
                } else if (Pattern.matches("^/tasks/subtask/id=\\d+$", path + query)) {
                    return Endpoint.DELETE_SUBTASK;
                } else if (Pattern.matches("^/tasks/epic/id=\\d+$", path + query)) {
                    return Endpoint.DELETE_EPIC;
                } else if (Pattern.matches("^/tasks/task$", path)) {
                    return Endpoint.DELETE_TASKS;
                } else if (Pattern.matches("^/tasks/subtask$", path)) {
                    return Endpoint.DELETE_SUBTASKS;
                } else if (Pattern.matches("^/tasks/epic$", path)) {
                    return Endpoint.DELETE_EPICS;
                } else if (Pattern.matches("^/tasks/task$", path)) {
                    return Endpoint.DELETE_ALL_TASKS;
                }
        }
        System.out.println(path);
        System.out.println(query);
        return defaultEndpoint;
    }

    class TasksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) {
            String taskId;
            int id;
            String response;
            String body;
            JsonElement jsonElement;

            try {
                String path = exchange.getRequestURI().getPath();
                String method = exchange.getRequestMethod();
                String query = exchange.getRequestURI().getQuery();
                Endpoint endpoint = getEndpoint(path, query, method);

                switch (endpoint) {
                    case GET_TASKS:
                        response = gson.toJson(taskManager.getAllTasks());
                        sendText(exchange, response);
                        break;
                    case GET_SUBTASKS:
                        response = gson.toJson(taskManager.getAllSubTasks());
                        sendText(exchange, response);
                        break;
                    case GET_EPICS:
                        response = gson.toJson(taskManager.getAllEpics());
                        sendText(exchange, response);
                        break;
                    case GET_ALL_TASKS:
                        response = gson.toJson(taskManager.getPrioritizedTasks());
                        sendText(exchange, response);
                        break;
                    case GET_TASK_BY_ID:
                        taskId = query.replaceFirst("id=", "");
                        id = parsePathId(taskId);
                        if (id != -1) {
                            response = gson.toJson(taskManager.getTask(id));
                            sendText(exchange, response);
                            return;
                        }
                        exchange.sendResponseHeaders(405, 0);
                        break;
                    case GET_SUBTASK_BY_ID:
                        taskId = query.replaceFirst("id=", "");
                        id = parsePathId(taskId);
                        if (id != -1) {
                            response = gson.toJson(taskManager.getSubTask(id));
                            sendText(exchange, response);
                            return;
                        }
                        exchange.sendResponseHeaders(405, 0);
                        break;
                    case GET_EPIC_BY_ID:
                        taskId = query.replaceFirst("id=", "");
                        id = parsePathId(taskId);
                        if (id != -1) {
                            response = gson.toJson(taskManager.getEpic(id));
                            sendText(exchange, response);
                            return;
                        }
                        exchange.sendResponseHeaders(405, 0);
                        break;
                    case GET_EPIC_SUBTASKS:
                        taskId = query.replaceFirst("id=", "");
                        id = parsePathId(taskId);
                        if (id != -1) {
                            response = gson.toJson(taskManager.getEpic(id).getSubTaskIds());
                            sendText(exchange, response);
                            return;
                        }
                        exchange.sendResponseHeaders(405, 0);
                        break;
                    case GET_HISTORY:
                        response = gson.toJson(taskManager.getHistory());
                        sendText(exchange, response);
                        break;
                    case POST_TASK:
                        InputStream inputStream = exchange.getRequestBody();
                        body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                        inputStream.close();
                        jsonElement = JsonParser.parseString(body);
                        if (!jsonElement.isJsonObject()) {
                            System.out.println("Ответ от сервера не соответствует ожидаемому.");
                            return;
                        }
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        id = jsonObject.get("id").getAsInt();
                        Task taskFromJson = gson.fromJson(body, Task.class);
                        if (id < taskManager.getNextId() && id != 0) {
                            taskManager.updateTask(taskFromJson);
                        } else {
                            taskManager.addTask(taskFromJson);
                        }
                        exchange.sendResponseHeaders(200, 0);
                        break;
                    case POST_SUBTASK:
                        inputStream = exchange.getRequestBody();
                        body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                        inputStream.close();
                        jsonElement = JsonParser.parseString(body);
                        if (!jsonElement.isJsonObject()) {
                            System.out.println("Ответ от сервера не соответствует ожидаемому.");
                            return;
                        }
                        jsonObject = jsonElement.getAsJsonObject();
                        id = jsonObject.get("id").getAsInt();
                        SubTask subtaskFromJson = gson.fromJson(body, SubTask.class);
                        if (id < taskManager.getNextId() && id != 0) {
                            taskManager.updateSubTask(subtaskFromJson);
                        } else {
                            taskManager.addSubTask(subtaskFromJson);
                        }
                        exchange.sendResponseHeaders(200, 0);
                        break;
                    case POST_EPIC:
                        inputStream = exchange.getRequestBody();
                        body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                        inputStream.close();
                        jsonElement = JsonParser.parseString(body);
                        if (!jsonElement.isJsonObject()) {
                            System.out.println("Ответ от сервера не соответствует ожидаемому.");
                            return;
                        }
                        jsonObject = jsonElement.getAsJsonObject();
                        id = jsonObject.get("id").getAsInt();
                        Epic epicFromJson = gson.fromJson(body, Epic.class);
                        if (id < taskManager.getNextId() && id != 0) {
                            taskManager.updateEpic(epicFromJson);
                        } else {
                            taskManager.addEpic(epicFromJson);
                        }
                        exchange.sendResponseHeaders(200, 0);
                        break;
                    case DELETE_TASK:
                        taskId = query.replaceFirst("id=", "");
                        id = parsePathId(taskId);
                        if (id != -1) {
                            taskManager.removeTask(id);
                            exchange.sendResponseHeaders(200, 0);
                        }
                        break;
                    case DELETE_EPIC:
                        taskId = query.replaceFirst("id=", "");
                        id = parsePathId(taskId);
                        if (id != -1) {
                            taskManager.removeEpic(id);
                            exchange.sendResponseHeaders(200, 0);
                        }
                        break;
                    case DELETE_SUBTASK:
                        taskId = query.replaceFirst("id=", "");
                        id = parsePathId(taskId);
                        if (id != -1) {
                            taskManager.removeSubTask(id);
                            exchange.sendResponseHeaders(200, 0);
                        }
                        break;
                    case DELETE_TASKS:
                        taskManager.removeAllTasks();
                        exchange.sendResponseHeaders(200, 0);
                        break;
                    case DELETE_EPICS:
                        taskManager.removeAllEpics();
                        exchange.sendResponseHeaders(200, 0);
                        break;
                    case DELETE_SUBTASKS:
                        taskManager.removeAllSubTasks();
                        exchange.sendResponseHeaders(200, 0);
                        break;
                    case DELETE_ALL_TASKS:
                        taskManager.removeAllTasks();
                        taskManager.removeAllEpics();
                        exchange.sendResponseHeaders(200, 0);
                        break;
                    case UNKNOWN:
                        System.out.println("Некорректный запрос");
                        exchange.sendResponseHeaders(405, 0);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Во время выполнения запроса возникла ошибка");
            } finally {
                exchange.close();
            }
        }
    }

    private int parsePathId(String pathId) {
        try {
            return Integer.parseInt(pathId);
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    public void start() {
        System.out.println("Запустили сервер на порту " + PORT);
        System.out.println("http://localhost:" + PORT + "/task");
        httpServer.start();
    }

    public void stop() {
        try (Writer fileWriter = new FileWriter(save)) {
            fileWriter.write("");
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
        httpServer.stop(0);
        System.out.println("Остановили сервер на порту " + PORT);
    }

    public String readText(HttpExchange exchange) throws IOException {
        return new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
    }

    public void sendText(HttpExchange exchange, String text) throws IOException {
        byte[] response = text.getBytes(DEFAULT_CHARSET);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(200, response.length);
        exchange.getResponseBody().write(response);
        exchange.close();
    }
}

