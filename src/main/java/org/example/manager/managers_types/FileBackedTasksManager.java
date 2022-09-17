package org.example.manager.managers_types;

import org.example.manager.exceptions.ManagerSaveException;
import org.example.manager.interfaces_and_utilities.HistoryManager;
import org.example.tasks.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.valueOf;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final File FILE;
    private static final String HEADER_OF_FILE = "id,type,name,status,description,epic, startTime, duration, endTime\n";

    public FileBackedTasksManager(File file) {
        this.FILE = file;
    }

    /**
     * Сохранение задач по строкам в файл
     */
    protected void save() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILE))) {
            bufferedWriter.append(HEADER_OF_FILE);

            for (Map.Entry<Long, Task> idTaskEntry : allTasks.entrySet()) {
                bufferedWriter.append(toString(idTaskEntry.getValue()));
                bufferedWriter.newLine();
            }

            for (Map.Entry<Long, Epic> idEpicEntry : allEpicTasks.entrySet()) {
                bufferedWriter.append(toString(idEpicEntry.getValue()));
                bufferedWriter.newLine();
            }

            for (Map.Entry<Long, Subtask> idSubtaskEntry : allSubtasks.entrySet()) {
                bufferedWriter.append(toString(idSubtaskEntry.getValue()));
                bufferedWriter.newLine();
            }

            bufferedWriter.newLine();
            bufferedWriter.append(historyToString(historyManager));

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка в файле: " + FILE.getAbsolutePath());
        }
    }

    /**
     * Получение строки из задачи
     */
    public String toString(Task task) {
        return task.toString();
    }

    /**
     * Получение строки с историей
     */
    private static String historyToString(HistoryManager historyManager) {
        StringBuilder sb = new StringBuilder();
        for (Task task : historyManager.getHistory()) {
            sb.append(task.getId()).append(",");
        }
        return sb.toString();
    }

    /**
     * Загрузка задач в файл
     */
    public static FileBackedTasksManager loadFromFile(File file) {
        final FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        fileBackedTasksManager.load();
        return fileBackedTasksManager;
    }

    /**
     * Загрузка задач построчно
     */
    private void load() {
        long maxId = 0L;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(FILE, StandardCharsets.UTF_8))) {
            String line = bufferedReader.readLine();
            while (line != null) {
                line = bufferedReader.readLine();
                if (line.isEmpty()) {
                    break;
                }

                long numberId = getTaskFromString(line).getId();
                switch (getTaskFromString(line).getType()) {
                    case TASK:
                        allTasks.put(numberId, getTaskFromString(line));
                        prioritizedMapOfTasks.put(getTaskFromString(line).getStartTime(), getTaskFromString(line));
                        break;
                    case EPIC:
                        allEpicTasks.put(numberId, (Epic) getTaskFromString(line));
                        break;
                    case SUBTASK:
                        allSubtasks.put(numberId, (Subtask) getTaskFromString(line));
                        prioritizedMapOfTasks.put(getTaskFromString(line).getStartTime(),
                                getTaskFromString(line));
                        if (allEpicTasks.containsKey(((Subtask) getTaskFromString(line)).getEpicId())) {
                            Epic epic = allEpicTasks.get(((Subtask) getTaskFromString(line)).getEpicId());
                            epic.getIdsOfSubtasksEpic().add(numberId);
                        }
                        break;
                }
                if (maxId < numberId) {
                    maxId = numberId;
                }
            }

            line = bufferedReader.readLine();
            for (Integer historyIdTasks : getHistoryFromString(line)) {
                getTaskByID(historyIdTasks);
                getEpicByID(historyIdTasks);
                getSubtaskByID(historyIdTasks);
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка в файле: " + FILE.getAbsolutePath());
        }
        createdID = maxId;
    }

    /**
     * Получение задачи из строки
     */
    private Task getTaskFromString(String value) {
        final String[] tasksFromString = value.split(",");
        Task unifiedTask = null;
        switch (TypesTasks.valueOf(tasksFromString[1])) {
            case TASK:
                unifiedTask = new Task(tasksFromString[2], tasksFromString[4]);
                unifiedTask.setStatus(Status.valueOf(tasksFromString[3]));
                unifiedTask.setId(Long.parseLong(tasksFromString[0]));
                if (!tasksFromString[5].equals("null")) {
                    unifiedTask.setStartTime(LocalDateTime.parse(tasksFromString[5]));
                    unifiedTask.setDuration(Duration.parse(tasksFromString[6]));
                    unifiedTask.getEndTime();
                }
                break;
            case EPIC:
                unifiedTask = new Epic(tasksFromString[2], tasksFromString[4], this);
                unifiedTask.setStatus(Status.valueOf(tasksFromString[3]));
                unifiedTask.setId(Long.parseLong(tasksFromString[0]));
                if (!tasksFromString[5].equals("null")) {
                    unifiedTask.setStartTime(LocalDateTime.parse(tasksFromString[5]));
                    unifiedTask.setDuration(Duration.parse(tasksFromString[6]));
                    this.setEndTimeEpic((Epic) unifiedTask);
                }
                break;
            case SUBTASK:
                unifiedTask = new Subtask(tasksFromString[2], tasksFromString[4], Long.parseLong(tasksFromString[5]));
                unifiedTask.setStatus(Status.valueOf(tasksFromString[3]));
                unifiedTask.setId(Long.parseLong(tasksFromString[0]));
                if (!tasksFromString[6].equals("null")) {
                    unifiedTask.setStartTime(LocalDateTime.parse(tasksFromString[6]));
                    unifiedTask.setDuration(Duration.parse(tasksFromString[7]));
                    unifiedTask.getEndTime();
                }
                break;
        }
        return unifiedTask;
    }

    /**
     * Получение истории из строки
     */
    static List<Integer> getHistoryFromString(String value) {
        final String[] ids = value.split(",");
        List<Integer> listIds = new ArrayList<>();
        for (String id : ids) {
            listIds.add(valueOf(id));
        }
        return listIds;
    }

    /**
     * Удаление всех задач.
     */
    @Override
    public void deleteListOfTask() {
        super.deleteListOfTask();
        save();
    }

    /**
     * Удаление всех эпиков.
     */
    @Override
    public void deleteListOfEpic() {
        super.deleteListOfEpic();
        save();
    }

    /**
     * Удаление всех подзадач.
     */
    @Override
    public void deleteListOfSubtask() {
        super.deleteListOfSubtask();
        save();
    }

    /**
     * Обновление задачи.
     */
    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    /**
     * Обновление эпика.
     */
    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    /**
     * Обновление подзадачи.
     */
    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    /**
     * Удаление задачи по идентификатору.
     */
    @Override
    public void deleteTaskForID(long numberId) {
        super.deleteTaskForID(numberId);
        save();
    }

    /**
     * Удаление эпика по идентификатору.
     */
    @Override
    public void deleteEpicForID(long numberId) {
        super.deleteEpicForID(numberId);
        save();
    }

    /**
     * Удаление подзадачи по идентификатору.
     */
    @Override
    public void deleteSubtaskForID(long numberId) {
        super.deleteSubtaskForID(numberId);
        save();
    }

    /**
     * Установка статуса для задачи
     */
    @Override
    public Task setStatusForTask(Task task, Status status) {
        Task childTask = super.setStatusForTask(task, status);
        save();
        return childTask;
    }

    /**
     * Установка статуса для подзадачи
     */
    @Override
    public Subtask setStatusForSubtask(Subtask subtask, Status status) {
        Subtask childSubtask = super.setStatusForSubtask(subtask, status);
        save();
        return childSubtask;
    }

    /**
     * Установка статуса для эпиков
     */
    @Override
    public void setStatusForEpic(long numberEpicID) {
        super.setStatusForEpic(numberEpicID);
        save();
    }

    /**
     * Создание задачи. Сам объект должен передаваться в качестве параметра.
     */
    @Override
    public long createTask(Task task) {
        long childTask = super.createTask(task);
        save();
        return childTask;
    }

    /**
     * Создание эпика. Сам объект должен передаваться в качестве параметра.
     */
    @Override
    public long createTask(Epic epic) {
        long childEpic = super.createTask(epic);
        save();
        return childEpic;
    }

    /**
     * Создание подзадачи. Сам объект должен передаваться в качестве параметра.
     */
    @Override
    public long createTask(Subtask subtask) {
        long childSubtask = super.createTask(subtask);
        save();
        return childSubtask;
    }

    /**
     * Получение задачи по идентификатору.
     */
    @Override
    public Task getTaskByID(long numberId) {
        Task task = super.getTaskByID(numberId);
        save();
        return task;
    }

    /**
     * Получение эпика по идентификатору.
     */
    @Override
    public Epic getEpicByID(long numberId) {
        Epic epic = super.getEpicByID(numberId);
        save();
        return epic;
    }

    /**
     * Получение задачи по идентификатору.
     */
    @Override
    public Subtask getSubtaskByID(long numberId) {
        Subtask subtask = super.getSubtaskByID(numberId);
        save();
        return subtask;
    }

    @Override
    public LocalDateTime setStartTimeEpic(Epic epic) {
        LocalDateTime localDateTime = super.setStartTimeEpic(epic);
        save();
        return localDateTime;
    }

    @Override
    public LocalDateTime setEndTimeEpic(Epic epic) {
        LocalDateTime localDateTime = super.setEndTimeEpic(epic);
        save();
        return localDateTime;
    }

    @Override
    public Duration setDurationEpic(Epic epic) {
        Duration duration = super.setDurationEpic(epic);
        save();
        return duration;
    }

    @Override
    public Duration updateTimeEpic(Epic epic) {
        Duration duration = super.updateTimeEpic(epic);
        save();
        return duration;
    }

    @Override
    protected void addTasksInPrioritizedList(Task task) {
        super.addTasksInPrioritizedList(task);
    }

