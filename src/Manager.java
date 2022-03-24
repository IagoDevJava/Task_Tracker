import java.util.HashMap;
import java.util.Objects;

public class Manager {

    private int numberID = 0;

    /**
     * Возможность хранить задачи всех типов. Для этого вам нужно выбрать подходящую коллекцию.
     */
    HashMap<Integer, Task> allTasks = new HashMap<>();
    HashMap<Integer, Epic> allEpicTasks = new HashMap<>();

    /**
     * Создание задачи. Сам объект должен передаваться в качестве параметра.
     */
    public Task createTask(Task task) {
        task.setStatus(task.getStatusNEW());
        allTasks.put(++numberID, task);
        return task;
    }

    /**
     * Создание эпика. Сам объект должен передаваться в качестве параметра.
     */
    public Epic createTask(Epic epic) {
        epic.setStatus(epic.getStatusNEW());
        allEpicTasks.put(++numberID, epic);
        return epic;
    }

    /**
     * Создание подзадачи. Сам объект должен передаваться в качестве параметра.
     */
    public Subtask createTask(Subtask subtask, Epic epic) {
        subtask.setEpic(epic);
        subtask.setStatus(subtask.getStatusNEW());
        epic.allSubtasksOfEpic.put(++numberID, subtask);
        return subtask;
    }

    /**
     * Получение списка всех задач.
     */
    public String getListOfTask() {
        String listOfTask = "";
        if (!allTasks.isEmpty()) {
            for (Integer taskID : allTasks.keySet()) {
                listOfTask = listOfTask + taskID + " " + allTasks.get(taskID) + "\n";
            }
        } else {
            listOfTask = "Список задач пуст.";
        }
        return listOfTask;
    }

    /**
     * Получение списка всех эпиков.
     */
    public String getListOfEpic() {
        String listOfEpic = "";
        if (!allEpicTasks.isEmpty()) {
            for (Integer taskID : allEpicTasks.keySet()) {
                listOfEpic = listOfEpic + taskID + " " + allEpicTasks.get(taskID) + "\n";
            }
        } else {
            listOfEpic = "Список задач пуст.";
        }
        return listOfEpic;
    }

    /**
     * Получение списка всех подзадач.
     */
    public String getListOfSubtask(Epic epic) {
        String listOfSubtask = "";
        if (!epic.allSubtasksOfEpic.isEmpty() && allEpicTasks.containsValue(epic)) {
            for (Integer taskID : epic.allSubtasksOfEpic.keySet()) {
                listOfSubtask = listOfSubtask + taskID + " " + epic.allSubtasksOfEpic.get(taskID) + "\n";
            }
        } else {
            listOfSubtask = "Список задач пуст.\n";
        }
        return listOfSubtask;
    }

    /**
     * Удаление всех задач.
     */
    public void clearListOfTask() {
        allTasks.clear();
    }

    /**
     * Удаление всех эпиков.
     */
    public void clearListOfEpic() {
        allEpicTasks.clear();
    }

    /**
     * Удаление всех подзадач.
     */
    public void clearListOfSubtask(Epic epic) {
        epic.allSubtasksOfEpic.clear();
    }

    /**
     * Получение задачи по идентификатору.
     */
    public String getTaskForID(int numberID) {
        String neededTask = "";
        if (!allTasks.isEmpty() && allTasks.get(numberID) != null) {
            neededTask = numberID + " " + allTasks.get(numberID);
        } else {
            neededTask = "Задачи с таким ID нет в списке.";
        }
        return neededTask;
    }

    /**
     * Получение эпика по идентификатору.
     */
    public String getEpicForID(int numberID) {
        String neededEpic = "";
        if (!allEpicTasks.isEmpty() && allEpicTasks.get(numberID) != null) {
            neededEpic = numberID + " " + allEpicTasks.get(numberID);
        } else {
            neededEpic = "Задачи с таким ID нет в списке.";
        }
        return neededEpic;
    }

    /**
     * Получение подзадачи по идентификатору.
     */
    public String getSubtaskForID(int numberID, Epic epic) {
        String neededSubtask = "";
        if (!epic.allSubtasksOfEpic.isEmpty() && epic.allSubtasksOfEpic.get(numberID) != null
                && allEpicTasks.containsValue(epic)) {
            neededSubtask = numberID + " " + epic.allSubtasksOfEpic.get(numberID);
        } else {
            neededSubtask = "Задачи с таким ID нет в списке.";
        }
        return neededSubtask;
    }

