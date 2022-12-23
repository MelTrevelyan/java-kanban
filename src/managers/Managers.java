package managers;

import managers.inmemory.InMemoryHistoryManager;
import managers.inmemory.InMemoryTaskManager;

/**
 * Утилитарный класс Managers занимается созданием менеджера задач и менеждера истории задач;
 */
public class Managers {

    public static HistoryManager historyManager = new InMemoryHistoryManager();

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return historyManager;
    }
}
