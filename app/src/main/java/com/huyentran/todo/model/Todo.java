package com.huyentran.todo.model;

/**
 * Todo model.
 */
public class Todo {
    private long id;
    private String value;
    private String dueDate; // ISO8601 "YYYY-MM-DD HH:MM:SS.SSS"

    public Todo(String value) {
        this.value = value;
    }

    public Todo(long id, String value) {
        this.id = id;
        this.value = value;
    }

    public Todo(long id, String value, String dueDate) {
        this.id = id;
        this.value = value;
        this.dueDate = dueDate;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public long getId() {
        return this.id;
    }

    public String getValue() {
        return this.value;
    }

    public String getDueDate() {
        return this.dueDate;
    }
}
