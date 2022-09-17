package org.example.manager.managers_types;

import org.example.manager.interfaces_and_utilities.HistoryManager;
import org.example.manager.interfaces_and_utilities.Managers;
import org.example.manager.interfaces_and_utilities.TaskManager;
import org.example.tasks.*;

import java.time.Duration;
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
    protected final Map<Long, Task> allTasks = new HashMap<>();
    protected final Map<Long, Epic> allEpicTasks = new HashMap<>();
    protected final Map<Long, Subtask> allSubtasks = new HashMap<>();

    protected final Map<LocalDateTime, Task> prioritizedMapOfTasks = new TreeMap<>();

    protected long createdID = 0L;

    @Override
    public void createTasks(Task task) {
        TypesTasks type = task.getType();
        if (type == TypesTasks.TASK) {
            long id = creatingID();
            task.setId(id);
            task.setType(task.getType());
            task.setStatus(Status.NEW);

            if (task.getStartTime() == null) {
                task.setStartTime(LocalDateTime.now());
                task.setDuration(Duration.ofHours(1));
            }
            task.getEndTime();

            allTasks.put(id, task);
            addTasksInPrioritizedList(task);
        } else if (type == TypesTasks.SUBTASK) {
            long id = creatingID();
            long idEpic = task.getEpicId();
            task.setId(id);
            task.setType(task.getType());
            task.setStatus(Status.NEW);

            if (task.getStartTime() == null) {
                task.setStartTime(LocalDateTime.now());
                task.setDuration(Duration.ofHours(1));
            }
            task.getEndTime();

            allSubtasks.put(id, (Subtask) task);
            addTasksInPrioritizedList(task);
            Epic epic = getEpicByIdWithoutStory(idEpic);
            epic.getIdsOfSubtasksEpic().add(id);
        } else if (type == TypesTasks.EPIC) {
            long id = creatingID();
            task.setId(id);
            task.setType(task.getType());
            task.setStatus(Status.NEW);

            setStartTimeEpic((Epic) task);
            setEndTimeEpic((Epic) task);
            setDurationEpic((Epic) task);
            allEpicTasks.put(id, (Epic) task);
        }
    }

    /**
     * Создание задачи. Сам объект должен передаваться в качестве параметра.
     */
    @Override
    public long createTask(Task task) {
        long thisID = creatingID();
        task.setId(thisID);
        task.setType(task.getType());
        task.setStatus(Status.NEW);
        task.setStartTime(LocalDateTime.now());
        task.setDuration(Duration.ofHours(1));
        task.getEndTime();
        allTasks.put(thisID, task);
        addTasksInPrioritizedList(task);
        return thisID;
    }

    /**
     * Создание эпика. Сам объект должен передаваться в качестве параметра.
     */
    @Override
    public long createTask(Epic epic) {
        long thisID = creatingID();
        epic.setId(thisID);
        epic.setType(epic.getType());
        epic.setStatus(Status.NEW);

        setStartTimeEpic(epic);
        setEndTimeEpic(epic);
        setDurationEpic(epic);

        allEpicTasks.put(thisID, epic);

        return thisID;
    }

    /**
     * Создание подзадачи. Сам объект должен передаваться в качестве параметра.
     */
    @Override
    public long createTask(Subtask subtask) {
        long thisID = creatingID();
        long epicID = subtask.getEpicId();
        if (allEpicTasks.containsKey(epicID)) {
            subtask.setId(thisID);
            subtask.setType(subtask.getType());
            subtask.setStatus(Status.NEW);
            subtask.setStartTime(LocalDateTime.now());
            subtask.setDuration(Duration.ofHours(1));
            subtask.getEndTime();
            subtask.setEpicId(epicID);
            allSubtasks.put(thisID, subtask);
            addTasksInPrioritizedList(subtask);

            Epic thisEpic = allEpicTasks.get(epicID);
            thisEpic.getIdsOfSubtasksEpic().add(thisID);
            setStatusForEpic(epicID);
            updateTimeEpic(thisEpic);
        }
        return thisID;
    }

    /**
     * Получение списка всех задач.
     */
    @Override
    public List<Task> getListOfTask() {
        return new ArrayList<>(allTasks.values());
    }

    /**
     * Получение списка всех эпиков.
     */
    @Override
    public List<Task> getListOfEpic() {
        return new ArrayList<>(allEpicTasks.values());
    }

    /**
     * Получение списка всех подзадач.
     */
    @Override
    public List<Task> getListOfSubtask() {
        return new ArrayList<>(allSubtasks.values());
    }

    /**
     * Получение списка всех подзадач эпика.
     */
    @Override
    public List<Subtask> getListSubtasksOfEpic(Long idEpic) {
        List<Subtask> subtasksEpic = new ArrayList<>();
        if (allEpicTasks.containsKey(idEpic)) {
            for (Long aLong : getEpicByIdWithoutStory(idEpic).getIdsOfSubtasksEpic()) {
                subtasksEpic.add(getSubtaskByIdWithoutStory(aLong));
            }
        }
        return subtasksEpic;
    }

    /**
     * Получение списка задач по приоритетам.
     */
    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedMapOfTasks.values());
    }

    /**
     * Удаление всех задач.
     */
    @Override
    public void deleteListOfTask() {
        for (Long aLong : allTasks.keySet()) {
            historyManager.remove(aLong);
        }

        for (Task value : allTasks.values()) {
            prioritizedMapOfTasks.remove(value.getStartTime());
        }

        allTasks.clear();
    }

    /**
     * Удаление всех эпиков.
     */
    @Override
    public void deleteListOfEpic() {
        for (Epic epic : allEpicTasks.values()) {
            for (Long idSubtask : epic.getIdsOfSubtasksEpic()) {
                prioritizedMapOfTasks.remove(getSubtaskByIdWithoutStory(idSubtask).getStartTime());
                historyManager.remove(idSubtask);
            }
            historyManager.remove(epic.getId());
        }

        allEpicTasks.clear();
        allSubtasks.clear();
    }

    /**
     * Удаление всех подзадач.
     */
    @Override
    public void deleteListOfSubtask() {
        for (Subtask subtask : allSubtasks.values()) {
            prioritizedMapOfTasks.remove(subtask.getStartTime());
            historyManager.remove(subtask.getId());
        }

        allSubtasks.clear();

        for (Long aLong : allEpicTasks.keySet()) {
            allEpicTasks.get(aLong).getIdsOfSubtasksEpic().clear();
            setStatusForEpic(aLong);
            updateTimeEpic(getEpicByIdWithoutStory(aLong));
        }
    }

    /**
     * Получение задачи по идентификатору.
     */
    @Override
    public Task getTaskByID(long numberId) {
        Task task = allTasks.get(numberId);
        if (allTasks.containsKey(numberId)) {
            historyManager.add(task);
        }
        return task;
    }

    /**
     * Получение задачи по идентификатору без истории, для внутреннего пользования.
     */
    @Override //метод используется в тестах, поэтому пришлось оставить его публичным
    public Task getTaskByIdWithoutStory(long numberId) {
        return allTasks.get(numberId);
    }

    /**
     * Получение эпика по идентификатору.
     */
    @Override
    public Epic getEpicByID(long numberId) {
        Epic epic = allEpicTasks.get(numberId);
        if (allEpicTasks.containsKey(numberId)) {
            historyManager.add(epic);
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
            historyManager.add(subtask);
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
    public void updateTask(Task task) {
        final Task savedTask = allTasks.get(task.getId());
        if (checkTasksForIntersectionsByTime(savedTask)) {
            savedTask.setNameTask(task.getNameTask());
            savedTask.setDescription(task.getDescription());
            savedTask.setStatus(Status.NEW);
            savedTask.setStartTime(task.getStartTime());
            savedTask.setDuration(task.getDuration());
        }
    }

    /**
     * Обновление эпика.
     */
    @Override
    public void updateEpic(Epic epic) {
        final Epic savedEpic = allEpicTasks.get(epic.getId());
        savedEpic.setNameTask(epic.getNameTask());
        savedEpic.setDescription(epic.getDescription());
    }

    /**
     * Обновление подзадачи.
     */
    @Override
    public void updateSubtask(Subtask subtask) {
        Long id = subtask.getId();
        final Subtask savedSubtask = allSubtasks.get(id);
        if (checkTasksForIntersectionsByTime(savedSubtask)) {
            savedSubtask.setNameTask(subtask.getNameTask());
            savedSubtask.setDescription(subtask.getDescription());
            savedSubtask.setStatus(Status.NEW);
            savedSubtask.setStartTime(subtask.getStartTime());
            savedSubtask.setDuration(subtask.getDuration());
        }
        updateTimeEpic(allEpicTasks.get(savedSubtask.getEpicId()));
    }

    /**
     * Удаление задачи по идентификатору.
     */
    @Override
    public void deleteTaskForID(long numberId) {
        Task task = allTasks.get(numberId);
        if (task != null) {
            allTasks.remove(numberId);
            historyManager.remove(numberId);
        }
    }

    /**
     * Удаление эпика по идентификатору.
     */
    @Override
    public void deleteEpicForID(long numberId) {
        Epic epic = allEpicTasks.get(numberId);
        if (epic != null) {
            for (Long idSubtask : epic.getIdsOfSubtasksEpic()) {
                prioritizedMapOfTasks.remove(getSubtaskByIdWithoutStory(idSubtask).getStartTime());
                historyManager.remove(idSubtask);
                allSubtasks.remove(idSubtask);
            }

            allEpicTasks.remove(numberId);
            historyManager.remove(numberId);
        }
    }

    /**
     * Удаление подзадачи по идентификатору.
     */
    @Override
    public void deleteSubtaskForID(long numberId) {
        Subtask subtask = allSubtasks.get(numberId);
        if (subtask != null) {
            Epic epic = allEpicTasks.get(subtask.getEpicId());
            epic.getIdsOfSubtasksEpic().remove(numberId);
            allSubtasks.remove(numberId);
            historyManager.remove(numberId);
            updateTimeEpic(allEpicTasks.get(subtask.getEpicId()));
        }
    }

    /**
     * Установка статуса для задачи
     */
    @Override
    public Task setStatusForTask(Task task, Status status) {
        if (!(task instanceof Epic)) {
            task.setStatus(status);
            allTasks.put(task.getId(), task);
        }
        return task;
    }

    /**
     * Установка статуса для подзадачи
     */
    @Override
    public Subtask setStatusForSubtask(Subtask subtask, Status status) {
        subtask.setStatus(status);
        allSubtasks.put(subtask.getId(), subtask);

        Epic epic = getEpicByID(subtask.getEpicId());
        epic.getIdsOfSubtasksEpic().add(subtask.getId());
        historyManager.remove(epic.getId());
        setStatusForEpic(subtask.getEpicId());

        return subtask;
    }

    /**
     * Установка статуса для эпиков
     */
    @Override
    public void setStatusForEpic(long numberEpicID) {
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
     * Установка времени старта для эпиков
     */
    protected LocalDateTime setStartTimeEpic(Epic epic) {
        epic.setStartTime(LocalDateTime.MAX);
        if (epic.getIdsOfSubtasksEpic() != null) {
            if (!epic.getIdsOfSubtasksEpic().isEmpty()) {
                for (Long idSubtaskThisEpic : epic.getIdsOfSubtasksEpic()) {
                    Subtask subtask = getSubtaskByIdWithoutStory(idSubtaskThisEpic);
                    LocalDateTime startSubtask = subtask.getStartTime();
                    epic.setStartTime(startSubtask);
                    if (startSubtask.isBefore(epic.getStartTime())) {
                        epic.setStartTime(startSubtask);
                    }
                }
            }
        }
        return epic.getStartTime();
    }

    /**
     * Установка времени окончания для эпиков
     */
    protected LocalDateTime setEndTimeEpic(Epic epic) {
        epic.setEndTime(LocalDateTime.MIN);
        if (epic.getIdsOfSubtasksEpic() != null) {
            if (!epic.getIdsOfSubtasksEpic().isEmpty()) {
                for (Long idSubtaskThisEpic : epic.getIdsOfSubtasksEpic()) {
                    Subtask subtask = getSubtaskByIdWithoutStory(idSubtaskThisEpic);
                    LocalDateTime endSubtask = subtask.getEndTime();
                    epic.setEndTime(endSubtask);
                    if (endSubtask.isAfter(epic.getEndTime())) {
                        epic.setEndTime(endSubtask);
                    }
                }
            }
        }
        return epic.getEndTime();
    }

    /**
     * Установка продолжительности для эпиков
     */
    protected Duration setDurationEpic(Epic epic) {
        epic.setDuration(Duration.between(epic.getStartTime(), epic.getEndTime()));
        return epic.getDuration();
    }

    /**
     * Обновление временных меток для эпиков по подзадачам
     */
    protected Duration updateTimeEpic(Epic epic) {
        LocalDateTime startTimeEpic = epic.getStartTime();
        LocalDateTime endTimeEpic = epic.getEndTime();
        Duration durationEpic = epic.getDuration();
        for (Long aLong : epic.getIdsOfSubtasksEpic()) {
            LocalDateTime startTimeSubtask = getSubtaskByIdWithoutStory(aLong).getStartTime();
            LocalDateTime endTimeSubtask = getSubtaskByIdWithoutStory(aLong).getEndTime();

            if (startTimeSubtask.isBefore(startTimeEpic)) {
                epic.setStartTime(startTimeSubtask);
            }
            if (endTimeSubtask.isAfter(endTimeEpic)) {
                epic.setEndTime(endTimeSubtask);
            }
            durationEpic = durationEpic.plus(getSubtaskByIdWithoutStory(aLong).getDuration());
            epic.setDuration(durationEpic);
        }
        return epic.getDuration();
    }

    /**
     * Проверка задач на пересечение во времени.
     */
    private Boolean checkTasksForIntersectionsByTime(Task newTask) {

        if (prioritizedMapOfTasks.isEmpty()) {
            return true;
        }
        for (Task task : prioritizedMapOfTasks.values()) {
            if (task.getId() == newTask.getId()) {
                continue;
            }
            if (!newTask.getEndTime().isAfter(task.getStartTime())) {
                continue;
            }
            if (!newTask.getStartTime().isBefore(task.getEndTime())) {
                continue;
            }
            return false;
        }
        return true;
    }

    /**
     * Добавление задач в список по временным меткам.
     */
    protected void addTasksInPrioritizedList(Task task) {
        if (!(task instanceof Epic)) {
            if (checkTasksForIntersectionsByTime(task)) {
                prioritizedMapOfTasks.put(task.getStartTime(), task);
            } else {
                System.out.println("Измените время выполнения задачи - " + task.getId());
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

    /**
     * Получение сгенерированного id
     * */
    public long getCreatedID() {
        return createdID;
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
                && Objects.equals(allSubtasks, that.allSubtasks)
                && Objects.equals(prioritizedMapOfTasks, that.prioritizedMapOfTasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(historyManager, allTasks, allEpicTasks, allSubtasks, prioritizedMapOfTasks, createdID);
    }

    @Override
    public String toString() {
        return "InMemoryTaskManager{" +
                "historyManager=" + historyManager +
                ", allTasks=" + allTasks +
                ", allEpicTasks=" + allEpicTasks +
                ", allSubtasks=" + allSubtasks +
                ", prioritizedTasks=" + prioritizedMapOfTasks +
                ", createdID=" + createdID +
                '}';
    }
}