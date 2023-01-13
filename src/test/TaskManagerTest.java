package test;

import managers.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    /*
    protected T taskManager;

    public void addTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", 0, Status.NEW);
        taskManager.addTask(task);
        final int taskId = task.getId();

        final Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getAllTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    public void addSubTask() {
        SubTask subTask = new SubTask("Test addNewSubTask", "Test addNewSubTask description",
                0, Status.NEW, 1);
        Epic epic = new Epic("Test Test addNewSubTask", "Test addNewSubTask description",
                0, Status.NEW);

        taskManager.addSubTask(subTask);
        final int subTaskId = subTask.getId();
        epic.addSubTaskId(subTaskId);
        taskManager.addEpic(epic);
        final int epicId = epic.getId();

        final SubTask savedSubTask = taskManager.getSubTask(subTaskId);

        assertNotNull(savedSubTask, "Задача не найдена.");
        assertEquals(subTask, savedSubTask, "Задачи не совпадают.");

        final List<SubTask> subTasks = taskManager.getAllSubTasks();

        assertNotNull(subTasks, "Задачи на возвращаются.");
        assertEquals(1, subTasks.size(), "Неверное количество задач.");
        assertEquals(subTask, subTasks.get(0), "Задачи не совпадают.");
        assertEquals(epicId, savedSubTask.getEpicId(), "У подзадачи неправильно сохранился id эпика");
    }

    public void addEpic() {
        SubTask subTask1 = new SubTask("Test addNewEpic", "Test addNewEpic description",
                0, Status.NEW, 0);
        SubTask subTask2 = new SubTask("name", "description", 0, Status.NEW, 0);
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description",
                0, Status.NEW);

        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);

        final int subTaskId1 = subTask1.getId();
        final int subTaskId2 = subTask2.getId();

        epic.addSubTaskId(subTaskId1);
        epic.addSubTaskId(subTaskId2);

        taskManager.addEpic(epic);

        final int epicId = epic.getId();

        final List<Integer> subTaskIds = List.of(subTaskId1, subTaskId2);

        final Epic savedEpic = taskManager.getEpic(epicId);

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");

        final List<Epic> epics = taskManager.getAllEpics();

        assertNotNull(epics, "Задачи на возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.get(0), "Задачи не совпадают.");
        assertEquals(subTaskIds, savedEpic.getSubTaskIds(), "Эпик неправильно сохранил подзадачи");
    }

    public void updateEpic() {
        SubTask subTask1 = new SubTask("Test updateEpic", "Test updateEpic description",
                0, Status.NEW, 0);
        Epic epic1 = new Epic("Test updateEpic", "Test updateEpic description",
                0, Status.NEW);
        Epic epic2 = new Epic("updated epic", "to replace 1st", 2, Status.NEW);

        taskManager.addSubTask(subTask1);
        final int subTaskId = subTask1.getId();
        epic1.addSubTaskId(subTaskId);
        taskManager.addEpic(epic1);
        final int epicId = epic1.getId();

        taskManager.updateEpic(epic2);

        final Epic savedEpic = taskManager.getEpic(epicId);
        final List<Integer> subTasksOfEpic = List.of(subTaskId);

        assertNotEquals(epic1, savedEpic, "Старый эпик не заменился на новый");
        assertEquals(subTasksOfEpic, savedEpic.getSubTaskIds(), "Подзадачи не перешли к обновлённому эпику");
    }

    public void updateTask() {
        Task task1 = new Task("name1", "description1", 0, Status.NEW);
        Task task2 = new Task("name2", "description2", 1, Status.NEW);

        taskManager.addTask(task1);

        final int taskId = task1.getId();

        taskManager.updateTask(task2);

        final Task savedTask = taskManager.getTask(taskId);

        assertNotEquals(task1, savedTask, "Старая задача не заменилась на новую");
    }

    public void updateSubTask() {
        SubTask subTask1 = new SubTask("name1", "description1", 0, Status.NEW, 0);
        SubTask subTask2 = new SubTask("name2", "description2", 1, Status.NEW, 0);
        Epic epic = new Epic("name3", "description3", 0, Status.NEW);

        taskManager.addSubTask(subTask1);

        final int subTaskId = subTask1.getId();

        epic.addSubTaskId(subTaskId);

        taskManager.addEpic(epic);

        final int epicsId = subTask1.getEpicId();

        taskManager.updateSubTask(subTask2);

        final ArrayList<Integer> epicSaved = epic.getSubTaskIds();
        final SubTask savedSubTask = taskManager.getSubTask(epicSaved.get(0));

        assertNotEquals(savedSubTask, subTask1, "Подзадача не обновилась");
        assertEquals(epicsId, savedSubTask.getEpicId());
    }

    public void getAllTasks() {
        Task task1 = new Task("name1", "description1", 0, Status.NEW);
        Task task2 = new Task("name2", "description2", 1, Status.NEW);

        assertNotNull(taskManager.getAllTasks());

        taskManager.addTask(task1);
        taskManager.addTask(task2);

        List<Task> allTasks = taskManager.getAllTasks();

        assertEquals(2, allTasks.size(), "Возвращаются не все задачи");
    }

    public void getAllSubTasks() {
        SubTask subTask1 = new SubTask("name1", "description1", 0, Status.NEW, 0);
        SubTask subTask2 = new SubTask("name2", "description2", 1, Status.NEW, 0);

        assertNotNull(taskManager.getAllSubTasks());

        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);

        List<SubTask> allSubTasks = taskManager.getAllSubTasks();

        assertEquals(2, allSubTasks.size(), "Возвращаются не все задачи");
    }

    public void getAllEpics() {
        Epic epic1 = new Epic("name1", "description1", 1, Status.NEW);
        Epic epic2 = new Epic("name2", "description2", 2, Status.DONE);

        assertNotNull(taskManager.getAllEpics());

        List<Epic> beforeSaved = List.of(epic1, epic2);

        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);


        List<Epic> allEpics = taskManager.getAllEpics();

        assertEquals(2, allEpics.size(), "Возвращаются не все эпики");
        assertEquals(beforeSaved, allEpics, "Задачи возвращаются неверно");
    }

    public void getTask() {
        Task task = new Task("name", "description", 0, Status.NEW);

        taskManager.addTask(task);
        final int taskId = task.getId();

        Task savedTask = taskManager.getTask(taskId);

        assertEquals(task, savedTask);
    }

    public void getEpic() {
        Epic epic = new Epic("name", "description", 0, Status.NEW);

        taskManager.addEpic(epic);

        final int epicId = epic.getId();

        Epic savedEpic = taskManager.getEpic(epicId);

        assertEquals(epic, savedEpic);

    }

    public void getSubTask() {
        SubTask subTask = new SubTask("name", "description", 0, Status.NEW, 0);

        taskManager.addSubTask(subTask);

        final int subTaskId = subTask.getId();

        SubTask savedSubTask = taskManager.getSubTask(subTaskId);

        assertEquals(subTask, savedSubTask);
    }

    public void removeAllTasks() {
        Task task1 = new Task("name1", "description1", 0, Status.NEW);
        Task task2 = new Task("name2", "description2", 1, Status.NEW);

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.removeAllTasks();

        List emptyList = Collections.emptyList();
        assertEquals(emptyList, taskManager.getAllTasks());
    }

    public void removeAllEpics() {
        Epic epic1 = new Epic("name1", "description1", 0, Status.NEW);
        Epic epic2 = new Epic("name2", "description2", 0, Status.DONE);
        SubTask subTask1 = new SubTask("name3", "description3", 0, Status.NEW, 0);

        taskManager.addSubTask(subTask1);
        final int subTaskId = subTask1.getId();
        epic1.addSubTaskId(subTaskId);
        taskManager.addEpic(epic1);
        final int epic1Id = epic1.getId();
        taskManager.addEpic(epic2);

        taskManager.removeAllEpics();

        List emptyList = Collections.emptyList();
        assertEquals(emptyList, taskManager.getAllEpics());
        assertNull(taskManager.getEpic(epic1Id));
        assertNull(taskManager.getSubTask(subTaskId));
    }

    public void removeAllSubTasks() {
        SubTask subTask1 = new SubTask("name1", "description1", 0, Status.NEW, 0);
        SubTask subTask2 = new SubTask("name2", "description2", 0, Status.NEW, 0);
        Epic epic = new Epic("name", "description", 0, Status.NEW);

        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        final int subTask1Id = subTask1.getId();
        epic.addSubTaskId(subTask1Id);
        epic.addSubTaskId(subTask2.getId());
        taskManager.addEpic(epic);

        taskManager.removeAllSubTasks();

        List emptyList = Collections.emptyList();

        assertEquals(emptyList, taskManager.getAllSubTasks());
        assertEquals(emptyList, epic.getSubTaskIds());
        assertNull(taskManager.getSubTask(subTask1Id));
    }

    public void removeTask() {
        Task task1 = new Task("name1", "description1", 0, Status.NEW);

        taskManager.addTask(task1);

        final int taskId = task1.getId();

        taskManager.removeTask(taskId);

        assertNull(taskManager.getTask(taskId));
    }
/*
    public void removeEpic() {

    }

    public void removeSubTask() {

    }

    public void getSubTasksOfEpic() {

    }

    public void getHistory() {

    }

 */
}
