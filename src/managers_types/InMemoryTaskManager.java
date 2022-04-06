package managers_types;

import tasks_types.Epic;
import tasks_types.Status;
import tasks_types.Subtask;
import tasks_types.Task;

import java.util.HashMap;
import java.util.Objects;

public class InMemoryTaskManager implements TaskManager {

    private final HistoryManager historyManager = Managers.getDefaultHistory();
    /**
     * Возможность хранить задачи всех типов.
     */
    HashMap<Long, Task> allTasks = new HashMap<>();
    HashMap<Long, Epic> allEpicTasks = new HashMap<>();
    private long numberID = 0L;

    /**
     * Создание задачи. Сам объект должен передаваться в качестве параметра.
     */
    @Override
    public Task createTask(Task task) {
        task.setStatus(task.getStatusNEW());
        allTasks.put(++numberID, task);
        return task;
    }

    /**
     * Создание эпика. Сам объект должен передаваться в качестве параметра.
     */
    @Override
    public Epic createTask(Epic epic) {
        epic.setStatus(epic.getStatusNEW());
        allEpicTasks.put(++numberID, epic);
        return epic;
    }

    /**
     * Создание подзадачи. Сам объект должен передаваться в качестве параметра.
     */
    @Override
    public Subtask createTask(Subtask subtask, Epic epic) {
        subtask.setMyEpicID(allEpicTasks, epic);
        subtask.setStatus(subtask.getStatusNEW());
        epic.allSubtasksOfEpic.put(++numberID, subtask);
        return subtask;
    }

