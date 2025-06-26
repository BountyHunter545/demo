package com.example.todo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class TodoService {
    private final TodoRepository repository;

    public TodoService(TodoRepository repository) {
        this.repository = repository;
    }

    public List<TodoItem> getAll() {
        return repository.findAll();
    }

    public TodoItem add(String description) {
        TodoItem item = new TodoItem(description);
        return repository.save(item);
    }

    public TodoItem complete(long id) {
        Optional<TodoItem> opt = repository.findById(id);
        if (opt.isEmpty()) {
            return null;
        }
        TodoItem item = opt.get();
        item.markCompleted(LocalDate.now());
        return repository.save(item);
    }
}
