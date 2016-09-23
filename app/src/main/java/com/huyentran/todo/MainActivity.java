package com.huyentran.todo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final String POS_KEY = "pos";
    public static final String VALUE_KEY = "value";

    private static final String EMPTY_STRING = "";
    private static final String TODO_FILE = "todo.txt";
    private static final int REQUEST_CODE = 5;

    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView) findViewById(R.id.lvItems);
        items = new ArrayList<>();
        readItems();
        itemsAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_expandable_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListeners();
    }

    /**
     * Handler method for Add Item button.
     * @param view
     */
    public void onAddItem(View view) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        // todo: check for blank values
        itemsAdapter.add(itemText);
        etNewItem.setText(EMPTY_STRING);
        writeItems();
    }

    /**
     * Launches the EditItemActivity.
     */
    public void launchEditView(int pos) {
        String value = items.get(pos);
        Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
        intent.putExtra(POS_KEY, pos);
        intent.putExtra(VALUE_KEY, value);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            int pos = data.getExtras().getInt(POS_KEY);
            String newValue = data.getExtras().getString(VALUE_KEY);
            items.set(pos, newValue);
            itemsAdapter.notifyDataSetChanged();
            writeItems();
        }
    }

    /**
     * Adds listeners to the list view: item click and long item clicks for editing and removing.
     */
    private void setupListViewListeners() {
        // long click listener for removing items
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter, View view, int pos,
                                                   long id) {
                        items.remove(pos);
                        itemsAdapter.notifyDataSetChanged();
                        writeItems();
                        return true;
                    }
                }
        );

        // short click listener for editing items
        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View view, int pos, long id) {
                        launchEditView(pos);
                    }
                }
        );
    }

    /**
     * Reads a new line delimited list of items from a file.
     */
    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, TODO_FILE);
        try {
            items = new ArrayList<>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<>();
        }
    }

    /**
     * Writes a new line delimited list of items to a file.
     */
    private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, TODO_FILE);
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
