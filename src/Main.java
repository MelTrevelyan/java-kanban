import Managers.InMemoryTaskManager;
import Tasks.Epic;
import Tasks.Statuses;
import Tasks.SubTask;
import Tasks.Task;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        Task task1 = new Task("Купить цветы", "маме", 0, Statuses.NEW);
        Task task2 = new Task("Надуть шарики", "Чтобы украсить квартиру", 0, Statuses.DONE);

        SubTask stask1 = new SubTask("Убрать документы", "в папки", 0, Statuses.NEW, 0);
        SubTask stask2 = new SubTask("Сгруппировать вещи", "по коробкам", 0, Statuses.DONE, 0);
        Epic epic1 = new Epic("Переезд", "В Казань", 0, Statuses.DONE);

        SubTask task21 = new SubTask("Выбрать модель", "для первой машины", 0,
                Statuses.IN_PROGRESS, 0);
        Epic epic2 = new Epic("Купить машину", "тойоту", 0, Statuses.NEW);

        InMemoryTaskManager manager = new InMemoryTaskManager();

        manager.addTask(task1);
        manager.addTask(task2);

        manager.addSubTask(stask1);
        manager.addSubTask(stask2);
        epic1.addSubTaskId(stask1.getId());
        epic1.addSubTaskId(stask2.getId());
        manager.addEpic(epic1);

        manager.addSubTask(task21);
        epic2.addSubTaskId(task21.getId());
        manager.addEpic(epic2);

        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubTasks());

        Task task10 = new Task("Купить цветы", "маме", 1, Statuses.DONE);
        manager.updateTask(task10);

        SubTask stask10 = new SubTask("Убрать документы", "в папки", 3, Statuses.DONE, 0);
        manager.updateSubTask(stask10);

        Epic epic20 = new Epic("Купить машину", "тойоту", 7, Statuses.DONE);
        manager.updateEpic(epic20);

        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubTasks());

        manager.getHistory();

        manager.getEpic(5);
        manager.getSubTask(3);
        manager.getSubTask(4);
        manager.getTask(1);
        manager.getTask(2);


        manager.getHistory();
    }
}
