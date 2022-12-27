import managers.Managers;
import managers.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        Task task1 = new Task("Купить цветы", "маме", 0, Status.NEW);
        Task task2 = new Task("Надуть шарики", "Чтобы украсить квартиру", 0, Status.DONE);

        SubTask stask1 = new SubTask("Убрать документы", "в папки", 0, Status.NEW, 0);
        SubTask stask2 = new SubTask("Сгруппировать вещи", "по коробкам", 0, Status.DONE, 0);
        Epic epic1 = new Epic("Переезд", "В Казань", 0, Status.DONE);

        SubTask task21 = new SubTask("Выбрать модель", "для первой машины", 0,
                Status.IN_PROGRESS, 0);
        Epic epic2 = new Epic("Купить машину", "тойоту", 0, Status.NEW);

        TaskManager manager = Managers.getDefault();

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

        Task task10 = new Task("Купить цветы", "маме", 1, Status.DONE);
        manager.updateTask(task10);

        SubTask stask10 = new SubTask("Убрать документы", "в папки", 3, Status.DONE, 0);
        manager.updateSubTask(stask10);

        Epic epic20 = new Epic("Купить машину", "тойоту", 7, Status.DONE);
        manager.updateEpic(epic20);

        System.out.println(manager.getHistory());
        manager.getEpic(5);
        System.out.println(manager.getHistory());
        manager.getSubTask(3);
        System.out.println(manager.getHistory());
        manager.getSubTask(4);
        System.out.println(manager.getHistory());
        manager.getTask(1);
        System.out.println(manager.getHistory());
        manager.getTask(2);
        System.out.println(manager.getHistory());
        manager.getEpic(5);
        System.out.println(manager.getHistory());
        manager.getSubTask(3);
        System.out.println(manager.getHistory());
        manager.removeTask(1);
        System.out.println(manager.getHistory());
        manager.removeEpic(5);
        System.out.println(manager.getHistory());
        manager.removeTask(2);
        System.out.println(manager.getHistory());
    }
}
