import exceptions.ManagerSaveException;
import interfaces_and_utilities.Managers;
import interfaces_and_utilities.TaskManager;
import tasks_types.Epic;
import tasks_types.Status;
import tasks_types.Subtask;
import tasks_types.Task;

public class Main {
    public static void main(String[] args) throws ManagerSaveException {

        TaskManager manager = Managers.getDefaultTaskManager();
        Task t1 = new Task("Task 1", "DescriptionTask 1");
        Epic e1 = new Epic("Epic 1", "DescriptionEpic 1");
        Subtask s1 = new Subtask("Subtask 1", "DescriptionSubtask 1", e1.getNumberId());

        manager.createTask(t1);
        System.out.println("Tasks:");
        System.out.println(manager.getListOfTask());

        manager.createTask(e1);
        System.out.println("Epics:");
        System.out.println(manager.getListOfEpic());

        manager.createTask(s1);
        System.out.println("Subtasks:");
        System.out.println(manager.getListOfSubtask());

        System.out.println("Change Status");
        manager.setStatusForSubtask(s1, Status.IN_PROGRESS);
        System.out.println("subtasks - " + manager.getListOfSubtask());
        System.out.println("epics - " + manager.getListOfEpic());

//        System.out.println("Delete tasks and get them");
//        fileBackedTasksManager.deleteTaskForID(t1.getNumberId());
//        System.out.println(fileBackedTasksManager.getListOfTask());
//        System.out.println(fileBackedTasksManager.getListOfSubtask());

        System.out.println("Get history");
        manager.getTaskByID(t1.getNumberId());
        System.out.println(manager.getHistoryManager().getHistory());
    }
}