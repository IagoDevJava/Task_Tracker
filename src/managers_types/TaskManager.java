package managers_types;

import tasks_types.Epic;
import tasks_types.Status;
import tasks_types.Subtask;
import tasks_types.Task;

public interface TaskManager {

    /**
     * Создание задачи. Сам объект должен передаваться в качестве параметра.
     */
    Task createTask(Task task);

    /**
     * Создание эпика. Сам объект должен передаваться в качестве параметра.
     */
    Epic createTask(Epic epic);

    /**
     * Создание подзадачи. Сам объект должен передаваться в качестве параметра.
     */
    Subtask createTask(Subtask subtask, Epic epic);

    /**
     * Получение списка всех задач.
     */
    String getListOfTask();

    /**
     * Получение списка всех эпиков.
     */
    String getListOfEpic();

    /**
     * Получение списка всех подзадач.
     */
    String getListOfSubtask(Epic epic);

    /**
     * Удаление всех задач.
     */
    void clearListOfTask();

    /**
     * Удаление всех эпиков.
     */
    void clearListOfEpic();

    /**
     * Удаление всех подзадач.
     */
    void clearListOfSubtask(Epic epic);

    /**
     * Получение задачи по идентификатору.
     */
    String getTaskForID(long numberID);

    /**
     * Получение эпика по идентификатору.
     */
    String getEpicForID(long numberID);

    /**
     * Получение подзадачи по идентификатору.
     */
    String getSubtaskForID(long numberID, Epic epic);

    /**
     * Обновление задачи.
     */
    void enterNewTask(long numberID, Task task);

    /**
     * Обновление эпика.
     */
    void enterNewEpic(long numberID, Epic oldEpic, Epic newEpic);

    /**
     * Обновление подзадачи.
     */
    void enterNewSubtask(long numberID, Subtask subtask, Epic epic);

    /**
     * Удаление задачи по идентификатору.
     */
    void deleteTaskForID(long numberID);

    /**
     * Удаление эпика по идентификатору.
     */
    void deleteEpicForID(long numberID);

    /**
     * Удаление подзадачи по идентификатору.
     */
    void deleteSubtaskForID(long numberID, Epic epic);

    /**
     * Установка статуса для задачи
     */
    Task setStatusForTask(Task task, Status status);

    /**
     * Установка статуса для подзадачи
     */
    Subtask setStatusForSubtask(Subtask subtask, Status status);

    /**
     * Расчет статуса для эпиков
     */
    void createStatusForEpic(long numberEpicID);

    /**
     * Получаем объект для получения и добавления истории
     */
    HistoryManager getHistoryManager();
}
