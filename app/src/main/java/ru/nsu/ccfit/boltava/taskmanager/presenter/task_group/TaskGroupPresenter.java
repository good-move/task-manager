package ru.nsu.ccfit.boltava.taskmanager.presenter.task_group;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import ru.nsu.ccfit.boltava.taskmanager.model.Task;
import ru.nsu.ccfit.boltava.taskmanager.model.TaskGroup;
import ru.nsu.ccfit.boltava.taskmanager.model.database.TaskContract;
import ru.nsu.ccfit.boltava.taskmanager.model.database.TaskGroupContract;
import ru.nsu.ccfit.boltava.taskmanager.model.database.TaskManagerDbHelper;


public class TaskGroupPresenter {
    private TaskManagerDbHelper dbHelper;
    private TaskGroup taskGroup;
    private final int groupId;

    public TaskGroupPresenter(Context context, int groupId) {
        dbHelper = new TaskManagerDbHelper(context);
        this.groupId = groupId;
    }

    public TaskGroup getTaskGroup() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        TaskGroup taskGroup = getGroupInfo(db);
        if (taskGroup == null) {
            throw new RuntimeException();
        }
        this.taskGroup = taskGroup;

        fillTaskGroup(this.taskGroup, db);

        return this.taskGroup;
    }

    private void fillTaskGroup(TaskGroup taskGroup, SQLiteDatabase db) {
        final String selection = TaskContract.TaskEntry.COLUMN_TASK_GROUP_ID_NAME + "=?";
        final String[] selectionArgs = new String[]{ String.valueOf(groupId) };

        Cursor cursor = db.query(
            TaskContract.TaskEntry.TABLE_NAME,
            null,
            selection,
            selectionArgs,
            null,
            null,
            null
        );

        final int idColIndex = cursor.getColumnIndex(TaskContract.TaskEntry._ID);
        final int titleColIndex = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TITLE_NAME);
        final int descriptionColIndex = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_DESCRIPTION_NAME);
        final int priorityIdColIndex = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_PRIORITY_ID_NAME);

        for (int i = 0; i < cursor.getCount(); ++i) {
            cursor.moveToPosition(i);
            int taskId = cursor.getInt(idColIndex);
            String title = cursor.getString(titleColIndex);
            String desc = cursor.getString(descriptionColIndex);
            int priorityId = cursor.getInt(priorityIdColIndex);

            Task.TaskPriority priority = Task.TaskPriority.values()[priorityId];
            Task task = Task.getTaskBuilder(title, groupId)
                    .setId(taskId)
                    .setPriority(priority)
                    .setDescription(desc)
                    .createTask();
            taskGroup.addTask(task);
        }

        cursor.close();
    }

    private TaskGroup getGroupInfo(SQLiteDatabase db) {
        final String selection = TaskGroupContract.TaskGroupEntry._ID + "=?";
        final String[] selectionArgs = new String[]{String.valueOf(groupId)};
        Cursor cursor = db.query(
                TaskGroupContract.TaskGroupEntry.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.getCount() == 0) {
            return null;
        }

        cursor.moveToPosition(0);
        final int idColumnIndex =
                cursor.getColumnIndex(TaskGroupContract.TaskGroupEntry._ID);
        final int titleColumnIndex =
                cursor.getColumnIndex(TaskGroupContract.TaskGroupEntry.COLUMN_TITLE_NAME);
        final int colorColumnIndex =
                cursor.getColumnIndex(TaskGroupContract.TaskGroupEntry.COLUMN_PRIMARY_COLOR_NAME);

        int id = cursor.getInt(idColumnIndex);
        String title = cursor.getString(titleColumnIndex);
        String color = cursor.getString(colorColumnIndex);
//        String sizeString = cursor.getString(cursor.getColumnIndex(TaskGroupContract.TaskGroupEntry.COLUMN_SIZE_NAME));
        cursor.close();

        return new TaskGroup(id, title, color);
    }

    public TaskGroupAdapter getGroupAdapter(TaskGroup group) {
        return new TaskGroupAdapter(group);
    }

    public boolean addTask(Task task) {
        final int INSERTION_ERROR_CODE = -1;

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.COLUMN_TITLE_NAME, task.getTitle());
        values.put(TaskContract.TaskEntry.COLUMN_PRIORITY_ID_NAME, task.getPriority().toInt());
        values.put(TaskContract.TaskEntry.COLUMN_DESCRIPTION_NAME, task.getDescription());
        values.put(TaskContract.TaskEntry.COLUMN_TASK_GROUP_ID_NAME, task.getTaskGroupId());

        return db.insert(TaskContract.TaskEntry.TABLE_NAME, null, values) != INSERTION_ERROR_CODE;
    }

    public boolean updateTask(Task task) {
        final int UPDATE_ERROR_CODE = 0;

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        final String selection = TaskContract.TaskEntry._ID + "=?";
        final String[] selectionArgs = new String[] { String.valueOf(task.getId()) };

        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.COLUMN_TITLE_NAME, task.getTitle());
        values.put(TaskContract.TaskEntry.COLUMN_PRIORITY_ID_NAME, task.getPriority().toInt());
        values.put(TaskContract.TaskEntry.COLUMN_DESCRIPTION_NAME, task.getDescription());

        return db.update(TaskContract.TaskEntry.TABLE_NAME, values, selection, selectionArgs) != UPDATE_ERROR_CODE;
    }

    public boolean deleteTask(Task task) {
        final int DELETE_ERROR_CODE = 0;

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        final String selection = TaskContract.TaskEntry._ID + "=?";
        final String[] selectionArgs = new String[] { String.valueOf(task.getId()) };

        return db.delete(TaskContract.TaskEntry.TABLE_NAME, selection, selectionArgs) != DELETE_ERROR_CODE;
    }

}
