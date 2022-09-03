package Tests;

import interfaces_and_utilities.Managers;
import interfaces_and_utilities.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks_types.Epic;
import tasks_types.Status;
import tasks_types.Subtask;

class EpicTest {

    private final TaskManager manager = Managers.getDefaultTaskManager();

    @Test
    public void shouldEpicStatusWhenListOfSubtasksIsEmpty() {
        Epic epic = new Epic("Epic 1", "DescriptionEpic 1", manager);
        manager.createTask(epic);
        Assertions.assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    public void shouldEpicStatusWhenAllSubtasksStatusIsNew() {
        Epic epic = new Epic("Epic 1", "DescriptionEpic 1", manager);
        manager.createTask(epic);
        Subtask subtask = new Subtask("Subtask 1", "DescriptionSubtask 1",
                "2022-08-23 | 20:30", 11, 38, epic.getNumberId());
        manager.createTask(subtask);
        Subtask subtask2 = new Subtask("Subtask 2", "DescriptionSubtask 2",
                "2022-08-28 | 09:00", 1, 0, epic.getNumberId());
        manager.createTask(subtask2);
        Assertions.assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void shouldEpicStatusWhenAllSubtasksStatusIsDone() {
        Epic epic = new Epic("Epic 1", "DescriptionEpic 1", manager);
        manager.createTask(epic);
        Subtask subtask = new Subtask("Subtask 1", "DescriptionSubtask 1", "2022-08-23 | 20:30", 11, 38, epic.getNumberId());
        manager.createTask(subtask);
        Subtask subtask2 = new Subtask("Subtask 2", "DescriptionSubtask 2",
                "2022-08-28 | 09:00", 1, 0, epic.getNumberId());
        manager.createTask(subtask2);
        manager.setStatusForSubtask(subtask, Status.DONE);
        manager.setStatusForSubtask(subtask2, Status.DONE);
        Assertions.assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    public void shouldEpicStatusWhenFirstSubtasksStatusIsNewAndSecondSubtaskStatusIsDone() {
        Epic epic = new Epic("Epic 1", "DescriptionEpic 1", manager);
        manager.createTask(epic);
        Subtask subtask = new Subtask("Subtask 1", "DescriptionSubtask 1",
                "2022-08-23 | 20:30", 11, 38, epic.getNumberId());
        manager.createTask(subtask);
        Subtask subtask2 = new Subtask("Subtask 2", "DescriptionSubtask 2",
                "2022-08-28 | 09:00", 1, 0, epic.getNumberId());
        manager.createTask(subtask2);
        manager.setStatusForSubtask(subtask, Status.NEW);
        manager.setStatusForSubtask(subtask2, Status.DONE);
        Assertions.assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void shouldEpicStatusWhenAllSubtasksStatusIsInProgress() {
        Epic epic = new Epic("Epic 1", "DescriptionEpic 1", manager);
        manager.createTask(epic);
        Subtask subtask = new Subtask("Subtask 1", "DescriptionSubtask 1",
                "2022-08-23 | 20:30", 11, 38, epic.getNumberId());
        manager.createTask(subtask);
        Subtask subtask2 = new Subtask("Subtask 2", "DescriptionSubtask 2",
                "2022-08-28 | 09:00", 1, 0, epic.getNumberId());
        manager.createTask(subtask2);
        manager.setStatusForSubtask(subtask, Status.IN_PROGRESS);
        manager.setStatusForSubtask(subtask2, Status.IN_PROGRESS);
        Assertions.assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }
}