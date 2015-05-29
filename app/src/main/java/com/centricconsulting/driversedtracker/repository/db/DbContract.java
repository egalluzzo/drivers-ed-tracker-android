package com.centricconsulting.driversedtracker.repository.db;

import android.provider.BaseColumns;

/**
 * Created by eric on 4/24/15.
 */
public final class DbContract {
    private DbContract() {}

    public static final class Drive implements BaseColumns {
        private Drive() {}

        public static final String TABLE_NAME = "drive";
        public static final String COLUMN_NAME_START_TIME = "start_time";
        public static final String COLUMN_NAME_ELAPSED_TIME = "elapsed_time";
        public static final String COLUMN_NAME_DAY_NIGHT = "day_night";

        public static final String[] ALL_COLUMNS = {
                _ID,
                COLUMN_NAME_START_TIME,
                COLUMN_NAME_ELAPSED_TIME,
                COLUMN_NAME_DAY_NIGHT
        };

        public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY, "
                + COLUMN_NAME_START_TIME + " TEXT, "
                + COLUMN_NAME_ELAPSED_TIME + " INTEGER, "
                + COLUMN_NAME_DAY_NIGHT + " TEXT)";

        public static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
