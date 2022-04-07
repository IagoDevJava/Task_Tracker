package managers_types;

import tasks_types.Epic;
import tasks_types.Status;
import tasks_types.Subtask;
import tasks_types.Task;

import java.util.ArrayList;

public interface TaskManager {

    /**
     * Создание задачи. Сам объект должен передаваться в качестве параметра.
     *
     * @return
     */
    long createTask(Task task);

    /**
     * Создание эпика. Сам объект должен передаваться в качестве параметра.
     *
     * @return
     */
    long createTask(Epic epic);

    /**
     * Создание подзадачи. Сам объект должен передаваться в качестве параметра.
     *
     * @return
     */
    long createTask(Subtask subtask);

    /**
     * Получение списка всех задач.
     */
    ArrayList<Task> getListOfTask();

    /**
     * Получение списка всех эпиков.
     */
    ArrayList<Task> getListOfEpic();

    /**
     * Получение списка всех подзадач.
     */
    ArrayList<Task> getListOfSubtask();

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
    void clearListOfSubtask();

    /**
     * Получение задачи по идентификатору.
     *
     * @return
     */
    Task getTaskByID(long numberID);

    /**
     * Получение эпика по идентификатору.
     *
     * @return
     */
    Epic getEpicByID(long numberID);

    /**
     * Получение подзадачи по идентификатору.
     *
     * @return
     */
    Subtask getSubtaskByID(long numberID);

    /**
     * Обновление задачи.
     */
    void upgradeTask(Task task);

    /**
     * Обновление эпика.
     */
    void upgradeEpic(Epic epic);

    /**
     * Обновление подзадачи.
     */
    void upgradeSubtask(Subtask subtask);

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
    void deleteSubtaskForID(long numberID);

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
    void setStatusForEpics(long numberEpicID);

    /**
     * Получаем объект для получения и добавления истории
     */
    HistoryManager getHistoryManager();
}
