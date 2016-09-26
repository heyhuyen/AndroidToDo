package com.huyentran.todo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import static com.huyentran.todo.MainActivity.TODO_ID_KEY;
import static com.huyentran.todo.MainActivity.TODO_POS_KEY;
import static com.huyentran.todo.MainActivity.TODO_VALUE_KEY;

public class EditTodoActivity extends AppCompatActivity {
    private int pos;
    private long todo_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_todo);
        pos = getIntent().getExtras().getInt(TODO_POS_KEY);
        todo_id = getIntent().getExtras().getLong(TODO_ID_KEY);
        String oldValue = getIntent().getStringExtra(TODO_VALUE_KEY);
        EditText etEditTodo = (EditText) findViewById(R.id.etEditTodo);
        etEditTodo.setText(oldValue);
        etEditTodo.setSelection(oldValue.length());
    }

    /**
     * Closes the activity.
     */
    public void onSaveTodo(View view) {
        EditText editedText = (EditText) findViewById(R.id.etEditTodo);
        String newValue = editedText.getText().toString();
        // Todo: check against blank values
        Intent data = new Intent();
        data.putExtra(TODO_POS_KEY, pos);
        data.putExtra(TODO_ID_KEY, todo_id);
        data.putExtra(TODO_VALUE_KEY, newValue);
        setResult(RESULT_OK, data);
        this.finish();
    }
}
