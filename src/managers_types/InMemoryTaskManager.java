package managers_types;

import interfaces_and_utilities.HistoryManager;
import interfaces_and_utilities.Managers;
import interfaces_and_utilities.TaskManager;
import tasks_types.Epic;
import tasks_types.Status;
import tasks_types.Subtask;
import tasks_types.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class InMemoryTaskManager implements TaskManager {

    private final HistoryManager historyManager = Managers.getDefaultHistory();
    /**
     * Возможность хранить задачи всех типов.
     */
    private final HashMap<Long, Task> allTasks = new HashMap<>();
    private final HashMap<Long, Epic> allEpicTasks = new HashMap<>();
    private final HashMap<Long, Subtask> allSubtasks = new HashMap<>();
    private long createdID = 0L;

    /**
     * Создание задачи. Сам объект должен передаваться в качестве параметра.
     */
    @Override
    public long createTask(Task task) {
        long thisID = creatingID();
        task.numberId(thisID);
        allTasks.put(thisID, task);
        task.setStatus(Status.NEW);
        return thisID;
    }

    /**
     * Создание эпика. Сам объект должен передаваться в качестве параметра.
     */
    @Override
    public long createTask(Epic epic) {
        long thisID = creatingID();
        epic.numberId(thisID);
        allEpicTasks.put(thisID, epic);
        epic.setStatus(Status.NEW);
        return thisID;
    }

    /**
     * Создание подзадачи. Сам объект должен передаваться в качестве параметра.
     */
    @Override
    public long createTask(Subtask subtask) {
        long epicID = subtask.getMyEpicID();
        long thisID = creatingID();

        if (allEpicTasks.containsKey(epicID)) {
            subtask.numberId(thisID);
            allSubtasks.put(thisID, subtask);
            subtask.setStatus(Status.NEW);

            Epic epic = allEpicTasks.get(epicID);
            epic.getIdsOfSubtasksEpic().add(thisID);
        }

        setStatusForEpics(epicID);
        return thisID;
    }

    /**
     * Получение списка всех задач.
     */
    @Override
    public ArrayList<Task> getListOfTask() {
        return new ArrayList<>(allTasks.values());
    }

    /**
     * Получение списка всех эпиков.
     */
    @Override
    public ArrayList<Task> getListOfEpic() {
        return new ArrayList<>(allEpicTasks.values());
    }

    /**
     * Получение списка всех подзадач.
     */
    @Override
    public ArrayList<Task> getListOfSubtask() {
        return new ArrayList<>(allSubtasks.values());
    }

    /**
     * Удаление всех задач.
     */
    @Override
    public void clearListOfTask() {
        allTasks.clear();
    }

    /**
     * Удаление всех эпиков.
     */
    @Override
    public void clearListOfEpic() {
        allEpicTasks.clear();
        allSubtasks.clear();
    }

    /**
     * Удаление всех подзадач.
     */
    @Override
    public void clearListOfSubtask() {
        allSubtasks.clear();
        for (Long aLong : allEpicTasks.keySet()) {
            allEpicTasks.get(aLong).getIdsOfSubtasksEpic().clear();
            setStatusForEpics(aLong);
        }
    }

    /**
     * Получение задачи по идентификатору.
     */
    @Override
    public Task getTaskByID(long numberId) {
        Task task = allTasks.get(numberId);
        if (!allTasks.containsKey(numberId) && task == null) {
            System.out.println("Задачи с таким ID нет в списке.");
        }

        if (allTasks.containsKey(numberId)) {
            getHistoryManager().addHistory(task);
        }
        return task;
    }

    /**
     * Получение эпика по идентификатору.
     */
    @Override
    public Epic getEpicByID(long numberId) {
        Epic epic = allEpicTasks.get(numberId);
        if (!allEpicTasks.containsKey(numberId) && epic == null) {
            System.out.println("Задачи с таким ID нет в списке.");
        }

        if (allEpicTasks.containsKey(numberId)) {
            getHistoryManager().addHistory(epic);
        }
        return epic;
    }

    /**
     * Получение подзадачи по идентификатору.
     */
    @Override
    public Subtask getSubtaskByID(long numberId) {
        Subtask subtask = allSubtasks.get(numberId);
        if (!allSubtasks.containsKey(numberId) && subtask == null) {
            System.out.println("Задачи с таким ID нет в списке.");
        }

        if (allSubtasks.containsKey(numberId)) {
            getHistoryManager().addHistory(subtask);
        }
        return subtask;
    }

    /**
     * Обновление задачи.
     */
    @Override
    public void upgradeTask(Task task) {
        long numberId = task.getNumberId();
        if (allTasks.containsKey(numberId)) {
            allTasks.put(numberId, task);
        }
    }

    /**
     * Обновление эпика.
     */
    @Override
    public void upgradeEpic(Epic epic) {
        long numberId = epic.getNumberId();
        if (allEpicTasks.containsKey(numberId)) {
            allEpicTasks.put(numberId, epic);
        }
    }

    /**
     * Обновление подзадачи.
     */
    @Override
    public void upgradeSubtask(Subtask subtask) {
        long numberId = subtask.getNumberId();
        if (allSubtasks.containsKey(numberId)) {
            allSubtasks.put(numberId, subtask);

            setStatusForEpics(subtask.getMyEpicID());
        }
    }

    /**
     * Удаление задачи по идентификатору.
     */
    @Override
    public void deleteTaskForID(long numberId) {
        Task task = allTasks.get(numberId);
        if (task != null) {
            getHistoryManager().getHistory().remove(task);
            allTasks.remove(numberId);
            getHistoryManager().remove(numberId);
        }
    }

    /**
     * Удаление эпика по идентификатору.
     */
    @Override
    public void deleteEpicForID(long numberId) {
        Epic epic = allEpicTasks.get(numberId);
        if (epic != null) {
            for (Long aLong : allSubtasks.keySet()) {
                if (numberId == allSubtasks.get(aLong).getMyEpicID()) {
                    allSubtasks.remove(aLong);
                }
            }
            allEpicTasks.remove(numberId);
            getHistoryManager().remove(numberId);
        }
    }

    /**
     * Удаление подзадачи по идентификатору.
     */
    @Override
    public void deleteSubtaskForID(long numberId) {
        Subtask subtask = allSubtasks.get(numberId);
        if (subtask != null) {
            Epic epic = allEpicTasks.get(subtask.getMyEpicID());
            epic.getIdsOfSubtasksEpic().remove(numberId);
            allSubtasks.remove(numberId);
            getHistoryManager().remove(numberId);
        }
    }

    /**
     * Установка статуса для задачи
     */
    @Override
    public Task setStatusForTask(Task task, Status status) {
        if (!(task instanceof Epic)) {
            task.setStatus(status);
            long thisID = creatingID();
            task.numberId(thisID);
            allTasks.put(thisID, task);
        }
        return task;
    }

    /**
     * Установка статуса для подзадачи
     */
    @Override
    public Subtask setStatusForSubtask(Subtask subtask, Status status) {
        subtask.setStatus(status);
        long thisID = creatingID();
        subtask.numberId(thisID);
        allSubtasks.put(thisID, subtask);

        setStatusForEpics(subtask.getMyEpicID());
        return subtask;
    }

    /**
     * Установка статуса для эпиков
     */
    @Override
    public void setStatusForEpics(long numberEpicID) {
        boolean isStatus = true;

        Epic thisEpic = allEpicTasks.get(numberEpicID);
        for (Long aLong : thisEpic.getIdsOfSubtasksEpic()) {
            if (allSubtasks.get(aLong).getStatus().equals(Status.DONE)) {
                isStatus = false;
            } else if (allSubtasks.get(aLong).getStatus().equals(Status.IN_PROGRESS)) {
                isStatus = true;
                break;
            }
        }

        if (isStatus) {
            thisEpic.setStatus(Status.IN_PROGRESS);
        } else {
            thisEpic.setStatus(Status.DONE);
        }

        long thisID = creatingID();
        allEpicTasks.get(numberEpicID).numberId(thisID);
        allEpicTasks.put(thisID, thisEpic);
    }

    /**
     * Получаем объект для получения и добавления истории
     */
    @Override
    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    /**
     * Создание нового ID
     */
    public long creatingID() {
        return ++createdID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InMemoryTaskManager that = (InMemoryTaskManager) o;
        return createdID == that.createdID
                && Objects.equals(historyManager, that.historyManager)
                && Objects.equals(allTasks, that.allTasks)
                && Objects.equals(allEpicTasks, that.allEpicTasks)
                && Objects.equals(allSubtasks, that.allSubtasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(historyManager, allTasks, allEpicTasks, allSubtasks, createdID);
    }

    @Override
    public String toString() {
        return "InMemoryTaskManager{" +
                "historyManager=" + historyManager +
                ", allTasks=" + allTasks +
                ", allEpicTasks=" + allEpicTasks +
                ", allSubtasks=" + allSubtasks +
                ", createdID=" + createdID +
                '}';
    }
}