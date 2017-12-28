package ru.nsu.ccfit.boltava.taskmanager.model.database;

import android.provider.BaseColumns;

/**
 * Created by alexey on 29.10.17.
 */

public final class TaskGroupContract {

    public static final class TaskGroupEntry implements BaseColumns {
        public static final String TABLE_NAME = "TaskGroups";
        public static final String COLUMN_TITLE_NAME = "title";
        public static final String COLUMN_PRIMARY_COLOR_NAME = "primary_color";
    }

}
