import interfaces_and_utilities.Managers;
import interfaces_and_utilities.TaskManager;
import tasks_types.Epic;
import tasks_types.Status;
import tasks_types.Subtask;
import tasks_types.Task;

public class Main {
    public static void main(String[] args) {

        TaskManager manager = Managers.getDefaultTaskManager();

        Task t1 = new Task("Погулять с собакой", "Прогулка во дворе не менее часа");
        Task t2 = new Task("Выпить чай", "Заварить ахмад");
        manager.createTask(t1);
        manager.createTask(t2);
        System.out.println("Tasks:");
        System.out.println(manager.getListOfTask());

        Epic e1 = new Epic("Epic 1", "Description 1");
        Epic e2 = new Epic("Epic 2", "Description 2");
        manager.createTask(e1);
        manager.createTask(e2);
        System.out.println("Epics:");
        System.out.println(manager.getListOfEpic());

        Subtask s1 = new Subtask("Subtask 1", "Description 1", e1.getNumberId());
        Subtask s2 = new Subtask("Subtask 2", "Description 2", e1.getNumberId());
        Subtask s3 = new Subtask("Subtask 3", "Description 3", e2.getNumberId());
        manager.createTask(s1);
        manager.createTask(s2);
        manager.createTask(s3);
        System.out.println("Subtasks:");
        System.out.println(manager.getListOfSubtask());

        System.out.println("Change Status");

        manager.setStatusForSubtask(s1, Status.DONE);
        manager.setStatusForSubtask(s2, Status.IN_PROGRESS);
        manager.setStatusForSubtask(s3, Status.DONE);
        System.out.println(manager.getListOfSubtask());
        System.out.println(manager.getListOfEpic());

//        System.out.println("Delete tasks and get them");
//
//        manager.deleteTaskForID(t1.getNumberId());
//        manager.deleteSubtaskForID(s3.getNumberId());
//        System.out.println(manager.getListOfTask());
//        System.out.println(manager.getListOfSubtask());

        System.out.println("Get history");
        manager.getTaskByID(t1.getNumberId());
        manager.getTaskByID(t2.getNumberId());
        manager.getTaskByID(t2.getNumberId());
        manager.getTaskByID(t2.getNumberId());
        manager.getTaskByID(t2.getNumberId());
        manager.getTaskByID(t2.getNumberId());
        manager.getTaskByID(t2.getNumberId());
        manager.getTaskByID(t2.getNumberId());
        manager.getTaskByID(t2.getNumberId());
        manager.getTaskByID(t2.getNumberId());
        manager.getTaskByID(t2.getNumberId());
        manager.getTaskByID(t2.getNumberId());
        System.out.println(manager.getHistoryManager().getHistory().size());
    }
}