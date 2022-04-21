package interfaces_and_utilities;

import managers_types.InMemoryHistoryManager;
import managers_types.InMemoryTaskManager;

public class Managers {

    public static InMemoryHistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static InMemoryTaskManager getDefaultTaskManager() {
        return new InMemoryTaskManager();
    }
}