package com.huyentran.todo.model;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * Unit tests for @{link Todo}.
 */
public class TestTodo extends TestCase {

    @Test
    public void testConstruction() {
        long id = 1L;
        String value = "This is a todo";
        Todo todo = new Todo(id, value);
        assertEquals(id, todo.getId());
        assertEquals(value, todo.getValue());
    }
}
