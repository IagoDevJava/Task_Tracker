import exceptions.ManagerSaveException;
import interfaces_and_utilities.Managers;
import interfaces_and_utilities.TaskManager;
import tasks_types.Epic;
import tasks_types.Subtask;
import tasks_types.Task;

public class Main {
    public static void main(String[] args) throws ManagerSaveException {

        TaskManager manager = Managers.getDefaultTaskManager();
        Task t1 = new Task("Task 1", "DescriptionTask 1",
                "2022-08-25 | 10:00", 12, 30);
        manager.createTask(t1);

        Task t2 = new Task("Task 2", "DescriptionTask 2",
                "2022-08-26 | 12:00", 12, 30);
        manager.createTask(t2);

        Epic e1 = new Epic("Epic 1", "DescriptionEpic 1",
                manager);
        manager.createTask(e1);

        Subtask s1 = new Subtask("Subtask 1", "DescriptionSubtask 1",
                "2022-08-27 | 10:00", 72, 0, e1.getNumberId());
        manager.createTask(s1);
        Subtask s2 = new Subtask("Subtask 2", "DescriptionSubtask 2",
                "2022-08-28 | 10:00", 1, 0, e1.getNumberId());
        manager.createTask(s2);

//        System.out.println("Tasks:");
//        System.out.println(manager.getListOfTask());
//
//        System.out.println("Epics:");
//        System.out.println(manager.getListOfEpic());
//
//        System.out.println("Subtasks:");
//        System.out.println(manager.getListOfSubtask());
//        manager.clearListOfSubtask();


//        System.out.println("Change Status");
//        manager.setStatusForSubtask(s1, Status.DONE);
//        System.out.println("subtasks - " + manager.getListOfSubtask());
//        System.out.println("epics - " + manager.getListOfEpic());

//        System.out.println("Delete tasks and get them");
//        fileBackedTasksManager.deleteTaskForID(t1.getNumberId());
//        System.out.println(fileBackedTasksManager.getListOfTask());
//        System.out.println(fileBackedTasksManager.getListOfSubtask());

//        System.out.println("Get history");
//        manager.getTaskByID(t1.getNumberId());
//        manager.getSubtaskByID(s1.getNumberId());
//        manager.getEpicByID(e1.getNumberId());
//        manager.getHistoryManager().remove(3);
//        System.out.println(manager.getHistoryManager().getHistory());

//        System.out.println("Sorted:\n" + manager.getPrioritizedListOfTasks());
        System.out.println("Sorted:\n"
                + manager.getPrioritizedTasks());
    }
}