package edu.usc.csci310.focus.focus.managers;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

import edu.usc.csci310.focus.focus.MainActivity;
import edu.usc.csci310.focus.focus.R;
import edu.usc.csci310.focus.focus.blockers.NotificationBlocker;
import edu.usc.csci310.focus.focus.blockers.AppBlocker;
import edu.usc.csci310.focus.focus.blockers.LogEntry;
import edu.usc.csci310.focus.focus.dataobjects.App;
import edu.usc.csci310.focus.focus.dataobjects.Profile;
import edu.usc.csci310.focus.focus.dataobjects.Schedule;
import edu.usc.csci310.focus.focus.presentation.ProfileInterfaceController;
import edu.usc.csci310.focus.focus.presentation.ProfileList;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Handles blocking apps and notifications.
 */

public class BlockingManager extends IntentService implements ProfileManagerDelegate, ScheduleManagerDelegate, Runnable {
    // How often in milliseconds to poll for changes in scheduled profiles.
    private static int UPDATE_POLL_INTERVAL = 5 * 1000;

    private static BlockingManager defaultManager = null;

    public static @NonNull BlockingManager getDefaultManager() {
        return defaultManager;
    }

    public static void createBlockingManagerWithContext(Context context) {
        Intent blockingManagerIntent = new Intent(context, BlockingManager.class);
        context.startService(blockingManagerIntent);
    }

    private void setDefaultManager(BlockingManager manager) {
        defaultManager = manager;
    }

    private ScheduleManager scheduleManager;
    private ProfileManager profileManager;

    private WeakReference<NotificationBlocker> notificationBlocker;
    private WeakReference<AppBlocker> appBlocker;

    public WeakReference<BlockingManagerLogEntryDelegate> logEntryDelegate;

    public void setLogEntryDelegate(BlockingManagerLogEntryDelegate delegate) {
        this.logEntryDelegate = new WeakReference<BlockingManagerLogEntryDelegate>(delegate);
    }

    public void setAppBlocker(AppBlocker appBlocker) {
        this.appBlocker = new WeakReference<AppBlocker>(appBlocker);

        this.updateBlockingModuleApps();
    }

    public void setNotificationBlocker(NotificationBlocker notificationBlocker) {
        this.notificationBlocker = new WeakReference<NotificationBlocker>(notificationBlocker);

        this.updateBlockingModuleApps();
    }

    public void didUpdateLogEntries() {
        if (this.logEntryDelegate != null && this.logEntryDelegate.get() != null) {
            this.logEntryDelegate.get().blockingManagerDidUpdateLogEntries(this);
        }
    }

    public BlockingManager(String name) {
        super(name);
    }

    public BlockingManager() {
        super("BlockingManager");
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        setDefaultManager(this);

        // Initialize manager singleton references
        this.scheduleManager = ScheduleManager.getDefaultManager();
        this.profileManager = ProfileManager.getDefaultManager();

        this.scheduleManager.delegate = new WeakReference<ScheduleManagerDelegate>(this);
        this.profileManager.delegate = new WeakReference<ProfileManagerDelegate>(this);

        this.startBlockingModules();

        this.run();
    }

