package edu.usc.csci310.focus.focus.dataobjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.usc.csci310.focus.focus.managers.ScheduleManager;

/**
 * A collection of profiles and recurring times when they should activate.
 */

public class Schedule extends NamedObject {
    private ArrayList<Profile> profiles = new ArrayList<Profile>();
    private Map<String, RecurringTime> profileTimes = new HashMap<String, RecurringTime>();
    private boolean isActive = false;

    /**
     * Create a new Schedule object with a list of profiles.
     * @param profiles The initial profiles to populate the schedule with.
     * @param name The name of the schedule.
     */
    public Schedule(ArrayList<Profile> profiles, String name) {
        super(name);
        this.profiles = profiles;
        ScheduleManager.getDefaultManager().setSchedule(this);
    }

    /**
     * Add a profile to the schedule along with a recurring time entry.
     * @param profile The profile to include.
     * @param time The recurring times the profile should activate in the schedule.
     */
    public void addProfile(Profile profile, RecurringTime time) {
        this.profiles.add(profile);
        this.profileTimes.put(profile.getName(), time);
        ScheduleManager.getDefaultManager().setSchedule(this);
    }

    /**
     * Remove a profile from the schedule and remove its recurring time entry
     * @param profile The profile to remove.
     */
    public void removeProfile(Profile profile) {
        int index = this.profiles.indexOf(profile);
        this.profiles.remove(index);
        this.profileTimes.remove(profile.getName());
        ScheduleManager.getDefaultManager().setSchedule(this);
    }

    public ArrayList<Profile> getProfiles() {
        return this.profiles;
    }

    public Map<String, RecurringTime> getProfileTimes() {
        return this.profileTimes;
    }

    public void setIsActive(boolean flag) {
        this.isActive = flag;
        ScheduleManager.getDefaultManager().setSchedule(this);
    }

    public boolean getIsActive(boolean flag) {
        return this.isActive;
    }
}
