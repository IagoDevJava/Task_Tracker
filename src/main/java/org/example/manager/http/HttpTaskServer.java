package org.example.manager.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.example.manager.interfaces_and_utilities.Managers;
import org.example.manager.interfaces_and_utilities.TaskManager;
import org.example.tasks.Epic;
import org.example.tasks.Subtask;
import org.example.tasks.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {
    public static final int PORT = 8079;
    private final HttpServer httpServer;
    private final Gson gson;
    private final TaskManager taskManager;

    public HttpTaskServer() throws IOException {
        this(Managers.getDefault());
    }

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        gson = Managers.getGson();
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress("localhost", PORT), 0);
        httpServer.createContext("/tasks", this::handler);
    }

    private void handler(HttpExchange httpExchange) {
        try {
            System.out.println("\n/tasks: " + httpExchange.getRequestURI());
            String path
                    = httpExchange.getRequestURI().getPath().substring(7);
            if (path.endsWith("/")) {
                path = path.substring(0, path.length() - 1);
            }

            switch (path) {
                case (""):
                    handlePrioritizedListAllTasks(httpExchange);
                    break;
                case ("history"):
                    handleHistory(httpExchange);
                    break;
                case ("task"):
                    handleTask(httpExchange);
                    break;
                case ("subtask"):
                    handleSubtask(httpExchange);
                    break;
                case ("subtask/epic"):
                    handleSubtaskEpic(httpExchange);
                    break;
                case ("epic"):
                    handleEpic(httpExchange);
                    break;
                default:
                    System.out.println("Неизвестный запрос: " + httpExchange.getRequestURI());
                    httpExchange.sendResponseHeaders(404, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Методы работы с задачей
     */
    private void handleTask(HttpExchange httpExchange) throws IOException {
        String query = httpExchange.getRequestURI().getQuery();
        String response;
        String idStr;
        long id;
        Task task;
        switch (httpExchange.getRequestMethod()) {
            case "GET":
                if (query == null) {
                    List<Task> listOfTask = taskManager.getListOfTask();
                    response = gson.toJson(listOfTask);
                    System.out.println("Получили все задачи");
                    sendText(httpExchange, response);
                    return;
                }
                idStr = query.substring(3);
                id = Long.parseLong(idStr);
                task = taskManager.getTaskByIdWithoutStory(id);
                response = gson.toJson(task);
                System.out.println("Получили задачу по id=" + id);
                sendText(httpExchange, response);
                break;
            case "DELETE":
                try {
                    if (query == null) {
                        taskManager.deleteListOfTask();
                        System.out.println("Удалили все задачи");
                        httpExchange.sendResponseHeaders(200, 0);
                        return;
                    }

                    idStr = query.substring(3);
                    id = Long.parseLong(idStr);
                    taskManager.deleteTaskForID(id);
                    System.out.println("Удалили задачу по id=" + id);
                    httpExchange.sendResponseHeaders(200, 0);
                    break;
                } finally {
                    httpExchange.close();
                }
            case "POST":
                try {
                    String json = readText(httpExchange);
                    if (json.isEmpty()) {
                        System.out.println("Body с задачей - пуст");
                        httpExchange.sendResponseHeaders(400, 0);
                        return;
                    }
                    task = gson.fromJson(json, Task.class);
                    id = task.getId();
                    if (taskManager.getListOfTask().contains(task)) {
                        taskManager.updateTask(task);
                        response = gson.toJson(task);
                        System.out.println("Обновили задачу по id=" + id);
                        sendText(httpExchange, response);
                    } else {
                        taskManager.createTasks(task);
                        System.out.println("Создали задачу");
                        response = gson.toJson(task);
                        sendText(httpExchange, response);
                    }
                } finally {
                    httpExchange.close();
                }
        }
    }

    /**
     * Методы работы с эпиком
     */
    private void handleEpic(HttpExchange httpExchange) throws IOException {
        String query = httpExchange.getRequestURI().getQuery();
        String response;
        String idStr;
        long id;
        Epic epic;
        switch (httpExchange.getRequestMethod()) {
            case "GET":
                if (query == null) {
                    List<Task> listOfTask = taskManager.getListOfEpic();
                    response = gson.toJson(listOfTask);
                    System.out.println("Получили все эпики");
                    sendText(httpExchange, response);
                    return;
                }
                idStr = query.substring(3);
                id = Long.parseLong(idStr);
                epic = taskManager.getEpicByIdWithoutStory(id);
                response = gson.toJson(epic);
                System.out.println("Получили эпик по id=" + id);
                sendText(httpExchange, response);
                break;
            case "DELETE":
                try {
                    if (query == null) {
                        taskManager.deleteListOfEpic();
                        System.out.println("Удалили все эпики");
                        httpExchange.sendResponseHeaders(200, 0);
                        return;
                    }
                    idStr = query.substring(3);
                    id = Long.parseLong(idStr);
                    taskManager.deleteEpicForID(id);
                    httpExchange.sendResponseHeaders(200, 0);
                    System.out.println("Удалили эпик по id=" + id);
                    break;
                } finally {
                    httpExchange.close();
                }
            case "POST":
                try {
                    String json = readText(httpExchange);
                    if (json.isEmpty()) {
                        System.out.println("Body с эпиком - пуст");
                        httpExchange.sendResponseHeaders(400, 0);
                        return;
                    }
                    epic = gson.fromJson(json, Epic.class);
                    id = epic.getId();
                    if (taskManager.getListOfEpic().contains(epic)) {
                        taskManager.updateEpic(epic);
                        response = gson.toJson(epic);
                        System.out.println("Обновили эпик по id=" + id);
                        sendText(httpExchange, response);
                    } else {
                        taskManager.createTasks(epic);
                        System.out.println("Создали эпик");
                        response = gson.toJson(epic);
                        sendText(httpExchange, response);
                    }
                } finally {
                    httpExchange.close();
                }
        }
    }

    /**
     * Методы работы с подзадачей
     */
    private void handleSubtask(HttpExchange httpExchange) throws IOException {
        String query = httpExchange.getRequestURI().getQuery();
        String response;
        String idStr;
        long id;
        Subtask subtask;
        switch (httpExchange.getRequestMethod()) {
            case "GET":
                if (query == null) {
                    List<Task> listOfSubtask = taskManager.getListOfSubtask();
                    response = gson.toJson(listOfSubtask);
                    System.out.println("Получили все подзадачи");
                    sendText(httpExchange, response);
                    return;
                }
                idStr = query.substring(3);
                id = Long.parseLong(idStr);
                subtask = taskManager.getSubtaskByIdWithoutStory(id);
                response = gson.toJson(subtask);
                System.out.println("Получили подзадачу по id=" + id);
                sendText(httpExchange, response);
                break;
            case "DELETE":
                try {
                    if (query == null) {
                        taskManager.deleteListOfSubtask();
                        System.out.println("Удалили все подзадачи");
                        httpExchange.sendResponseHeaders(200, 0);
                        return;
                    }
                    idStr = query.substring(3);
                    id = Long.parseLong(idStr);
                    taskManager.deleteSubtaskForID(id);
                    System.out.println("Удалили подзадачу по id=" + id);
                    httpExchange.sendResponseHeaders(200, 0);
                    break;
                } finally {
                    httpExchange.close();
                }
            case "POST":
                try {
                    String json = readText(httpExchange);
                    if (json.isEmpty()) {
                        System.out.println("Body с подзадачей - пуст");
                        httpExchange.sendResponseHeaders(400, 0);
                        return;
                    }
                    subtask = gson.fromJson(json, Subtask.class);
                    id = subtask.getId();
                    if (taskManager.getListOfSubtask().contains(subtask)) {
                        taskManager.updateSubtask(subtask);
                        response = gson.toJson(subtask);
                        System.out.println("Обновили подзадачу по id=" + id);
                        sendText(httpExchange, response);
                    } else {
                        taskManager.createTasks(subtask);
                        System.out.println("Создали подзадачу");
                        response = gson.toJson(subtask);
                        sendText(httpExchange, response);
                    }
                } finally {
                    httpExchange.close();
                }
        }
    }

    /**
     * Методы работы с подзадачей эпика
     */
    private void handleSubtaskEpic(HttpExchange httpExchange) throws IOException {
        if (!httpExchange.getRequestMethod().equals("GET")) {
            httpExchange.sendResponseHeaders(405, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(("/subtask/epic ждёт GET-запрос, а получил " + httpExchange.getRequestMethod()).getBytes());
            }
            return;
        }
        final String query = httpExchange.getRequestURI().getQuery();
        String idStr = query.substring(3);
        final long id = Long.parseLong(idStr);
        List<Subtask> subtasksEpic = taskManager.getListSubtasksOfEpic(id);
        final String response = gson.toJson(subtasksEpic);
        System.out.println("Получили список сабтаск эпика id=" + id);
        sendText(httpExchange, response);
    }

    /**
     * Методы работы с историей задач
     */
    private void handleHistory(HttpExchange httpExchange) throws IOException {
        if (!httpExchange.getRequestMethod().equals("GET")) {
            System.out.println();
            httpExchange.sendResponseHeaders(405, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(("/history ждёт GET-запрос, а получил " + httpExchange.getRequestMethod()).getBytes());
            }
            return;
        }
        final String response = gson.toJson(taskManager.getHistoryManager().getHistory());
        sendText(httpExchange, response);
    }

    private void handlePrioritizedListAllTasks(HttpExchange httpExchange) throws IOException {
        if (!httpExchange.getRequestMethod().equals("GET")) {
            httpExchange.sendResponseHeaders(405, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(("/ ждёт GET-запрос, а получил " + httpExchange.getRequestMethod()).getBytes());
            }
            return;
        }
        final String response = gson.toJson(taskManager.getPrioritizedTasks());
        sendText(httpExchange, response);
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/");
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
        System.out.println("Сервер остановлен на порту " + PORT);
    }

    protected String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }
}