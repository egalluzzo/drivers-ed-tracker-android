package com.centricconsulting.driversedtracker.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.WeakHashMap;

/**
 * Encapsulates the preferences for this app in a typesafe, non-bag-ish way.
 *
 * Created by eric on 6/26/15.
 */
public class AppPreferences {
    private static AppPreferences sInstance;
    private SharedPreferences mSharedPreferences;
    private WeakHashMap<Listener, Object> mListeners = new WeakHashMap<Listener, Object>();

    public interface Listener {
        void onPreferenceChanged(AppPreferences preferences);
    }

    public static AppPreferences getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new AppPreferences(context);
        }
        return sInstance;
    }

    public AppPreferences(Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void fireChangeEvent() {
        for (Listener listener : mListeners.keySet()) {
            listener.onPreferenceChanged(this);
        }
    }

    public void addListener(Listener listener) {
        mListeners.put(listener, new Object());
    }

    public void removeListener(Listener listener) {
        mListeners.remove(listener);
    }

    public int getTotalHours() {
        return Integer.parseInt(mSharedPreferences.getString("pref_key_total_hours", "50"));
    }

    public int getDaytimeHours() {
        return Integer.parseInt(mSharedPreferences.getString("pref_key_daytime_hours", "40"));
    }

    public int getNighttimeHours() {
        return Integer.parseInt(mSharedPreferences.getString("pref_key_nighttime_hours", "10"));
    }
}
