package com.centricconsulting.driversedtracker.progress;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.centricconsulting.driversedtracker.R;
import com.centricconsulting.driversedtracker.model.DayNight;
import com.centricconsulting.driversedtracker.model.Drive;
import com.centricconsulting.driversedtracker.repository.DriveRepository;

import java.util.List;

/**
 * Created by larry.wildey on 5/29/2015.
 */
public class ProgressFragment extends Fragment{

    private int nightDriveTime;
    private int dayDriveTime;
    String statusMessage;
    private DriveRepository mDriveRepository;

    /**
     * Required public default constructor.  Please use {@link #newInstance} instead.
     */
    public ProgressFragment() {
    }

    public static ProgressFragment newInstance(DriveRepository driveRepository) {
        ProgressFragment fragment = new ProgressFragment();
        fragment.mDriveRepository = driveRepository;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Get Drives
        List<Drive> myDrives = mDriveRepository.findAll();
        for (Drive d : myDrives) {
            if(d.getDayNight()== DayNight.DAY)
                dayDriveTime += d.getElapsedTimeInSeconds();
            else
                nightDriveTime += d.getElapsedTimeInSeconds();
        }
        dayDriveTime += 20 * 3600;
        nightDriveTime += 5 * 3600;

        //Calculate Status
        double requiredNight = 10 * 3600;
        double requiredDay = 40 * 3600;
        double percentDayComplete = (dayDriveTime/requiredDay) * 100;
        double percentNightComplete = (nightDriveTime /requiredNight) * 100;
        double percentTotalComplete = ((dayDriveTime + nightDriveTime)/(requiredDay + requiredNight)) * 100;
        if (percentTotalComplete == 100.0)
            statusMessage="WooHoo!! You made it! Good work Speed Racer.";
        else if (percentTotalComplete > 75)
            statusMessage="You're doing great. Keep on truckin'!";
        else if (percentTotalComplete > 50)
            statusMessage="You're doing great. Keep on truckin'!";
        else if (percentTotalComplete > 25)
            statusMessage="Come on. Get out there and drive!";  
        else
            statusMessage="You haven't even started! Why are you looking at your progress?";

        //Display Status
        View rootView = inflater.inflate(R.layout.fragment_progress, container, false);
        TextView statusText = (TextView)rootView.findViewById(R.id.progress_status_text);
        statusText.setText(statusMessage);

        ProgressBar statusProgressBar = (ProgressBar)rootView.findViewById(R.id.progressBar);
        statusProgressBar.setProgress((int)percentTotalComplete);

        ProgressBar nightProgressBar = (ProgressBar)rootView.findViewById(R.id.night_progressBar);
        nightProgressBar.setProgress((int)percentNightComplete);

        ProgressBar dayProgressBar = (ProgressBar)rootView.findViewById(R.id.day_progressBar);
        dayProgressBar.setProgress((int)percentDayComplete);

        return rootView;
    }


}
