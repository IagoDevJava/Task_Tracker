package org.example.Tests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.manager.http.HttpTaskServer;
import org.example.manager.interfaces_and_utilities.Managers;
import org.example.manager.interfaces_and_utilities.TaskManager;
import org.example.server.KVServer;
import org.example.tasks.Epic;
import org.example.tasks.Subtask;
import org.example.tasks.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest {
    KVServer server;
    TaskManager taskManager;
    Task task;
    Epic epic;
    Subtask subtask;
    Gson gson;

    @BeforeEach
    void setUp() throws IOException {
        server = Managers.getDefaultKVServer();
        taskManager = Managers.getDefault();
        gson = Managers.getGson();
        task = new Task("taskT1", "DescriptionT1",
                "2022-08-27 | 10:00", 1, 30);
        taskManager.createTasks(task);
        epic = new Epic("epicT1", "descriptionE1", taskManager);
        taskManager.createTasks(epic);
        subtask = new Subtask("subtaskT1", "descriptionS1",
                "2022-08-26 | 10:00", 1, 30, 2);
        taskManager.createTasks(subtask);
    }

    @AfterEach
    void tearDown() {
        server.stop();
    }

    @Test
    public void loadFromHttpServer() throws IOException {
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();

        taskManager.getTaskByID(task.getId());
        taskManager.getEpicByID(epic.getId());
        taskManager.getSubtaskByID(subtask.getId());

        final List<Task> tasks = taskManager.getListOfTask();
        assertNotNull(tasks, "Возвращает не пустой список задач");
        assertEquals(1, tasks.size(), "Возвращает не пустой список задач");
        final List<Task> epics = taskManager.getListOfEpic();
        assertNotNull(epics, "Возвращает не пустой список эпиков");
        assertEquals(1, epics.size(), "Возвращает не пустой список эпиков");
        final List<Task> subtasks = taskManager.getListOfSubtask();
        assertNotNull(subtasks, "Возвращает не пустой список подзадач");
        assertEquals(1, subtasks.size(), "Возвращает не пустой список подзадач");
        final List<Task> history = taskManager.getHistoryManager().getHistory();
        assertNotNull(history, "Возвращает не пустой список истории");
        assertEquals(3, history.size(), "Возвращает не пустой список истории");

        httpTaskServer.stop();
    }

    //TODO POST for tasks, epic, subtasks
    @Test
    void getTasks() throws IOException, InterruptedException {
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8079/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<ArrayList<Task>>(){

        }.getType();
        List<Task> tasks = gson.fromJson(response.body(), taskType);
        assertNotNull(tasks, "Задачи не возвращаются");
        assertEquals(1, tasks.size(), "Неверное количество задач");
        assertEquals(task, tasks.get(0), "Задачи не совпадают");

        httpTaskServer.stop();
    }

    @Test
    void getTaskById() throws IOException, InterruptedException {
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8079/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<Task>(){

        }.getType();
        Task actualTask = gson.fromJson(response.body(), taskType);
        assertNotNull(actualTask, "Задача не возвращаются");
        assertEquals(actualTask, task, "Задачи не совпадают");

        httpTaskServer.stop();
    }

    @Test
    void deleteTasks() throws IOException, InterruptedException {
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8079/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        client = HttpClient.newHttpClient();
        uri = URI.create("http://localhost:8079/tasks/task");
        request = HttpRequest.newBuilder().uri(uri).GET().build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<ArrayList<Task>>(){

        }.getType();
        List<Task> tasks = gson.fromJson(response.body(), taskType);
        assertNotNull(tasks, "Задачи возвращаются");
        assertEquals(0, tasks.size(), "Неверное количество задач");

        httpTaskServer.stop();
    }

    @Test
    void deleteTaskById() throws IOException, InterruptedException {
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8079/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        client = HttpClient.newHttpClient();
        uri = URI.create("http://localhost:8079/tasks/task");
        request = HttpRequest.newBuilder().uri(uri).GET().build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<ArrayList<Task>>(){

        }.getType();
        List<Task> tasks = gson.fromJson(response.body(), taskType);
        assertNotNull(tasks, "Задачи возвращаются");
        assertEquals(0, tasks.size(), "Неверное количество задач");

        httpTaskServer.stop();
    }

    @Test
    void postTasks() throws IOException, InterruptedException {
        Task newTask = new Task("TaskT2", "descriptionT2",
                "2022-08-27 | 14:00", 1, 30);
        taskManager.createTasks(newTask);

        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8079/tasks/task");
        String taskJson = gson.toJson(newTask);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<Task>(){

        }.getType();
        Task actualTask = gson.fromJson(response.body(), taskType);
        assertNotNull(actualTask, "Задача не создана");
        assertEquals(newTask, actualTask, "Задачи не совпадают");

        httpTaskServer.stop();
    }

    @Test
    void getEpics() throws IOException, InterruptedException {
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8079/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<ArrayList<Epic>>(){

        }.getType();
        List<Epic> epics = gson.fromJson(response.body(), taskType);
        assertNotNull(epics, "Эпики не возвращаются");
        assertEquals(1, epics.size(), "Неверное количество эпиков");
        assertEquals(epic, epics.get(0), "Эпики не совпадают");

        httpTaskServer.stop();
    }

    @Test
    void getEpicById() throws IOException, InterruptedException {
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8079/tasks/epic/?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<Epic>(){

        }.getType();
        Epic actualEpic = gson.fromJson(response.body(), taskType);
        assertNotNull(actualEpic, "Эпик не возвращаются");
        assertEquals(actualEpic, epic, "Эпики не совпадают");

        httpTaskServer.stop();
    }

    @Test
    void deleteEpics() throws IOException, InterruptedException {
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8079/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        client = HttpClient.newHttpClient();
        uri = URI.create("http://localhost:8079/tasks/epic");
        request = HttpRequest.newBuilder().uri(uri).GET().build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<ArrayList<Epic>>(){

        }.getType();
        List<Epic> epics = gson.fromJson(response.body(), taskType);
        assertNotNull(epics, "задачи возвращаются");
        assertEquals(0, epics.size(), "Неверное количество задач");

        httpTaskServer.stop();
    }

    @Test
    void deleteEpicById() throws IOException, InterruptedException {
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8079/tasks/epic/?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        client = HttpClient.newHttpClient();
        uri = URI.create("http://localhost:8079/tasks/epic");
        request = HttpRequest.newBuilder().uri(uri).GET().build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<ArrayList<Epic>>(){

        }.getType();
        List<Epic> epics = gson.fromJson(response.body(), taskType);
        assertNotNull(epics, "задачи возвращаются");
        assertEquals(0, epics.size(), "Неверное количество задач");

        httpTaskServer.stop();
    }

    @Test
    void postEpics() throws IOException, InterruptedException {
        Epic newEpic = new Epic("EpicT2", "descriptionT2", taskManager);
        taskManager.createTasks(newEpic);

        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8079/tasks/epic");
        String taskJson = gson.toJson(newEpic);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<Epic>(){

        }.getType();
        Epic actualTask = gson.fromJson(response.body(), taskType);
        assertNotNull(actualTask, "Задача не создана");
        assertEquals(newEpic, actualTask, "Задачи не совпадают");

        httpTaskServer.stop();
    }

    @Test
    void getSubtasks() throws IOException, InterruptedException {
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8079/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<ArrayList<Subtask>>(){

        }.getType();
        List<Subtask> subtasks = gson.fromJson(response.body(), taskType);
        assertNotNull(subtasks, "Подзадачи не возвращаются");
        assertEquals(1, subtasks.size(), "Неверное количество подзадач");
        assertEquals(subtask, subtasks.get(0), "Подзадачи не совпадают");

        httpTaskServer.stop();
    }

    @Test
    void getSubtaskById() throws IOException, InterruptedException {
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8079/tasks/subtask/?id=3");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<Subtask>(){

        }.getType();
        Subtask actualSubtask = gson.fromJson(response.body(), taskType);
        assertNotNull(actualSubtask, "Подзадача не возвращаются");
        assertEquals(actualSubtask, subtask, "Подзадачи не совпадают");

        httpTaskServer.stop();
    }

    @Test
    void deleteSubtasks() throws IOException, InterruptedException {
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8079/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        client = HttpClient.newHttpClient();
        uri = URI.create("http://localhost:8079/tasks/subtask");
        request = HttpRequest.newBuilder().uri(uri).GET().build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<ArrayList<Subtask>>(){

        }.getType();
        List<Subtask> subtasks = gson.fromJson(response.body(), taskType);
        assertNotNull(subtasks, "Подзадачи возвращаются");
        assertEquals(0, subtasks.size(), "Неверное количество подзадач");

        httpTaskServer.stop();
    }

    @Test
    void deleteSubtaskById() throws IOException, InterruptedException {
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8079/tasks/subtask/?id=3");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        client = HttpClient.newHttpClient();
        uri = URI.create("http://localhost:8079/tasks/subtask");
        request = HttpRequest.newBuilder().uri(uri).GET().build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<ArrayList<Subtask>>(){

        }.getType();
        List<Subtask> subtasks = gson.fromJson(response.body(), taskType);
        assertNotNull(subtasks, "Подзадачи возвращаются");
        assertEquals(0, subtasks.size(), "Неверное количество подзадач");

        httpTaskServer.stop();
    }

    @Test
    void postSubtasks() throws IOException, InterruptedException {
        Subtask newSubtask = new Subtask("subtaskT1", "descriptionS1",
                "2022-08-29 | 10:00", 1, 30, 2);
        taskManager.createTasks(newSubtask);

        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8079/tasks/subtask");
        String taskJson = gson.toJson(newSubtask);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<Subtask>(){

        }.getType();
        Subtask actualTask = gson.fromJson(response.body(), taskType);
        assertNotNull(actualTask, "Задача не создана");
        assertEquals(newSubtask, actualTask, "Задачи не совпадают");

        httpTaskServer.stop();
    }

    @Test
    void getSubtaskEpic() throws IOException, InterruptedException {
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8079/tasks/subtask/epic/?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<ArrayList<Subtask>>(){

        }.getType();
        List<Subtask> subtasksEpic = gson.fromJson(response.body(), taskType);
        assertNotNull(subtasksEpic, "Подзадачи не возвращаются");
        assertEquals(1, subtasksEpic.size(), "Неверное количество подзадач");
        assertEquals(subtask, subtasksEpic.get(0), "Подзадачи не совпадают");

        httpTaskServer.stop();
    }

    @Test
    void getHistory() throws IOException, InterruptedException {
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();

        taskManager.getTaskByID(task.getId());
        taskManager.getEpicByID(epic.getId());
        taskManager.getSubtaskByID(subtask.getId());

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8079/tasks/history");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<ArrayList<Task>>(){

        }.getType();
        List<Task> history = gson.fromJson(response.body(), taskType);
        assertNotNull(history, "История не возвращаются");
        assertEquals(3, history.size(), "Неверное количество задач в истории");

        httpTaskServer.stop();
    }

    @Test
    void getPrioritizedListAllTasks() throws IOException, InterruptedException {
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8079/tasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<ArrayList<Task>>(){

        }.getType();
        List<Task> prioritizedList = gson.fromJson(response.body(), taskType);
        assertNotNull(prioritizedList, "Подзадачи не возвращаются");
        assertEquals(2, prioritizedList.size(), "Неверное количество подзадач");
        assertEquals(task, prioritizedList.get(1), "Задачи сортируются неправильно");

        httpTaskServer.stop();
    }
}