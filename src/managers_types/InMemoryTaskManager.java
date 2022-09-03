package managers_types;

import exceptions.ManagerSaveException;
import interfaces_and_utilities.HistoryManager;
import interfaces_and_utilities.Managers;
import interfaces_and_utilities.TaskManager;
import tasks_types.Epic;
import tasks_types.Status;
import tasks_types.Subtask;
import tasks_types.Task;

import java.time.LocalDateTime;
import java.util.*;

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

    protected final Map<LocalDateTime, Task> prioritizedMapOfTasks = new TreeMap<>();

    protected long createdID = 0L;

    /**
     * Создание задачи. Сам объект должен передаваться в качестве параметра.
     */
    @Override
    public long createTask(Task task) throws ManagerSaveException {
        long thisID = creatingID();
        task.setNumberId(thisID);
        allTasks.put(thisID, task);
        task.setStatus(Status.NEW);
        addTasksInPrioritizedList(task);
        return thisID;
    }

    /**
     * Создание эпика. Сам объект должен передаваться в качестве параметра.
     */
    @Override
    public long createTask(Epic epic) throws ManagerSaveException {
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
    public long createTask(Subtask subtask) throws ManagerSaveException {
        long thisID = creatingID();
        long epicID = subtask.getMyEpicID();
        if (allEpicTasks.containsKey(epicID)) {
            subtask.setNumberId(thisID);
            allSubtasks.put(thisID, subtask);
            subtask.setStatus(Status.NEW);
            addTasksInPrioritizedList(subtask);

            Epic thisEpic = allEpicTasks.get(epicID);
            thisEpic.getIdsOfSubtasksEpic().add(thisID);
            setStatusForEpic(epicID);
            thisEpic.updateTimeForEpic(this);
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
     * Получение списка задач по приоритетам.
     */
    @Override
    public Set<Task> getPrioritizedTasks() {
        final Comparator<Task> taskComparator = Comparator.comparing(Task::getStartTime);
        final Set<Task> prioritizedListOfTasks = new TreeSet<>(taskComparator);
        prioritizedListOfTasks.addAll(prioritizedMapOfTasks.values());
        return prioritizedListOfTasks;
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
            setStatusForEpic(aLong);
        }
    }

    /**
     * Получение задачи по идентификатору.
     */
    @Override
    public Task getTaskByID(long numberId) throws ManagerSaveException {
        Task task = allTasks.get(numberId);
        if (allTasks.containsKey(numberId)) {
            getHistoryManager().add(task);
        }
        return task;
    }

    /**
     * Получение задачи по идентификатору без истории, для внутреннего пользования.
     */
    @Override
    public Task getTaskByIdWithoutStory(long numberId) throws ManagerSaveException {
        return allTasks.get(numberId);
    }

    /**
     * Получение эпика по идентификатору.
     */
    @Override
    public Epic getEpicByID(long numberId) {
        Epic epic = allEpicTasks.get(numberId);
        if (allEpicTasks.containsKey(numberId)) {
            getHistoryManager().add(epic);
        }
        return epic;
    }

    /**
     * Получение эпика по идентификатору, без истории для внутреннего пользования.
     */
    @Override
    public Epic getEpicByIdWithoutStory(long numberId) {
        return allEpicTasks.get(numberId);
    }

    /**
     * Получение подзадачи по идентификатору.
     */
    @Override
    public Subtask getSubtaskByID(long numberId) {
        Subtask subtask = allSubtasks.get(numberId);
        if (allSubtasks.containsKey(numberId)) {
            getHistoryManager().add(subtask);
        }
        return subtask;
    }

    /**
     * Получение подзадачи по идентификатору, без истории для внутреннего пользования.
     */
    @Override
    public Subtask getSubtaskByIdWithoutStory(long numberId) {
        return allSubtasks.get(numberId);
    }

    /**
     * Обновление задачи.
     */
    @Override
    public void updateTask(Task task, long numberOldTask) throws ManagerSaveException {
        final Task savedTask = getTaskByID(numberOldTask);
        savedTask.setNameTask(task.getNameTask());
        savedTask.setDescription(task.getDescription());
        savedTask.setStatus(Status.NEW);
        savedTask.setStartTime(task.getStartTime());
        savedTask.setDuration(task.getDuration());
        savedTask.setEndTime(task.getEndTime());

        if (allTasks.containsKey(savedTask.getNumberId())) {
            allTasks.put(savedTask.getNumberId(), savedTask);
        }
    }

    /**
     * Обновление эпика.
     */
    @Override
    public void updateEpic(Epic epic, long numberOldEpic) throws ManagerSaveException {
        final Epic savedEpic = getEpicByID(numberOldEpic);
        savedEpic.setNameTask(epic.getNameTask());
        savedEpic.setDescription(epic.getDescription());

        if (allEpicTasks.containsKey(savedEpic.getNumberId())) {
            allEpicTasks.put(savedEpic.getNumberId(), savedEpic);
        }
    }

    /**
     * Обновление подзадачи.
     */
    @Override
    public void updateSubtask(Subtask subtask, long numberOldSubtask) throws ManagerSaveException {
        final Subtask oldSubtask = getSubtaskByID(numberOldSubtask);
        oldSubtask.setNumberId(numberOldSubtask);
        oldSubtask.setNameTask(subtask.getNameTask());
        oldSubtask.setDescription(subtask.getDescription());
        oldSubtask.setStatus(Status.NEW);
        oldSubtask.setStartTime(subtask.getStartTime());
        oldSubtask.setDuration(subtask.getDuration());
        oldSubtask.setEndTime(subtask.getEndTime());

        if (allSubtasks.containsKey(oldSubtask.getNumberId())) {
            allSubtasks.put(oldSubtask.getNumberId(), oldSubtask);
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
            task.setStatus(status);
            allTasks.put(task.getNumberId(), task);
        }
        return task;
    }

    /**
     * Установка статуса для подзадачи
     */
    @Override
    public Subtask setStatusForSubtask(Subtask subtask, Status status) throws ManagerSaveException {
        subtask.setStatus(status);
        allSubtasks.put(subtask.getNumberId(), subtask);

        Epic epic = getEpicByID(subtask.getMyEpicID());
        epic.getIdsOfSubtasksEpic().add(subtask.getNumberId());
        historyManager.remove(epic.getNumberId());
        setStatusForEpic(subtask.getMyEpicID());

        return subtask;
    }

    /**
     * Установка статуса для эпиков
     */
    @Override
    public void setStatusForEpic(long numberEpicID) throws ManagerSaveException {
        boolean isStatus = true;

        Epic newEpic = allEpicTasks.get(numberEpicID);
        for (Long aLong : newEpic.getIdsOfSubtasksEpic()) {
            if (allSubtasks.get(aLong).getStatus().equals(Status.IN_PROGRESS)
                    || allSubtasks.get(aLong).getStatus().equals(Status.NEW)) {
                isStatus = false;
            }
        }

        if (isStatus) {
            newEpic.setStatus(Status.DONE);
        } else {
            newEpic.setStatus(Status.IN_PROGRESS);
        }

        allEpicTasks.put(numberEpicID, newEpic);
    }

    /**
     * Проверка задач на пересечение во времени.
     */
    private Boolean checkTasksForIntersectionsByTime(Task newTask) {
        boolean checkTaskTime = false;
        if (!prioritizedMapOfTasks.isEmpty()) {
            for (Task oldTask : prioritizedMapOfTasks.values()) {
                if (newTask.getStartTime().isAfter(oldTask.getEndTime())) {
                    checkTaskTime = true;
                } else if (newTask.getEndTime().isBefore(oldTask.getStartTime())) {
                    checkTaskTime = true;
                } else {
                    checkTaskTime = false;
                }
            }
        } else {
            checkTaskTime = true;
        }
        return checkTaskTime;
    }

    /**
     * Добавление задач в список по временным меткам.
     */
    private void addTasksInPrioritizedList(Task task) {
        if (task instanceof Epic) {
            prioritizedMapOfTasks.put(task.getStartTime(), task);
        } else {
            if (checkTasksForIntersectionsByTime(task)) {
                prioritizedMapOfTasks.put(task.getStartTime(), task);
            } else {
                System.out.println("Измените время выполнения задачи - " + task.getNumberId());
            }
        }
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