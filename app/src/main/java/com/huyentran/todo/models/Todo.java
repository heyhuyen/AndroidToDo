package com.huyentran.todo.models;

/**
 * Todo model.
 */
public class Todo {
    public static final int PRIORITY_LOW = 0;
    public static final int PRIORITY_MED = 1;
    public static final int PRIORITY_HI = 2;

    private long id;
    private String value;
    private String dueDate; // ISO8601 "YYYY-MM-DD"
    private boolean status = false;
    private String notes;
    private int priority = PRIORITY_LOW;

    public Todo(String value) {
        this.value = value;
    }

    public Todo(long id, String value, String dueDate, boolean status, String notes, int priority) {
        this.id = id;
        this.value = value;
        this.dueDate = dueDate;
        this.status = status;
        this.notes = notes;
        this.priority = priority;
    }

    public static class TodoBuilder {
        private long id;
        private String value;
        private String dueDate;
        private boolean status = false;
        private String notes;
        private int priority = PRIORITY_LOW;

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

        public TodoBuilder notes(String notes) {
            this.notes = notes;
            return this;
        }

        public TodoBuilder priority(int priority) {
            this.priority = priority;
            return this;
        }

        public Todo build() {
            return new Todo(this.id, this.value, this.dueDate, this.status, this.notes, this.priority);
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

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setPriority(int priority) {
        this.priority = priority;
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

    public String getNotes() {
        return this.notes;
    }

    public int getPriority() {
        return this.priority;
    }
}
