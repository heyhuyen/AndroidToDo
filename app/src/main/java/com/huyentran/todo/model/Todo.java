package com.huyentran.todo.model;

/**
 * Todo model.
 */
public class Todo {
    private long id;
    private String value;

    public Todo(String value) {
        this.value = value;
    }

    public Todo(long id, String value) {
        this.id = id;
        this.value = value;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public long getId() {
        return this.id;
    }

    public String getValue() {
        return this.value;
    }
}
