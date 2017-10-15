package edu.usc.csci310.focus.focus.managers;

import android.content.Context;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.usc.csci310.focus.focus.blockers.AppBlocker;
import edu.usc.csci310.focus.focus.blockers.LogEntry;
import edu.usc.csci310.focus.focus.blockers.NotificationBlocker;
import edu.usc.csci310.focus.focus.dataobjects.App;
import edu.usc.csci310.focus.focus.dataobjects.Profile;
import edu.usc.csci310.focus.focus.dataobjects.Schedule;

/**
 * Handles blocking apps and notifications.
 */

public class BlockingManager implements ProfileManagerDelegate, ScheduleManagerDelegate {
    private static BlockingManager defaultManager = new BlockingManager();

    public static BlockingManager getDefaultManager() {
        return defaultManager;
    }
    public static BlockingManager getDefaultManagerWithContext(Context context) {
        defaultManager.setContext(context);
        defaultManager.setContext(context);

        return defaultManager;
    }

    private Context context = null;

    public void setContext(Context context) {
        this.context = context;
        this.appBlocker.setContext(context);
        this.notificationBlocker.setContext(context);
    }

    private ScheduleManager scheduleManager;
    private ProfileManager profileManager;

    private NotificationBlocker notificationBlocker = new NotificationBlocker("notification-blocker");
    private AppBlocker appBlocker = new AppBlocker("app-blocker");

    private BlockingManager() {
        // Initialize manager singleton references
        this.scheduleManager = ScheduleManager.getDefaultManager();
        this.profileManager = ProfileManager.getDefaultManager();

        this.scheduleManager.delegate = new WeakReference<ScheduleManagerDelegate>(this);
        this.profileManager.delegate = new WeakReference<ProfileManagerDelegate>(this);
    }

    public void debug_triggerForeground() {
        this.appBlocker.bringToForeground();
    }

    /**
     * Return the notification log entries captured by the NotificationBlocker module.
     * @return An ArrayList of LogEntries with the NOTFICIATION type.
     */
    public ArrayList<LogEntry> getNotificationLogEntries() {
        return this.notificationBlocker.getLogEntries();
    }


    /** Manage the blocking modules **/

    private void updateBlockingModuleApps() {
        HashMap<String, Profile> uniqueActiveProfiles = new HashMap<String, Profile>();

        // Get the current schedule from the ScheduleManager
        ArrayList<Schedule> activeSchedules = ScheduleManager.getDefaultManager().getActiveSchedules();

        for (Schedule schedule : activeSchedules) {
            // Get active profiles in this schedule
            ArrayList<Profile> activeProfiles = schedule.getActiveProfiles();

            for (Profile profile : activeProfiles) {
                if (!uniqueActiveProfiles.containsKey(profile.getIdentifier())) {
                    uniqueActiveProfiles.put(profile.getIdentifier(), profile);
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
        this.appBlocker.setApps(blockedApps);
        this.notificationBlocker.setApps(blockedApps);
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
