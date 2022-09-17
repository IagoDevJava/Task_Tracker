package org.example.Tests;

import org.example.manager.interfaces_and_utilities.TaskManager;
import org.example.manager.managers_types.FileBackedTasksManager;
import org.example.manager.managers_types.InMemoryTaskManager;
import org.example.tasks.Epic;
import org.example.tasks.Subtask;
import org.example.tasks.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

class FileBackedTasksManagerTest extends TaskManagerTest {
    private FileBackedTasksManager fileBackedTasksManager;
    private InMemoryTaskManager inMemoryTaskManager;
    File file;
    private Task task1;
    private Task task2;
    private Epic epic1;
    private Epic epic2;
    private Subtask subtask;
    String expectedTask1;
    String expectedTask2;
    String expectedEpic;
    String expectedSubtask;

    @BeforeEach
    public void beforeEach() {
        file = new File("file.csv");
        fileBackedTasksManager = new FileBackedTasksManager(file);
        inMemoryTaskManager = new InMemoryTaskManager();
        task1 = new Task("Task 1", "DescriptionTask 1",
                "2022-08-25 | 10:00", 1, 30);
        task2 = new Task("Task 2", "DescriptionTask 2",
                "2022-08-24 | 10:00", 1, 30);
        epic1 = new Epic("Epic 1", "DescriptionEpic 1", fileBackedTasksManager);
        epic2 = new Epic("Epic 1", "DescriptionEpic 1", inMemoryTaskManager);
        subtask = new Subtask("Subtask 1", "DescriptionSubtask 1",
                "2022-08-25 | 10:00", 1, 30, 1);
        expectedTask1 = "1,TASK,Task 1,NEW,DescriptionTask 1,2022-08-25T10:00,PT1H30M,2022-08-25T11:30";
        expectedTask2 = "2,TASK,Task 1,NEW,DescriptionTask 1,2022-08-26T10:00,PT1H30M,2022-08-26T11:30";
        expectedEpic = "1,EPIC,Epic 1,NEW,DescriptionEpic 1," +
                "+999999999-12-31T23:59:59.999999999,PT-17531639991215H-59M-59.999999999S,-999999999-01-01T00:00";
        expectedSubtask = "2,SUBTASK,Subtask 1,NEW,DescriptionSubtask 1,1,2022-08-25T10:00,PT1H30M,2022-08-25T11:30";
    }

    @Test
    void saveTestTask() {
        fileBackedTasksManager.createTask(task1);
        fileBackedTasksManager.getTaskByID(1);

        FileBackedTasksManager.loadFromFile(file);
        Task taskByID = fileBackedTasksManager.getTaskByIdWithoutStory(task1.getId());

        Assertions.assertEquals(expectedTask1, taskByID.toString(), "Задачи сохранены неверно");
    }

    @Test
    void saveTestEpic() {
        fileBackedTasksManager.createTask(epic1);
        fileBackedTasksManager.getEpicByID(1);

        FileBackedTasksManager.loadFromFile(file);
        Epic epicByID = fileBackedTasksManager.getEpicByIdWithoutStory(epic1.getId());

        Assertions.assertEquals(expectedEpic, epicByID.toString(), "Эпики сохранены неверно");
    }

    @Test
    void saveTestSubtask() {
        fileBackedTasksManager.createTask(epic1);
        fileBackedTasksManager.createTask(subtask);
        fileBackedTasksManager.getSubtaskByID(2);

        FileBackedTasksManager.loadFromFile(file);
        Subtask subtaskByID = fileBackedTasksManager.getSubtaskByIdWithoutStory(subtask.getId());

        Assertions.assertEquals(expectedSubtask, subtaskByID.toString(), "Подзадачи сохранены неверно");
    }

    @Test
    void shouldLoadFromFileNewTask() {
        fileBackedTasksManager.createTask(task1);
        fileBackedTasksManager.getTaskByID(1);

        FileBackedTasksManager.loadFromFile(file);
        Task newTask = fileBackedTasksManager.getTaskByID(1);

        Assertions.assertEquals(task1, newTask, "Новая задача не получена из файла");
    }

    @Test
    void shouldLoadFromFileListOfTasks() {
        fileBackedTasksManager.createTask(task1);
        fileBackedTasksManager.getTaskByID(1);
        inMemoryTaskManager.createTask(task1);

        FileBackedTasksManager.loadFromFile(file);

        Assertions.assertEquals(inMemoryTaskManager.getListOfTask(), fileBackedTasksManager.getListOfTask(),
                "Список задач после выгрузки не совпадает");
    }

    @Test
    void shouldLoadFromFileListOfEpics() {
        fileBackedTasksManager.createTask(epic1);
        fileBackedTasksManager.getEpicByID(1);
        inMemoryTaskManager.createTask(epic1);

        FileBackedTasksManager.loadFromFile(file);

        Assertions.assertEquals(inMemoryTaskManager.getListOfEpic(), fileBackedTasksManager.getListOfEpic(),
                "Список задач после выгрузки не совпадает");
    }

    @Test
    void shouldLoadFromFileListOfSubtasks() {
        fileBackedTasksManager.createTask(epic1);
        fileBackedTasksManager.createTask(subtask);
        fileBackedTasksManager.getSubtaskByID(2);
        inMemoryTaskManager.createTask(epic2);
        inMemoryTaskManager.createTask(subtask);

        FileBackedTasksManager.loadFromFile(file);

        Assertions.assertEquals(inMemoryTaskManager.getListOfSubtask(), fileBackedTasksManager.getListOfSubtask(),
                "Список задач после выгрузки не совпадает");
    }

    @Test
    void shouldLoadFromFileListOfPrioritized() {
        fileBackedTasksManager.createTask(task1);
        fileBackedTasksManager.createTask(task2);
        fileBackedTasksManager.getTaskByID(1);
        inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.createTask(task2);

        FileBackedTasksManager.loadFromFile(file);

        Assertions.assertEquals(inMemoryTaskManager.getPrioritizedTasks(), fileBackedTasksManager.getPrioritizedTasks(),
                "Список отсортированных задач после выгрузки не совпадает");
    }

    @Test
    void shouldLoadFromFileListOfHistory() {
        fileBackedTasksManager.createTask(task1);
        fileBackedTasksManager.createTask(task2);
        fileBackedTasksManager.getTaskByID(2);
        fileBackedTasksManager.getTaskByID(1);
        inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.createTask(task2);
        inMemoryTaskManager.getTaskByID(2);
        inMemoryTaskManager.getTaskByID(1);

        FileBackedTasksManager.loadFromFile(file);

        Assertions.assertEquals(inMemoryTaskManager.getHistoryManager().getHistory(),
                fileBackedTasksManager.getHistoryManager().getHistory(),
                "Список задач в истории после выгрузки не совпадает");
    }

    @Override
    TaskManager createTaskManager() {
        File file = new File("file.csv");
        return new FileBackedTasksManager(file);
    }
}