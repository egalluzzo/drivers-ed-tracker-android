package com.centricconsulting.driversedtracker.model;

import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

/**
 * Created by eric on 6/29/15.
 */
public class LocalTime implements Comparable<LocalTime> {
    private int hours;
    private int minutes;

    public LocalTime(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
    }

    public LocalTime(String hhmm) {
        Scanner scanner = new Scanner(hhmm).useDelimiter(":");
        this.hours = scanner.nextInt();
        this.minutes = scanner.nextInt();
    }

    public LocalTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        this.hours = cal.get(Calendar.HOUR_OF_DAY);
        this.minutes = cal.get(Calendar.MINUTE);
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    @Override
    public String toString() {
        return String.format("%d:%02d", hours, minutes);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof LocalTime)) {
            return false;
        }

        LocalTime other = (LocalTime)o;
        return other.hours == this.hours && other.minutes == this.minutes;
    }

    @Override
    public int hashCode() {
        return hours * 60 + minutes;
    }

    @Override
    public int compareTo(LocalTime other) {
        int result = hours - other.hours;
        if (result == 0) {
            result = minutes - other.minutes;
        }
        return result;
    }

    /**
     * Returns a date with the given date part, and this object's local time.
     * @param other A date
     * @return The date with this object's time
     */
    public Date onDate(Date other) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(other);
        cal.set(Calendar.HOUR_OF_DAY, hours);
        cal.set(Calendar.MINUTE, minutes);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}