    /**
     * Получение списка всех задач.
     */
    @Override
    public String getListOfTask() {
        String listOfTask = "";
        if (!allTasks.isEmpty()) {
            for (Long taskID : allTasks.keySet()) {
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
    @Override
    public String getListOfEpic() {
        String listOfEpic = "";
        if (!allEpicTasks.isEmpty()) {
            for (Long taskID : allEpicTasks.keySet()) {
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
    @Override
    public String getListOfSubtask(Epic epic) {
        String listOfSubtask = "";
        if (!epic.allSubtasksOfEpic.isEmpty() && allEpicTasks.containsValue(epic)) {
            for (Long taskID : epic.allSubtasksOfEpic.keySet()) {
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
    @Override
    public void clearListOfTask() {
        allTasks.clear();
    }

    /**
     * Удаление всех эпиков.
     */
    @Override
    public void clearListOfEpic() {
        allEpicTasks.clear();
    }

    /**
     * Удаление всех подзадач.
     */
    @Override
    public void clearListOfSubtask(Epic epic) {
        epic.allSubtasksOfEpic.clear();
    }

    /**
     * Получение задачи по идентификатору.
     */
    @Override
    public String getTaskForID(long numberID) {
        Task task = allTasks.get(numberID);
        getHistoryManager().addHistory(task);
        String neededTask = "";
        if (!allTasks.isEmpty() && task != null) {
            neededTask = numberID + " " + task;
        } else {
            neededTask = "Задачи с таким ID нет в списке.";
        }
        return neededTask;
    }

    /**
     * Получение эпика по идентификатору.
     */
    @Override
    public String getEpicForID(long numberID) {
        Epic epic = allEpicTasks.get(numberID);
        getHistoryManager().addHistory(epic);
        String neededEpic = "";
        if (!allEpicTasks.isEmpty() && epic != null) {
            neededEpic = numberID + " " + epic;
        } else {
            neededEpic = "Задачи с таким ID нет в списке.";
        }
        return neededEpic;
    }

    /**
     * Получение подзадачи по идентификатору.
     */
    @Override
    public String getSubtaskForID(long numberID, Epic epic) {
        Subtask subTask = epic.allSubtasksOfEpic.get(numberID);
        getHistoryManager().addHistory(subTask);
        String neededSubtask = "";
        if (!epic.allSubtasksOfEpic.isEmpty() && subTask != null
                && allEpicTasks.containsValue(epic)) {
            neededSubtask = numberID + " " + subTask;
        } else {
            neededSubtask = "Задачи с таким ID нет в списке.";
        }
        return neededSubtask;
    }

    /**
     * Обновление задачи.
     */
    @Override
    public void enterNewTask(long numberID, Task task) {
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
    @Override
    public void enterNewEpic(long numberID, Epic oldEpic, Epic newEpic) {
        if (!allEpicTasks.isEmpty() && allEpicTasks.get(numberID) != null) {
            createStatusForEpic(numberID);
            allEpicTasks.put(numberID, newEpic);
            newEpic.allSubtasksOfEpic = oldEpic.allSubtasksOfEpic;
        } else {
            System.out.println("Задачи с таким ID нет в списке.");
        }
    }

    /**
     * Обновление подзадачи.
     */
    @Override
    public void enterNewSubtask(long numberID, Subtask subtask, Epic epic) {
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
    @Override
    public void deleteTaskForID(long numberID) {
        if (!allTasks.isEmpty() && allTasks.get(numberID) != null) {
            allTasks.remove(numberID);
        } else {
            System.out.println("Задачи с таким ID нет в списке.");
        }
    }

    /**
     * Удаление эпика по идентификатору.
     */
    @Override
    public void deleteEpicForID(long numberID) {
        if (!allEpicTasks.isEmpty() && allEpicTasks.get(numberID) != null) {
            allEpicTasks.remove(numberID);
        } else {
            System.out.println("Задачи с таким ID нет в списке.");
        }
    }

    /**
     * Удаление подзадачи по идентификатору.
     */
    @Override
    public void deleteSubtaskForID(long numberID, Epic epic) {
        if (!epic.allSubtasksOfEpic.isEmpty() && epic.allSubtasksOfEpic.get(numberID) != null) {
            epic.allSubtasksOfEpic.remove(numberID);
        } else {
            System.out.println("Задачи с таким ID нет в списке.");
        }
    }

    /**
     * Установка статуса для задачи
     */
    @Override
    public Task setStatusForTask(Task task, Status status) {
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
    @Override
    public Subtask setStatusForSubtask(Subtask subtask, Status status) {
        subtask.setStatus(status);
        allTasks.put(++numberID, subtask);
        createStatusForEpic(subtask.getMyEpicID());
        return subtask;
    }

    /**
     * Расчет статуса для эпиков
     */
    @Override
    public void createStatusForEpic(long numberEpicID) {
        boolean isStatus = true;
        Epic thisEpic = allEpicTasks.get(numberEpicID); //Создал для улучшения читабельности кода
        for (Long aLong : thisEpic.allSubtasksOfEpic.keySet()) {
            if (thisEpic.allSubtasksOfEpic.get(aLong).getStatus().equals(Status.DONE)) {
                isStatus = false;
            } else if (thisEpic.allSubtasksOfEpic.get(aLong).getStatus()
                    .equals(Status.IN_PROGRESS)) {
                isStatus = true;
                break;
            }
        }
        if (isStatus) {
            thisEpic.setStatus(thisEpic.getStatusIN_PROGRESS());
        } else {
            thisEpic.setStatus(thisEpic.getStatusDONE());
        }
        allTasks.put(++numberID, thisEpic);
    }

    /**
     * Получаем объект для получения и добавления истории
     */
    @Override
    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InMemoryTaskManager that = (InMemoryTaskManager) o;
        return numberID == that.numberID
                && Objects.equals(historyManager, that.historyManager)
                && Objects.equals(allTasks, that.allTasks)
                && Objects.equals(allEpicTasks, that.allEpicTasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberID, allTasks, allEpicTasks, historyManager);
    }

    @Override
    public String toString() {
        return "program_logic.Manager{" +
                "allTasks=" + allTasks +
                ", allEpicTasks=" + allEpicTasks +
                '}';
    }
}