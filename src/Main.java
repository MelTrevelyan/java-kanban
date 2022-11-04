import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        Task task1 = new Task("Купить цветы", "маме", 0, "NEW");
        Task task2 = new Task("Надуть шарики", "Чтобы украсить квартиру", 0, "DONE");

        SubTask stask1 = new SubTask("Убрать документы", "в папки", 0, "NEW", 0);
        SubTask stask2 = new SubTask("Сгруппировать вещи", "по коробкам", 0, "DONE", 0);
        Epic epic1 = new Epic("Переезд", "В Казань", 0, "DONE");

        SubTask task21 = new SubTask("Выбрать модель", "для первой машины", 0,
                "IN_PROGRESS", 0);
        Epic epic2 = new Epic("Купить машину", "тойоту", 0, "NEW");

        Manager manager = new Manager();

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

        Task task10 = new Task("Купить цветы", "маме", 1, "DONE");
        manager.updateTask(task10);

        SubTask stask10 = new SubTask("Убрать документы", "в папки", 3, "DONE", 0);
        manager.updateSubTask(stask10);

        Epic epic20 = new Epic("Купить машину", "тойоту", 7, "DONE");
        manager.updateEpic(epic20);

        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubTasks());



        manager.removeTask(task1.getId());
        manager.removeSubTask(stask10.getId());
        manager.removeEpic(epic20.getId());
    }
}
