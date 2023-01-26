package managers;

import managers.filebacked.FileBackedTasksManager;
import managers.inmemory.InMemoryHistoryManager;
import managers.inmemory.InMemoryTaskManager;

import java.io.File;

/**
 * Утилитарный класс Managers занимается созданием менеджера задач и менеждера истории задач;
 */
public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static TaskManager getFileBacked() {
        File save = new File("resources/save.csv");
        return new FileBackedTasksManager(save);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
