package com.centricconsulting.driversedtracker.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A drive that the user has taken.
 * Created by eric on 4/23/15.
 */
public class Drive {
    private long mId;
    private Date mStartTime;
    private int mElapsedTimeInSeconds;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public Date getStartTime() {
        return mStartTime;
    }

    public void setStartTime(Date startTime) {
        mStartTime = startTime;
    }

    public int getElapsedTimeInSeconds() {
        return mElapsedTimeInSeconds;
    }

    public void setElapsedTimeInSeconds(int elapsedTimeInSeconds) {
        mElapsedTimeInSeconds = elapsedTimeInSeconds;
    }

    public String toString() {
        return new SimpleDateFormat("M/dd/yyyy h:mm a").format(mStartTime) + " - " + getFormattedElapsedTime();
    }

    public String getFormattedElapsedTime() {
        int hours = mElapsedTimeInSeconds / (60 * 60);
        int minutes = (mElapsedTimeInSeconds / 60) % 60;
        int seconds = mElapsedTimeInSeconds % 60;

        // Cheap and cheerful, but it works
        return hours
                + ":" + (minutes < 10 ? "0" + minutes : minutes)
                + ":" + (seconds < 10 ? "0" + seconds : seconds);
    }
}
