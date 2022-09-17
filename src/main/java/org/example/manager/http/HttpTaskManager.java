package org.example.manager.http;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.manager.interfaces_and_utilities.Managers;
import org.example.manager.managers_types.FileBackedTasksManager;
import org.example.tasks.Epic;
import org.example.tasks.Subtask;
import org.example.tasks.Task;
import org.example.tasks.TypesTasks;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTasksManager {
    private final Gson gson;
    private final KVTaskClient client;

    public HttpTaskManager(String url) {
        super(null);
        gson = Managers.getGson();
        client = new KVTaskClient(url);
//        load();
    }

    protected void addTasks(List<? extends Task> tasks) {
        for (Task task : tasks) {
            final long id = task.getId();
            if (id > createdID) {
                createdID = id;
            }
            TypesTasks type = task.getType();
            if (type == TypesTasks.TASK) {
                allTasks.put(id, task);
                addTasksInPrioritizedList(task);
            } else if (type == TypesTasks.SUBTASK) {
                allSubtasks.put(id, (Subtask) task);
                addTasksInPrioritizedList(task);
            } else if (type == TypesTasks.EPIC) {
                allEpicTasks.put(id, (Epic) task);
            }
        }
    }

    public void load() {
        ArrayList<Task> tasks = gson.fromJson(client.load("tasks"),
                new TypeToken<ArrayList<Task>>() {
                }.getType());
        addTasks(tasks);

        ArrayList<Epic> epics = gson.fromJson(client.load("epics"),
                new TypeToken<ArrayList<Epic>>() {
                }.getType());
        addTasks(epics);

        ArrayList<Subtask> subtasks = gson.fromJson(client.load("subtasks"),
                new TypeToken<ArrayList<Subtask>>() {
                }.getType());
        addTasks(subtasks);

        List<Integer> history = gson.fromJson(client.load("history"),
                new TypeToken<ArrayList<Integer>>() {
                }.getType());

        for (Integer integer : history) {
            historyManager.add(getTaskByIdWithoutStory(integer));
            historyManager.add(getEpicByIdWithoutStory(integer));
            historyManager.add(getSubtaskByIdWithoutStory(integer));
        }
    }

    @Override
    protected void save() {
        String jsonTasks = gson.toJson(new ArrayList<>(allTasks.values()));
        client.put("tasks", jsonTasks);
        String jsonEpics = gson.toJson(new ArrayList<>(allEpicTasks.values()));
        client.put("epics", jsonEpics);
        String jsonSubtasks = gson.toJson(new ArrayList<>(allSubtasks.values()));
        client.put("subtasks", jsonSubtasks);

        String jsonHistory = gson.toJson(historyManager.getHistory()
                .stream().map(Task::getId).collect(Collectors.toList()));
        client.put("history", jsonHistory);
    }
}
