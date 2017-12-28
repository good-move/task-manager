package ru.nsu.ccfit.boltava.taskmanager.model.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by alexey on 29.10.17.
 */

public class TaskManagerDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "task_manager.db";
    private static final int DB_VERSION = 7;
    private static SQLiteDatabase.CursorFactory cursorFactory = null;

    public TaskManagerDbHelper(Context context) {
        super(context, DB_NAME, cursorFactory, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTaskGroupsTable(db);
        createTasksTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TaskContract.TaskEntry.TABLE_NAME + ";");
        db.execSQL("DROP TABLE " + TaskGroupContract.TaskGroupEntry.TABLE_NAME + ";");
        onCreate(db);
    }


    private void createTaskGroupsTable(SQLiteDatabase db) {
        final String query = "CREATE TABLE " +
                TaskGroupContract.TaskGroupEntry.TABLE_NAME + " (" +
                TaskGroupContract.TaskGroupEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TaskGroupContract.TaskGroupEntry.COLUMN_TITLE_NAME + " VARCHAR(100) UNIQUE NOT NULL, " +
                TaskGroupContract.TaskGroupEntry.COLUMN_PRIMARY_COLOR_NAME + " VARCHAR(100) NOT NULL " +
                ");";
        try {
            db.execSQL(query);
        } catch (SQLException e) {
            System.err.println("Failed to create " + TaskContract.TaskEntry.TABLE_NAME + " table");
            e.printStackTrace();
        }
    }

    private void createTasksTable(SQLiteDatabase db) {
        final String query = "CREATE TABLE " +
                TaskContract.TaskEntry.TABLE_NAME + " (" +
                TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TaskContract.TaskEntry.COLUMN_TITLE_NAME + " VARCHAR(100) NOT NULL, " +
                TaskContract.TaskEntry.COLUMN_DESCRIPTION_NAME + " TEXT NOT NULL, " +
                TaskContract.TaskEntry.COLUMN_PRIORITY_ID_NAME + " INTEGER NOT NULL, " +
                TaskContract.TaskEntry.COLUMN_TASK_GROUP_ID_NAME + " INTEGER NOT NULL " +
                ");";
        try {
            db.execSQL(query);
        } catch (SQLException e) {
            System.err.println("Failed to create " + TaskContract.TaskEntry.TABLE_NAME + " table");
            e.printStackTrace();
        }
    }
}