//    public static void main(String[] args) {
//
//        File file = new File("file.csv");
//        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
//
//        Task t1 = new Task("Task 1", "DescriptionTask 1",
//                "2022-08-25 | 10:00", 12, 30);
//        fileBackedTasksManager.createTask(t1);
//
//
//        System.out.println("Tasks:");
//        System.out.println("Time: " + t1.getStartTime() + " - " + t1.getDuration() + " - " + t1.getEndTime());
//        System.out.println(fileBackedTasksManager.getListOfTask());
//
//        Epic e1 = new Epic("Epic 1", "DescriptionEpic 1", fileBackedTasksManager);
//        fileBackedTasksManager.createTask(e1);
//        System.out.println("Epics:");
//        System.out.println(fileBackedTasksManager.getListOfEpic());
//
//        Subtask s1 = new Subtask("Subtask 1", "DescriptionSubtask 1",
//                "2022-08-23 | 10:00", 12, 0, e1.getNumberId());
//        Subtask s2 = new Subtask("Subtask 2", "DescriptionSubtask 2",
//                "2022-08-28 | 10:00", 1, 0, e1.getNumberId());
//        fileBackedTasksManager.createTask(s1);
//        fileBackedTasksManager.createTask(s2);
//        System.out.println("Subtasks:");
//        System.out.println(fileBackedTasksManager.getListOfSubtask());
//
//        System.out.println("Change Status");
//        fileBackedTasksManager.setStatusForSubtask(s1, Status.DONE);
//        fileBackedTasksManager.setStatusForSubtask(s2, Status.DONE);
//        fileBackedTasksManager.setStatusForTask(t1, Status.IN_PROGRESS);
//        System.out.println("tasks - " + fileBackedTasksManager.getListOfTask());
//        System.out.println("subtasks - " + fileBackedTasksManager.getListOfSubtask());
//        System.out.println("epics - " + fileBackedTasksManager.getListOfEpic());
//
//        fileBackedTasksManager.getTaskByID(t1.getNumberId());
//        fileBackedTasksManager.getEpicByID(e1.getNumberId());
//        fileBackedTasksManager.getTaskByID(1);
//        System.out.println("History: " + fileBackedTasksManager.getHistoryManager().getHistory());
//
//        System.out.println("Sorted:\n" + fileBackedTasksManager.getPrioritizedTasks());
//
//        FileBackedTasksManager.loadFromFile(file);
//    }
}