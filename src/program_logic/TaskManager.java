package program_logic;

import program_entities.Epic;
import program_entities.Status;
import program_entities.Subtask;
import program_entities.Task;

public interface TaskManager {

    /**
     * Создание задачи. Сам объект должен передаваться в качестве параметра.
     */
    public Task createTask(Task task);

    /**
     * Создание эпика. Сам объект должен передаваться в качестве параметра.
     */
    public Epic createTask(Epic epic);

    /**
     * Создание подзадачи. Сам объект должен передаваться в качестве параметра.
     */
    public Subtask createTask(Subtask subtask, Epic epic);

    /**
     * Получение списка всех задач.
     */
    public String getListOfTask();

    /**
     * Получение списка всех эпиков.
     */
    public String getListOfEpic();

    /**
     * Получение списка всех подзадач.
     */
    public String getListOfSubtask(Epic epic);

    /**
     * Удаление всех задач.
     */
    public void clearListOfTask();

    /**
     * Удаление всех эпиков.
     */
    public void clearListOfEpic();

    /**
     * Удаление всех подзадач.
     */
    public void clearListOfSubtask(Epic epic);

    /**
     * Получение задачи по идентификатору.
     */
    public String getTaskForID(long numberID);

    /**
     * Получение эпика по идентификатору.
     */
    public String getEpicForID(long numberID);

    /**
     * Получение подзадачи по идентификатору.
     */
    public String getSubtaskForID(long numberID, Epic epic);

    /**
     * Обновление задачи.
     */
    public void enterNewTask(long numberID, Task task);

    /**
     * Обновление эпика.
     */
    public void enterNewEpic(long numberID, Epic oldEpic, Epic newEpic);

    /**
     * Обновление подзадачи.
     */
    public void enterNewSubtask(long numberID, Subtask subtask, Epic epic);
    /**
     * Удаление задачи по идентификатору.
     */
    public void deleteTaskForID(long numberID);

    /**
     * Удаление эпика по идентификатору.
     */
    public void deleteEpicForID(long numberID);

    /**
     * Удаление подзадачи по идентификатору.
     */
    public void deleteSubtaskForID(long numberID, Epic epic);
    /**
     * Установка статуса для задачи
     */
    public Task setStatusForTask(Task task, Status status);

    /**
     * Установка статуса для подзадачи
     */
    public Subtask setStatusForSubtask(Subtask subtask, Status status);
    /**
     * Расчет статуса для эпиков
     */
    public void createStatusForEpic(long numberEpicID);
}
