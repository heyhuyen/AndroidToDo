package com.huyentran.todo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import static com.huyentran.todo.MainActivity.POS_KEY;
import static com.huyentran.todo.MainActivity.VALUE_KEY;
import static com.huyentran.todo.R.id.etEditItem;

public class EditItemActivity extends AppCompatActivity {
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        pos = getIntent().getExtras().getInt("pos");
        String oldValue = getIntent().getStringExtra("value");
        EditText etEditItem = (EditText) findViewById(R.id.etEditItem);
        etEditItem.setText(oldValue);
    }

    /**
     * Closes the activity.
     */
    public void onSubmit(View view) {
        EditText editedText = (EditText) findViewById(etEditItem);
        String newValue = editedText.getText().toString();
        // Todo: check against blank values
        Intent data = new Intent();
        data.putExtra(POS_KEY, pos);
        data.putExtra(VALUE_KEY, newValue);
        setResult(RESULT_OK, data);
        this.finish();
    }
}
