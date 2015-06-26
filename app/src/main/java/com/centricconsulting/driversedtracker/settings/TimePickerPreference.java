package com.centricconsulting.driversedtracker.settings;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

import com.centricconsulting.driversedtracker.R;

import java.util.Scanner;

/**
 * Created by larry.wildey on 6/26/2015.
 */
public class TimePickerPreference extends DialogPreference {
    private static final String DEFAULT_VALUE = "00:00";

    private TimePicker timePicker;

    public TimePickerPreference (Context context) {
        this(context, null);
    }

    public TimePickerPreference (Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimePickerPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setPositiveButtonText(getContext().getString(R.string.pref_button_set));
        setNegativeButtonText(getContext().getString(R.string.button_cancel));
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    @Override
    public View onCreateDialogView() {
        String persistedTime = getPersistedString(DEFAULT_VALUE);
        Scanner scanner = new Scanner(persistedTime).useDelimiter(":");
        int hour = scanner.nextInt();
        int minutes = scanner.nextInt();
        timePicker =  new TimePicker(getContext());
        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minutes);
        return timePicker;
    }

    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            persistString(String.format("%d:%02d", timePicker.getCurrentHour(), timePicker.getCurrentMinute()));
        }
    }
}