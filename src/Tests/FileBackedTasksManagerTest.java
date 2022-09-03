package Tests;

import interfaces_and_utilities.TaskManager;
import managers_types.FileBackedTasksManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks_types.Epic;
import tasks_types.Subtask;
import tasks_types.Task;

import java.io.File;

class FileBackedTasksManagerTest extends TaskManagerTest {
    private FileBackedTasksManager fileBackedTasksManager;
    File file;
    private Task task;
    private Epic epic;
    private Subtask subtask;
    String expectedTask;
    String expectedEpic;
    String expectedSubtask;

    @BeforeEach
    public void beforeEach() {
        file = new File("file.csv");
        fileBackedTasksManager = new FileBackedTasksManager(file);
        task = new Task("Task 1", "DescriptionTask 1",
                "2022-08-25 | 10:00", 12, 30);
        epic = new Epic("Epic 1", "DescriptionEpic 1", fileBackedTasksManager);
        expectedTask = "1,TASK,Task 1,NEW,DescriptionTask 1,2022-08-25T10:00,PT12H30M,2022-08-25T22:30";
        expectedEpic = "1,EPIC,Epic 1,NEW,DescriptionEpic 1";
        expectedSubtask = "2,SUBTASK,Subtask 1,NEW,DescriptionSubtask 1,1";
    }

    @Test
    void saveTest() {
        fileBackedTasksManager.createTask(task);
        fileBackedTasksManager.getTaskByID(1);

        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(file);
        Task taskByID = fileBackedTasksManager.getTaskByIdWithoutStory(task.getNumberId());

        Assertions.assertEquals(expectedTask, taskByID.toString());
    }

    @Test
    void shouldLoadFromFile() {
        fileBackedTasksManager.createTask(task);
        fileBackedTasksManager.getTaskByID(1);

        FileBackedTasksManager.loadFromFile(file);
        Task newTask = fileBackedTasksManager.getTaskByID(1);

        Assertions.assertEquals(task, newTask);
    }

    @Override
    TaskManager createTaskManager() {
        File file = new File("file.csv");
        return new FileBackedTasksManager(file);
    }
}