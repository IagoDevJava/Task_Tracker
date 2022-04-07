package managers_types;

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
        task.setNumberId(thisID);
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
        epic.setNumberId(thisID);
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

        subtask.setNumberId(thisID);
        allSubtasks.put(thisID, subtask);
        subtask.setStatus(Status.NEW);

        if (allEpicTasks.containsKey(epicID)) {
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
    }

    /**
     * Получение задачи по идентификатору.
     */
    @Override
    public Task getTaskByID(long numberID) {
        if (allTasks.isEmpty() && allTasks.get(numberID) == null) {
            System.out.println("Задачи с таким ID нет в списке.");
        }
        return allTasks.get(numberID);
    }

    /**
     * Получение эпика по идентификатору.
     */
    @Override
    public Epic getEpicByID(long numberID) {
        if (allEpicTasks.isEmpty() && allEpicTasks.get(numberID) == null) {
            System.out.println("Задачи с таким ID нет в списке.");
        }
        return allEpicTasks.get(numberID);
    }

    /**
     * Получение подзадачи по идентификатору.
     */
    @Override
    public Subtask getSubtaskByID(long numberID) {
        if (allSubtasks.isEmpty() && allSubtasks.get(numberID) == null) {
            System.out.println("Задачи с таким ID нет в списке.");
        }
        return allSubtasks.get(numberID);
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
    public void deleteTaskForID(long numberID) {
        if (!allTasks.isEmpty() && allTasks.get(numberID) != null) {
            allTasks.remove(numberID);
        }
    }

    /**
     * Удаление эпика по идентификатору.
     */
    @Override
    public void deleteEpicForID(long numberID) {
        if (!allEpicTasks.isEmpty() && allEpicTasks.get(numberID) != null) {
            allEpicTasks.remove(numberID);
            allEpicTasks.get(numberID).getIdsOfSubtasksEpic().clear();
        }
    }

    /**
     * Удаление подзадачи по идентификатору.
     */
    @Override
    public void deleteSubtaskForID(long numberID) {
        if (!allSubtasks.isEmpty() && allSubtasks.get(numberID) != null) {
            allSubtasks.remove(numberID);
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
            task.setNumberId(thisID);
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
        subtask.setNumberId(thisID);
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
        Epic thisEpic = allEpicTasks.get(numberEpicID); //Создал для улучшения читабельности кода

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
        allEpicTasks.get(numberEpicID).setNumberId(thisID);
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
        return createdID == that.createdID && Objects.equals(historyManager, that.historyManager) && Objects.equals(allTasks, that.allTasks) && Objects.equals(allEpicTasks, that.allEpicTasks) && Objects.equals(allSubtasks, that.allSubtasks);
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