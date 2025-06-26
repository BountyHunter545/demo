package com.example.todo;

import static org.junit.Assert.*;

import com.example.todo.api.model.TodoItem;
import java.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate rest;

    @Test
    public void endToEndFlow() {
        TodoItem req = new TodoItem();
        req.setDescription("task");
        ResponseEntity<TodoItem> addResp = rest.postForEntity("http://localhost:" + port + "/api/todos", req, TodoItem.class);
        assertEquals(200, addResp.getStatusCodeValue());
        TodoItem created = addResp.getBody();
        assertNotNull(created.getId());

        ResponseEntity<TodoItem[]> listResp = rest.getForEntity("http://localhost:" + port + "/api/todos", TodoItem[].class);
        assertEquals(200, listResp.getStatusCodeValue());
        assertTrue(Arrays.stream(listResp.getBody()).anyMatch(t -> t.getId().equals(created.getId())));
    }
}
