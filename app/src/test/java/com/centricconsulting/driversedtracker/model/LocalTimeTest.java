package com.centricconsulting.driversedtracker.model;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by eric on 7/27/15.
 */
public class LocalTimeTest {
    @Test
    public void testCreateFromString() {
        LocalTime time = new LocalTime("23:30");
        assertEquals(23, time.getHours());
        assertEquals(30, time.getMinutes());
    }

    @Test
    public void testCreateFromDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 21);
        cal.set(Calendar.MINUTE, 25);
        LocalTime time = new LocalTime(cal.getTime());
        assertEquals(21, time.getHours());
        assertEquals(25, time.getMinutes());
    }

    @Test
    public void testEqualsAndHashCode_WhenSameThenReturnsTrue() {
        LocalTime time1 = new LocalTime(12, 34);
        assertTrue(time1.equals(time1));
        assertEquals(time1.hashCode(), time1.hashCode());
    }

    @Test
    public void testEqualsAndHashCode_WhenNullThenReturnsFalse() {
        LocalTime time1 = new LocalTime(12, 34);
        assertFalse(time1.equals(null));
    }

    @Test
    public void testEqualsAndHashCode_WhenEqualThenReturnsTrue() {
        LocalTime time1 = new LocalTime(12, 34);
        LocalTime time2 = new LocalTime(12, 34);
        assertTrue(time1.equals(time2));
        assertEquals(time1.hashCode(), time2.hashCode());
    }

    @Test
    public void testEqualsAndHashCode_WhenHoursDifferentThenReturnsFalse() {
        LocalTime time1 = new LocalTime(12, 34);
        LocalTime time2 = new LocalTime(11, 34);
        assertFalse(time1.equals(time2));
        assertNotEquals(time1.hashCode(), time2.hashCode());
    }

    @Test
    public void testEqualsAndHashCode_WhenMinutesDifferentThenReturnsFalse() {
        LocalTime time1 = new LocalTime(12, 34);
        LocalTime time2 = new LocalTime(12, 35);
        assertFalse(time1.equals(time2));
        assertNotEquals(time1.hashCode(), time2.hashCode());
    }

    @Test
    public void testCompareTo_WhenSame_ThenReturnsZero() {
        LocalTime time1 = new LocalTime(12, 34);
        assertEquals(0, time1.compareTo(time1));
    }

    @Test
    public void testCompareTo_WhenEqual_ThenReturnsZero() {
        LocalTime time1 = new LocalTime(12, 34);
        LocalTime time2 = new LocalTime(12, 34);
        assertEquals(0, time1.compareTo(time2));
    }

    @Test
    public void testCompareTo_WhenHoursLess_ThenReturnsLessThanZero() {
        LocalTime time1 = new LocalTime(12, 34);
        LocalTime time2 = new LocalTime(13, 34);
        assertTrue(time1.compareTo(time2) < 0);
    }

    @Test
    public void testCompareTo_WhenHoursGreater_ThenReturnsGreaterThanZero() {
        LocalTime time1 = new LocalTime(12, 34);
        LocalTime time2 = new LocalTime(11, 34);
        assertTrue(time1.compareTo(time2) > 0);
    }

    @Test
    public void testCompareTo_WhenMinutesLess_ThenReturnsLessThanZero() {
        LocalTime time1 = new LocalTime(12, 34);
        LocalTime time2 = new LocalTime(12, 35);
        assertTrue(time1.compareTo(time2) < 0);
    }

    @Test
    public void testCompareTo_WhenMinutesGreater_ThenReturnsGreaterThanZero() {
        LocalTime time1 = new LocalTime(12, 34);
        LocalTime time2 = new LocalTime(12, 33);
        assertTrue(time1.compareTo(time2) > 0);
    }

    @Test
    public void testOnDate() throws ParseException {
        LocalTime time = new LocalTime(12, 34);
        Date date = stringToDate("01/01/2015 07:23:56.789");
        Date dateWithTime = time.onDate(date);
        assertEquals(stringToDate("01/01/2015 12:34:00.000"), dateWithTime);
    }

    private Date stringToDate(String dateString) throws ParseException {
        return new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS").parse(dateString);
    }
}
