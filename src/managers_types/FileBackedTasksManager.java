package managers_types;

import exceptions.ManagerSaveException;
import interfaces_and_utilities.HistoryManager;
import interfaces_and_utilities.Managers;
import interfaces_and_utilities.TaskManager;
import tasks_types.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;
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
            bufferedWriter.append("id,type,name,status,description,epic\n");

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
            bufferedWriter.append(toString(historyManager));

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
    private static String toString(HistoryManager historyManager) {
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
            bufferedReader.readLine();
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

                getTaskFromString(line);
            }
            line = bufferedReader.readLine();
            getHistoryFromString(line);
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
                break;
            case EPIC:
                unifiedTask = new Epic(tasksFromString[2], tasksFromString[4]);
                break;
            case SUBTASK:
                unifiedTask = new Subtask(tasksFromString[2], tasksFromString[4], parseInt(tasksFromString[5]));
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
    public void upgradeTask(Task task) throws ManagerSaveException {
        super.upgradeTask(task);
        save();
    }

    /**
     * Обновление эпика.
     */
    @Override
    public void upgradeEpic(Epic epic) throws ManagerSaveException {
        super.upgradeEpic(epic);
        save();
    }

    /**
     * Обновление подзадачи.
     */
    @Override
    public void upgradeSubtask(Subtask subtask) throws ManagerSaveException {
        super.upgradeSubtask(subtask);
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
    public void setStatusForEpics(long numberEpicID) throws ManagerSaveException {
        super.setStatusForEpics(numberEpicID);
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

        Task t1 = new Task("Task 1", "DescriptionTask 1");
        Epic e1 = new Epic("Epic 1", "DescriptionEpic 1");
        Subtask s1 = new Subtask("Subtask 1", "DescriptionSubtask 1", 2);
        Subtask s2 = new Subtask("Subtask 2", "DescriptionSubtask 2", 2);

        File file = new File("file.csv");
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);

        fileBackedTasksManager.createTask(t1);
        System.out.println("Tasks:");
        System.out.println(fileBackedTasksManager.getListOfTask());

        fileBackedTasksManager.createTask(e1);
        System.out.println("Epics:");
        System.out.println(fileBackedTasksManager.getListOfEpic());

        fileBackedTasksManager.createTask(s1);
        fileBackedTasksManager.createTask(s2);
        System.out.println("Subtasks:");
        System.out.println(fileBackedTasksManager.getListOfSubtask());

        System.out.println("Change Status");
        System.out.println("subtasks - " + fileBackedTasksManager.getListOfSubtask());
        fileBackedTasksManager.setStatusForSubtask(s1, Status.DONE);
        fileBackedTasksManager.setStatusForSubtask(s2, Status.DONE);
        fileBackedTasksManager.setStatusForTask(t1, Status.IN_PROGRESS);
        System.out.println("tasks - " + fileBackedTasksManager.getListOfTask());
        System.out.println("subtasks - " + fileBackedTasksManager.getListOfSubtask());
        System.out.println("epics - " + fileBackedTasksManager.getListOfEpic());

        System.out.println("Get history");
        fileBackedTasksManager.getTaskByID(t1.getNumberId());
        fileBackedTasksManager.getEpicByID(12);
        fileBackedTasksManager.getTaskByID(13);
        System.out.println(fileBackedTasksManager.getHistoryManager().getHistory());

        FileBackedTasksManager.loadFromFile(file);

        //сравниваем списки всех задач, подзадач, эпиков и истории в обоих менеджерах
        Task t1m = new Task("Task 1", "DescriptionTask 1");
        Epic e1m = new Epic("Epic 1", "DescriptionEpic 1");
        Subtask s1m = new Subtask("Subtask 1", "DescriptionSubtask 1", 2);
        Subtask s2m = new Subtask("Subtask 2", "DescriptionSubtask 2", 2);

        TaskManager manager = Managers.getDefaultTaskManager();

        manager.createTask(t1m);
        manager.getListOfTask();

        manager.createTask(e1m);
        manager.getListOfEpic();

        manager.createTask(s1m);
        manager.createTask(s2m);
        manager.getListOfSubtask();

        manager.getListOfSubtask();
        manager.setStatusForSubtask(s1m, Status.DONE);
        manager.setStatusForSubtask(s2m, Status.DONE);
        manager.setStatusForTask(t1m, Status.IN_PROGRESS);
        manager.getListOfTask();
        manager.getListOfSubtask();
        manager.getListOfEpic();


        manager.getTaskByID(t1m.getNumberId());
        manager.getEpicByID(12);
        manager.getTaskByID(13);
        manager.getHistoryManager().getHistory();

        System.out.println("TESTS:");
        System.out.println(fileBackedTasksManager.getListOfTask().equals(manager.getListOfTask()));
        System.out.println(fileBackedTasksManager.getListOfEpic().equals(manager.getListOfEpic()));
        System.out.println(fileBackedTasksManager.getListOfSubtask().equals(manager.getListOfSubtask()));
        System.out.println(fileBackedTasksManager.getHistoryManager().getHistory().equals(manager.getHistoryManager().getHistory()));
    }
}