    /**
     * Обновление задачи.
     */
    public void enterNewTask(int numberID, Task task) {
        if (!allTasks.isEmpty() && allTasks.get(numberID) != null) {
            task.setStatus(task.getStatusNEW());
            allTasks.put(numberID, task);
        } else {
            System.out.println("Задачи с таким ID нет в списке.");
        }
    }

    /**
     * Обновление эпика.
     */
    public void enterNewEpic(int numberID, Epic oldEpic, Epic newEpic) {
        if (!allEpicTasks.isEmpty() && allEpicTasks.get(numberID) != null) {
            createStatusForEpic(newEpic);
            allEpicTasks.put(numberID, newEpic);
            newEpic.allSubtasksOfEpic = oldEpic.allSubtasksOfEpic;
        } else {
            System.out.println("Задачи с таким ID нет в списке.");
        }
    }

    /**
     * Обновление подзадачи.
     */
    public void enterNewSubtask(int numberID, Subtask subtask, Epic epic) {
        if (!epic.allSubtasksOfEpic.isEmpty() && epic.allSubtasksOfEpic.get(numberID) != null) {
            subtask.setStatus(subtask.getStatusNEW());
            epic.allSubtasksOfEpic.put(numberID, subtask);
        } else {
            System.out.println("Задачи с таким ID нет в списке.");
        }
    }

    /**
     * Удаление задачи по идентификатору.
     */
    public void deleteTaskForID(int numberID) {
        if (!allTasks.isEmpty() && allTasks.get(numberID) != null) {
            allTasks.remove(numberID);
        } else {
            System.out.println("Задачи с таким ID нет в списке.");
        }
    }

    /**
     * Удаление эпика по идентификатору.
     */
    public void deleteEpicForID(int numberID) {
        if (!allEpicTasks.isEmpty() && allEpicTasks.get(numberID) != null) {
            allEpicTasks.remove(numberID);
        } else {
            System.out.println("Задачи с таким ID нет в списке.");
        }
    }

    /**
     * Удаление подзадачи по идентификатору.
     */
    public void deleteSubtaskForID(int numberID, Epic epic) {
        if (!epic.allSubtasksOfEpic.isEmpty() && epic.allSubtasksOfEpic.get(numberID) != null) {
            epic.allSubtasksOfEpic.remove(numberID);
        } else {
            System.out.println("Задачи с таким ID нет в списке.");
        }
    }

    /**
     * Установка статуса для задачи
     */
    public Task setStatusForTask(Task task, String status) {
        if (task instanceof Epic) {
            System.out.println("Действие невозможно, проверьте статусы подзадач");
        } else {
            task.setStatus(status);
            allTasks.put(++numberID, task);
        }
        return task;
    }

    /**
     * Установка статуса для подзадачи
     */
    public Subtask setStatusForSubtask(Subtask subtask, String status) {
        subtask.setStatus(status);
        allTasks.put(++numberID, subtask);
        createStatusForEpic(subtask.getEpic());
        return subtask;
    }

    /**
     * Расчет статуса для эпиков
     */
    public void createStatusForEpic(Epic epic) {
        boolean isStatus = true;
        for (Integer integer : epic.allSubtasksOfEpic.keySet()) {
            if (epic.allSubtasksOfEpic.get(integer).getStatus().equals("DONE")) {
                isStatus = false;
            } else if (epic.allSubtasksOfEpic.get(integer).getStatus()
                    .equals("IN_PROGRESS")) {
                isStatus = true;
                break;
            }
        }
        if (isStatus) {
            epic.setStatus(epic.getStatusIN_PROGRESS());
        } else {
            epic.setStatus(epic.getStatusDONE());
        }
        allTasks.put(++numberID, epic);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Manager manager = (Manager) o;
        return numberID == manager.numberID
                && Objects.equals(allTasks, manager.allTasks)
                && Objects.equals(allEpicTasks, manager.allEpicTasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberID, allTasks, allEpicTasks);
    }

    @Override
    public String toString() {
        return "Manager{" +
                "allTasks=" + allTasks +
                ", allEpicTasks=" + allEpicTasks +
                '}';
    }
}