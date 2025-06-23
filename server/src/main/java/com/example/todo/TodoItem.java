package com.example.todo;

import java.time.LocalDate;

public class TodoItem {
    private Long id;
    private String description;
    private LocalDate completedDate;

    public TodoItem(Long id, String description) {
        this.id = id;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getCompletedDate() {
        return completedDate;
    }

    public void markCompleted(LocalDate date) {
        this.completedDate = date;
    }

    public boolean isCompleted() {
        return completedDate != null;
    }
}
