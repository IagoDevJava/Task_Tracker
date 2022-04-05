package program_logic;

import program_entities.Epic;
import program_entities.Subtask;
import program_entities.Task;

public class Main {
    public static void main(String[] args) {

        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

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
        inMemoryTaskManager.createTask(t1);
        //2
        inMemoryTaskManager.createTask(t2);
        //3
        inMemoryTaskManager.createTask(e2);
        //4
        inMemoryTaskManager.createTask(s3, e2);
        //5
        inMemoryTaskManager.createTask(e1);
        //6
        inMemoryTaskManager.createTask(s1, e1);
        //7
        inMemoryTaskManager.createTask(s2, e1);

//        //Получить список всех задач
//        System.out.println("Получить список всех задач");
//        System.out.println("t\n" + inMemoryTaskManager.getListOfTask());
//        System.out.println("e\n" + inMemoryTaskManager.getListOfEpic());
//        System.out.println("e2\n" + inMemoryTaskManager.getListOfSubtask(e2));
//        System.out.println("e1\n" + inMemoryTaskManager.getListOfSubtask(e1));
//        System.out.println("_________________________________________");

        //Получить задачу
        System.out.println("Вывести задачу:");
        System.out.println(inMemoryTaskManager.getTaskForID(1));
        System.out.println(inMemoryTaskManager.getEpicForID(3));
        System.out.println(inMemoryTaskManager.getSubtaskForID(7, e1));
        System.out.println(inMemoryTaskManager.getTaskForID(1));
        System.out.println(inMemoryTaskManager.getEpicForID(3));
        System.out.println(inMemoryTaskManager.getSubtaskForID(7, e1));
        System.out.println(inMemoryTaskManager.getTaskForID(1));
        System.out.println(inMemoryTaskManager.getEpicForID(3));
        System.out.println(inMemoryTaskManager.getSubtaskForID(7, e1));
        System.out.println(inMemoryTaskManager.getSubtaskForID(7, e1));
        System.out.println(inMemoryTaskManager.getEpicForID(3));
        System.out.println("_________________________________________");

//        //Вывести историю просмотров
//        System.out.println(inMemoryTaskManager.getHistory().size());
//
//        System.out.println("**********");
//        for (int i = 9; i >= 0; i--) {
//            System.out.println("Сюда: " + (i + 1) + " мы поместим это - " + i);
//        }

//        //Обновить задачу
//        System.out.println("Обновить задачу");
//        inMemoryTaskManager.enterNewTask(1, new_t1);
//        inMemoryTaskManager.enterNewEpic(3, e2, new_e2);
//        inMemoryTaskManager.enterNewSubtask(4, new_s3, e2);
//
//        System.out.println(inMemoryTaskManager.getListOfTask());
//        System.out.println(inMemoryTaskManager.getListOfEpic());
//        System.out.println(inMemoryTaskManager.getListOfSubtask(new_e2));
//        System.out.println("_________________________________________");
//
//        //Удалить задачу
//        System.out.println("Удалить задачу");
//        inMemoryTaskManager.deleteTaskForID(2);
//        inMemoryTaskManager.deleteEpicForID(3);
//        inMemoryTaskManager.deleteSubtaskForID(6, e1);
//        System.out.println(inMemoryTaskManager.getListOfTask());
//        System.out.println(inMemoryTaskManager.getListOfEpic());
//        System.out.println(inMemoryTaskManager.getListOfSubtask(e2));
//        System.out.println(inMemoryTaskManager.getListOfSubtask(e1));
//        System.out.println("_________________________________________");
//
//        //Обновить статус
//        inMemoryTaskManager.setStatusForTask(t1, t1.getStatusIN_PROGRESS());
////        manager.setStatusForTask(e1, e1.getStatusDONE());
//        inMemoryTaskManager.setStatusForSubtask(s1, s1.getStatusIN_PROGRESS());
//        inMemoryTaskManager.setStatusForSubtask(s2, s2.getStatusDONE());
//        System.out.println(inMemoryTaskManager.getListOfEpic());
//        System.out.println(inMemoryTaskManager.getListOfSubtask(e1));
//
//        //Удалить все задачи
//        System.out.println("Удалить все задачи");
//        inMemoryTaskManager.clearListOfTask();
//        inMemoryTaskManager.clearListOfEpic();
//        inMemoryTaskManager.clearListOfSubtask(e1);
//        System.out.println(inMemoryTaskManager.getListOfTask());
//        System.out.println(inMemoryTaskManager.getListOfEpic());
//        System.out.println(inMemoryTaskManager.getListOfSubtask(e1));
//        System.out.println("_________________________________________");
    }
}
