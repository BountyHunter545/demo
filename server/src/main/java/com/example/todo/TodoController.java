package com.example.todo;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/todos")
@CrossOrigin
public class TodoController {
    private final TodoService service;

    public TodoController(TodoService service) {
        this.service = service;
    }

    @GetMapping
    public Iterable<TodoItem> list() {
        return service.getAll();
    }

    @PostMapping
    public TodoItem add(@RequestBody Map<String, String> body) {
        return service.add(body.get("description"));
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<TodoItem> complete(@PathVariable long id) {
        TodoItem item = service.complete(id);
        if (item == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(item);
    }
}
