package com.centricconsulting.driversedtracker.progress;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.centricconsulting.driversedtracker.R;
import com.centricconsulting.driversedtracker.model.AppPreferences;
import com.centricconsulting.driversedtracker.model.DayNight;
import com.centricconsulting.driversedtracker.model.Drive;
import com.centricconsulting.driversedtracker.repository.DriveRepository;

import java.util.List;

/**
 * Created by larry.wildey on 5/29/2015.
 */
public class ProgressFragment extends Fragment implements AppPreferences.Listener {
    private AppPreferences mPrefs;
    private DriveRepository mDriveRepository;
    private TextView mStatusTextView;
    private ProgressBar mTotalProgressBar;
    private TextView mTotalProgressText;
    private ProgressBar mDaytimeProgressBar;
    private TextView mDaytimeProgressText;
    private ProgressBar mNighttimeProgressBar;
    private TextView mNighttimeProgressText;

    /**
     * Required public default constructor.  Please use {@link #newInstance} instead.
     */
    public ProgressFragment() {
    }

    public static ProgressFragment newInstance(AppPreferences prefs, DriveRepository driveRepository) {
        ProgressFragment fragment = new ProgressFragment();
        fragment.mPrefs = prefs;
        fragment.mDriveRepository = driveRepository;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPrefs.addListener(this);
    }

    @Override
    public void onDestroy() {
        mPrefs.removeListener(this);
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_progress, container, false);

        mStatusTextView = (TextView)rootView.findViewById(R.id.progress_status_text);

        mTotalProgressBar = (ProgressBar)rootView.findViewById(R.id.progressBar);
        mTotalProgressText = (TextView)rootView.findViewById(R.id.progress_text);

        mNighttimeProgressBar = (ProgressBar)rootView.findViewById(R.id.night_progressBar);
        mNighttimeProgressText = (TextView)rootView.findViewById(R.id.night_progress_text);

        mDaytimeProgressBar = (ProgressBar)rootView.findViewById(R.id.day_progressBar);
        mDaytimeProgressText = (TextView)rootView.findViewById(R.id.day_progress_text);

        refresh();

        return rootView;
    }

    public void refresh() {

        int nightDriveTime = 0;
        int dayDriveTime = 0;
        int totalDriveTime = 0;

        //Get Drives
        List<Drive> myDrives = mDriveRepository.findAll();
        for (Drive d : myDrives) {
            if(d.getDayNight()== DayNight.DAY)
                dayDriveTime += d.getElapsedTimeInSeconds();
            else if (d.getDayNight() == DayNight.NIGHT)
                nightDriveTime += d.getElapsedTimeInSeconds();
            totalDriveTime += d.getElapsedTimeInSeconds();
        }
        dayDriveTime += 20 * 3600;
        nightDriveTime += 5 * 3600;
        totalDriveTime += 25 * 3600;

        //Calculate Status
        double requiredNight = mPrefs.getNighttimeHours() * 3600;
        double requiredDay = mPrefs.getDaytimeHours() * 3600;
        double requiredTotal = mPrefs.getTotalHours() * 3600;
        double percentTotalComplete = ((double)totalDriveTime / requiredTotal) * 100;
        String statusMessage = determineStatusMessage(percentTotalComplete);

        //Display Status
        mStatusTextView.setText(statusMessage);
        setProgress(dayDriveTime, requiredDay, mDaytimeProgressBar, mDaytimeProgressText);
        setProgress(nightDriveTime, requiredNight, mNighttimeProgressBar, mNighttimeProgressText);
        setProgress(totalDriveTime, requiredTotal, mTotalProgressBar, mTotalProgressText);
    }

    private void setProgress(int driveTime, double requiredDriveTime, ProgressBar progressBar, TextView progressText) {
        double percentComplete = ((double)driveTime / requiredDriveTime) * 100;
        progressBar.setProgress((int) percentComplete);
        progressText.setText(String.format("%.1f / %d", (double)driveTime / 3600, (int)Math.round(requiredDriveTime / 3600)));
    }

    private String determineStatusMessage(double percentTotalComplete) {
        String statusMessage;
        if (percentTotalComplete >= 100)
            statusMessage="Woohoo! You made it! Good work, Speed Racer.";
        else if (percentTotalComplete > 75)
            statusMessage="Nearly there! You can do it!";
        else if (percentTotalComplete > 50)
            statusMessage="You're doing great. Keep on truckin'!";
        else if (percentTotalComplete > 25)
            statusMessage="Come on. Get out there and drive!";
        else
            statusMessage="You haven't even started! Why are you looking at your progress?";
        return statusMessage;
    }

    @Override
    public void onPreferenceChanged(AppPreferences preferences) {
        refresh();
    }
}
