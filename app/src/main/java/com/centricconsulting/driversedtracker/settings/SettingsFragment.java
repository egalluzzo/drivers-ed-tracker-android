package com.centricconsulting.driversedtracker.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.centricconsulting.driversedtracker.R;

/**
 * Created by eric on 5/29/15.
 */
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }
}
