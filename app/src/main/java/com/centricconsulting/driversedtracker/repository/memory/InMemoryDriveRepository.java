package com.centricconsulting.driversedtracker.repository.memory;

import android.support.v4.util.LongSparseArray;
import android.util.SparseArray;

import com.centricconsulting.driversedtracker.model.Drive;
import com.centricconsulting.driversedtracker.repository.DriveRepository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * A repository of {@link Drive}s that is not persisted to disk at all.  When the app dies, so does
 * this repository.
 *
 * Created by eric on 4/24/15.
 */
public class InMemoryDriveRepository implements DriveRepository {
    private static InMemoryDriveRepository sInstance;
    private static DateFormat sDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");

    private Set<Drive> mDrives = new TreeSet<Drive>(new Comparator<Drive>() {
        @Override
        public int compare(Drive lhs, Drive rhs) {
            return (int)(rhs.getStartTime().getTime() - lhs.getStartTime().getTime());
        }
    });

    private LongSparseArray<Drive> mIdToDriveMap = new LongSparseArray<Drive>();
    private long mNextId = 1;

    public static DriveRepository getInstance() {
        if (sInstance == null) {
            sInstance = new InMemoryDriveRepository();
            sInstance.save(createDrive("04/23/2015 08:15", 3700));
            sInstance.save(createDrive("04/23/2015 17:30", 3500));
            sInstance.save(createDrive("04/24/2015 08:00", 3650));
            sInstance.save(createDrive("04/24/2015 17:20", 3470));
        }
        return sInstance;
    }

    private static Drive createDrive(String startTimeMMDDYYYYHHMM, int elapsedTimeInSeconds) {
        Drive drive = new Drive();
        try {
            drive.setStartTime(sDateFormat.parse(startTimeMMDDYYYYHHMM));
        } catch (ParseException pe) {
            throw new RuntimeException(pe);
        }
        drive.setElapsedTimeInSeconds(elapsedTimeInSeconds);
        return drive;
    }

    @Override
    public Drive findById(long id) {
        return mIdToDriveMap.get(id);
    }

    @Override
    public List<Drive> findAll() {
        return new ArrayList<Drive>(mDrives);
    }

    @Override
    public Drive save(Drive drive) {
        if (drive.getId() == 0) {
            drive.setId(mNextId++);
        }
        mDrives.add(drive);
        mIdToDriveMap.put(drive.getId(), drive);
        return drive;
    }
}
