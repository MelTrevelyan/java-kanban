package server;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import managers.filebacked.FileBackedTasksManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {
    private KVTaskClient taskClient;
    private Gson gson;

    public HttpTaskManager(URI uri) {
        try {
            gson = new Gson();
            taskClient = new KVTaskClient(uri);
            loadFromServer();
        } catch (Exception e) {
            System.out.println("При создании класса HttpTaskManager возникла ошибка");
            e.printStackTrace();
        }
    }

    @Override
    protected void save() {
        try {
            taskClient.put("allTasks", gson.toJson(getAllTasks()));
            taskClient.put("allSubtasks", gson.toJson(getAllSubTasks()));
            taskClient.put("allEpics", gson.toJson(getAllEpics()));
            taskClient.put("history", gson.toJson(getHistory()));
            taskClient.put("prioritizedTasks", gson.toJson(getPrioritizedTasks()));
        } catch (Exception e) {
            System.out.println("При сохранении данных возникла ошибка");
            e.printStackTrace();
        }
    }

    private void uploadManager(List<Task> tasks, List<Task> subtasks, List<Task> epics, List<Task> history,
                               List<Task> priority) {
        for (Task task : tasks) {
            tasksMap.put(task.getId(), task);
        }
        for (Task subtask : subtasks) {
            subTasksMap.put(subtask.getId(), (SubTask) subtask);
        }
        for (Task epic : epics) {
            epicTasksMap.put(epic.getId(), (Epic) epic);
        }
        for (Task task : history) {
            historyManager.add(task);
        }
        anyTypeTasks.addAll(priority);
    }

    /*
    private void loadFromServer() throws IOException, InterruptedException {
        List<Task> tasks = convertTasksFromJson(taskClient.load("allTasks"));
        List<Task> subtasks = convertTasksFromJson(taskClient.load("allSubtasks"));
        List<Task> epics = convertTasksFromJson(taskClient.load("allEpics"));
        List<Task> history = convertTasksFromJson(taskClient.load("history"));
        List<Task> priority = convertTasksFromJson(taskClient.load("prioritizedTasks"));
        uploadManager(tasks, subtasks, epics, history, priority);
    }

     */

    private void loadFromServer() throws IOException, InterruptedException {
        loadAllTasks();
        loadAllSubTasks();
        loadAllEpics();
        loadHistory();
        loadPriority();
    }

    private void loadAllTasks() throws IOException, InterruptedException {
        List<Task> tasks = convertTasksFromJson(taskClient.load("allTasks"));
        if (!tasks.isEmpty()) {
            for (Task task : tasks) {
                tasksMap.put(task.getId(), task);
            }
        }
    }

    private void loadAllSubTasks() throws IOException, InterruptedException {
        List<Task> subtasks = convertTasksFromJson(taskClient.load("allSubtasks"));
        if (!subtasks.isEmpty()) {
            for (Task subtask : subtasks) {
                subTasksMap.put(subtask.getId(), (SubTask) subtask);
            }
        }
    }

    private void loadAllEpics() throws IOException, InterruptedException {
        List<Task> epics = convertTasksFromJson(taskClient.load("allEpics"));
        if (!epics.isEmpty()) {
            for (Task epic : epics) {
                epicTasksMap.put(epic.getId(), (Epic) epic);
            }
        }
    }

    private void loadHistory() throws IOException, InterruptedException {
        List<Task> history = convertTasksFromJson(taskClient.load("history"));
        if (!history.isEmpty()) {
            for (Task task : history) {
                historyManager.add(task);
            }
        }
    }

    private void loadPriority() throws IOException, InterruptedException {
        List<Task> priority = convertTasksFromJson(taskClient.load("prioritizedTasks"));
        if (!priority.isEmpty()) {
            anyTypeTasks.addAll(priority);
        }
    }


    private List<Task> convertTasksFromJson(String json) {
        List<Task> tasks = new ArrayList<>();
        JsonElement jsonElement = JsonParser.parseString(json);
        if (!jsonElement.isJsonNull()) {
            if (jsonElement.isJsonObject() || jsonElement.isJsonNull()) {
                System.out.println("Ответ от сервера не соответствует ожидаемому.");
                return tasks;
            }
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            if (jsonArray != null) {
                for (JsonElement element : jsonArray) {
                    tasks.add(gson.fromJson(element, Task.class));
                }
            }
        }
        return tasks;
    }
}
