package managers_types;

public class Managers {

    public static InMemoryHistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static InMemoryTaskManager getDefaultTaskManager() {
        return new InMemoryTaskManager();
    }
}
