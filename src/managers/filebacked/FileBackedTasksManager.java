package managers.filebacked;

import exception.ManagerSaveException;
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
        manager.getSubTask(1);

        manager = loadFromFile(save);

        manager.addSubTask(stask2);
        epic1.addSubTaskId(stask1.getId());
        epic1.addSubTaskId(stask2.getId());
        manager.addEpic(epic1);

        manager.getSubTask(2);
        manager.getEpic(3);
        manager.getSubTask(1);

        manager = loadFromFile(save);

        manager.addTask(task1);
        manager.addTask(task2);
        manager.getTask(4);

        System.out.println(manager.getHistory());
    }

    /**
     * Метод сохраняет в файл задачи и их историю;
     */
    private void save() {
        try (Writer fileWriter = new FileWriter(saving)) {
            List<Task> allTypesTasks = new ArrayList<>(tasksMap.values());
            allTypesTasks.addAll(epicTasksMap.values());
            allTypesTasks.addAll(subTasksMap.values());
            fileWriter.write("id,type,name,status,description,epic\n");
            for (Task task : allTypesTasks) {
                fileWriter.write(CsvConverter.toString(task) + "\n");
            }
            fileWriter.write("\n" + CsvConverter.historyToString(historyManager));

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
        List<String> strings = new ArrayList<>();
        int stringsSize;
        try (FileReader fileReader = new FileReader(file)) {
            BufferedReader br = new BufferedReader(fileReader);
            while (br.ready()) {
                String line = br.readLine();
                strings.add(line);
            }
            stringsSize = strings.size();
            if (strings.get(stringsSize - 1).isEmpty()) {
                manager.nextId = stringsSize - 1;
                for (int i = 1; i < stringsSize - 1; i++) {
                    manager.uploadTask(strings.get(i));
                }
            } else {
                manager.nextId = stringsSize - 2;
                for (int i = 1; i < stringsSize - 2; i++) {
                    manager.uploadTask(strings.get(i));
                }
                manager.uploadHistory(CsvConverter.historyFromString(strings.get(stringsSize - 1)));
            }
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
        return manager;
    }

    private void uploadTask(String text) {
        Task task = CsvConverter.fromString(text);
        if (task instanceof Epic) {
            epicTasksMap.put(task.getId(), (Epic) task);
        } else if (task instanceof SubTask) {
            subTasksMap.put(task.getId(), (SubTask) task);
        } else {
            tasksMap.put(task.getId(), task);
        }
    }

    private void uploadHistory(List<Integer> ids) {
        for (Integer id : ids) {
            if (epicTasksMap.containsKey(id)) {
                historyManager.add(getEpic(id));
            } else if (subTasksMap.containsKey(id)) {
                historyManager.add(getSubTask(id));
            } else {
                historyManager.add(getTask(id));
            }
        }
    }
}
