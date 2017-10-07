package edu.usc.csci310.focus.focus.managers;

import java.lang.ref.WeakReference;

import edu.usc.csci310.focus.focus.dataobjects.Profile;
import edu.usc.csci310.focus.focus.dataobjects.Schedule;

/**
 * Handles blocking apps and notifications.
 */

public class BlockingManager implements ProfileManagerDelegate, ScheduleManagerDelegate {
    private ScheduleManager scheduleManager;
    private ProfileManager profileManager;

    public BlockingManager() {
        // Initialize manager singleton references
        this.scheduleManager = ScheduleManager.getDefaultManager();
        this.profileManager = ProfileManager.getDefaultManager();

        this.scheduleManager.delegate = new WeakReference<ScheduleManagerDelegate>(this);
        this.profileManager.delegate = new WeakReference<ProfileManagerDelegate>(this);
    }

    /** ProfileManagerDelegate implementation **/

    public void managerDidUpdateProfile(ProfileManager manager, Profile profile) {
        System.out.println("[BlockingManager] Got profile was updated: " + profile.getName());
    }

    public void managerDidRemoveProfile(ProfileManager manager, Profile profile) {
        System.out.println("[BlockingManager] Got profile was removed: " + profile.getName());
    }


    /** ScheduleManagerDelegate implementation **/

    public void managerDidUpdateSchedule(ScheduleManager manager, Schedule schedule) {
        System.out.println("[BlockingManager] Got schedule was updated: " + schedule.getName());
    }

    public void managerDidRemoveSchedule(ScheduleManager manager, Schedule schedule) {
        System.out.println("[BlockingManager] Got schedule was removed: " + schedule.getName());
    }
}
