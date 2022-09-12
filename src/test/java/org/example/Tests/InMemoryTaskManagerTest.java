package org.example.Tests;

import org.example.interfaces_and_utilities.TaskManager;
import org.example.managers_types.InMemoryTaskManager;

class InMemoryTaskManagerTest extends TaskManagerTest {

    @Override
    TaskManager createTaskManager() {
        return new InMemoryTaskManager();
    }
}