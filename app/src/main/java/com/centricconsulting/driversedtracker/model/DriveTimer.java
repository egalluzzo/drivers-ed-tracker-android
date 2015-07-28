package com.centricconsulting.driversedtracker.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by larry.wildey on 4/24/2015.
 */
public class DriveTimer {
    private Date mStartTime;
    private Date mEndTime;

    Date getStartTime() {
        return mStartTime;
    }

    void setStartTime(Date startTime) {
        mStartTime = startTime;
    }

    Date getEndTime() {
        return mEndTime;
    }

    void setEndTime(Date endTime) {
        mEndTime = endTime;
    }

    public int getElapsedTimeInSeconds() {
        if (mStartTime == null) {
            return 0;
        } else {
            long endTimeInMillis;
            if (mEndTime == null) {
                endTimeInMillis = System.currentTimeMillis();
            } else {
                endTimeInMillis = mEndTime.getTime();
            }
            return (int) ((endTimeInMillis - mStartTime.getTime()) / 1000);
        }
    }

    public boolean isRunning() {
        return mStartTime != null && mEndTime == null;
    }

    public void start() {
        mStartTime = new Date();
        mEndTime = null;
    }

    public void clear() {
        mStartTime = null;
        mEndTime = null;
    }

    public void stop() {
        mEndTime = new Date();
    }

    public List<Drive> createDrives(Range<LocalTime> daylightHours) {
        Date start = getStartTime();
        Date end = getEndTime();
        if (getElapsedTimeInSeconds() >= 24 * 60 * 60) {
            // We don't support more than 24-hour drives.
            end = new Date(getStartTime().getTime() + 24 * 60 * 60 * 1000);
        }
        LocalTime startTime = new LocalTime(start);
        LocalTime endTime = new LocalTime(end);

        // TODO: This won't work for drives that start before sunrise and end after sunset.
        List<Drive> drives = new ArrayList<Drive>();
        if (daylightHours.contains(startTime)) {
            if (daylightHours.contains(endTime)) {
                addDriveUnlessEmpty(drives, start, end, DayNight.DAY);
            } else {
                Date sunset = daylightHours.getEnd().onDate(start);
                addDriveUnlessEmpty(drives, start, sunset, DayNight.DAY);
                addDriveUnlessEmpty(drives, sunset, end, DayNight.NIGHT);
            }
        } else {
            if (daylightHours.contains(endTime)) {
                Date sunrise = daylightHours.getStart().onDate(end);
                addDriveUnlessEmpty(drives, start, sunrise, DayNight.NIGHT);
                addDriveUnlessEmpty(drives, sunrise, end, DayNight.DAY);
            } else {
                addDriveUnlessEmpty(drives, start, end, DayNight.NIGHT);
            }
        }
        return drives;
    }

    private void addDriveUnlessEmpty(List<Drive> drives, Date start, Date end, DayNight dayNight) {
        int elapsedTimeInSeconds = (int) ((end.getTime() - start.getTime()) / 1000);
        if (elapsedTimeInSeconds > 0) {
            Drive drive = new Drive();
            drive.setStartTime(start);
            drive.setElapsedTimeInSeconds(elapsedTimeInSeconds);
            drive.setDayNight(dayNight);
            drives.add(drive);
        }
    }
}
