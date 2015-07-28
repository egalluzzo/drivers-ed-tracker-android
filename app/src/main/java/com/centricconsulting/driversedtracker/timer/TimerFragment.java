package com.centricconsulting.driversedtracker.timer;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.centricconsulting.driversedtracker.R;
import com.centricconsulting.driversedtracker.model.AppPreferences;
import com.centricconsulting.driversedtracker.model.Drive;
import com.centricconsulting.driversedtracker.model.DriveTimer;

import java.util.List;

/**
 * A {@link Fragment} representing the Timer screen.
 * Activities that contain this fragment must implement the
 * {@link TimerFragment.Listener} interface
 * to handle interaction events.
 * Use the {@link #newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimerFragment extends Fragment {

    private static final int TICK_DELAY_IN_MS = 100; // ms

    private Listener mListener;

    private TextView mElapsedTimeView;
    private Button mStartStopButton;
    private Button mResetButton;
    private Button mSaveButton;

    private Handler mTimerHandler = new Handler();
    private DriveTimer mDriveTimer;
    private AppPreferences mAppPreferences;

    private Runnable mTicker = new Runnable() {
        public void run() {
            if (mDriveTimer.isRunning()) {
                updateElapsedTime();
                mTimerHandler.postDelayed(this, TICK_DELAY_IN_MS);
            }
        }
    };

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    // TODO: Rename and change types and number of parameters
    public static TimerFragment newInstance(DriveTimer td, AppPreferences appPreferences) {
        TimerFragment fragment = new TimerFragment();
        fragment.setArguments(new Bundle());
        fragment.mDriveTimer = td;
        fragment.mAppPreferences = appPreferences;
        return fragment;
    }

    /**
     * Required public default constructor.  Please use
     * {@link #newInstance(DriveTimer, AppPreferences)} instead.
     */
    public TimerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_timer, container, false);

        mElapsedTimeView = (TextView)rootView.findViewById(R.id.timer_elapsed_time_text);

        mStartStopButton = (Button)rootView.findViewById(R.id.timer_start_button);
        mStartStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDriveTimer.isRunning()) {
                    stopTimer();
                } else {
                    startTimer();
                }
            }
        });

        mResetButton = (Button)rootView.findViewById(R.id.timer_reset_button);
        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

        mSaveButton = (Button)rootView.findViewById(R.id.timer_save_button);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDrive();
            }
        });

        updateElapsedTime();
        updateButtonStates();

        if (mDriveTimer.isRunning()) {
            mTimerHandler.postDelayed(mTicker, TICK_DELAY_IN_MS);
        }

        return rootView;
    }

    public void startTimer() {
        mDriveTimer.start();
        mTimerHandler.postDelayed(mTicker, TICK_DELAY_IN_MS);
        updateButtonStates();
    }

    public void stopTimer() {
        mDriveTimer.stop();
        mTimerHandler.removeCallbacks(mTicker);
        updateButtonStates();
    }

    public void resetTimer() {
        stopTimer();
        mDriveTimer.clear();
        updateElapsedTime();
        updateButtonStates();
    }

    public void saveDrive() {
        if (mListener != null) {
            List<Drive> drives = mDriveTimer.createDrives(mAppPreferences.getDaytimeRange());
            for (Drive drive : drives) {
                mListener.onSaveDrive(drive);
            }
        }
    }

    private void updateElapsedTime() {
        mElapsedTimeView.setText(mDriveTimer.getFormattedElapsedTime());
    }

    private void updateButtonStates() {
        mStartStopButton.setText(getResources().getText(mDriveTimer.isRunning() ? R.string.timer_stop_button_text : R.string.timer_start_button_text));
        mStartStopButton.setEnabled(mDriveTimer.isRunning() || mDriveTimer.getElapsedTimeInSeconds() == 0);
        mResetButton.setEnabled(mDriveTimer.isRunning() || mDriveTimer.getElapsedTimeInSeconds() > 0);
        mSaveButton.setEnabled(!mDriveTimer.isRunning() && mDriveTimer.getElapsedTimeInSeconds() > 0);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (Listener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface Listener {
        void onSaveDrive(Drive drive);
    }
}
