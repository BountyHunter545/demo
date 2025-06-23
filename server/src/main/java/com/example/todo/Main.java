package com.example.todo;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Main {
    private static final TodoService service = new TodoService();

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/api/todos", new TodosHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Server started on port 8080");
    }

    static class TodosHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            if ("GET".equals(method) && "/api/todos".equals(path)) {
                handleList(exchange);
            } else if ("POST".equals(method) && "/api/todos".equals(path)) {
                handleAdd(exchange);
            } else if ("POST".equals(method) && path.startsWith("/api/todos/") && path.endsWith("/complete")) {
                handleComplete(exchange);
            } else {
                exchange.sendResponseHeaders(404, -1);
            }
        }

        private void handleList(HttpExchange exchange) throws IOException {
            List<TodoItem> items = service.getAll();
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (int i = 0; i < items.size(); i++) {
                if (i > 0) sb.append(",");
                sb.append(toJson(items.get(i)));
            }
            sb.append("]");
            byte[] bytes = sb.toString().getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }

        private void handleAdd(HttpExchange exchange) throws IOException {
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            String description = extractField(body, "description");
            if (description == null) {
                exchange.sendResponseHeaders(400, -1);
                return;
            }
            TodoItem item = service.add(description);
            byte[] bytes = toJson(item).getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }

        private void handleComplete(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            try {
                long id = Long.parseLong(path.split("/")[3]);
                TodoItem item = service.complete(id);
                if (item == null) {
                    exchange.sendResponseHeaders(404, -1);
                    return;
                }
                byte[] bytes = toJson(item).getBytes(StandardCharsets.UTF_8);
                exchange.getResponseHeaders().add("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, bytes.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(bytes);
                }
            } catch (NumberFormatException e) {
                exchange.sendResponseHeaders(400, -1);
            }
        }

        private String extractField(String json, String field) {
            // Very naive JSON parsing assuming {"field":"value"}
            int idx = json.indexOf(field);
            if (idx == -1) return null;
            int start = json.indexOf('"', idx + field.length() + 2);
            int end = json.indexOf('"', start + 1);
            if (start == -1 || end == -1) return null;
            return json.substring(start + 1, end);
        }

        private String toJson(TodoItem item) {
            StringBuilder sb = new StringBuilder();
            sb.append("{\"id\":").append(item.getId())
              .append(",\"description\":\"").append(item.getDescription()).append("\"");
            if (item.isCompleted()) {
                sb.append(",\"completedDate\":\"").append(item.getCompletedDate()).append("\"");
            }
            sb.append("}");
            return sb.toString();
        }
    }
}
