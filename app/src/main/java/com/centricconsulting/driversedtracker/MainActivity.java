package com.centricconsulting.driversedtracker;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import com.centricconsulting.driversedtracker.drivinglog.DrivingLogFragment;
import com.centricconsulting.driversedtracker.model.AppPreferences;
import com.centricconsulting.driversedtracker.model.Drive;
import com.centricconsulting.driversedtracker.model.DriveTimer;
import com.centricconsulting.driversedtracker.progress.ProgressFragment;
import com.centricconsulting.driversedtracker.repository.DriveRepository;
import com.centricconsulting.driversedtracker.repository.db.DbDriveRepository;
import com.centricconsulting.driversedtracker.settings.SettingsActivity;
import com.centricconsulting.driversedtracker.timer.TimerFragment;


public class MainActivity
        extends Activity
        implements ActionBar.TabListener, TimerFragment.Listener, DrivingLogFragment.Listener {

    private static final String TAG = MainActivity.class.getName();

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private DriveTimer mDriveTimer;
    private DriveRepository mDriveRepository;
    private AppPreferences mAppPreferences;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDriveTimer = new DriveTimer();
        mAppPreferences = AppPreferences.getInstance(getApplicationContext());
        mDriveRepository = new DbDriveRepository(getApplicationContext());

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onSaveDrive(Drive drive) {
        mDriveRepository.save(drive);
        Toast toast = Toast.makeText(this, "Drive has been saved.", Toast.LENGTH_SHORT);
        toast.show();

        ProgressFragment progressFragment = (ProgressFragment) mSectionsPagerAdapter.getRegisteredFragment(1);
        if (progressFragment != null) {
            progressFragment.refresh();
        }

        DrivingLogFragment drivingLogFragment = (DrivingLogFragment) mSectionsPagerAdapter.getRegisteredFragment(2);
        if (drivingLogFragment != null) {
            drivingLogFragment.refresh();
        }
    }

    @Override
    public void onDriveTap(Drive drive) {
        // FIXME: Implement this
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     *
     * FIXME: This class needs to die.  Switch statements are evil (usually).
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private Map<Integer, Fragment> mFragmentMap = new HashMap<Integer, Fragment>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return TimerFragment.newInstance(mDriveTimer, mAppPreferences);
                case 1:
                    return ProgressFragment.newInstance(mAppPreferences, mDriveRepository);
                case 2:
                    return DrivingLogFragment.newInstance(mDriveRepository);
                default:
                    Log.d(TAG, "No fragment in main view pager at index " + position);
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_timer).toUpperCase(l);
                case 1:
                    return getString(R.string.title_progress).toUpperCase(l);
                case 2:
                    return getString(R.string.title_driving_log).toUpperCase(l);
            }
            return null;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            mFragmentMap.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            mFragmentMap.remove(position);
            super.destroyItem(container, position, object);
        }

        public Fragment getRegisteredFragment(int position) {
            return mFragmentMap.get(position);
        }
    }
}
