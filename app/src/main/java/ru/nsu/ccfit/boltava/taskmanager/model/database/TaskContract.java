package ru.nsu.ccfit.boltava.taskmanager.model.database;

import android.provider.BaseColumns;

/**
 * Created by alexey on 29.10.17.
 */

public final class TaskContract {

    public static final class TaskEntry implements BaseColumns {
        public static final String TABLE_NAME = "Tasks";
        public static final String COLUMN_TITLE_NAME = "title";
        public static final String COLUMN_DESCRIPTION_NAME = "description";
        public static final String COLUMN_TASK_GROUP_ID_NAME = "task_group_id";
        public static final String COLUMN_PRIORITY_ID_NAME = "priority_id";
    }

}
