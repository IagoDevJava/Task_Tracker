package managers_types;

import interfaces_and_utilities.HistoryManager;
import my_LinkedList.CustomLinkedList;
import my_LinkedList.Node;
import tasks_types.Task;

import java.util.List;
import java.util.Objects;

public class InMemoryHistoryManager<T> implements HistoryManager {

    /**
     * Возможность хранить историю просмотров задач всех типов.
     */
    private CustomLinkedList<Task> historyViewsOfTasks = new CustomLinkedList<>();

    /**
     * Добавление задач в историю
     */
    @Override
    public void addHistory(Task task) {
        if (historyViewsOfTasks.getSize() >= 10) {
            historyViewsOfTasks.removeFirst();
        }
        historyViewsOfTasks.linkLast(task);
    }

    /**
     * удаляет задачи из списка просмотренных.
     */
    @Override
    public void remove(long id) {
        Node requiredNode = historyViewsOfTasks.getNodeValuesByIdNumbers().get(id);
        historyViewsOfTasks.removeNode(requiredNode);
    }

    /**
     * Получение истории
     */
    @Override
    public List<Task> getHistory() {
        return historyViewsOfTasks.getTask();
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