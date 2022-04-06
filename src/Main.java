import managers_types.Managers;
import managers_types.TaskManager;
import tasks_types.Epic;
import tasks_types.Subtask;
import tasks_types.Task;

public class Main {
    public static void main(String[] args) {

        TaskManager manager = Managers.getDefaultTaskManager();

        Task t1 = new Task("Погулять с собакой", "Прогулка во дворе не менее часа");
        Task t2 = new Task("Выпить чай", "Заварить ахмад");
        Epic e1 = new Epic("Переезд", "Переехать до 24.04");
        Subtask s1 = new Subtask("Собрать мебель", "Не забыть упаковать фурнитуру");
        Subtask s2 = new Subtask("Собрать посуду", "Разложить по коробкам и подписать");
        Epic e2 = new Epic("Поиск работы", "Необходима, чтобы кушать");
        Subtask s3 = new Subtask("Обновить резюме", "Зайти на сайт и тп");
        Task new_t1 = new Task("Покормить кота", "Собаки то у меня и нет, " +
                "а вот жирный котик имеется :)");
        Epic new_e2 = new Epic("Поиск учебы", "Чтоб найти работу надо сначала выучиться");
        Subtask new_s3 = new Subtask("Сравнить курсы", "Выбрать несколько курсов и сравнить");

        //Ввести новую задачу
        System.out.println("Ввести новую задачу");
        //1
        manager.createTask(t1);
        //2
        manager.createTask(t2);
        //3
        manager.createTask(e2);
        //4
        manager.createTask(s3, e2);
        //5
        manager.createTask(e1);
        //6
        manager.createTask(s1, e1);
        //7
        manager.createTask(s2, e1);

//        //Получить список всех задач
//        System.out.println("Получить список всех задач");
//        System.out.println("t\n" + manager.getListOfTask());
//        System.out.println("e\n" + manager.getListOfEpic());
//        System.out.println("e2\n" + manager.getListOfSubtask(e2));
//        System.out.println("e1\n" + manager.getListOfSubtask(e1));
//        System.out.println("_________________________________________");

        //Получить задачу
        System.out.println("Вывести задачу:");
        System.out.println(manager.getTaskForID(1));
        System.out.println(manager.getEpicForID(3));
        System.out.println(manager.getSubtaskForID(7, e1));
        System.out.println(manager.getTaskForID(1));
        System.out.println(manager.getEpicForID(3));
        System.out.println(manager.getSubtaskForID(7, e1));
        System.out.println(manager.getTaskForID(1));
        System.out.println(manager.getEpicForID(3));
        System.out.println(manager.getEpicForID(3));
        System.out.println(manager.getSubtaskForID(7, e1));
        System.out.println(manager.getSubtaskForID(7, e1));
        System.out.println(manager.getEpicForID(3));
        System.out.println(manager.getTaskForID(1));
        System.out.println(manager.getEpicForID(3));
        System.out.println("_________________________________________");

        //Вывести историю просмотров
        System.out.println(manager.getHistoryManager().getHistory().size());

//        //Обновить задачу
//        System.out.println("Обновить задачу");
//        manager.enterNewTask(1, new_t1);
//        manager.enterNewEpic(3, e2, new_e2);
//        manager.enterNewSubtask(4, new_s3, e2);
//
//        System.out.println(manager.getListOfTask());
//        System.out.println(manager.getListOfEpic());
//        System.out.println(manager.getListOfSubtask(new_e2));
//        System.out.println("_________________________________________");
//
//        //Удалить задачу
//        System.out.println("Удалить задачу");
//        manager.deleteTaskForID(2);
//        manager.deleteEpicForID(3);
//        manager.deleteSubtaskForID(6, e1);
//        System.out.println(manager.getListOfTask());
//        System.out.println(manager.getListOfEpic());
//        System.out.println(manager.getListOfSubtask(e2));
//        System.out.println(manager.getListOfSubtask(e1));
//        System.out.println("_________________________________________");
//
//        //Обновить статус
//        manager.setStatusForTask(t1, t1.getStatusIN_PROGRESS());
////        manager.setStatusForTask(e1, e1.getStatusDONE());
//        manager.setStatusForSubtask(s1, s1.getStatusIN_PROGRESS());
//        manager.setStatusForSubtask(s2, s2.getStatusDONE());
//        System.out.println(manager.getListOfEpic());
//        System.out.println(manager.getListOfSubtask(e1));
//
//        //Удалить все задачи
//        System.out.println("Удалить все задачи");
//        manager.clearListOfTask();
//        manager.clearListOfEpic();
//        manager.clearListOfSubtask(e1);
//        System.out.println(manager.getListOfTask());
//        System.out.println(manager.getListOfEpic());
//        System.out.println(manager.getListOfSubtask(e1));
//        System.out.println("_________________________________________");
    }
}
