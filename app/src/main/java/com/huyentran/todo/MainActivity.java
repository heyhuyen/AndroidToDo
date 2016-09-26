package com.huyentran.todo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final String TODO_POS_KEY = "todo_pos";
    public static final String TODO_ID_KEY = "todo_id";
    public static final String TODO_VALUE_KEY = "todo_value";

    private static final String EMPTY_STRING = "";
    private static final int REQUEST_CODE = 5;

    private ArrayList<Todo> todos;
    private ArrayAdapter<Todo> todosAdapter;
    private ListView lvTodos;
    private TodoDatabaseHelper todoDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        todoDatabaseHelper = TodoDatabaseHelper.getInstance(this);
        lvTodos = (ListView) findViewById(R.id.lvTodos);
        initTodos();
        todosAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_expandable_list_item_1, todos);
        lvTodos.setAdapter(todosAdapter);
        setupListViewListeners();
    }

    /**
     * Handler method for Add todo button.
     * @param view
     */
    public void onAddTodo(View view) {
        EditText etNewTodo = (EditText) findViewById(R.id.etNewTodo);
        String value = etNewTodo.getText().toString();
        // todo: check for blank values
        Todo newTodo = new Todo(value);
        todosAdapter.add(newTodo);
        etNewTodo.setText(EMPTY_STRING);
        todoDatabaseHelper.addTodo(newTodo);
    }

    /**
     * Launches the EditTodoActivity.
     */
    public void launchEditView(int pos) {
        Todo todo = todos.get(pos);
        Intent intent = new Intent(MainActivity.this, EditTodoActivity.class);
        intent.putExtra(TODO_POS_KEY, pos);
        intent.putExtra(TODO_ID_KEY, todo.id);
        intent.putExtra(TODO_VALUE_KEY, todo.value);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            int todoPos = data.getExtras().getInt(TODO_POS_KEY);
            long todoId = data.getExtras().getLong(TODO_ID_KEY);
            String newValue = data.getExtras().getString(TODO_VALUE_KEY);
            Todo updatedTodo = new Todo(todoId, newValue);
            todos.set(todoPos, updatedTodo);
            todosAdapter.notifyDataSetChanged();
            todoDatabaseHelper.updateTask(updatedTodo);
        }
    }

    /**
     * Adds listeners to the list view: item click and long item clicks for editing and removing.
     */
    private void setupListViewListeners() {
        // long click listener for removing todos
        lvTodos.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter, View view, int pos,
                                                   long id) {
                        long taskId = todos.get(pos).id;
                        todos.remove(pos);
                        todosAdapter.notifyDataSetChanged();
                        todoDatabaseHelper.deleteTask(taskId);
                        return true;
                    }
                }
        );

        // short click listener for editing todos
        lvTodos.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View view, int pos, long id) {
                        launchEditView(pos);
                    }
                }
        );
    }

    /**
     * Initialize todo list and load existing todos from database.
     */
    private void initTodos() {
        todos = new ArrayList<>();
        todos.addAll(todoDatabaseHelper.getAllTodos());
    }
}
