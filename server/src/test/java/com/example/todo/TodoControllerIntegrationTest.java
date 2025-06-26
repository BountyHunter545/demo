package com.example.todo;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class TodoControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate rest;

    @Test
    void endToEndFlow() {
        ResponseEntity<TodoItem[]> emptyList = rest.getForEntity("/api/todos", TodoItem[].class);
        assertThat(emptyList.getBody()).hasSize(0);

        TodoItem added = rest.postForObject("/api/todos", Map.of("description", "task"), TodoItem.class);
        assertThat(added.getId()).isNotNull();

        ResponseEntity<TodoItem[]> list = rest.getForEntity("/api/todos", TodoItem[].class);
        assertThat(list.getBody()).hasSize(1);

        ResponseEntity<TodoItem> completed = rest.postForEntity("/api/todos/" + added.getId() + "/complete", null, TodoItem.class);
        assertThat(completed.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(completed.getBody().getCompletedDate()).isNotNull();
    }
}
