package edu.usc.csci310.focus.focus.managers;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

import edu.usc.csci310.focus.focus.blockers.NotificationBlocker;
import edu.usc.csci310.focus.focus.blockers.AppBlocker;
import edu.usc.csci310.focus.focus.blockers.LogEntry;
import edu.usc.csci310.focus.focus.dataobjects.App;
import edu.usc.csci310.focus.focus.dataobjects.Profile;
import edu.usc.csci310.focus.focus.dataobjects.Schedule;

/**
 * Handles blocking apps and notifications.
 */

public class BlockingManager implements ProfileManagerDelegate, ScheduleManagerDelegate {
    private static BlockingManager defaultManager = new BlockingManager();

    public static @NonNull BlockingManager getDefaultManager() {
        return defaultManager;
    }
    public static @NonNull BlockingManager getDefaultManagerWithContext(Context context) {
        defaultManager.setContext(context);

        return defaultManager;
    }

    private Context context = null;

    public void setContext(Context context) {
        this.context = context;
    }

    private ScheduleManager scheduleManager;
    private ProfileManager profileManager;

    private WeakReference<NotificationBlocker> notificationBlocker;
    private WeakReference<AppBlocker> appBlocker;

    public void setAppBlocker(AppBlocker appBlocker) {
        this.appBlocker = new WeakReference<AppBlocker>(appBlocker);

        if (this.notificationBlocker != null) {
            this.updateBlockingModuleApps();
        }
    }

    public void setNotificationBlocker(NotificationBlocker notificationBlocker) {
        this.notificationBlocker = new WeakReference<NotificationBlocker>(notificationBlocker);

        if (this.appBlocker != null) {
            this.updateBlockingModuleApps();
        }
    }

    private BlockingManager() {
        // Initialize manager singleton references
        this.scheduleManager = ScheduleManager.getDefaultManager();
        this.profileManager = ProfileManager.getDefaultManager();

        this.scheduleManager.delegate = new WeakReference<ScheduleManagerDelegate>(this);
        this.profileManager.delegate = new WeakReference<ProfileManagerDelegate>(this);
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
        this.appBlocker.get().setApps(blockedApps);
        if (this.notificationBlocker != null) {
            this.notificationBlocker.get().setApps(blockedApps);
        }
    }

    public void startBlockingModules() {
        Intent appBlockerIntent = new Intent(this.context, AppBlocker.class);
        this.context.startService(appBlockerIntent);

//        Intent notificationBlockerIntent = new Intent(this.context, NotificationBlocker.class);
//        this.context.startService(notificationBlockerIntent);

//        Intent notificationBlockingServiceIntent = new Intent(this.context, NotificationBlocker.class);
//        this.context.startService(notificationBlockingServiceIntent);
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
}
