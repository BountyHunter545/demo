package com.example.todo.api.model;

import java.time.LocalDate;

public class TodoItem {
    private Long id;
    private String description;
    private LocalDate completedDate;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getCompletedDate() { return completedDate; }
    public void setCompletedDate(LocalDate completedDate) { this.completedDate = completedDate; }
}
