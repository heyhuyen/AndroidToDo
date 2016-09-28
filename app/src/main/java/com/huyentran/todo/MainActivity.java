package com.huyentran.todo;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.huyentran.todo.db.TodoDatabaseHelper;
import com.huyentran.todo.model.Todo;
import com.huyentran.todo.view.TodosAdapter;

import java.util.ArrayList;

/**
 * The main activity for the Todo app.
 */
public class MainActivity extends AppCompatActivity implements EditTodoDialogFragment.EditTodoDialogListener{
    public static final String TODO_POS_KEY = "todo_pos";
    public static final String TODO_ID_KEY = "todo_id";
    public static final String TODO_VALUE_KEY = "todo_value";

    private static final String EMPTY_STRING = "";

    private ArrayList<Todo> todos;
    private TodosAdapter todosAdapter;
    private ListView lvTodos;
    private TodoDatabaseHelper todoDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        todoDatabaseHelper = TodoDatabaseHelper.getInstance(this);
        lvTodos = (ListView) findViewById(R.id.lvTodos);
        initTodos();
        todosAdapter = new TodosAdapter(this, todos);
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
        long id = todoDatabaseHelper.addTodo(newTodo);
        newTodo.setId(id);
        todosAdapter.add(newTodo);
        etNewTodo.setText(EMPTY_STRING);
    }

    @Override
    public void onFinishEditDialog(int pos, long id, String value) {
        Todo updatedTodo = new Todo(id, value);
        todos.set(pos, updatedTodo);
        todosAdapter.notifyDataSetChanged();
        todoDatabaseHelper.updateTodo(updatedTodo);
    }

    /**
     * Launches a dialog for editing the todo item for the given position.
     */
    private void showEditDialog(int pos) {
        FragmentManager fm = getSupportFragmentManager();
        EditTodoDialogFragment editTodoDialogFragment =
                EditTodoDialogFragment.newInstance(pos, todos.get(pos));
        editTodoDialogFragment.show(fm, "fragment_edit_todo");
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
                        long todoId = todos.get(pos).getId();
                        todos.remove(pos);
                        todosAdapter.notifyDataSetChanged();
                        todoDatabaseHelper.deleteTodo(todoId);
                        return true;
                    }
                }
        );

        // short click listener for editing todos
        lvTodos.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View view, int pos, long id) {
                        showEditDialog(pos);
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
