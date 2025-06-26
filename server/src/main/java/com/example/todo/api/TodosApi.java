package com.example.todo.api;

import com.example.todo.api.model.TodoItem;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Generated interface for Todo API.
 */
public interface TodosApi {

    @RequestMapping(value = "/api/todos", method = RequestMethod.GET)
    ResponseEntity<List<TodoItem>> getTodos();

    @RequestMapping(value = "/api/todos", method = RequestMethod.POST)
    ResponseEntity<TodoItem> addTodo(@RequestBody TodoItem item);

    @RequestMapping(value = "/api/todos/{id}/complete", method = RequestMethod.POST)
    ResponseEntity<TodoItem> complete(@PathVariable("id") Long id);
}
