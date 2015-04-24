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

import com.centricconsulting.driversedtracker.MainActivity;
import com.centricconsulting.driversedtracker.R;
import com.centricconsulting.driversedtracker.model.Drive;
import com.centricconsulting.driversedtracker.model.TrackerData;

import java.util.Date;

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
    //private boolean mRunning;
    //private long mStartMillis;
    //private int mElapsedTimeInSeconds;

    private TrackerData mTrackerData;

    private Runnable mTicker = new Runnable() {
        public void run() {
            //if (mRunning) {
            if (mTrackerData.getIsTrackerRunning()) {
                //mElapsedTimeInSeconds = (int)((System.currentTimeMillis() - mStartMillis) / 1000);
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
    public static TimerFragment newInstance() {
        TimerFragment fragment = new TimerFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    /**
     * Required public default constructor.  Please use {@link #newInstance()} instead.
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
                //if (mRunning) {
                if (mTrackerData.getIsTrackerRunning()) {
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

        Bundle arguments = getArguments();
        //if (arguments != null) {
        //    mRunning = arguments.getBoolean("mRunning", false);
        //    mStartMillis = arguments.getLong("mStartMillis", 0);
        //    mElapsedTimeInSeconds = arguments.getInt("mElapsedTimeInSeconds", 0);
        //}

        updateElapsedTime();
        updateButtonStates();

        //if (mRunning) {
        if (mTrackerData.getIsTrackerRunning()) {
            mTimerHandler.postDelayed(mTicker, TICK_DELAY_IN_MS);
        }

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();

        Bundle arguments = getArguments();
        //arguments.putBoolean("mRunning", mRunning);
        //arguments.putLong("mStartMillis", mStartMillis);
        //arguments.putInt("mElapsedTimeInSeconds", mElapsedTimeInSeconds);
    }

    public void startTimer() {
        //mRunning = true;
        //mStartMillis = System.currentTimeMillis();
        mTrackerData.startTracker();
        mTimerHandler.postDelayed(mTicker, TICK_DELAY_IN_MS);
        updateButtonStates();
    }

    public void stopTimer() {
        //mRunning = false;
        mTrackerData.stopTracker();
        mTimerHandler.removeCallbacks(mTicker);
        updateButtonStates();
    }

    public void resetTimer() {
        stopTimer();
        //mElapsedTimeInSeconds = 0;
        mTrackerData.clearTracker();
        updateElapsedTime();
        updateButtonStates();
    }

    public void saveDrive() {
        Drive drive = new Drive();
        //drive.setStartTime(new Date(mStartMillis));
        //drive.setElapsedTimeInSeconds(mElapsedTimeInSeconds);
        drive.setStartTime(new Date(mTrackerData.getTrackerStartTime()));
        drive.setElapsedTimeInSeconds(mTrackerData.getTrackerElapsedTime());
        if (mListener != null) {
            mListener.onSaveDrive(drive);
        }
    }

    private void updateElapsedTime() {
        mElapsedTimeView.setText(getFormattedElapsedTime());
    }

    private void updateButtonStates() {
        //mStartStopButton.setText(getResources().getText(mRunning ? R.string.timer_stop_button_text : R.string.timer_start_button_text));
        //mStartStopButton.setEnabled(mRunning || mElapsedTimeInSeconds == 0);
        //mResetButton.setEnabled(mRunning || mElapsedTimeInSeconds > 0);
        //mSaveButton.setEnabled(!mRunning && mElapsedTimeInSeconds > 0);

        mStartStopButton.setText(getResources().getText(mTrackerData.getIsTrackerRunning() ? R.string.timer_stop_button_text : R.string.timer_start_button_text));
        mStartStopButton.setEnabled(mTrackerData.getIsTrackerRunning() || mTrackerData.getTrackerElapsedTime() == 0);
        mResetButton.setEnabled(mTrackerData.getIsTrackerRunning() || mTrackerData.getTrackerElapsedTime() > 0);
        mSaveButton.setEnabled(!mTrackerData.getIsTrackerRunning() && mTrackerData.getTrackerElapsedTime() > 0);
    }

    private String getFormattedElapsedTime() {
        int elapsedTimeInSeconds = mTrackerData.getTrackerElapsedTime();
        int hours = elapsedTimeInSeconds / (60 * 60);
        int minutes = (elapsedTimeInSeconds / 60) % 60;
        int seconds = elapsedTimeInSeconds % 60;

        // Cheap and cheerful, but it works
        return hours
                + ":" + (minutes < 10 ? "0" + minutes : minutes)
                + ":" + (seconds < 10 ? "0" + seconds : seconds);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (Listener) activity;
            MainActivity main = (MainActivity)getActivity();
            mTrackerData = (TrackerData) main.mTrackerData;
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
        public void onSaveDrive(Drive drive);
    }
}
