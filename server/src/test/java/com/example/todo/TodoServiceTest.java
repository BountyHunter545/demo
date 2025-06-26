package com.example.todo;

import static org.junit.Assert.*;

import com.example.todo.domain.TodoItem;
import com.example.todo.service.TodoService;

import org.junit.Test;

public class TodoServiceTest {
    @Test
    public void addAndCompleteFlow() {
        TodoService service = new TodoService();
        TodoItem item = service.add("test");
        assertNotNull(item);
        assertFalse(item.isCompleted());
        assertEquals(1, service.getAll().size());
        TodoItem completed = service.complete(item.getId());
        assertTrue(completed.isCompleted());
        assertEquals(item.getId(), completed.getId());
    }
}
