package com.example.todo.service;

import com.example.todo.domain.TodoItem;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

@Service
public class TodoService {
    private final AtomicLong idCounter = new AtomicLong();
    private final List<TodoItem> items = new ArrayList<>();

    public List<TodoItem> getAll() {
        return Collections.unmodifiableList(items);
    }

    public TodoItem add(String description) {
        TodoItem item = new TodoItem(idCounter.incrementAndGet(), description);
        items.add(item);
        return item;
    }

    public TodoItem complete(long id) {
        for (TodoItem item : items) {
            if (item.getId() == id) {
                item.markCompleted(LocalDate.now());
                return item;
            }
        }
        return null;
    }
}
