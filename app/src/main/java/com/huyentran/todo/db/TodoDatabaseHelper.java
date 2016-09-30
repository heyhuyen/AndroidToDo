package com.huyentran.todo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.huyentran.todo.models.Todo;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class TodoDatabaseHelper extends SQLiteOpenHelper {
    private static TodoDatabaseHelper todoDatabaseHelper;

    // Database Info
    private static final String DATABASE_NAME = "todoDatabase";
    private static final int DATABASE_VERSION = 4;

    // Table Names
    private static final String TABLE_TODOS = "todos";

    // Post Table Columns
    private static final String KEY_TODO_ID = "id";
    private static final String KEY_TODO_VALUE = "value";
    private static final String KEY_TODO_DUE_DATE = "due_date";
    private static final String KEY_TODO_STATUS = "status";
    private static final String KEY_TODO_NOTES = "notes";
    private static final String KEY_TODO_PRIORITY = "priority";

    public static synchronized TodoDatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (todoDatabaseHelper == null) {
            todoDatabaseHelper = new TodoDatabaseHelper(context.getApplicationContext());
        }
        return todoDatabaseHelper;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private TodoDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_POSTS_TABLE = "CREATE TABLE " + TABLE_TODOS +
                "(" +
                KEY_TODO_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                KEY_TODO_VALUE + " TEXT," +
                KEY_TODO_DUE_DATE + " TEXT," +
                KEY_TODO_STATUS + " INTEGER DEFAULT 0," + // 1: complete
                KEY_TODO_NOTES + " TEXT," +
                KEY_TODO_PRIORITY + " INTEGER DEFAULT 0" + // 0: low
                ")";

        db.execSQL(CREATE_POSTS_TABLE);
    }

    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODOS);
            onCreate(db);
        }
    }

    /**
     * Retrieve all the todos from the database.
     * @return list of todos
     */
    public List<Todo> getAllTodos() {
        List<Todo> todos = new ArrayList<>();

        String TASKS_SELECT_QUERY = String.format("SELECT * FROM %s", TABLE_TODOS);

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(TASKS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    long id = cursor.getLong(cursor.getColumnIndex(KEY_TODO_ID));
                    String value = cursor.getString(cursor.getColumnIndex(KEY_TODO_VALUE));
                    String dueDate = cursor.getString(cursor.getColumnIndex(KEY_TODO_DUE_DATE));
                    boolean status = cursor.getInt(cursor.getColumnIndex(KEY_TODO_STATUS)) == 1;
                    String notes = cursor.getString(cursor.getColumnIndex(KEY_TODO_NOTES));
                    int priority = cursor.getInt(cursor.getColumnIndex(KEY_TODO_PRIORITY));
                    todos.add(new Todo(id, value, dueDate, status, notes, priority));
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get todos from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return todos;
    }

    /**
     * Persist given {@link Todo} to the database.
     * @param todo todo to persist
     */
    public long addTodo(Todo todo) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        long id = -1;
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_TODO_VALUE, todo.getValue());

            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            id = db.insertOrThrow(TABLE_TODOS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add todo to database");
        } finally {
            db.endTransaction();
        }
        return id;
    }

    /**
     * Update given {@link Todo}
     * @param todo todo to update
     */
    public int updateTodo(Todo todo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TODO_VALUE, todo.getValue());
        values.put(KEY_TODO_DUE_DATE, todo.getDueDate());
        values.put(KEY_TODO_STATUS, todo.getStatus());
        values.put(KEY_TODO_NOTES, todo.getNotes());
        values.put(KEY_TODO_PRIORITY, todo.getPriority());

        return db.update(TABLE_TODOS, values, KEY_TODO_ID + " = ?",
                new String[] { String.valueOf(todo.getId()) });
    }

    /**
     * Delete {@link Todo} for the given primary key id from the database.
     * @param id primary key
     */
    public void deleteTodo(long id) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_TODO_ID, id);
            db.delete(TABLE_TODOS, KEY_TODO_ID + " = ?", new String[] { String.valueOf(id) });
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete todo from database");
        } finally {
            db.endTransaction();
        }
    }
}