package org.example.Tests;

import org.example.manager.interfaces_and_utilities.TaskManager;
import org.example.manager.managers_types.InMemoryTaskManager;

class InMemoryTaskManagerTest extends TaskManagerTest {

    @Override
    TaskManager createTaskManager() {
        return new InMemoryTaskManager();
    }
}