package edu.usc.csci310.focus.focus.dataobjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    }

    /**
     * Add a profile to the schedule along with a recurring time entry.
     * @param profile The profile to include.
     * @param time The recurring times the profile should activate in the schedule.
     */
    public void addProfile(Profile profile, RecurringTime time) {

    }

    public ArrayList<Profile> getProfiles() {
        return null;
    }

    public HashMap<String, RecurringTime> getProfileTimes() {
        return null;
    }

    public void setIsActive(boolean flag) {
        this.isActive = flag;
    }

    public boolean getIsActive(boolean flag) {
        return this.isActive;
    }
}
