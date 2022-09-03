package Tests;

import interfaces_and_utilities.TaskManager;
import managers_types.InMemoryTaskManager;

class InMemoryTaskManagerTest extends TaskManagerTest {

    @Override
    TaskManager createTaskManager() {
        return new InMemoryTaskManager();
    }
}