    /**
     * Continuously poll for profile and schedule updates in the background and update
     * blocked app lists in blocking modules.
     */
    public void run() {
        while (true) {
            this.updateBlockingModuleApps();

            try {
                Thread.sleep(UPDATE_POLL_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Return the notification log entries captured by the NotificationBlocker module.
     * @return An ArrayList of LogEntries with the NOTFICIATION type.
     */
    public @NonNull ArrayList<LogEntry> getNotificationLogEntries() {
        return NotificationBlocker.getLogEntries();
    }

    public void clearAllNotificationLogEntries() {
        NotificationBlocker.removeAllLogEntries();
    }

    public @NonNull ArrayList<LogEntry> getAppOpenLogEntries() {
        return AppBlocker.getLogEntries();
    }

    public void clearAllAppOpenLogEntries() {
        AppBlocker.removeAllLogEntries();
    }

    /** Manage the blocking modules **/

    private void updateBlockingModuleApps() {
        HashMap<String, Profile> uniqueActiveProfiles = new HashMap<String, Profile>();

        // Get the current schedule from the ScheduleManager
        ArrayList<Schedule> activeSchedules = ScheduleManager.getDefaultManager().getActiveSchedules();

        for (Schedule schedule : activeSchedules) {
            // Get active profiles in this schedule
            ArrayList<String> activeProfileIdentifiers = schedule.getActiveProfileIdentifiers();

            for (String profileIdentifier : activeProfileIdentifiers) {
                if (!uniqueActiveProfiles.containsKey(profileIdentifier)) {
                    Profile profile = ProfileManager.getDefaultManager().getProfileWithIdentifier(profileIdentifier);
                    uniqueActiveProfiles.put(profileIdentifier, profile);
                }
            }
        }

        HashMap<String, App> uniqueBlockedApps = new HashMap<String, App>();
        for (String key : uniqueActiveProfiles.keySet()) {
            Profile profile = uniqueActiveProfiles.get(key);

            for (App app : profile.getApps()) {
                if (!uniqueBlockedApps.containsKey(app.getIdentifier())) {
                    uniqueBlockedApps.put(app.getIdentifier(), app);
                }
            }
        }

        ArrayList<App> blockedApps = new ArrayList<App>();
        for (String key : uniqueBlockedApps.keySet()) {
            blockedApps.add(uniqueBlockedApps.get(key));
        }

        // Update modules
        if (this.appBlocker != null) {
            this.appBlocker.get().setApps(blockedApps);
        }
        if (this.notificationBlocker != null) {
            this.notificationBlocker.get().setApps(blockedApps);
        }
    }

    public void startBlockingModules() {
        Intent appBlockerIntent = new Intent(this, AppBlocker.class);
        this.startService(appBlockerIntent);

//        Intent notificationBlockerIntent = new Intent(this, NotificationBlocker.class);
//        this.startService(notificationBlockerIntent);

//        Intent notificationBlockingServiceIntent = new Intent(this, NotificationBlocker.class);
//        this.startService(notificationBlockingServiceIntent);
    }

    /** ProfileManagerDelegate implementation **/

    public void managerDidUpdateProfile(ProfileManager manager, Profile profile) {
        System.out.println("[BlockingManager] Got profile was updated: " + profile.getName());
        this.updateBlockingModuleApps();
    }

    public void managerDidRemoveProfile(ProfileManager manager, Profile profile) {
        System.out.println("[BlockingManager] Got profile was removed: " + profile.getName());
        this.updateBlockingModuleApps();
    }


    /** ScheduleManagerDelegate implementation **/

    public void managerDidUpdateSchedule(ScheduleManager manager, Schedule schedule) {
        System.out.println("[BlockingManager] Got schedule was updated: " + schedule.getName());
        this.updateBlockingModuleApps();
    }

    public void managerDidRemoveSchedule(ScheduleManager manager, Schedule schedule) {
        System.out.println("[BlockingManager] Got schedule was removed: " + schedule.getName());
        this.updateBlockingModuleApps();
    }

    //build and issue notifications
    @RequiresApi(api = 26)
    public void issueNotification(String title, String message){
        String channelId = "my_channel";
        //NOTIFICATION CHANNEL
        String channelName = "channel_name";
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.RED);
        notificationChannel.setShowBadge(true);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

        //NOTIFICATION MANAGER
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        int mNotificationId = 1;
        notificationManager.createNotificationChannel(notificationChannel);

        //NOTIFICATION BUILDER
        Notification.Builder mBuilder = new Notification.Builder(this, channelId);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(message);

        //build an intent that sends the user to the profile that is active/inactive
        //upon clicking the notification.

        Intent onClickIntent = new Intent(this, MainActivity.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        onClickIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);

        notificationManager.notify(mNotificationId, mBuilder.build());

    }
}
