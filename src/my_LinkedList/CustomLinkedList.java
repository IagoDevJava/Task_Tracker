package my_LinkedList;

import tasks_types.Task;

import java.util.*;

public class CustomLinkedList<T> {
    public Node head;
    public Node tail;
    private int size = 0;

    private Map<Long, Node> nodeValuesByIdNumbers = new HashMap<>();

    /**
     * Замена ссылок и добавление в конец
     */
    public void linkLast(Task task) {
        final Node oldTail = tail;
        final Node newNode = new Node<>(oldTail, task, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        size++;
        nodeValuesByIdNumbers.put(task.getNumberId(), newNode);
    }

    /**
     * Получение списка истории задач
     */
    public List<Task> getTask() {
        ArrayList<Task> outputOfTasks = new ArrayList<>();
        Node<Task> node = head;
        while (node != null) {
            outputOfTasks.add(node.data);
            node = node.next;
        }
        return outputOfTasks;
    }

    /**
     * Удаление ноды, при ссылке на нее черед ID задачи
     */
    public void removeNode(Node node) {
        if (node != null) {
            final Node nextNode = node.next;
            final Node prevNode = node.prev;

            if (nextNode == null) {
                tail = node.prev;
                size--;
            } else if (prevNode == null) {
                head = node.next;
                size--;
            } else {
                node.next = prevNode.next;
                node.prev = nextNode.prev;

                size--;
            }
        }
    }

    /**
     * Удаление первого элемента списка
     */
    public void removeFirst() {
        head = head.next;
        if (head == null)
            tail = null;
        else
            head.prev = null;
        size--;
    }

    /**
     * Получение мапы с нодами и идентификаторами
     */
    public Map<Long, Node> getNodeValuesByIdNumbers() {
        return nodeValuesByIdNumbers;
    }

    /**
     * Получение размера списка
     */
    public int getSize() {
        return size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomLinkedList<?> that = (CustomLinkedList<?>) o;
        return size == that.size
                && Objects.equals(head, that.head)
                && Objects.equals(tail, that.tail)
                && Objects.equals(nodeValuesByIdNumbers, that.nodeValuesByIdNumbers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(head, tail, size, nodeValuesByIdNumbers);
    }

    @Override
    public String toString() {
        return "CustomLinkedList{" +
                "head=" + head +
                ", tail=" + tail +
                ", size=" + size +
                ", nodeValuesByIdNumbers=" + nodeValuesByIdNumbers +
                '}';
    }
}
