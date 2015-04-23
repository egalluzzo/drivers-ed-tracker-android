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
    private boolean mRunning;
    private long mStartMillis;
    private int mElapsedTimeInSeconds;

    private Runnable mTicker = new Runnable() {
        public void run() {
            if (mRunning) {
                mElapsedTimeInSeconds = (int)((System.currentTimeMillis() - mStartMillis) / 1000);
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
        return new TimerFragment();
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
                startTimer();
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
                if (mListener != null) {
                    mListener.onSaveDrive();
                }
            }
        });

        mElapsedTimeInSeconds = 0;
        updateElapsedTime();
        updateButtonStates();

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("mRunning", mRunning);
        outState.putLong("mStartMillis", mStartMillis);
        outState.putInt("mElapsedTimeInSeconds", mElapsedTimeInSeconds);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        mRunning = savedInstanceState.getBoolean("mRunning");
        mStartMillis = savedInstanceState.getLong("mStartMillis");
        mElapsedTimeInSeconds = savedInstanceState.getInt("mElapsedTimeInSeconds");

        updateElapsedTime();
        updateButtonStates();
    }

    public void startTimer() {
        mRunning = true;
        mStartMillis = System.currentTimeMillis();
        mTimerHandler.postDelayed(mTicker, TICK_DELAY_IN_MS);

        mStartStopButton.setText(getResources().getText(R.string.timer_stop_button_text));
        mStartStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTimer();
            }
        });

        updateButtonStates();
    }

    public void stopTimer() {
        mRunning = false;
        mTimerHandler.removeCallbacks(mTicker);

        mStartStopButton.setText(getResources().getText(R.string.timer_start_button_text));
        mStartStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
            }
        });

        updateButtonStates();
    }

    public void resetTimer() {
        stopTimer();
        mElapsedTimeInSeconds = 0;
        updateElapsedTime();
        updateButtonStates();
    }

    private void updateElapsedTime() {
        mElapsedTimeView.setText(getFormattedElapsedTime());
    }

    private void updateButtonStates() {
        mStartStopButton.setText(getResources().getText(mRunning ? R.string.timer_stop_button_text : R.string.timer_start_button_text));
        mStartStopButton.setEnabled(mRunning || mElapsedTimeInSeconds == 0);
        mResetButton.setEnabled(mRunning || mElapsedTimeInSeconds > 0);
        mSaveButton.setEnabled(!mRunning && mElapsedTimeInSeconds > 0);
    }

    private String getFormattedElapsedTime() {
        int hours = mElapsedTimeInSeconds / (60 * 60);
        int minutes = (mElapsedTimeInSeconds / 60) % 60;
        int seconds = mElapsedTimeInSeconds % 60;

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
        public void onSaveDrive();
    }
}
