package com.huyentran.todo;

/**
 * Todo model
 */
public class Todo {
    public long id;
    public String value;

    public Todo(String value) {
        this.value = value;
    }

    public Todo(long id, String value) {
        this.id = id;
        this.value = value;
    }

    // need to override this in order for the ArrayAdapter to know what to display
    @Override
    public String toString() {
        return this.value;
    }
}

