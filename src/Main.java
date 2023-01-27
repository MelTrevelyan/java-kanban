import managers.Managers;
import managers.TaskManager;
import server.HttpTaskServer;
import server.KVServer;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static tasks.Task.FORMATTER;

public class Main {
/*
    public static void main(String[] args) throws IOException {
        System.out.println("Поехали!");
        new KVServer().start();
        Task task1 = new Task("Купить цветы", "маме", 0, Status.NEW, Duration.ofMinutes(100),
                LocalDateTime.parse("13.01.2023;14:30", FORMATTER));
        Task task2 = new Task("Надуть шарики", "Чтобы украсить квартиру", 0, Status.DONE,
                Duration.ofMinutes(200), LocalDateTime.parse("10.02.2023;10:00", FORMATTER));
        Task task3 = new Task("Приготовить пирог", "на ужин", 4, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.parse("16.02.2023;17:00", FORMATTER));
        SubTask stask1 = new SubTask("Убрать документы", "в папки", 0, Status.NEW,
                Duration.ofMinutes(20), LocalDateTime.parse("11.02.2023;15:00", FORMATTER), 0);
        SubTask stask2 = new SubTask("Сгруппировать вещи", "по коробкам", 0, Status.DONE,
                Duration.ofMinutes(300), LocalDateTime.parse("11.02.2023;21:00", FORMATTER), 0);
        SubTask stask3 = new SubTask("name2", "description2", 1, Status.NEW, Duration.ofMinutes(30),
                LocalDateTime.parse("20.02.2023;10:00", FORMATTER), 0);
        Epic epic1 = new Epic("Переезд", "В Казань", 0, Status.DONE, Duration.ofMinutes(0),
                LocalDateTime.parse("15.02.2023;14:00", FORMATTER));
        Epic epic2 = new Epic("name1", "description1", 3, Status.DONE, Duration.ofMinutes(500),
                LocalDateTime.MAX);

        HttpTaskServer server = new HttpTaskServer();
        TaskManager manager = server.getTaskManager();

        manager.addSubTask(stask1);
        manager.getSubTask(1);

        manager.addSubTask(stask2);
        epic1.addSubTaskId(stask1.getId());
        epic1.addSubTaskId(stask2.getId());
        manager.addEpic(epic1);

        manager.getSubTask(2);
        manager.getEpic(3);
        manager.getSubTask(1);

        manager.addTask(task1);
        manager.addTask(task2);
        manager.getTask(4);

        manager.updateSubTask(stask3);

        System.out.println(manager.getPrioritizedTasks());
    }

 */
}
