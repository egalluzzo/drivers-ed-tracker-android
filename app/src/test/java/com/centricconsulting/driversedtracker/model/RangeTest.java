package com.centricconsulting.driversedtracker.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by eric on 7/27/15.
 */
public class RangeTest {
    @Test
    public void testEqualsAndHashCode_WhenSameThenReturnsTrue() {
        Range<LocalTime> range1 = new Range<LocalTime>(new LocalTime(11, 0), new LocalTime(14, 0));
        assertTrue(range1.equals(range1));
        assertEquals(range1.hashCode(), range1.hashCode());
    }

    @Test
    public void testEqualsAndHashCode_WhenNullThenReturnsFalse() {
        Range<LocalTime> range1 = new Range<LocalTime>(new LocalTime(11, 0), new LocalTime(14, 0));
        assertFalse(range1.equals(null));
    }

    @Test
    public void testEqualsAndHashCode_WhenEqualThenReturnsTrue() {
        Range range1 = new Range<LocalTime>(new LocalTime(11, 0), new LocalTime(14, 0));
        Range range2 = new Range<LocalTime>(new LocalTime(11, 0), new LocalTime(14, 0));
        assertTrue(range1.equals(range2));
        assertEquals(range1.hashCode(), range2.hashCode());
    }

    @Test
    public void testEqualsAndHashCode_WhenStartTimeDifferentThenReturnsFalse() {
        Range<LocalTime> range1 = new Range<LocalTime>(new LocalTime(11, 0), new LocalTime(14, 0));
        Range<LocalTime> range2 = new Range<LocalTime>(new LocalTime(11, 1), new LocalTime(14, 0));
        assertFalse(range1.equals(range2));
        assertNotEquals(range1.hashCode(), range2.hashCode());
    }

    @Test
    public void testEqualsAndHashCode_WhenEndTimeDifferentThenReturnsFalse() {
        Range<LocalTime> range1 = new Range<LocalTime>(new LocalTime(11, 0), new LocalTime(14, 0));
        Range<LocalTime> range2 = new Range<LocalTime>(new LocalTime(11, 0), new LocalTime(14, 4));
        assertFalse(range1.equals(range2));
        assertNotEquals(range1.hashCode(), range2.hashCode());
    }

    @Test
    public void testContains_WhenLessThanStartAndStartLessThanEnd_ReturnsFalse() {
        Range<LocalTime> range = new Range<LocalTime>(new LocalTime(11, 0), new LocalTime(14, 0));
        assertFalse(range.contains(new LocalTime("10:59")));
    }

    @Test
    public void testContains_WhenEqualToStartAndStartLessThanEnd_ReturnsTrue() {
        Range<LocalTime> range = new Range<LocalTime>(new LocalTime(11, 0), new LocalTime(14, 0));
        assertTrue(range.contains(new LocalTime("11:00")));
    }

    @Test
    public void testContains_WhenInRangeAndStartLessThanEnd_ReturnsTrue() {
        Range<LocalTime> range = new Range<LocalTime>(new LocalTime(11, 0), new LocalTime(14, 0));
        assertTrue(range.contains(new LocalTime("13:00")));
    }

    @Test
    public void testContains_WhenAtEndAndStartLessThanEnd_ReturnsFalse() {
        Range<LocalTime> range = new Range<LocalTime>(new LocalTime(11, 0), new LocalTime(14, 0));
        assertFalse(range.contains(new LocalTime("14:00")));
    }

    @Test
    public void testContains_WhenLessThanStartAndStartGreaterThanEnd_ReturnsTrue() {
        Range<LocalTime> range = new Range<LocalTime>(new LocalTime(14, 0), new LocalTime(11, 0));
        assertTrue(range.contains(new LocalTime("10:59")));
    }

    @Test
    public void testContains_WhenEqualToStartAndStartGreaterThanEnd_ReturnsFalse() {
        Range<LocalTime> range = new Range<LocalTime>(new LocalTime(14, 0), new LocalTime(11, 0));
        assertFalse(range.contains(new LocalTime("11:00")));
    }

    @Test
    public void testContains_WhenBetweenEndAndStartAndStartGreaterThanEnd_ReturnsFalse() {
        Range<LocalTime> range = new Range<LocalTime>(new LocalTime(14, 0), new LocalTime(11, 0));
        assertFalse(range.contains(new LocalTime("12:00")));
    }

    @Test
    public void testContains_WhenAtEndAndStartGreaterThanEnd_ReturnsFalse() {
        Range<LocalTime> range = new Range<LocalTime>(new LocalTime(14, 0), new LocalTime(11, 0));
        assertFalse(range.contains(new LocalTime("11:00")));
    }

    @Test
    public void testContains_WhenAtStartAndStartGreaterThanEnd_ReturnsTrue() {
        Range<LocalTime> range = new Range<LocalTime>(new LocalTime(14, 0), new LocalTime(11, 0));
        assertTrue(range.contains(new LocalTime("14:00")));
    }

    @Test
    public void testContains_WhenAfterStartAndStartGreaterThanEnd_ReturnsTrue() {
        Range<LocalTime> range = new Range<LocalTime>(new LocalTime(14, 0), new LocalTime(11, 0));
        assertTrue(range.contains(new LocalTime("19:00")));
    }
}
