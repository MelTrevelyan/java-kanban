package managers;

import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
    File saving;

    private FileBackedTasksManager(File saving) {
        this.saving = saving;
        loadInfo();
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

    private void save() {
        try (Writer fileWriter = new FileWriter(saving, false)) {
            List<Task> allTypesTasks = new ArrayList<>(super.getAllTasks());
            allTypesTasks.addAll(super.getAllEpics());
            allTypesTasks.addAll(super.getAllSubTasks());
                fileWriter.write("id,type,name,status,description,epic\n");
            for (Task task : allTypesTasks) {
                fileWriter.write(toString(task) + "\n");
            }
            fileWriter.write( "\n" + historyToString(Managers.historyManager));

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
        return super.getAllTasks();
    }

    @Override
    public ArrayList<SubTask> getAllSubTasks() {
        return super.getAllSubTasks();
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        return super.getAllEpics();
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

    private String toString(Task task) {
        TaskType taskType;
        String epicId = "";
        if (task instanceof Epic) {
            taskType = TaskType.EPIC;
        } else if (task instanceof SubTask) {
            taskType = TaskType.SUBTASK;
            epicId += ((SubTask) task).getEpicId();
        } else {
            taskType = TaskType.TASK;
        }
        return String.format("%d,%s,%s,%s,%s,%s", task.getId(), taskType, task.getName(), task.getStatus(),
                task.getDescription(), epicId);
    }

    static Task fromString(String value) {
        Status status;
        String[] split = value.split(",");
        if (split[3].equals("NEW")) {
            status = Status.NEW;
        } else if (split[3].equals("IN_PROGRESS")) {
            status = Status.IN_PROGRESS;
        } else {
            status = Status.DONE;
        }
        if (split[1].equals("SUBTASK")) {
            return new SubTask(split[2], split[4], Integer.parseInt(split[0]), status, Integer.parseInt(split[5]));
        } else if (split[1].equals("EPIC")) {
            return new Epic(split[2], split[4], Integer.parseInt(split[0]), status);
        }
        return new Task(split[2], split[4], Integer.parseInt(split[0]), status);
    }

    public static String historyToString(HistoryManager manager) {
        String[] historyIds = new String[manager.getHistory().size()];
        for (int i = 0; i < manager.getHistory().size(); i++) {
            Integer id = manager.getHistory().get(i).getId();
            historyIds[i] = id.toString();
        }
        return String.join(",", historyIds);
    }

    public static List<Integer> historyFromString(String value) {
        String[] split = value.split(",");
        Integer[] history = new Integer[split.length];
        for (int i = 0; i < split.length; i++) {
            history[i] = Integer.parseInt(split[i]);
        }
        return List.of(history);
    }

    static FileBackedTasksManager loadFromFile(File file) {
        return new FileBackedTasksManager(file);
    }

    private void uploadTask(String text) {
        Task task = fromString(text);
        if (task instanceof Epic) {
            super.addEpic((Epic) task);
        } else if (task instanceof SubTask) {
            super.addSubTask((SubTask) task);
        } else {
            super.addTask(task);
        }
    }

    private void loadInfo() {
        List<String> strings = new ArrayList<>();
        try (FileReader fileReader = new FileReader(saving)) {
            BufferedReader br = new BufferedReader(fileReader);
            while (br.ready()) {
                String line = br.readLine();
                strings.add(line);
            }
            if (strings.size() > 4) {
                for (int i = 1; i < strings.size() - 2; i++) {
                    uploadTask(strings.get(i));
                }
                List<Integer> historyIds = new ArrayList<>(historyFromString(strings.get(strings.size() - 1)));
                for (Integer id : historyIds) {
                    if (super.epicTasksMap.containsKey(id)) {
                        Managers.historyManager.add(getEpic(id));
                    } else if (super.subTasksMap.containsKey(id)) {
                        Managers.historyManager.add(getSubTask(id));
                    } else {
                        Managers.historyManager.add(getTask(id));
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }
}
