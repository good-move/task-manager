package ru.nsu.ccfit.boltava.taskmanager.model.database;

import android.provider.BaseColumns;

/**
 * Created by alexey on 29.10.17.
 */

public final class CrucialPointContract {

    public static final class CrucialPointEntry implements BaseColumns {
        static final String TABLE_NAME = "CrucialPoints";
        static final String COLUMN_TITLE_NAME = "title";
        static final String COLUMN_TIME_NAME = "time";
        static final String COLUMN_TYPE_ID_NAME = "type_id";
        static final String COLUMN_TASK_ID_NAME = "task_id";
    }

}
