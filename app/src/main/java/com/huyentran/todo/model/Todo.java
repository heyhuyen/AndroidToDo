package com.huyentran.todo.model;

/**
 * Todo model.
 */
public class Todo {
    private long id;
    private String value;
    private String dueDate; // ISO8601 "YYYY-MM-DD HH:MM:SS.SSS"
    private boolean status = false;

    public Todo(String value) {
        this.value = value;
    }

    public Todo(long id, String value, String dueDate, boolean status) {
        this.id = id;
        this.value = value;
        this.dueDate = dueDate;
        this.status = status;
    }

    public static class TodoBuilder {
        private long id;
        private String value;
        private String dueDate;
        private boolean status = false;

        public TodoBuilder(String value) {
            this.value = value;
        }

        public TodoBuilder id(long id) {
            this.id = id;
            return this;
        }

        public TodoBuilder value(String value) {
            this.value = value;
            return this;
        }

        public TodoBuilder dueDate(String dueDate) {
            this.dueDate = dueDate;
            return this;
        }

        public TodoBuilder status(boolean status) {
            this.status = status;
            return this;
        }

        public Todo build() {
            return new Todo(this.id, this.value, this.dueDate, this.status);
        }
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

    public void setStatus(boolean status) {
        this.status = status;
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

    public boolean getStatus() {
        return this.status;
    }
}
