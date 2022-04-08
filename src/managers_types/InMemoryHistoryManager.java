package managers_types;

import tasks_types.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InMemoryHistoryManager implements HistoryManager {

    /**
     * Возможность хранить историю просмотров задач всех типов.
     */
    private List<Task> historyViewsOfTasks = new ArrayList<>(10);

    /**
     * Добавление задач в историю
     */
    @Override
    public void addHistory(Task task) {
        if (historyViewsOfTasks.size() >= 10) {
            historyViewsOfTasks.remove(0);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InMemoryHistoryManager that = (InMemoryHistoryManager) o;
        return Objects.equals(historyViewsOfTasks, that.historyViewsOfTasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(historyViewsOfTasks);
    }

    @Override
    public String toString() {
        return "InMemoryHistoryManager{" +
                "historyViewsOfTasks=" + historyViewsOfTasks +
                '}';
    }
}