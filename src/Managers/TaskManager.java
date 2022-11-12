package Managers;

import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;

import java.util.ArrayList;

public interface TaskManager {

    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubTask(SubTask task);

    void updateEpic(Epic epic);

    void updateSubTask(SubTask task);

    void updateTask(Task task);

    ArrayList<Task> getAllTasks();

    ArrayList<SubTask> getAllSubTasks();

    ArrayList<Epic> getAllEpics();

    Task getTask(int taskId);

    Epic getEpic(int epicId);

    SubTask getSubTask(int SubTaskId);

    void removeAllTasks();

    void removeAllEpics();

    void removeAllSubTasks();

    void removeTask(int taskId);

    void removeEpic(int epicId);

    void removeSubTask(int subTaskId);

    void printSubTasksOfEpic(int epicId);

    void getHistory();
}
