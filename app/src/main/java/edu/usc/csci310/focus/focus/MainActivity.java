package edu.usc.csci310.focus.focus;

import android.Manifest;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import edu.usc.csci310.focus.focus.dataobjects.Profile;
import edu.usc.csci310.focus.focus.managers.BlockingManager;
import edu.usc.csci310.focus.focus.managers.ProfileManager;
import edu.usc.csci310.focus.focus.presentation.NotificationListFragment;
import edu.usc.csci310.focus.focus.presentation.ProfileList;
import edu.usc.csci310.focus.focus.storage.StorageManager;
import edu.usc.csci310.focus.focus.presentation.schedule.ScheduleList;

public class MainActivity extends AppCompatActivity {
    MyPagerAdapter viewPagerAdapter;

    ViewPager mViewPager;
    TabLayout tabLayout;

    // Tab titles
    private static String[] tabs = { "Profiles", "Schedules", "Notifications" };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up managers
        StorageManager.getDefaultManagerWithContext(getApplicationContext());
        BlockingManager.getDefaultManagerWithContext(getApplicationContext());

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(viewPagerAdapter);

        tabLayout = (TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        this.requestPermissions();
    }

    public class MyPagerAdapter extends FragmentStatePagerAdapter {
        private int NUM_TABS = 3;
        private FragmentManager fm;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            fm = fragmentManager;
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_TABS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    ArrayList<Profile> profiles = ProfileManager.getDefaultManager().getAllProfiles();
                    return ProfileList.newInstance(0, tabs[0], profiles);
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return ScheduleList.newInstance(1, tabs[1]);
                case 2: // Fragment # 1 - This will show SecondFragment
                    return NotificationListFragment.newInstance(2, tabs[2]);
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return tabs[0];
                case 1:
                    return tabs[1];
                case 2:
                    return tabs[2];
                default:
                    return null;
            }
        }
    }

    private void requestPermissions() {
        this.requestAppUsagePermissions();
        this.requestNotificationListenerPermissions();
    }

    /**
     * Go to settings if the app does not have sufficient permissions
     * @source https://stackoverflow.com/a/40397196
     */
    private void requestNotificationListenerPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            boolean havePermission = false;

            for (String service : NotificationManagerCompat.getEnabledListenerPackages(this)) {
                if (service.equals(getPackageName())) {
                    havePermission = true;
                    break;
                }
            }

            if (!havePermission) {
                new AlertDialog.Builder(this)
                        .setTitle("Focus! Needs Notification Permissions")
                        .setMessage("To block notifications from apps, you must give Focus! permissions to access your device notifications.")
                        .setPositiveButton("Open Settings", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which) {
                                // Ask the user for notification listener permissions
                                Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                                startActivity(intent);
                            }

                        })
                        .show();
            }
        }
    }

    private void requestAppUsagePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            boolean havePermission = this.hasAppUsagePermissions();

            if (!havePermission) {
                new AlertDialog.Builder(this)
                        .setTitle("Focus! Needs Usage Permissions")
                        .setMessage("To block apps from opening, you must give Focus! permissions to access your device usage.")
                        .setPositiveButton("Open Settings", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which) {
                                // Ask the user for app usage permissions
                                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                                startActivity(intent);
                            }

                        })
                        .show();
            }
        }
    }

    /**
     * Get whether the app has permissions to access app usage stats.
     * @return True if the app has permissions, false otherwise or on error.
     */
    private boolean hasAppUsagePermissions() {
        try {
            Context context = this.getApplicationContext();
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (PackageManager.NameNotFoundException e) {
            return false;
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
