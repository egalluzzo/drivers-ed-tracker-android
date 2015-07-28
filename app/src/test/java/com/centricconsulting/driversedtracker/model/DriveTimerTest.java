package com.centricconsulting.driversedtracker.model;

import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by eric on 7/27/15.
 */
public class DriveTimerTest {
    private Range<LocalTime> daylightHours;

    @Before
    public void setUp() {
        daylightHours = new Range<LocalTime>(new LocalTime(7, 0), new LocalTime(19, 0));
    }

    @Test
    public void testLifecycle() throws InterruptedException {
        DriveTimer driveTimer = new DriveTimer();
        assertFalse(driveTimer.isRunning());
        assertEquals(0, driveTimer.getElapsedTimeInSeconds());

        driveTimer.start();

        Thread.sleep(1100); // Not 1000 due to clock timer resolution
        assertTrue(driveTimer.isRunning());
        assertEquals(1, driveTimer.getElapsedTimeInSeconds());

        driveTimer.stop();

        Thread.sleep(1100);
        assertFalse(driveTimer.isRunning());
        assertEquals(1, driveTimer.getElapsedTimeInSeconds());
    }

    @Test
    public void testCreateDrives_DayOnly() {
        DriveTimer driveTimer = new DriveTimer();
        driveTimer.setStartTime(stringToDate("07/01/2015 12:00:00"));
        driveTimer.setEndTime(stringToDate("07/01/2015 14:00:00"));
        List<Drive> drives = driveTimer.createDrives(daylightHours);
        assertEquals(1, drives.size());
        assertDrive(drives.get(0), driveTimer.getStartTime(), 2 * 60 * 60, DayNight.DAY);
    }

    @Test
    public void testCreateDrives_ExactDaylightHours() {
        DriveTimer driveTimer = new DriveTimer();
        driveTimer.setStartTime(stringToDate("07/01/2015 07:00:00"));
        driveTimer.setEndTime(stringToDate("07/01/2015 19:00:00"));
        List<Drive> drives = driveTimer.createDrives(daylightHours);
        assertEquals(1, drives.size());
        assertDrive(drives.get(0), driveTimer.getStartTime(), 12 * 60 * 60, DayNight.DAY);
    }

    @Test
    public void testCreateDrives_DayToNight() {
        DriveTimer driveTimer = new DriveTimer();
        driveTimer.setStartTime(stringToDate("07/01/2015 18:00:00"));
        driveTimer.setEndTime(stringToDate("07/01/2015 20:00:00"));
        List<Drive> drives = driveTimer.createDrives(daylightHours);
        assertEquals(2, drives.size());
        assertDrive(drives.get(0), driveTimer.getStartTime(), 60 * 60, DayNight.DAY);
        assertDrive(drives.get(1), stringToDate("07/01/2015 19:00:00"), 60 * 60, DayNight.NIGHT);
    }

    @Test
    public void testCreateDrives_NightToDay() {
        DriveTimer driveTimer = new DriveTimer();
        driveTimer.setStartTime(stringToDate("07/01/2015 22:00:00"));
        driveTimer.setEndTime(stringToDate("07/02/2015 12:00:00"));
        List<Drive> drives = driveTimer.createDrives(daylightHours);
        assertEquals(2, drives.size());
        assertDrive(drives.get(0), driveTimer.getStartTime(), 9 * 60 * 60, DayNight.NIGHT);
        assertDrive(drives.get(1), stringToDate("07/02/2015 07:00:00"), 5 * 60 * 60, DayNight.DAY);
    }

    @Test
    public void testCreateDrives_NightOnly() {
        DriveTimer driveTimer = new DriveTimer();
        driveTimer.setStartTime(stringToDate("07/01/2015 23:00:00"));
        driveTimer.setEndTime(stringToDate("07/02/2015 01:00:00"));
        List<Drive> drives = driveTimer.createDrives(daylightHours);
        assertEquals(1, drives.size());
        assertDrive(drives.get(0), driveTimer.getStartTime(), 2 * 60 * 60, DayNight.NIGHT);
    }

    private void assertDrive(Drive drive, Date trackerStartTime, int elapsedTimeInSeconds, DayNight dayNight) {
        assertEquals(trackerStartTime, drive.getStartTime());
        assertEquals(elapsedTimeInSeconds, drive.getElapsedTimeInSeconds());
        assertEquals(dayNight, drive.getDayNight());
    }

    private Date stringToDate(String dateString) {
        try {
            return new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
