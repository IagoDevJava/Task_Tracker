package managers_types;

import tasks_types.Task;

import java.util.List;

public interface HistoryManager {

    /**
     * помечает задачи как просмотренные.
     */
    void addHistory(Task task);

    /**
     * возвращает список просмотренных задач.
     */
    List<Task> getHistory();
}