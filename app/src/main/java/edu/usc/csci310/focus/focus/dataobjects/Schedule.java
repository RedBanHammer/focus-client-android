package edu.usc.csci310.focus.focus.dataobjects;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import edu.usc.csci310.focus.focus.managers.ScheduleManager;

/**
 * A collection of profiles and recurring times when they should activate.
 */

public class Schedule extends NamedObject {
    private static final long serialVersionUID = 2L;

    private ArrayList<Profile> profiles = new ArrayList<Profile>();
    private Map<String, RecurringTime> profileTimes = new HashMap<String, RecurringTime>();
    private Boolean isActive = false;

    /**
     * Create a new Schedule object with a list of profiles.
     * @param profiles The initial profiles to populate the schedule with.
     * @param name The name of the schedule.
     */
    public Schedule(@NonNull ArrayList<Profile> profiles, @NonNull String name) {
        super(name);
        this.profiles = profiles;
    }

    public Schedule(@NonNull String name) {
        super(name);
        this.profiles = new ArrayList<Profile>();
    }

    /**
     * Add a profile to the schedule along with a recurring time entry.
     * @param profile The profile to include.
     * @param time The recurring times the profile should activate in the schedule.
     */
    public void addProfile(@NonNull Profile profile, @NonNull RecurringTime time) {
        this.profiles.add(profile);
        this.profileTimes.put(profile.getName(), time);
    }

    public void setProfiles(@NonNull ArrayList<Profile> profiles) {
        this.profiles = profiles;
    }

    /**
     * Remove a profile from the schedule and remove its recurring time entry
     * @param profile The profile to remove.
     */
    public void removeProfile(@NonNull Profile profile) {
        int index = this.profiles.indexOf(profile);
        this.profiles.remove(index);
        this.profileTimes.remove(profile.getName());
    }

    public @NonNull ArrayList<Profile> getProfiles() {
        return this.profiles != null ? this.profiles : new ArrayList<Profile>();
    }

    /**
     * Get all profiles that are currently scheduled.
     * @return An ArrayList of active profiles.
     */
    public @NonNull ArrayList<Profile> getActiveProfiles() {
        ArrayList<Profile> activeProfiles = new ArrayList<Profile>();

        for (Profile profile : this.getProfiles()) {
            RecurringTime profileTime = this.getProfileTimeWithIdentifier(profile.getIdentifier());

            if (profileTime == null) {
                continue;
            }

            Calendar c = Calendar.getInstance();
            long now = c.getTimeInMillis();
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            long passed = now - c.getTimeInMillis();
            long minutesPassed = passed / 1000 / 60;
            int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1; // Sun = 1 (index starting at 1)

            // Check if the minutes index (minutesPassed) is in a recurring time block for today
            Map<Long, Long> times = profileTime.getTimes().get(dayOfWeek);

            for (Long key : times.keySet()) {
                Long duration = times.get(key);

                // Check if contained within the start and start+duration
                if (minutesPassed >= key && minutesPassed <= key+duration) {
                    activeProfiles.add(profile);
                    break;
                }
            }
        }

        return activeProfiles;
    }

    /**
     * Get all scheduled profile times.
     * @return A Map of profile identifiers to a scheduled RecurringTime.
     */
    public @NonNull Map<String, RecurringTime> getProfileTimes() {
        return this.profileTimes;
    }

    /**
     * Get the scheduled time for a profile given its identifier.
     * @param identifier The string identifier of a profile.
     * @return The RecurringTime if the profile is scheduled. Null if not scheduled.
     */
    public @Nullable RecurringTime getProfileTimeWithIdentifier(String identifier) {
        return this.profileTimes.get(identifier);
    }

    /**
     * Set the active state of the schedule.
     * @param flag Whether the profile is active.
     */
    public void setIsActive(boolean flag) {
        this.isActive = flag;
    }

    public boolean getIsActive() {
        return this.isActive;
    }
}
