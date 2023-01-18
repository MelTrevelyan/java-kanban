package test;

import exception.ManagerValidateException;
import managers.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static tasks.Task.FORMATTER;

abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    public void addTaskTest() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", 0, Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.parse("01.01.2023;12:00", FORMATTER));
        taskManager.addTask(task);
        final int taskId = task.getId();

        final Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getAllTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
        assertEquals(taskManager.getPrioritizedTasks(), tasks, "Задачи из списка по приоритету добавлены" +
                "неправильно");
        assertThrows(
                NullPointerException.class,
                () -> taskManager.getTask(0)
        );
    }

    public void addSubTaskTest() {
        SubTask stask1 = new SubTask("Убрать документы", "в папки", 0, Status.NEW,
                Duration.ofMinutes(20), LocalDateTime.parse("11.02.2023;15:00", FORMATTER), 0);

        taskManager.addSubTask(stask1);
        final int subTaskId = stask1.getId();

        final SubTask savedSubTask = taskManager.getSubTask(subTaskId);

        assertNotNull(savedSubTask, "Задача не найдена.");
        assertEquals(stask1, savedSubTask, "Задачи не совпадают.");

        final List<SubTask> subTasks = taskManager.getAllSubTasks();

        assertNotNull(subTasks, "Задачи на возвращаются.");
        assertEquals(1, subTasks.size(), "Неверное количество задач.");
        assertEquals(stask1, subTasks.get(0), "Задачи не совпадают.");
        assertEquals(1, taskManager.getPrioritizedTasks().size());
        assertThrows(
                NullPointerException.class,
                () -> taskManager.getSubTask(0)
        );
    }

    public void addEpicTest() {
        SubTask subTask1 = new SubTask("Test addNewEpic", "Test addNewEpic description", 0,
                Status.NEW, Duration.ofMinutes(100), LocalDateTime.parse("02.01.2023;10:00", FORMATTER), 0);
        SubTask subTask2 = new SubTask("name", "description", 0, Status.NEW, Duration.ofMinutes(90),
                LocalDateTime.parse("15.02.2023;14:00", FORMATTER), 0);
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description",
                0, Status.NEW, Duration.ZERO, LocalDateTime.MIN);

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
        assertEquals(epic, savedEpic, "Задачи не совпадают.");
        assertEquals(subTaskIds, savedEpic.getSubTaskIds(), "Эпик неправильно сохранил подзадачи");
        assertEquals(3, taskManager.getPrioritizedTasks().size());
        assertThrows(
                NullPointerException.class,
                () -> taskManager.getEpic(0)
        );
    }

    public void updateEpicTest() {
        SubTask subTask1 = new SubTask("Test updateEpic", "Test updateEpic description", 0,
                Status.NEW, Duration.ofMinutes(60), LocalDateTime.parse("01.01.2023;12:00", FORMATTER), 0);
        Epic epic1 = new Epic("Test updateEpic", "Test updateEpic description",
                0, Status.NEW, Duration.ZERO, LocalDateTime.MIN);
        Epic epic2 = new Epic("updated epic", "to replace 1st", 2, Status.NEW, Duration.ZERO,
                LocalDateTime.MIN);

        taskManager.addSubTask(subTask1);
        final int subTaskId = subTask1.getId();
        epic1.addSubTaskId(subTaskId);
        taskManager.addEpic(epic1);
        final int epicId = epic1.getId();

        taskManager.updateEpic(epic2);

        final Epic savedEpic = taskManager.getEpic(epicId);
        final List<Integer> subTasksOfEpic = List.of(subTaskId);

        assertNotNull(taskManager.getAllTasks());
        assertNotEquals(epic1, savedEpic, "Старый эпик не заменился на новый");
        assertEquals(subTasksOfEpic, savedEpic.getSubTaskIds(), "Подзадачи не перешли к обновлённому эпику");
        assertEquals(1, taskManager.getAllEpics().size());
        assertEquals(1, taskManager.getAllSubTasks().size());
        assertEquals(2, taskManager.getPrioritizedTasks().size());
        assertThrows(
                NullPointerException.class,
                () -> taskManager.getEpic(0)
        );
    }

    public void updateTaskTest() {
        Task task1 = new Task("name1", "description1", 0, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("01.01.2023;12:00", FORMATTER));
        Task task2 = new Task("name2", "description2", 1, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("02.01.2023;12:00", FORMATTER));

        taskManager.addTask(task1);

        final int taskId = task1.getId();

        taskManager.updateTask(task2);

        final Task savedTask = taskManager.getTask(taskId);

        assertNotNull(taskManager.getAllTasks(), "Задачи на возвращаются.");
        assertNotEquals(task1, savedTask, "Старая задача не заменилась на новую");
        assertEquals(1, taskManager.getAllTasks().size());
        assertEquals(1, taskManager.getPrioritizedTasks().size());
        assertThrows(
                NullPointerException.class,
                () -> taskManager.getTask(0)
        );
    }

    public void updateSubTaskTest() {
        SubTask subTask1 = new SubTask("name1", "description1", 0, Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.parse("01.01.2023;12:00", FORMATTER), 0);
        SubTask subTask2 = new SubTask("name2", "description2", 1, Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.parse("02.01.2023;12:00", FORMATTER), 0);
        Epic epic = new Epic("name3", "description3", 0, Status.NEW, Duration.ZERO,
                LocalDateTime.MIN);

        taskManager.addSubTask(subTask1);

        final int subTaskId = subTask1.getId();

        epic.addSubTaskId(subTaskId);

        taskManager.addEpic(epic);

        final int epicId = subTask1.getEpicId();

        taskManager.updateSubTask(subTask2);

        final ArrayList<Integer> epicSaved = epic.getSubTaskIds();
        final SubTask savedSubTask = taskManager.getSubTask(epicSaved.get(0));

        assertNotEquals(savedSubTask, subTask1, "Подзадача не обновилась");
        assertEquals(epicId, savedSubTask.getEpicId());
        assertEquals(1, taskManager.getAllSubTasks().size());
        assertEquals(1, taskManager.getAllEpics().size());
        assertEquals(2, taskManager.getPrioritizedTasks().size());
        assertThrows(
                NullPointerException.class,
                () -> taskManager.getSubTask(0)
        );
    }

    public void getAllTasksTest() {
        Task task1 = new Task("name1", "description1", 0, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("02.01.2023;12:00", FORMATTER));
        Task task2 = new Task("name2", "description2", 1, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("03.01.2023;12:00", FORMATTER));

        taskManager.addTask(task1);
        taskManager.addTask(task2);

        List<Task> allTasks = taskManager.getAllTasks();

        assertNotNull(allTasks);
        assertEquals(2, allTasks.size(), "Возвращаются не все задачи");
        assertEquals(2, taskManager.getPrioritizedTasks().size());
    }

    public void getAllSubTasksTest() {
        SubTask subTask1 = new SubTask("name1", "description1", 0, Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.parse("02.01.2023;12:00", FORMATTER), 0);
        SubTask subTask2 = new SubTask("name2", "description2", 1, Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.parse("03.01.2023;12:00", FORMATTER), 0);

        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);

        List<SubTask> allSubTasks = taskManager.getAllSubTasks();

        assertNotNull(allSubTasks);
        assertEquals(2, allSubTasks.size(), "Возвращаются не все задачи");
        assertEquals(2, taskManager.getPrioritizedTasks().size());
    }

    public void getAllEpicsTest() {
        Epic epic1 = new Epic("name1", "description1", 1, Status.NEW, Duration.ZERO,
                LocalDateTime.MIN);
        Epic epic2 = new Epic("name2", "description2", 2, Status.DONE,
                Duration.ZERO, LocalDateTime.MAX);

        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);


        List<Epic> allEpics = taskManager.getAllEpics();

        assertEquals(2, allEpics.size(), "Возвращаются не все эпики");
        assertNotNull(allEpics);
        assertEquals(2, taskManager.getPrioritizedTasks().size());
    }

    public void getTaskTest() {
        Task task = new Task("name", "description", 0, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("02.01.2023;12:00", FORMATTER));

        taskManager.addTask(task);
        final int taskId = task.getId();

        Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask);
        assertEquals(1, taskId);
        assertThrows(
                NullPointerException.class,
                () -> taskManager.getTask(0)
        );
    }

    public void getEpicTest() {
        Epic epic = new Epic("name", "description", 0, Status.NEW, Duration.ZERO, LocalDateTime.MIN);

        taskManager.addEpic(epic);

        final int epicId = epic.getId();

        Epic savedEpic = taskManager.getEpic(epicId);

        assertNotNull(savedEpic);
        assertEquals(1, epicId);
        assertThrows(
                NullPointerException.class,
                () -> taskManager.getEpic(0)
        );
    }

    public void getSubTaskTest() {
        SubTask subTask = new SubTask("name", "description", 0, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("02.01.2023;12:00", FORMATTER), 0);

        taskManager.addSubTask(subTask);

        final int subTaskId = subTask.getId();

        SubTask savedSubTask = taskManager.getSubTask(subTaskId);

        assertNotNull(savedSubTask);
        assertEquals(1, subTaskId);
        assertThrows(
                NullPointerException.class,
                () -> taskManager.getSubTask(0)
        );
    }

    public void removeAllTasksTest() {
        Task task1 = new Task("name1", "description1", 0, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("02.01.2023;12:00", FORMATTER));
        Task task2 = new Task("name2", "description2", 1, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("03.01.2023;12:00", FORMATTER));

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.removeAllTasks();

        List<Task> emptyList = Collections.emptyList();
        assertEquals(emptyList, taskManager.getAllTasks());
        assertEquals(emptyList, taskManager.getPrioritizedTasks());
        assertThrows(
                NullPointerException.class,
                () -> taskManager.getTask(1)
        );
    }

    public void removeAllEpicsTest() {
        Epic epic1 = new Epic("name1", "description1", 0, Status.NEW, Duration.ZERO,
                LocalDateTime.MIN);
        Epic epic2 = new Epic("name2", "description2", 0, Status.DONE, Duration.ZERO,
                LocalDateTime.MAX);
        SubTask subTask1 = new SubTask("name3", "description3", 0, Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.parse("03.01.2023;12:00", FORMATTER), 0);

        taskManager.addSubTask(subTask1);
        final int subTaskId = subTask1.getId();
        epic1.addSubTaskId(subTaskId);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        taskManager.removeAllEpics();

        List<Task> emptyList = Collections.emptyList();

        assertEquals(emptyList, taskManager.getAllEpics());
        assertEquals(emptyList, taskManager.getAllSubTasks());
        assertEquals(emptyList, taskManager.getPrioritizedTasks());
        assertThrows(
                NullPointerException.class,
                () -> taskManager.getEpic(1)
        );
    }

    public void removeAllSubTasksTest() {
        SubTask subTask1 = new SubTask("name1", "description1", 0, Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.parse("02.01.2023;12:00", FORMATTER), 0);
        SubTask subTask2 = new SubTask("name2", "description2", 0, Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.parse("03.01.2023;12:00", FORMATTER), 0);
        Epic epic = new Epic("name", "description", 0, Status.NEW, Duration.ZERO, LocalDateTime.MAX);

        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);

        epic.addSubTaskId(subTask1.getId());
        epic.addSubTaskId(subTask2.getId());
        taskManager.addEpic(epic);

        taskManager.removeAllSubTasks();

        List emptyList = Collections.emptyList();

        assertEquals(emptyList, taskManager.getAllSubTasks());
        assertEquals(emptyList, epic.getSubTaskIds());
        assertEquals(1, taskManager.getPrioritizedTasks().size());
        assertThrows(
                NullPointerException.class,
                () -> taskManager.getSubTask(1)
        );
    }

    public void removeTaskTest() {
        Task task1 = new Task("name1", "description1", 0, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("02.01.2023;12:00", FORMATTER));

        taskManager.addTask(task1);

        final int taskId = task1.getId();

        assertEquals(1, taskManager.getAllTasks().size());

        taskManager.removeTask(taskId);

        List<Task> emptyList = Collections.emptyList();

        assertEquals(emptyList, taskManager.getAllTasks());
        assertEquals(emptyList, taskManager.getPrioritizedTasks());
        assertThrows(
                NullPointerException.class,
                () -> taskManager.getTask(1)
        );
    }

    public void removeEpicTest() {
        Epic epic = new Epic("name", "description", 0, Status.NEW, Duration.ZERO, LocalDateTime.MAX);
        SubTask subTask1 = new SubTask("name1", "description1", 0, Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.parse("02.01.2023;12:00", FORMATTER), 0);

        taskManager.addSubTask(subTask1);
        epic.addSubTaskId(subTask1.getId());
        taskManager.addEpic(epic);

        final int epicId = epic.getId();

        assertEquals(1, taskManager.getAllEpics().size());

        taskManager.removeEpic(epicId);

        List<Task> emptyList = Collections.emptyList();

        assertEquals(emptyList, taskManager.getAllEpics());
        assertEquals(emptyList, taskManager.getPrioritizedTasks());
        assertEquals(emptyList, taskManager.getAllSubTasks());
        assertThrows(
                NullPointerException.class,
                () -> taskManager.getEpic(1)
        );
    }

    public void removeSubTaskTest() {
        SubTask subTask1 = new SubTask("name1", "description1", 0, Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.parse("02.01.2023;12:00", FORMATTER), 0);
        SubTask subTask2 = new SubTask("name2", "description2", 0, Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.parse("03.01.2023;12:00", FORMATTER), 0);
        Epic epic = new Epic("name", "description", 0, Status.NEW, Duration.ZERO, LocalDateTime.MAX);

        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        epic.addSubTaskId(subTask1.getId());
        epic.addSubTaskId(subTask2.getId());
        taskManager.addEpic(epic);

        taskManager.removeSubTask(1);

        assertEquals(1, taskManager.getAllSubTasks().size());
        assertEquals(1, epic.getSubTaskIds().size());
        assertEquals(taskManager.getAllSubTasks().size(), epic.getSubTaskIds().size());
        assertThrows(
                NullPointerException.class,
                () -> taskManager.getSubTask(1)
        );
    }

    public void getSubTasksOfEpicTest() {
        SubTask subTask1 = new SubTask("name1", "description1", 0, Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.parse("02.01.2023;12:00", FORMATTER), 0);
        SubTask subTask2 = new SubTask("name2", "description2", 0, Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.parse("03.01.2023;12:00", FORMATTER), 0);
        Epic epic = new Epic("name", "description", 0, Status.NEW, Duration.ZERO, LocalDateTime.MAX);

        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        epic.addSubTaskId(subTask1.getId());
        epic.addSubTaskId(subTask2.getId());
        taskManager.addEpic(epic);

        List<Integer> subTasks = List.of(subTask1.getId(), subTask2.getId());

        assertEquals(subTasks, epic.getSubTaskIds());
    }

    public void getHistoryTest() {
        SubTask subTask1 = new SubTask("name1", "description1", 0, Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.parse("02.01.2023;12:00", FORMATTER), 0);
        SubTask subTask2 = new SubTask("name2", "description2", 0, Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.parse("03.01.2023;12:00", FORMATTER), 0);
        Epic epic = new Epic("name3", "description3", 0, Status.NEW, Duration.ZERO,
                LocalDateTime.MAX);
        Task task1 = new Task("name4", "description4", 0, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("06.01.2023;12:00", FORMATTER));
        Task task2 = new Task("name5", "description5", 1, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("07.01.2023;12:00", FORMATTER));

        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        epic.addSubTaskId(subTask1.getId());
        epic.addSubTaskId(subTask2.getId());
        taskManager.addEpic(epic);
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        taskManager.getTask(4);
        taskManager.getTask(5);
        taskManager.getSubTask(1);
        taskManager.getEpic(3);
        taskManager.getTask(4);

        List<Task> historyTasks = List.of(task2, subTask1, epic, task1);

        assertEquals(historyTasks, taskManager.getHistory());
        assertNotNull(taskManager.getHistory());
    }

    public void getPrioritizedTasksTest() {
        SubTask subTask1 = new SubTask("name1", "description1", 0, Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.parse("02.01.2023;12:00", FORMATTER), 0);
        SubTask subTask2 = new SubTask("name2", "description2", 0, Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.parse("03.01.2023;12:00", FORMATTER), 0);
        Epic epic = new Epic("name3", "description3", 0, Status.NEW, Duration.ZERO,
                LocalDateTime.MAX);
        Task task1 = new Task("name4", "description4", 0, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("06.01.2023;12:00", FORMATTER));
        Task task2 = new Task("name5", "description5", 1, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("07.01.2023;12:00", FORMATTER));

        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        epic.addSubTaskId(subTask1.getId());
        epic.addSubTaskId(subTask2.getId());
        taskManager.addEpic(epic);
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        List<Task> tasks = List.of(epic, subTask1, subTask2, task1, task2);

        assertEquals(tasks, taskManager.getPrioritizedTasks());
    }

    public void taskValidationTest() {
        SubTask subTask1 = new SubTask("name1", "description1", 0, Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.parse("02.01.2023;12:00", FORMATTER), 0);
        SubTask subTask2 = new SubTask("name2", "description2", 0, Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.parse("02.01.2023;12:00", FORMATTER), 0);
        Task task1 = new Task("name3", "description3", 0, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("06.01.2023;12:00", FORMATTER));
        Task task2 = new Task("name4", "description4", 1, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("06.01.2023;12:00", FORMATTER));

        taskManager.addTask(task1);
        taskManager.addSubTask(subTask1);
        assertThrows(
                ManagerValidateException.class,
                () -> taskManager.addSubTask(subTask2)
        );

        assertThrows(
                ManagerValidateException.class,
                () -> taskManager.addTask(task2)
        );
    }

    public void epicStatusShouldBeNewWithEmptySubtasks() {
        Epic epic = new Epic("name1", "description1", 0, Status.DONE, Duration.ZERO,
                LocalDateTime.MAX);
        taskManager.addEpic(epic);
        assertEquals(Status.NEW, taskManager.getEpic(1).getStatus(), "Статус определяется" +
                " неправильно");
    }

    public void epicStatusShouldBeNew() {
        SubTask stask1 = new SubTask("name1", "description1", 0, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("02.01.2023;12:00", FORMATTER), 0);
        SubTask stask2 = new SubTask("name2", "description2", 0, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("03.01.2023;12:00", FORMATTER), 0);
        Epic epic = new Epic("name3", "description3", 0, Status.DONE, Duration.ZERO,
                LocalDateTime.MAX);

        taskManager.addSubTask(stask1);
        taskManager.addSubTask(stask2);
        epic.addSubTaskId(stask1.getId());
        epic.addSubTaskId(stask2.getId());
        taskManager.addEpic(epic);
        assertEquals(Status.NEW, taskManager.getEpic(3).getStatus(), "Статус определяется неправильно");
    }

    public void epicStatusShouldBeDone() {
        SubTask stask1 = new SubTask("name1", "description1", 0, Status.DONE, Duration.ofMinutes(60),
                LocalDateTime.parse("02.01.2023;12:00", FORMATTER), 0);
        SubTask stask2 = new SubTask("name2", "description2", 0, Status.DONE, Duration.ofMinutes(60),
                LocalDateTime.parse("03.01.2023;12:00", FORMATTER), 0);
        Epic epic = new Epic("name3", "description3", 0, Status.NEW, Duration.ZERO,
                LocalDateTime.MAX);

        taskManager.addSubTask(stask1);
        taskManager.addSubTask(stask2);
        epic.addSubTaskId(stask1.getId());
        epic.addSubTaskId(stask2.getId());
        taskManager.addEpic(epic);
        assertEquals(Status.DONE, taskManager.getEpic(3).getStatus(), "Статус определяется" +
                " неправильно");
    }

    public void epicStatusShouldBeInProgressWithNewAndDoneSubtasks() {
        SubTask stask1 = new SubTask("name1", "description1", 0, Status.DONE, Duration.ofMinutes(60),
                LocalDateTime.parse("02.01.2023;12:00", FORMATTER), 0);
        SubTask stask2 = new SubTask("name2", "description2", 0, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("03.01.2023;12:00", FORMATTER), 0);
        Epic epic = new Epic("name3", "description3", 0, Status.NEW, Duration.ZERO,
                LocalDateTime.MAX);

        taskManager.addSubTask(stask1);
        taskManager.addSubTask(stask2);
        epic.addSubTaskId(stask1.getId());
        epic.addSubTaskId(stask2.getId());
        taskManager.addEpic(epic);
        assertEquals(Status.IN_PROGRESS, taskManager.getEpic(3).getStatus(), "Статус" +
                " определяется неправильно");
    }

    public void epicStatusShouldBeInProgressWithInProgressSubtasks() {
        SubTask stask1 = new SubTask("name1", "description1", 0, Status.IN_PROGRESS,
                Duration.ofMinutes(60), LocalDateTime.parse("02.01.2023;12:00", FORMATTER), 0);
        SubTask stask2 = new SubTask("name2", "description2", 0, Status.IN_PROGRESS,
                Duration.ofMinutes(60), LocalDateTime.parse("03.01.2023;12:00", FORMATTER), 0);
        Epic epic = new Epic("name3", "description3", 0, Status.NEW, Duration.ZERO,
                LocalDateTime.MAX);

        taskManager.addSubTask(stask1);
        taskManager.addSubTask(stask2);
        epic.addSubTaskId(stask1.getId());
        epic.addSubTaskId(stask2.getId());
        taskManager.addEpic(epic);
        assertEquals(Status.IN_PROGRESS, taskManager.getEpic(3).getStatus(), "Статус" +
                " определяется неправильно");
    }
}
