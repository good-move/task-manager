package ru.nsu.ccfit.boltava.taskmanager.presenter.task_group_list;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import ru.nsu.ccfit.boltava.taskmanager.model.TaskGroup;
import ru.nsu.ccfit.boltava.taskmanager.model.database.TaskGroupContract;
import ru.nsu.ccfit.boltava.taskmanager.model.database.TaskManagerDbHelper;

/**
 * Created by alexey on 20.10.17.
 */

public class TaskGroupListPresenter {

    private ArrayList<TaskGroup> taskGroups = new ArrayList<>();
    private TaskManagerDbHelper dbHelper;

    public TaskGroupListPresenter(Context context) {
        dbHelper = new TaskManagerDbHelper(context);

        createMockTaskGroups();
    }

    private void createMockTaskGroups() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        final String[][] mocks = new String[][] {
            { "Notifications", "red" },
            { "Food", "red" },
            { "OOAD", "blue" },
            { "Books", "pink" }
        };

        for (String[] mock : mocks) {
            ContentValues values = new ContentValues();
            values.put(TaskGroupContract.TaskGroupEntry.COLUMN_TITLE_NAME, mock[0]);
            values.put(TaskGroupContract.TaskGroupEntry.COLUMN_PRIMARY_COLOR_NAME, mock[1]);
            db.insert(TaskGroupContract.TaskGroupEntry.TABLE_NAME, null, values);
        }
    }

    public TaskGroup[] getTaskGroups() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // fetch all groups
        Cursor cursor = db.query(
                TaskGroupContract.TaskGroupEntry.TABLE_NAME,
                null,null,null,
                null,null,null
        );

        TaskGroup[] groups = new TaskGroup[cursor.getCount()];

        final int idColumnIndex =
                cursor.getColumnIndex(TaskGroupContract.TaskGroupEntry._ID);
        final int titleColumnIndex =
                cursor.getColumnIndex(TaskGroupContract.TaskGroupEntry.COLUMN_TITLE_NAME);
        final int colorColumnIndex =
                cursor.getColumnIndex(TaskGroupContract.TaskGroupEntry.COLUMN_PRIMARY_COLOR_NAME);

        for (int i = 0; i < cursor.getCount(); ++i) {
            cursor.moveToPosition(i);
            int id = cursor.getInt(idColumnIndex);
            String title = cursor.getString(titleColumnIndex);
            String color = cursor.getString(colorColumnIndex);
            groups[i] = new TaskGroup(id, title, color);
        }
        cursor.close();

        return groups;

    }

}
