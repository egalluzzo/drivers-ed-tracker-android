package com.centricconsulting.driversedtracker.model;

import java.util.Date;

/**
 * Created by larry.wildey on 4/24/2015.
 */
public class TrackerData {
    private long mTrackerStartTime;
    private boolean mIsTrackerRunning;

    public long getTrackerStartTime(){ return mTrackerStartTime; }
    public int getTrackerElapsedTime() {
        if (mTrackerStartTime == 0)
            return 0;
        else
            return (int)((System.currentTimeMillis() - mTrackerStartTime) / 1000); }
    public boolean getIsTrackerRunning(){ return mIsTrackerRunning; }

    public void startTracker(){
        mTrackerStartTime = System.currentTimeMillis();
        mIsTrackerRunning = true;
    }
    public void clearTracker(){
        mTrackerStartTime = 0;
        mIsTrackerRunning = false;
    }
    public void stopTracker(){
        mIsTrackerRunning = false;
    }
}
