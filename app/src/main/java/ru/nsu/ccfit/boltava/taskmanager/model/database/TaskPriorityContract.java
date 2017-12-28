package ru.nsu.ccfit.boltava.taskmanager.model.database;

import android.provider.BaseColumns;

/**
 * Created by alexey on 29.10.17.
 */

public final class TaskPriorityContract {

    public static final class TaskPriorityEntry implements BaseColumns {
        static final String TABLE_NAME = "TaskPriorities";
        static final String COLUMN_TITLE_NAME = "title";
    }

}
