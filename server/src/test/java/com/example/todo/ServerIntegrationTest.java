package com.example.todo;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ServerIntegrationTest {
    private static Thread serverThread;

    @BeforeClass
    public static void startServer() throws Exception {
        serverThread = new Thread(() -> {
            try {
                Main.main(new String[]{});
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        serverThread.setDaemon(true);
        serverThread.start();
        // wait briefly for server to start
        Thread.sleep(500);
    }

    @AfterClass
    public static void stopServer() {
        serverThread.interrupt();
    }

    @Test
    public void endToEndFlow() throws Exception {
        // add item
        URL url = new URL("http://localhost:8080/api/todos");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.getOutputStream().write("{\"description\":\"task\"}".getBytes());
        assertEquals(200, conn.getResponseCode());
        String response = new BufferedReader(new InputStreamReader(conn.getInputStream())).readLine();
        assertTrue(response.contains("task"));
        conn.disconnect();

        // list items
        url = new URL("http://localhost:8080/api/todos");
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        assertEquals(200, conn.getResponseCode());
        response = new BufferedReader(new InputStreamReader(conn.getInputStream())).readLine();
        assertTrue(response.contains("task"));
        conn.disconnect();
    }
}
