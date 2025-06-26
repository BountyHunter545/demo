package com.example.todo.controller;

import com.example.todo.api.TodosApi;
import com.example.todo.api.model.TodoItem;
import com.example.todo.service.TodoService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TodoController implements TodosApi {

    @Autowired
    private TodoService service;

    @Override
    public ResponseEntity<List<TodoItem>> getTodos() {
        List<TodoItem> items = service.getAll().stream()
                .map(this::toApi)
                .collect(Collectors.toList());
        return ResponseEntity.ok(items);
    }

    @Override
    public ResponseEntity<TodoItem> addTodo(TodoItem item) {
        com.example.todo.domain.TodoItem domain = service.add(item.getDescription());
        return ResponseEntity.ok(toApi(domain));
    }

    @Override
    public ResponseEntity<TodoItem> complete(Long id) {
        com.example.todo.domain.TodoItem domain = service.complete(id);
        if (domain == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toApi(domain));
    }

    private TodoItem toApi(com.example.todo.domain.TodoItem item) {
        TodoItem api = new TodoItem();
        api.setId(item.getId());
        api.setDescription(item.getDescription());
        api.setCompletedDate(item.getCompletedDate());
        return api;
    }
}
