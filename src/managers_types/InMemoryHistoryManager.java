package managers_types;

import interfaces_and_utilities.HistoryManager;
import tasks_types.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    /**
     * Возможность хранить и получать Node за O(1).
     */
    protected Map<Long, Node> nodeValuesByIdNumbers = new HashMap<>();
    /**
     * Возможность изменить методы LinkedList.
     */
    private Node head;
    private Node tail;

    /**
     * Добавление задач в историю
     */
    @Override
    public void add(Task task) {
        if (nodeValuesByIdNumbers.containsKey(task.getNumberId())) {
            Node requiredNode = nodeValuesByIdNumbers.get(task.getNumberId());
            removeNode(requiredNode);
        }
        linkLast(task);
    }

    /**
     * удаляет задачи из списка просмотренных.
     */
    @Override
    public void remove(long id) {
        Node requiredNode = nodeValuesByIdNumbers.get(id);
        removeNode(requiredNode);
    }

    /**
     * Получение истории
     */
    @Override
    public List<Task> getHistory() {
        return getTask();
    }

    /**
     * Замена ссылок и добавление в конец
     */
    private void linkLast(Task task) {
        final Node oldTail = tail;
        final Node newNode = new Node(oldTail, task, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        nodeValuesByIdNumbers.put(task.getNumberId(), newNode);
    }

    /**
     * Получение списка истории задач
     */
    private List<Task> getTask() {
        ArrayList<Task> outputOfTasks = new ArrayList<>();
        Node node = head;
        while (node != null) {
            outputOfTasks.add(node.data);
            node = node.next;
        }
        return outputOfTasks;
    }

    /**
     * Удаление ноды, при ссылке на нее черед ID задачи
     */
    private void removeNode(Node node) {
        if (node != null) {
            final Node nextNode = node.next;
            final Node prevNode = node.prev;

            if (nextNode != null && prevNode != null) { // из середины
                prevNode.next = node.next;
                nextNode.prev = node.prev;
            } else if (nextNode == null && prevNode != null) { // последнюю
                tail = node.prev;
                node.prev.next = null;
            } else if (nextNode != null) { // первую
                head = node.next;
                node.next.prev = null;
            } else { // единственную
                head = null;
                tail = null;
            }
            nodeValuesByIdNumbers.remove(node.data.getNumberId());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InMemoryHistoryManager that = (InMemoryHistoryManager) o;
        return Objects.equals(head, that.head) && Objects.equals(tail, that.tail) && Objects.equals(nodeValuesByIdNumbers, that.nodeValuesByIdNumbers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(head, tail, nodeValuesByIdNumbers);
    }

    @Override
    public String toString() {
        return "InMemoryHistoryManager{" +
                "head=" + head +
                ", tail=" + tail +
                ", nodeValuesByIdNumbers=" + nodeValuesByIdNumbers +
                '}';
    }

    static class Node {
        private final Task data;
        private Node next;
        private Node prev;

        public Node(Node prev, Task data, Node next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }
}