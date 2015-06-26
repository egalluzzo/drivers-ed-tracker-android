package com.centricconsulting.driversedtracker.settings;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

import java.util.Scanner;

/**
 * Created by larry.wildey on 6/26/2015.
 */

public class TimePickerPreference extends DialogPreference {
    Context context;
    TimePickerPreference timePickerPreference;
    private TimePicker timePicker;
    private final static String TAG = "TIME_DIALOG_PREFERENCE";
    private final String DEFAULT_VALUE = "00:00";
    String key;

    public TimePickerPreference (Context context) {
        this(context, null);
        this.context=context;
    }

    public TimePickerPreference (Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context=context;
    }

    public TimePickerPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        this.context=context;

        setPositiveButtonText("Set");
        setNegativeButtonText("Cancel");
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    @Override
    public View onCreateDialogView(){
        String persistedTime = getPersistedString(DEFAULT_VALUE);
        Scanner scanner = new Scanner(persistedTime).useDelimiter(":");
        int hour = scanner.nextInt();
        int minutes = scanner.nextInt();
        timePicker =  new TimePicker(context);
        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minutes);
        return timePicker;
    }

    public void onDialogClosed(boolean positiveResult) {
        persistString(String.format("%d:%02d", timePicker.getCurrentHour(), timePicker.getCurrentMinute()));
    }
}