package edu.usc.csci310.focus.focus;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import edu.usc.csci310.focus.focus.dataobjects.AchievementStat;
import edu.usc.csci310.focus.focus.dataobjects.Profile;
import edu.usc.csci310.focus.focus.dataobjects.ProfileStat;
import edu.usc.csci310.focus.focus.dataobjects.Schedule;
import edu.usc.csci310.focus.focus.dataobjects.ScheduledProfile;
import edu.usc.csci310.focus.focus.managers.BlockingManager;
import edu.usc.csci310.focus.focus.managers.ProfileManager;
import edu.usc.csci310.focus.focus.managers.ScheduleManager;
import edu.usc.csci310.focus.focus.presentation.AchievementFragment;
import edu.usc.csci310.focus.focus.managers.StatsManager;
import edu.usc.csci310.focus.focus.presentation.NotificationListFragment;
import edu.usc.csci310.focus.focus.presentation.ProfileList;
import edu.usc.csci310.focus.focus.presentation.UsageFragment;
import edu.usc.csci310.focus.focus.receivers.AlarmReceiver;
import edu.usc.csci310.focus.focus.storage.StorageManager;
import edu.usc.csci310.focus.focus.presentation.schedule.ScheduleList;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

import static edu.usc.csci310.focus.focus.R.id.viewPager;

public class MainActivity extends AppCompatActivity {
    public static Context mainActivityContext;

    MyPagerAdapter viewPagerAdapter;

    ViewPager mViewPager;
    TabLayout tabLayout;
    private ArrayList<AchievementStat> achievementStats;
    private ArrayList<ProfileStat> profileStatArrayList;

    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";

    // Tab titles
    private static String[] tabs = { "Profiles", "Schedules", "Notifications", "Usage", "Achievements" };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivity.mainActivityContext = this;

        // Set up managers
        StorageManager.getDefaultManagerWithContext(getApplicationContext());
        StatsManager.getDefaultManager();
        achievementStats = StatsManager.getDefaultManager().createAchievements();

        BlockingManager.createBlockingManagerWithContext(getApplicationContext());
//        BlockingManager.getDefaultManagerWithContext(getApplicationContext());
//        BlockingManager.getDefaultManager().startBlockingModules();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewPager = (ViewPager) findViewById(viewPager);
        viewPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(viewPagerAdapter);

        //set up intent for navigating to usage tab
        int defaultValue = 0;
        int page = getIntent().getIntExtra("Usage", defaultValue);
        mViewPager.setCurrentItem(page);

        tabLayout = (TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        this.createNotificationChannel();
        this.setUpWeeklyNotification();
        this.requestPermissions();
    }

    public void onDestroy() {
        super.onDestroy();

        System.out.println("MainActivity was destroyed...");
    }

    public class MyPagerAdapter extends FragmentStatePagerAdapter {
        private int NUM_TABS = 5;
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
            final KonfettiView konfettiView = (KonfettiView)findViewById(R.id.konfettiView);

            profileStatArrayList = StatsManager.getDefaultManager().getAllProfileStats();

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) - 1);
            Long duration= new Long(3);

            HashMap<Calendar, Long> focusedIntervals = null;
            ArrayList<Schedule> activeSchedules = ScheduleManager.getDefaultManager().getActiveSchedules();

            for (Schedule s : activeSchedules) {
                ArrayList<ScheduledProfile> profiles = s.getScheduledProfiles();

                for (ScheduledProfile p : profiles) {
                    ArrayList<Long> timesRemaining = s.getTimesRemainingWithProfileIdentifier(p.identifier);

                    for (ProfileStat ps : profileStatArrayList) {
                        if (ps.getIdentifier().equals(p.identifier)) {
                            focusedIntervals = ps.getFocusedIntervalsInInterval(calendar, duration);
                            break;
                        }
                    }

                    for (Long t : timesRemaining) {
                        if (t == 0) {
                            if (focusedIntervals != null && focusedIntervals.size() > 0) {
                                konfettiView.build()
                                        .addColors(Color.rgb(66, 220, 244), Color.rgb(49, 226, 223), Color.rgb(43, 179, 188))
                                        .setDirection(0.0, 359.0)
                                        .setSpeed(1f, 5f)
                                        .setFadeOutEnabled(true)
                                        .setTimeToLive(2000L)
                                        .addShapes(Shape.RECT, Shape.CIRCLE)
                                        .addSizes(new Size(12, 5f))
                                        .setPosition(-50f, konfettiView.getWidth() + 50f, -50f, -50f)
                                        .stream(300, 5000L);
                                break;
                            }
                        }
                    }
                }
            }

            ArrayList<Profile> profiles = ProfileManager.getDefaultManager().getAllProfiles();
            switch (position) {
                case 0: // Profile List Fragment
                    return ProfileList.newInstance(0, tabs[0]);
                case 1: // Schedule List Fragment
                    return ScheduleList.newInstance(1, tabs[1]);
                case 2: // Notification List Fragment
                    return NotificationListFragment.newInstance(2, tabs[2]);
                case 3: // Usage Tab Fragment
                    return new UsageFragment();
                case 4: // Achievement Tab Fragment
                    return AchievementFragment.newInstance(achievementStats);
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
                case 3:
                    return tabs[3];
                case 4:
                    return tabs[4];
                default:
                    return null;
            }
        }


    }

    private void requestPermissions() {
        this.requestAppUsagePermissions();
        this.requestNotificationListenerPermissions();
//        this.requestNotificationPermissions();
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

    private void requestNotificationPermissions() {
        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        // Check if the notification policy access has been granted for the app.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!mNotificationManager.isNotificationPolicyAccessGranted()) {
                new AlertDialog.Builder(this)
                        .setTitle("Focus! Needs Notification Permissions")
                        .setMessage("To send notifications when schedules activate, you must give Focus! permissions to control your notifications.")
                        .setPositiveButton("Open Settings", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which) {
                                // Ask the user for app usage permissions
                                Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                                startActivity(intent);
                            }

                        })
                        .show();
            }
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
        return super.onOptionsItemSelected(item);
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

    private void setUpWeeklyNotification(){
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.AM_PM, Calendar.AM );
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

        // Check we aren't setting it in the past which would trigger it to fire instantly
        if(calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 7);
        }
        int page = 3;
        Intent notifyIntent = new Intent(this, AlarmReceiver.class);
        notifyIntent.putExtra("Usage", page);

        PendingIntent pendingIntent = PendingIntent.getBroadcast
                (this, 0, notifyIntent, 0);
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7,
                 pendingIntent);
    }

    private void createNotificationChannel(){
        //create notification channel
        if (android.os.Build.VERSION.SDK_INT >= 26) {
            NotificationManager mNotificationManager = (NotificationManager)getSystemService(
                    Context.NOTIFICATION_SERVICE);
            String channelId = "csci310-focus";
            String channelName = "focus-channel";
            String channelDescription = "focus";
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(channelDescription);
            channel.enableLights(false);
            channel.enableVibration(false);

            //set notification channel in manager
            mNotificationManager.createNotificationChannel(channel);
        }
    }
}
