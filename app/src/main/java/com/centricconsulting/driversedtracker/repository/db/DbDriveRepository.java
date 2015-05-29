package com.centricconsulting.driversedtracker.repository.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.centricconsulting.driversedtracker.model.DayNight;
import com.centricconsulting.driversedtracker.model.Drive;
import com.centricconsulting.driversedtracker.repository.DriveRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by eric on 4/24/15.
 */
public class DbDriveRepository implements DriveRepository {
    private DbOpenHelper mDbOpenHelper;

    public DbDriveRepository(Context context) {
        mDbOpenHelper = new DbOpenHelper(context);
    }

    public List<Drive> findAll() {
        Cursor cursor = mDbOpenHelper.getReadableDatabase().query(
                DbContract.Drive.TABLE_NAME,
                DbContract.Drive.ALL_COLUMNS,
                null, // no where clause
                null, // no where values
                null, // no grouping
                null, // no having statement
                DbContract.Drive.COLUMN_NAME_START_TIME + " DESC");

        try {
            List<Drive> drives = new ArrayList<Drive>();
            while (cursor.moveToNext()) {
                drives.add(hydrateDrive(cursor));
            }
            return drives;
        } finally {
            cursor.close();
        }
    }

    public Drive findById(long id) {
        Cursor cursor = mDbOpenHelper.getReadableDatabase().query(
                DbContract.Drive.TABLE_NAME,
                DbContract.Drive.ALL_COLUMNS,
                DbContract.Drive._ID + " = ?",
                new String[] {String.valueOf(id)}, // yuck
                null, // no grouping
                null, // no having statement
                DbContract.Drive.COLUMN_NAME_START_TIME + " DESC");

        try {
            return hydrateDrive(cursor);
        } finally {
            cursor.close();
        }
    }

    public Drive save(Drive drive) {
        ContentValues values = new ContentValues();
        values.put(DbContract.Drive.COLUMN_NAME_START_TIME, drive.getStartTime().getTime());
        values.put(DbContract.Drive.COLUMN_NAME_ELAPSED_TIME, drive.getElapsedTimeInSeconds());
        values.put(DbContract.Drive.COLUMN_NAME_DAY_NIGHT, drive.getDayNight().toString());
        if (drive.getId() == 0) {
            long newRowId = mDbOpenHelper.getWritableDatabase().insert(
                    DbContract.Drive.TABLE_NAME, null, values);
            drive.setId(newRowId);
        } else {
            values.put(DbContract.Drive._ID, drive.getId());
            mDbOpenHelper.getWritableDatabase().update(
                    DbContract.Drive.TABLE_NAME,
                    values,
                    DbContract.Drive._ID + " = ?",
                    new String[] { String.valueOf(drive.getId()) });
        }
        return drive;
    }

    private Drive hydrateDrive(Cursor cursor) {
        Drive drive = new Drive();
        drive.setId(cursor.getLong(cursor.getColumnIndex(DbContract.Drive._ID)));
        drive.setStartTime(new Date(cursor.getLong(cursor.getColumnIndex(DbContract.Drive.COLUMN_NAME_START_TIME))));
        drive.setElapsedTimeInSeconds(cursor.getInt(cursor.getColumnIndex(DbContract.Drive.COLUMN_NAME_ELAPSED_TIME)));
        drive.setDayNight(DayNight.valueOf(cursor.getString(cursor.getColumnIndex(DbContract.Drive.COLUMN_NAME_DAY_NIGHT))));
        return drive;
    }
}
