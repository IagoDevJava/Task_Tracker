package interfaces_and_utilities;

import exceptions.ManagerSaveException;
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
    long createTask(Task task) throws ManagerSaveException;

    /**
     * Создание эпика. Сам объект должен передаваться в качестве параметра.
     *
     * @return
     */
    long createTask(Epic epic) throws ManagerSaveException;

    /**
     * Создание подзадачи. Сам объект должен передаваться в качестве параметра.
     *
     * @return
     */
    long createTask(Subtask subtask) throws ManagerSaveException;

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
    void clearListOfTask() throws ManagerSaveException;

    /**
     * Удаление всех эпиков.
     */
    void clearListOfEpic() throws ManagerSaveException;

    /**
     * Удаление всех подзадач.
     */
    void clearListOfSubtask() throws ManagerSaveException;

    /**
     * Получение задачи по идентификатору.
     *
     * @return
     */
    Task getTaskByID(long numberID) throws ManagerSaveException;

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
    void upgradeTask(Task task) throws ManagerSaveException;

    /**
     * Обновление эпика.
     */
    void upgradeEpic(Epic epic) throws ManagerSaveException;

    /**
     * Обновление подзадачи.
     */
    void upgradeSubtask(Subtask subtask) throws ManagerSaveException;

    /**
     * Удаление задачи по идентификатору.
     */
    void deleteTaskForID(long numberID) throws ManagerSaveException;

    /**
     * Удаление эпика по идентификатору.
     */
    void deleteEpicForID(long numberID) throws ManagerSaveException;

    /**
     * Удаление подзадачи по идентификатору.
     */
    void deleteSubtaskForID(long numberID) throws ManagerSaveException;

    /**
     * Установка статуса для задачи
     */
    Task setStatusForTask(Task task, Status status) throws ManagerSaveException;

    /**
     * Установка статуса для подзадачи
     */
    Subtask setStatusForSubtask(Subtask subtask, Status status) throws ManagerSaveException;

    /**
     * Расчет статуса для эпиков
     */
    void setStatusForEpics(long numberEpicID) throws ManagerSaveException;

    /**
     * Получаем объект для получения и добавления истории
     */
    HistoryManager getHistoryManager();
}