package AllManagers;

public class Managers {

    /**
     * Утилитарный класс Managers занимается созданием менеджера задач и менеждера истории задач;
     */
    public static HistoryManager historyManager = new InMemoryHistoryManager();

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return historyManager;
    }
}
