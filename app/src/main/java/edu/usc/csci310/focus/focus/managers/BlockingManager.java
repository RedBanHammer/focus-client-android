package edu.usc.csci310.focus.focus.managers;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
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
    HashMap<String, Profile> uniqueActiveProfiles = new HashMap<String, Profile>();;


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

    @RequiresApi(api = 26)
    public void setAppBlocker(AppBlocker appBlocker) {
        this.appBlocker = new WeakReference<AppBlocker>(appBlocker);

        this.updateBlockingModuleApps();
    }

    @RequiresApi(api = 26)
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

    @RequiresApi(api = 26)
    @Override
    protected void onHandleIntent(Intent workIntent) {
        setDefaultManager(this);

        // Initialize manager singleton references
        this.scheduleManager = ScheduleManager.getDefaultManager();
        this.profileManager = ProfileManager.getDefaultManager();

        this.scheduleManager.delegate = new WeakReference<ScheduleManagerDelegate>(this);
        this.profileManager.delegate = new WeakReference<ProfileManagerDelegate>(this);

        if (NotificationBlocker.getSharedNotificationBlocker() != null) {
            this.setNotificationBlocker(NotificationBlocker.getSharedNotificationBlocker());
        }

        this.startBlockingModules();

        this.run();
    }

    /**
     * Continuously poll for profile and schedule updates in the background and update
     * blocked app lists in blocking modules.
     */
    @RequiresApi(api = 26)
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

    @RequiresApi(api = 26)
    private void updateBlockingModuleApps() {
        HashMap<String, Profile> uniqueActiveProfiles2 = new HashMap<String, Profile>();

        // Get the current schedule from the ScheduleManager
        ArrayList<Schedule> activeSchedules = ScheduleManager.getDefaultManager().getActiveSchedules();

        for (Schedule schedule : activeSchedules) {
            // Get active profiles in this schedule
            ArrayList<String> activeProfileIdentifiers = schedule.getActiveProfileIdentifiers();

            for (String profileIdentifier : activeProfileIdentifiers) {
                if (!uniqueActiveProfiles2.containsKey(profileIdentifier)) {
                    //find new profile to be blocked
                    Profile profile = ProfileManager.getDefaultManager().getProfileWithIdentifier(profileIdentifier);
                    uniqueActiveProfiles2.put(profileIdentifier, profile);

                    //if we found a profile that wasn't blocked before and is now blocked
                    if(!uniqueActiveProfiles.containsKey(profileIdentifier)) {
                        //send notification to user that the profile went active
                        String title = "Active Profile";
                        String message = "Profile '" + profile.getName() + "' is now active.";
                        issueNotification(title, message);
                    }

                }

            }

        }

        for(String removedProfile : uniqueActiveProfiles.keySet())
        {
            if(!uniqueActiveProfiles2.containsKey(removedProfile))
            {
                //profile is no longer being blocked, send notification
                //make temp profile
                Profile p = ProfileManager.getDefaultManager().getProfileWithIdentifier(removedProfile);
                String title = "Deactivated Profile";
                String message = "Profile '" + p.getName() + "' has been deactivated";
                issueNotification(title, message);
            }
        }

        HashMap<String, App> uniqueBlockedApps = new HashMap<String, App>();
        for (String key : uniqueActiveProfiles2.keySet()) {
            Profile profile = uniqueActiveProfiles2.get(key);

            if (profile == null) {
                continue;
            }

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

        uniqueActiveProfiles = uniqueActiveProfiles2;
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

    @RequiresApi(api = 26)
    public void managerDidUpdateProfile(ProfileManager manager, Profile profile) {
        System.out.println("[BlockingManager] Got profile was updated: " + profile.getName());
        this.updateBlockingModuleApps();
    }

    @RequiresApi(api = 26)
    public void managerDidRemoveProfile(ProfileManager manager, Profile profile) {
        System.out.println("[BlockingManager] Got profile was removed: " + profile.getName());
        this.updateBlockingModuleApps();
    }


    /** ScheduleManagerDelegate implementation **/

    @RequiresApi(api = 26)
    public void managerDidUpdateSchedule(ScheduleManager manager, Schedule schedule) {
        System.out.println("[BlockingManager] Got schedule was updated: " + schedule.getName());
        this.updateBlockingModuleApps();
    }
    @RequiresApi(api = 26)
    public void managerDidRemoveSchedule(ScheduleManager manager, Schedule schedule) {
        System.out.println("[BlockingManager] Got schedule was removed: " + schedule.getName());
        this.updateBlockingModuleApps();
    }

    //build and issue notifications
    public void issueNotification(String title, String message){
        //NOTIFICATION CHANNEL
        String channelId = "csci310-focus";
        Notification.Builder builder = null;
        if (android.os.Build.VERSION.SDK_INT >= 26) {
            builder = new Notification.Builder(this, channelId);
        }
        else
        {
            builder = new Notification.Builder(this);
        }

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        int mNotificationId = 1;
        System.out.println(getClass().getResource("res/drawable/ic_focus.png"));
        builder.setSmallIcon(R.drawable.ic_focus);
        builder.setContentText(message);
        builder.setContentTitle(title);


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
        builder.setContentIntent(resultPendingIntent);

        notificationManager.notify(mNotificationId, builder.build());

    }
}
