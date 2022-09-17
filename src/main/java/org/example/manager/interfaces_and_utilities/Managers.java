package org.example.manager.interfaces_and_utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.manager.adapter.LocalDateTimeAdapter;
import org.example.manager.http.HttpTaskManager;
import org.example.manager.managers_types.InMemoryHistoryManager;
import org.example.manager.managers_types.InMemoryTaskManager;
import org.example.server.KVServer;

import java.io.IOException;
import java.time.LocalDateTime;

public class Managers {

    public static InMemoryHistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static InMemoryTaskManager getDefaultTaskManager() {
        return new InMemoryTaskManager();
    }

    public static KVServer getDefaultKVServer() throws IOException {
        final KVServer kvServer = new KVServer();
        kvServer.start();
        return kvServer;
    }

    public static TaskManager getDefault() {
        return new HttpTaskManager("http://localhost:" + KVServer.PORT + "/");
    }

    public static Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .serializeNulls()
                .create();
    }
}