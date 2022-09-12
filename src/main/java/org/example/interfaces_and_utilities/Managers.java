package org.example.interfaces_and_utilities;

import org.example.managers_types.InMemoryHistoryManager;
import org.example.managers_types.InMemoryTaskManager;

public class Managers {

    public static InMemoryHistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static InMemoryTaskManager getDefaultTaskManager() {
        return new InMemoryTaskManager();
    }
}