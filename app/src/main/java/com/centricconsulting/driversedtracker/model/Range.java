package com.centricconsulting.driversedtracker.model;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by eric on 6/29/15.
 */
public class Range<T extends Comparable<? super T>> {
    private T start;
    private T end;

    public Range(T start, T end) {
        this.start = start;
        this.end = end;
    }

    public T getStart() {
        return start;
    }

    public T getEnd() {
        return end;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Range)) {
            return false;
        }

        Range other = (Range)o;
        return ((start == null) ? (other.start == null) : start.equals(other.start))
                && ((end == null) ? (other.end == null) : end.equals(other.end));
    }

    @Override
    public int hashCode() {
        int result = 31;
        result = 17 * result + (start == null ? 0 : start.hashCode());
        result = 17 * result + (end == null ? 0 : end.hashCode());
        return result;
    }

    public boolean contains(T t) {
        int comparison = start.compareTo(end);
        if (comparison == 0) {
            return false;
        } else if (comparison < 0) {
            return (start.compareTo(t) <= 0 && t.compareTo(end) < 0);
        } else {
            return (end.compareTo(t) > 0 || t.compareTo(start) >= 0);
        }
    }
}
