package managers;

import managers.inmemory.InMemoryHistoryManager;
import server.HttpTaskManager;

import java.net.URI;

/**
 * Утилитарный класс Managers занимается созданием менеджера задач и менеждера истории задач;
 */
public class Managers {

    public static TaskManager getDefault() {
            return new HttpTaskManager(URI.create("http://localhost:8078/register"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
