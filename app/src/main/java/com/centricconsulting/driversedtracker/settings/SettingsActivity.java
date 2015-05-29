package com.centricconsulting.driversedtracker.settings;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by eric on 5/29/15.
 */
public class SettingsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the settings fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
