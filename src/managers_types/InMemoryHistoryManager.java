package managers_types;

import tasks_types.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    /**
     * Возможность хранить историю просмотров задач всех типов.
     */
    List<Task> historyViewsOfTasks = new ArrayList<>(10);

    /**
     * Добавление задач в историю
     */
    @Override
    public void addHistory(Task task) {
        if (historyViewsOfTasks.size() >= 10) {
            historyViewsOfTasks.remove(historyViewsOfTasks.get(0));
        }
        historyViewsOfTasks.add(task);
    }

    /**
     * Получение истории
     */
    @Override
    public List<Task> getHistory() {
        return historyViewsOfTasks;
    }
}
