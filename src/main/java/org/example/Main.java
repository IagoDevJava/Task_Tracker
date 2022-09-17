package org.example;

import org.example.manager.http.HttpTaskServer;
import org.example.server.KVServer;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        new KVServer().start();
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
//        httpTaskServer.stop();

    }
}
