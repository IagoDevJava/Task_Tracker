package managers_types;

import exceptions.ManagerSaveException;
import interfaces_and_utilities.HistoryManager;
import tasks_types.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.valueOf;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    /**
     * Сохранение задач по строкам в файл
     */
    private void save() throws ManagerSaveException {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            bufferedWriter.append("id,type,name,status,description,epic, startTime, duration, endTime\n");

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
            throw new ManagerSaveException("Ошибка в файле: " + file.getAbsolutePath());
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
        StringBuilder sb = new StringBuilder("");
        for (Task task : historyManager.getHistory()) {
            sb.append(task.getNumberId()).append(",");
        }
        return sb.toString();
    }

    /**
     * Загрузка задач в файл
     */
    public static FileBackedTasksManager loadFromFile(File file) throws ManagerSaveException {
        final FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        fileBackedTasksManager.load();
        return fileBackedTasksManager;
    }

    /**
     * Загрузка задач построчно
     */
    private void load() throws ManagerSaveException {
        long maxId = 0L;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line = bufferedReader.readLine();
            while (line != null) {
                line = bufferedReader.readLine();
                if (line.isEmpty()) {
                    break;
                }

                long numberId = getTaskFromString(line).getNumberId();
                switch (getTaskFromString(line).getType()) {
                    case TASK:
                        allTasks.put(numberId, getTaskFromString(line));
                        break;
                    case EPIC:
                        allEpicTasks.put(numberId, (Epic) getTaskFromString(line));
                        break;
                    case SUBTASK:
                        allSubtasks.put(numberId, (Subtask) getTaskFromString(line));
                        if (allEpicTasks.containsKey(((Subtask) getTaskFromString(line)).getMyEpicID())) {
                            Epic epic = allEpicTasks.get(((Subtask) getTaskFromString(line)).getMyEpicID());
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
            throw new ManagerSaveException("Ошибка в файле: " + file.getAbsolutePath());
        }
        createdID = maxId;
    }

    /**
     * Получение задачи из строки
     */
    private Task getTaskFromString(String value) throws ManagerSaveException {
        final String[] tasksFromString = value.split(",");
        Task unifiedTask = null;
        switch (TypesTasks.valueOf(tasksFromString[1])) {
            case TASK:
                unifiedTask = new Task(tasksFromString[2], tasksFromString[4]);
                unifiedTask.setStatus(Status.valueOf(tasksFromString[3]));
                unifiedTask.setNumberId(Long.parseLong(tasksFromString[0]));
                if (!tasksFromString[5].equals("null")) {
                    unifiedTask.setStartTime(LocalDateTime.parse(tasksFromString[5]));
                    unifiedTask.setEndTime(LocalDateTime.parse(tasksFromString[7]));
                    unifiedTask.setDuration(Duration.between(unifiedTask.getStartTime(), unifiedTask.getEndTime()));
                }
                break;
            case EPIC:
                unifiedTask = new Epic(tasksFromString[2], tasksFromString[4], this);
                unifiedTask.setStatus(Status.valueOf(tasksFromString[3]));
                unifiedTask.setNumberId(Long.parseLong(tasksFromString[0]));
                if (!tasksFromString[5].equals("null")) {
                    unifiedTask.setStartTime(LocalDateTime.parse(tasksFromString[5]));
                    unifiedTask.setEndTime(LocalDateTime.parse(tasksFromString[7]));
                    unifiedTask.setDuration(Duration.between(unifiedTask.getStartTime(), unifiedTask.getEndTime()));
                }
                break;
            case SUBTASK:
                unifiedTask = new Subtask(tasksFromString[2], tasksFromString[4], Long.parseLong(tasksFromString[5]));
                unifiedTask.setStatus(Status.valueOf(tasksFromString[3]));
                unifiedTask.setNumberId(Long.parseLong(tasksFromString[0]));
                if (!tasksFromString[6].equals("null")) {
                    unifiedTask.setStartTime(LocalDateTime.parse(tasksFromString[6]));
                    unifiedTask.setEndTime(LocalDateTime.parse(tasksFromString[8]));
                    unifiedTask.setDuration(Duration.between(unifiedTask.getStartTime(), unifiedTask.getEndTime()));
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
    public void clearListOfTask() throws ManagerSaveException {
        super.clearListOfTask();
        save();
    }

    /**
     * Удаление всех эпиков.
     */
    @Override
    public void clearListOfEpic() throws ManagerSaveException {
        super.clearListOfEpic();
        save();
    }

    /**
     * Удаление всех подзадач.
     */
    @Override
    public void clearListOfSubtask() throws ManagerSaveException {
        super.clearListOfSubtask();
        save();
    }

    /**
     * Обновление задачи.
     */
    @Override
    public void updateTask(Task task, long numberOldTask) throws ManagerSaveException {
        super.updateTask(task, numberOldTask);
        save();
    }

    /**
     * Обновление эпика.
     */
    @Override
    public void updateEpic(Epic epic, long numberOldEpic) throws ManagerSaveException {
        super.updateEpic(epic, numberOldEpic);
        save();
    }

    /**
     * Обновление подзадачи.
     */
    @Override
    public void updateSubtask(Subtask subtask, long numberOldTask) throws ManagerSaveException {
        super.updateSubtask(subtask, numberOldTask);
        save();
    }

    /**
     * Удаление задачи по идентификатору.
     */
    @Override
    public void deleteTaskForID(long numberId) throws ManagerSaveException {
        super.deleteTaskForID(numberId);
        save();
    }

    /**
     * Удаление эпика по идентификатору.
     */
    @Override
    public void deleteEpicForID(long numberId) throws ManagerSaveException {
        super.deleteEpicForID(numberId);
        save();
    }

    /**
     * Удаление подзадачи по идентификатору.
     */
    @Override
    public void deleteSubtaskForID(long numberId) throws ManagerSaveException {
        super.deleteSubtaskForID(numberId);
        save();
    }

    @Override
    public Task setStatusForTask(Task task, Status status) throws ManagerSaveException {
        Task childTask = super.setStatusForTask(task, status);
        save();
        return childTask;
    }

    @Override
    public Subtask setStatusForSubtask(Subtask subtask, Status status) throws ManagerSaveException {
        Subtask childSubtask = super.setStatusForSubtask(subtask, status);
        save();
        return childSubtask;
    }

    @Override
    public void setStatusForEpic(long numberEpicID) throws ManagerSaveException {
        super.setStatusForEpic(numberEpicID);
        save();
    }

    @Override
    public long createTask(Task task) throws ManagerSaveException {
        long childTask = super.createTask(task);
        save();
        return childTask;
    }

    @Override
    public long createTask(Epic epic) throws ManagerSaveException {
        long childEpic = super.createTask(epic);
        save();
        return childEpic;
    }

    @Override
    public long createTask(Subtask subtask) throws ManagerSaveException {
        long childSubtask = super.createTask(subtask);
        save();
        return childSubtask;
    }

    @Override
    public Task getTaskByID(long numberId) throws ManagerSaveException {
        Task t = super.getTaskByID(numberId);
        save();
        return t;
    }

    public static void main(String[] args) {

        File file = new File("file.csv");
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);

        Task t1 = new Task("Task 1", "DescriptionTask 1",
                "2022-08-25 | 10:00", 12, 30);
        fileBackedTasksManager.createTask(t1);


        System.out.println("Tasks:");
        System.out.println("Time: " + t1.getStartTime() + " - " + t1.getDuration() + " - " + t1.getEndTime());
        System.out.println(fileBackedTasksManager.getListOfTask());

        Epic e1 = new Epic("Epic 1", "DescriptionEpic 1", fileBackedTasksManager);
        fileBackedTasksManager.createTask(e1);
        System.out.println("Epics:");
        System.out.println(fileBackedTasksManager.getListOfEpic());

        Subtask s1 = new Subtask("Subtask 1", "DescriptionSubtask 1",
                "2022-08-23 | 10:00", 12, 0, e1.getNumberId());
        Subtask s2 = new Subtask("Subtask 2", "DescriptionSubtask 2",
                "2022-08-28 | 10:00", 1, 0, e1.getNumberId());
        fileBackedTasksManager.createTask(s1);
        fileBackedTasksManager.createTask(s2);
        System.out.println("Subtasks:");
        System.out.println(fileBackedTasksManager.getListOfSubtask());

        System.out.println("Change Status");
        fileBackedTasksManager.setStatusForSubtask(s1, Status.DONE);
        fileBackedTasksManager.setStatusForSubtask(s2, Status.DONE);
        fileBackedTasksManager.setStatusForTask(t1, Status.IN_PROGRESS);
        System.out.println("tasks - " + fileBackedTasksManager.getListOfTask());
        System.out.println("subtasks - " + fileBackedTasksManager.getListOfSubtask());
        System.out.println("epics - " + fileBackedTasksManager.getListOfEpic());

        fileBackedTasksManager.getTaskByID(t1.getNumberId());
        fileBackedTasksManager.getEpicByID(e1.getNumberId());
        fileBackedTasksManager.getTaskByID(1);
        System.out.println("History: " + fileBackedTasksManager.getHistoryManager().getHistory());

        System.out.println("Sorted:\n" + fileBackedTasksManager.getPrioritizedTasks());

        FileBackedTasksManager.loadFromFile(file);
    }
}