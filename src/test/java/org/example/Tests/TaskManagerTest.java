package org.example.Tests;

import org.example.manager.interfaces_and_utilities.TaskManager;
import org.example.tasks.Epic;
import org.example.tasks.Status;
import org.example.tasks.Subtask;
import org.example.tasks.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    String expectedTask;
    String expectedEpic;
    String expectedSubtask;
    private T taskManager;
    private Task task;
    private Epic epic;
    private Subtask subtask;

    abstract T createTaskManager();

    @BeforeEach
    public void updateTaskManager() {
        taskManager = createTaskManager();
        task = new Task("Task 1", "DescriptionTask 1",
                "2022-08-25 | 10:00", 1, 10);
        epic = new Epic("Epic 1", "DescriptionEpic 1", taskManager);
        expectedTask = "1,TASK,Task 1.1,NEW,DescriptionTask 1,2022-08-25T10:00,PT1H20M,2022-08-25T11:20";
        expectedEpic = "1,EPIC,Epic 1.1,IN_PROGRESS,DescriptionEpic 1," +
                "2022-08-25T10:00,PT-17531639991214H-39M-59.999999999S,2022-08-25T11:20";
        expectedSubtask = "2,SUBTASK,Subtask 1.1,NEW,DescriptionSubtask 1.1,1,2022-08-25T10:00,PT1H20M,2022-08-25T11:20";
    }

    @Test
    public void shouldCreateTask() {
        final long taskId = taskManager.createTask(task);
        final Task savedTask = taskManager.getTaskByID(taskId);

        assertEquals(task, savedTask, "Задачи не совпадают.");
    }

    @Test
    public void shouldCreateTaskNotNull() {
        final long taskId = taskManager.createTask(task);
        final Task savedTask = taskManager.getTaskByID(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
    }

    @Test
    public void shouldCreateTaskWithUnknownId() {
        taskManager.createTask(task);

        final Task savedTask = taskManager.getTaskByID(2);

        assertNull(savedTask, "Задача не null.");
    }

    @Test
    public void shouldCreateEpic() {
        final long epicId = taskManager.createTask(epic);
        final Epic savedEpic = taskManager.getEpicByID(epicId);

        assertEquals(epic, savedEpic, "Задачи не совпадают.");
    }

    @Test
    public void shouldCreateEpicNotNull() {
        final long epicId = taskManager.createTask(epic);
        final Task savedEpic = taskManager.getEpicByID(epicId);

        assertNotNull(savedEpic, "Задача не найдена.");
    }

    @Test
    public void shouldCreateEpicWithUnknownId() {
        final Epic savedEpic = taskManager.getEpicByID(2);

        assertNull(savedEpic, "Задача не null.");
    }

    @Test
    public void shouldCreateSubtask() {
        taskManager.createTask(epic);
        subtask = new Subtask("Subtask 1", "DescriptionSubtask 1",
                "2022-08-23 | 20:30", 11, 38, epic.getId());
        final long subtaskId = taskManager.createTask(subtask);

        final Subtask savedSubtask = taskManager.getSubtaskByID(subtaskId);

        assertEquals(subtask, savedSubtask, "Задачи не совпадают.");
    }

    @Test
    public void shouldCreateSubtaskNotNull() {
        taskManager.createTask(epic);
        subtask = new Subtask("Subtask 1", "DescriptionSubtask 1",
                "2022-08-23 | 20:30", 11, 38, epic.getId());
        final long subtaskId = taskManager.createTask(subtask);
        final Subtask savedSubtask = taskManager.getSubtaskByID(subtaskId);

        assertNotNull(savedSubtask, "Задача не найдена.");
    }

    @Test
    public void shouldCreateSubtaskWithUnknownId() {
        final Subtask savedSubtask = taskManager.getSubtaskByID(3);

        assertNull(savedSubtask, "Задача не null.");
    }

    @Test
    public void shouldGetListOfTask() {
        taskManager.createTask(task);

        final List<Task> tasks = taskManager.getListOfTask();

        assertEquals(1, tasks.size(), "Неверное количество задач.");
    }

    @Test
    public void shouldGetTask() {
        taskManager.createTask(task);

        final List<Task> tasks = taskManager.getListOfTask();

        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void shouldNotGetListOfTask() {
        taskManager.createTask(task);

        final List<Task> tasks = taskManager.getListOfTask();

        assertNotNull(tasks, "Задачи не возвращаются.");
    }

    @Test
    public void shouldGetListOfEpic() {
        taskManager.createTask(epic);

        final List<Task> epics = taskManager.getListOfEpic();

        assertEquals(1, epics.size(), "Неверное количество задач.");
    }

    @Test
    public void shouldGetEpic() {
        taskManager.createTask(epic);

        final List<Task> epics = taskManager.getListOfEpic();

        assertEquals(epic, epics.get(0), "Задачи не совпадают.");
    }

    @Test
    public void shouldNotGetListOfEpic() {
        taskManager.createTask(epic);

        final List<Task> epics = taskManager.getListOfTask();

        assertNotNull(epics, "Задачи не возвращаются.");
    }

    @Test
    public void shouldGetListOfSubtask() {
        taskManager.createTask(epic);
        subtask = new Subtask("Subtask 1", "DescriptionSubtask 1",
                "2022-08-23 | 20:30", 11, 38, epic.getId());
        taskManager.createTask(subtask);

        final List<Task> subtask = taskManager.getListOfSubtask();

        assertEquals(1, subtask.size(), "Неверное количество задач.");
    }

    @Test
    public void shouldGetSubtask() {
        taskManager.createTask(epic);
        subtask = new Subtask("Subtask 1", "DescriptionSubtask 1",
                "2022-08-23 | 20:30", 11, 38, epic.getId());
        taskManager.createTask(subtask);

        final List<Task> subtask = taskManager.getListOfEpic();

        assertEquals(epic, subtask.get(0), "Задачи не совпадают.");
    }

    @Test
    public void shouldNotGetListOfSubtask() {
        taskManager.createTask(epic);
        subtask = new Subtask("Subtask 1", "DescriptionSubtask 1",
                "2022-08-23 | 20:30", 11, 38, epic.getId());
        taskManager.createTask(subtask);

        final List<Task> subtasks = taskManager.getListOfTask();

        assertNotNull(subtasks, "Задачи не возвращаются.");
    }

    @Test
    public void shouldBeEpicForSubtask() {
        taskManager.createTask(epic);
        subtask = new Subtask("Subtask 1", "DescriptionSubtask 1",
                "2022-08-23 | 20:30", 11, 38,
                epic.getId());
        taskManager.createTask(subtask);

        final Epic savedEpic = taskManager.getEpicByID(subtask.getEpicId());

        assertEquals(epic, savedEpic, "Задачи не совпадают.");
    }

    @Test
    public void willTheTaskListBeCleared() {
        taskManager.createTask(task);
        taskManager.deleteListOfTask();

        assertEquals(0, taskManager.getListOfTask().size(), "Неверное количество задач.");
    }

    @Test
    public void willTheTaskListBeNotCleared() {
        taskManager.createTask(task);

        taskManager.deleteListOfTask();
        boolean isClearLIstOfTasks = taskManager.getListOfTask().isEmpty();

        assertTrue(isClearLIstOfTasks, "Список задач не очищен.");
    }

    @Test
    public void willTheEpicListBeCleared() {
        taskManager.createTask(epic);
        taskManager.deleteListOfEpic();

        assertEquals(0, taskManager.getListOfEpic().size(), "Список эпиков не очищен.");
        assertEquals(0, taskManager.getListOfSubtask().size(), "Список подзадач не очищен.");
    }

    @Test
    public void willTheEpicListBeNotCleared() {
        taskManager.createTask(epic);

        taskManager.deleteListOfEpic();
        boolean isClearLIstOfEpic = taskManager.getListOfEpic().isEmpty();
        boolean isClearLIstOfSubtasksThisEpic = epic.getIdsOfSubtasksEpic().isEmpty();

        assertTrue(isClearLIstOfEpic, "Список эпиков не очищен.");
        assertTrue(isClearLIstOfSubtasksThisEpic, "Список подзадач эпика не очищен.");
    }

    @Test
    public void willTheSubtaskBeCleared() {
        taskManager.createTask(epic);
        subtask = new Subtask("Subtask 1", "DescriptionSubtask 1",
                "2022-08-23 | 20:30", 11, 38, epic.getId());
        taskManager.createTask(subtask);
        taskManager.deleteListOfSubtask();

        assertEquals(0, taskManager.getListOfSubtask().size(), "Список подзадач не очищен.");
    }

    @Test
    public void willTheSubtaskListBeNotCleared() {
        taskManager.createTask(epic);
        subtask = new Subtask("Subtask 1", "DescriptionSubtask 1",
                "2022-08-23 | 20:30", 11, 38, epic.getId());
        taskManager.createTask(subtask);
        taskManager.deleteListOfSubtask();

        boolean isClearLIstOfSubtasks = taskManager.getListOfSubtask().isEmpty();
        boolean isClearLIstOfSubtaskThisEpic = epic.getIdsOfSubtasksEpic().isEmpty();

        assertTrue(isClearLIstOfSubtasks, "Список подзадач не очищен.");
        assertTrue(isClearLIstOfSubtaskThisEpic, "Список подзадач эпика не очищен.");
    }

    @Test
    public void shouldGetTaskByID() {
        taskManager.createTask(task);

        assertEquals(task, taskManager.getTaskByID(1), "Номер задачи не найден");
    }

    @Test
    public void shouldNotGetTaskByWithUnknownId() {
        final Task savedTask = taskManager.getTaskByID(2);

        assertNull(savedTask, "Задача не null.");
    }

    @Test
    public void shouldGetEpicByID() {
        taskManager.createTask(epic);

        assertEquals(epic, taskManager.getEpicByID(1), "Номер эпика не найден");
    }

    @Test
    public void shouldNotGetEpicByWithUnknownId() {
        final Epic savedEpic = taskManager.getEpicByID(2);

        assertNull(savedEpic, "Задача не null.");
    }

    @Test
    public void shouldGetSubtaskByID() {
        taskManager.createTask(epic);
        subtask = new Subtask("Subtask 1", "DescriptionSubtask 1",
                "2022-08-23 | 20:30", 11, 38, epic.getId());
        taskManager.createTask(subtask);

        assertEquals(subtask, taskManager.getSubtaskByID(2), "Номер подзадачи не найден");
    }

    @Test
    public void shouldNotGetSubtaskByWithUnknownId() {
        final Subtask savedSubtask = taskManager.getSubtaskByID(3);

        assertNull(savedSubtask, "Задача не null.");
    }

    @Test
    void shouldUpdateTask() {
        taskManager.createTask(task);
        Task newTask = new Task("Task 1.1", "DescriptionTask 1",
                "2022-08-25 | 10:00", 1, 20);
        newTask.setId(task.getId());
        taskManager.updateTask(newTask);

        assertEquals(expectedTask, task.toString(), "Задача не обновлена");
    }

    @Test
    void shouldNotUpdateTask() {
        taskManager.createTask(task);
        Task newTask = new Task("Task 1.1", "DescriptionTask 1",
                "2022-08-25 | 10:00", 1, 20);
        newTask.setId(task.getId());
        taskManager.updateTask(newTask);

        boolean isUpdateTask = newTask.equals(task);

        assertFalse(isUpdateTask, "Задачи обновились");
    }


    @Test
    void shouldUpdateEpic() {
        taskManager.createTask(epic);
        Epic newEpic = new Epic("Epic 1.1", "DescriptionEpic 1", taskManager);
        taskManager.createTask(new Subtask("Subtask 1.1", "DescriptionSubtask 1",
                "2022-08-25 | 10:00", 1, 20, 1));
        newEpic.setId(epic.getId());

        taskManager.updateEpic(newEpic);

        assertEquals(expectedEpic, epic.toString());
    }

    @Test
    void shouldNotUpdateEpic() {
        taskManager.createTask(epic);
        Epic newEpic = new Epic("Epic 1.1", "DescriptionEpic 1", taskManager);
        taskManager.createTask(new Subtask("Subtask 1.1", "DescriptionSubtask 1",
                "2022-08-25 | 10:00", 1, 20, 1));
        newEpic.setId(epic.getId());

        taskManager.updateEpic(newEpic);
        boolean isUpdateEpic = newEpic.equals(epic);

        assertFalse(isUpdateEpic, "Задачи обновились");
    }

    @Test
    void shouldUpdateSubtask() {
        taskManager.createTask(epic);
        subtask = new Subtask("Subtask 1", "DescriptionSubtask 1",
                "2022-08-25 | 10:00", 1, 10, epic.getId());
        taskManager.createTask(subtask);
        Subtask newSubtask = new Subtask("Subtask 1.1", "DescriptionSubtask 1.1",
                "2022-08-25 | 10:00", 1, 20, epic.getId());
        newSubtask.setId(subtask.getId());

        taskManager.updateSubtask(newSubtask);

        assertEquals(expectedSubtask, subtask.toString());
    }

    @Test
    void shouldNotUpdateSubtask() {
        taskManager.createTask(epic);
        subtask = new Subtask("Subtask 1", "DescriptionSubtask 1",
                "2022-08-25 | 10:00", 1, 10, epic.getId());
        taskManager.createTask(subtask);
        Subtask newSubtask = new Subtask("Subtask 1.1", "DescriptionSubtask 1",
                "2022-08-25 | 10:00", 1, 20, epic.getId());
        newSubtask.setId(subtask.getId());

        taskManager.updateSubtask(newSubtask);
        boolean isUpdateSubtask = newSubtask.equals(subtask);

        assertFalse(isUpdateSubtask, "Задачи обновились");
    }

    @Test
    public void shouldDeleteTaskForID() {
        taskManager.createTask(task);
        taskManager.deleteTaskForID(task.getId());

        assertNull(taskManager.getTaskByID(task.getId()), "Задача не удалилась");
    }

    @Test
    public void shouldNotDeleteTaskForID() {
        taskManager.createTask(task);
        taskManager.deleteTaskForID(2);

        assertNotNull(taskManager.getTaskByID(task.getId()), "Задача удалилась");
    }

    @Test
    public void shouldDeleteEpicForID() {
        taskManager.createTask(epic);
        taskManager.deleteEpicForID(epic.getId());

        assertNull(taskManager.getEpicByID(epic.getId()), "Эпик не удалился");
    }

    @Test
    public void shouldNotDeleteEpicForID() {
        taskManager.createTask(epic);
        taskManager.deleteEpicForID(2);

        assertNotNull(taskManager.getEpicByID(epic.getId()), "Эпик удалился");
    }

    @Test
    public void shouldDeleteSubtaskForID() {
        taskManager.createTask(epic);
        subtask = new Subtask("Subtask 1", "DescriptionSubtask 1",
                "2022-08-23 | 20:30", 11, 38, epic.getId());
        taskManager.createTask(subtask);
        taskManager.deleteSubtaskForID(subtask.getId());

        assertNull(taskManager.getSubtaskByID(subtask.getId()), "Подзадача не удалилась");
    }

    @Test
    public void shouldNotDeleteSubtaskForID() {
        taskManager.createTask(epic);
        subtask = new Subtask("Subtask 1", "DescriptionSubtask 1",
                "2022-08-23 | 20:30", 11, 38, epic.getId());
        taskManager.createTask(subtask);
        taskManager.deleteSubtaskForID(3);

        assertNotNull(taskManager.getSubtaskByID(subtask.getId()), "Подзадача не удалилась");
    }

    @Test
    public void shouldSetStatusTaskInProgress() {
        taskManager.createTask(task);
        taskManager.setStatusForTask(task, Status.IN_PROGRESS);

        assertEquals("IN_PROGRESS", task.getStatus().toString());
    }

    @Test
    public void shouldSetStatusTaskDone() {
        taskManager.createTask(task);
        taskManager.setStatusForTask(task, Status.DONE);

        assertEquals("DONE", task.getStatus().toString());
    }

    @Test
    public void shouldSetStatusSubtaskInProgress() {
        taskManager.createTask(epic);
        subtask = new Subtask("Subtask 1", "DescriptionSubtask 1",
                "2022-08-23 | 20:30", 11, 38, epic.getId());
        taskManager.createTask(subtask);
        taskManager.setStatusForSubtask(subtask, Status.IN_PROGRESS);

        assertEquals("IN_PROGRESS", subtask.getStatus().toString());
    }

    @Test
    public void shouldSetStatusSubtaskDone() {
        taskManager.createTask(epic);
        subtask = new Subtask("Subtask 1", "DescriptionSubtask 1",
                "2022-08-23 | 20:30", 11, 38, epic.getId());
        taskManager.createTask(subtask);
        taskManager.setStatusForSubtask(subtask, Status.DONE);

        assertEquals("DONE", subtask.getStatus().toString());
    }
}