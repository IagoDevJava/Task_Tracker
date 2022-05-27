package managers_types;

import exceptions.ManagerSaveException;
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

    /**
     * История задач.
     */
    protected final HistoryManager historyManager = Managers.getDefaultHistory();

    /**
     * Возможность хранить задачи всех типов.
     */
    protected final HashMap<Long, Task> allTasks = new HashMap<>();
    protected final HashMap<Long, Epic> allEpicTasks = new HashMap<>();
    protected final HashMap<Long, Subtask> allSubtasks = new HashMap<>();

    protected long createdID = 0L;

    /**
     * Создание задачи. Сам объект должен передаваться в качестве параметра.
     */
    @Override
    public long createTask(Task task) throws ManagerSaveException {
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
    public long createTask(Epic epic) throws ManagerSaveException {
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
    public long createTask(Subtask subtask) throws ManagerSaveException {
        long thisID = creatingID();
        long epicID = subtask.getMyEpicID();
        if (allEpicTasks.containsKey(epicID)) {
            subtask.numberId(thisID);
            allSubtasks.put(thisID, subtask);
            subtask.setStatus(Status.NEW);

            Epic epic = allEpicTasks.get(epicID);
            epic.getIdsOfSubtasksEpic().add(thisID);
            setStatusForEpics(epicID);
        }
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
    public void clearListOfTask() throws ManagerSaveException {
        allTasks.clear();
    }

    /**
     * Удаление всех эпиков.
     */
    @Override
    public void clearListOfEpic() throws ManagerSaveException {
        allEpicTasks.clear();
        allSubtasks.clear();
    }

    /**
     * Удаление всех подзадач.
     */
    @Override
    public void clearListOfSubtask() throws ManagerSaveException {
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
    public Task getTaskByID(long numberId) throws ManagerSaveException {
        Task task = allTasks.get(numberId);
        if (!allTasks.containsKey(numberId) && task == null) {
            System.out.println("Задачи с таким ID нет в списке.");
        }

        if (allTasks.containsKey(numberId)) {
            getHistoryManager().add(task);
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
            getHistoryManager().add(epic);
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
            getHistoryManager().add(subtask);
        }
        return subtask;
    }

    /**
     * Обновление задачи.
     */
    @Override
    public void upgradeTask(Task task) throws ManagerSaveException {
        long numberId = task.getNumberId();
        if (allTasks.containsKey(numberId)) {
            allTasks.put(numberId, task);
        }
    }

    /**
     * Обновление эпика.
     */
    @Override
    public void upgradeEpic(Epic epic) throws ManagerSaveException {
        long numberId = epic.getNumberId();
        if (allEpicTasks.containsKey(numberId)) {
            allEpicTasks.put(numberId, epic);
        }
    }

    /**
     * Обновление подзадачи.
     */
    @Override
    public void upgradeSubtask(Subtask subtask) throws ManagerSaveException {
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
    public void deleteTaskForID(long numberId) throws ManagerSaveException {
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
    public void deleteEpicForID(long numberId) throws ManagerSaveException {
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
    public void deleteSubtaskForID(long numberId) throws ManagerSaveException {
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
    public Task setStatusForTask(Task task, Status status) throws ManagerSaveException {
        if (!(task instanceof Epic)) {
            Task t = new Task(task.getNameTask(), task.getDescription());
            long thisID = createTask(t);
            t.setStatus(status);
            allTasks.put(thisID, t);
        }
        return task;
    }

    /**
     * Установка статуса для подзадачи
     */
    @Override
    public Subtask setStatusForSubtask(Subtask subtask, Status status) throws ManagerSaveException {
        Subtask st = new Subtask(subtask.getNameTask(), subtask.getDescription(), subtask.getMyEpicID());
        long thisID = createTask(st);
        ;
        st.setStatus(status);
        allSubtasks.put(thisID, st);
        setStatusForEpics(subtask.getMyEpicID());
        return subtask;
    }

    /**
     * Установка статуса для эпиков
     */
    @Override
    public void setStatusForEpics(long numberEpicID) throws ManagerSaveException {
        boolean isStatus = true;

        Epic thisEpic = allEpicTasks.get(numberEpicID);
        if (thisEpic != null) {
            for (Long aLong : thisEpic.getIdsOfSubtasksEpic()) {
                isStatus = !allSubtasks.get(aLong).getStatus().equals(Status.IN_PROGRESS)
                        && !allSubtasks.get(aLong).getStatus().equals(Status.NEW);
            }
        }

        Epic e = new Epic(allEpicTasks.get(numberEpicID).getNameTask(), allEpicTasks.get(numberEpicID).getDescription());
        long thisID = createTask(e);
        if (!isStatus) {
            e.setStatus(Status.IN_PROGRESS);
        } else {
            if (thisEpic != null) {
                e.setStatus(Status.DONE);
            }
        }
        allEpicTasks.put(thisID, e);
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