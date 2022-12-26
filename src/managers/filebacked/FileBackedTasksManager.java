package managers.filebacked;

import exception.ManagerSaveException;
import managers.HistoryManager;
import managers.Managers;
import managers.TaskManager;
import managers.inmemory.InMemoryTaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Этот класс включает в себя основную логику работы трекера задач, сохраняющего данные в файл;
 */
public class FileBackedTasksManager extends InMemoryTaskManager {
    private final File saving;

    public FileBackedTasksManager(File saving) {
        this.saving = saving;
    }

    public static void main(String[] args) {

        File save = new File("resources/save.csv");
        TaskManager manager = new FileBackedTasksManager(save);

        Task task1 = new Task("Купить цветы", "маме", 0, Status.NEW);
        Task task2 = new Task("Надуть шарики", "Чтобы украсить квартиру", 0, Status.DONE);
        SubTask stask1 = new SubTask("Убрать документы", "в папки", 0, Status.NEW, 0);
        SubTask stask2 = new SubTask("Сгруппировать вещи", "по коробкам", 0, Status.DONE, 0);
        Epic epic1 = new Epic("Переезд", "В Казань", 0, Status.DONE);

        manager.addSubTask(stask1);
        manager.addSubTask(stask2);

        epic1.addSubTaskId(stask1.getId());
        epic1.addSubTaskId(stask2.getId());
        manager.addEpic(epic1);

        manager.getSubTask(1);
        manager.getSubTask(2);
        manager.getEpic(3);
        manager.getSubTask(1);

        manager = loadFromFile(save);

        manager.addTask(task1);
        manager.getTask(4);
        manager.addTask(task2);
    }

    /**
     * Метод сохраняет в файл задачи и их историю;
     */
    private void save() {
        try (Writer fileWriter = new FileWriter(saving)) {
            List<Task> allTypesTasks = new ArrayList<>(super.tasksMap.values());
            allTypesTasks.addAll(super.epicTasksMap.values());
            allTypesTasks.addAll(super.subTasksMap.values());
            fileWriter.write("id,type,name,status,description,epic\n");
            for (Task task : allTypesTasks) {
                fileWriter.write(CsvConverter.toString(task) + "\n");
            }
            fileWriter.write("\n" + CsvConverter.historyToString(Managers.historyManager));

        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubTask(SubTask task) {
        super.addSubTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubTask(SubTask task) {
        super.updateSubTask(task);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> allTasks = super.getAllTasks();
        save();
        return allTasks;
    }

    @Override
    public ArrayList<SubTask> getAllSubTasks() {
        ArrayList<SubTask> allSubTasks = super.getAllSubTasks();
        save();
        return allSubTasks;
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        ArrayList<Epic> allEpics = super.getAllEpics();
        save();
        return allEpics;
    }

    @Override
    public Task getTask(int taskId) {
        Task task = super.getTask(taskId);
        save();
        return task;
    }

    @Override
    public Epic getEpic(int epicId) {
        Epic epic = super.getEpic(epicId);
        save();
        return epic;
    }

    @Override
    public SubTask getSubTask(int subTaskId) {
        SubTask task = super.getSubTask(subTaskId);
        save();
        return task;
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    @Override
    public void removeAllSubTasks() {
        super.removeAllSubTasks();
        save();
    }

    @Override
    public void removeTask(int taskId) {
        super.removeTask(taskId);
        save();
    }

    @Override
    public void removeEpic(int epicId) {
        super.removeEpic(epicId);
        save();
    }

    @Override
    public void removeSubTask(int subTaskId) {
        super.removeSubTask(subTaskId);
        save();
    }

    @Override
    public void printSubTasksOfEpic(int epicId) {
        super.printSubTasksOfEpic(epicId);
        save();
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager manager = new FileBackedTasksManager(file);
        HistoryManager historyManager = Managers.getDefaultHistory();
        List<String> strings = new ArrayList<>();
        try (FileReader fileReader = new FileReader(file)) {
            BufferedReader br = new BufferedReader(fileReader);
            while (br.ready()) {
                String line = br.readLine();
                strings.add(line);
            }
            if (strings.size() > 4) {
                for (int i = 1; i < strings.size() - 2; i++) {
                    manager.uploadTask(strings.get(i));
                }
                List<Integer> historyIds = new ArrayList<>(CsvConverter.historyFromString(strings.get(strings
                        .size() - 1)));
                for (Integer id : historyIds) {
                    if (manager.epicTasksMap.containsKey(id)) {
                        historyManager.add(manager.getEpic(id));
                    } else if (manager.subTasksMap.containsKey(id)) {
                        historyManager.add(manager.getSubTask(id));
                    } else {
                        historyManager.add(manager.getTask(id));
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
        return manager;
    }

    private void uploadTask(String text) {
        Task task = CsvConverter.fromString(text);
        if (task instanceof Epic) {
            super.addEpic((Epic) task);
        } else if (task instanceof SubTask) {
            super.addSubTask((SubTask) task);
        } else {
            super.addTask(task);
        }
    }
}
