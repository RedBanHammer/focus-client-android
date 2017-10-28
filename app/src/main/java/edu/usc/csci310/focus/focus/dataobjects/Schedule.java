package edu.usc.csci310.focus.focus.dataobjects;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import edu.usc.csci310.focus.focus.managers.ProfileManager;
import edu.usc.csci310.focus.focus.managers.ScheduleManager;

/**
 * A collection of profiles and recurring times when they should activate.
 */

public class Schedule extends NamedObject {
    private static final long serialVersionUID = 3L;

    private ArrayList<String> profileIdentifiers = new ArrayList<String>();
    private Map<String, RecurringTime> profileTimes = new HashMap<String, RecurringTime>();
    private Boolean isActive = false;

    /**
     * Create a new Schedule object with a list of profiles.
     * @param profileIdentifiers The initial profile identifiers to populate the schedule with.
     * @param name The name of the schedule.
     */
    public Schedule(@NonNull ArrayList<String> profileIdentifiers, @NonNull String name) {
        super(name);
        this.profileIdentifiers = profileIdentifiers;
    }

    public Schedule(@NonNull String name) {
        super(name);
        this.profileIdentifiers = new ArrayList<String>();
    }

    /**
     * Add a profile to the schedule along with a recurring time entry.
     * @param profileIdentifier The identifier of the profile to include.
     * @param time The recurring times the profile should activate in the schedule.
     */
    public void addProfile(@NonNull String profileIdentifier, @NonNull RecurringTime time) {
        if (!this.profileIdentifiers.contains(profileIdentifier)) {
            this.profileIdentifiers.add(profileIdentifier);
            this.profileTimes.put(profileIdentifier, time);
        } else {
            RecurringTime previousTime = this.profileTimes.get(profileIdentifier);
            previousTime.combineWith(time);
        }
    }

    /**
     * Set all profiles contained within the schedule.
     * @param profileIdentifiers An ArrayList of profile identifiers to set in the schedule.
     */
    public void setProfileIdentifiers(@NonNull ArrayList<String> profileIdentifiers) {
        this.profileIdentifiers = profileIdentifiers;
    }

    /**
     * Remove a profile from the schedule and remove its recurring time entry.
     * @param profile The profile to remove.
     */
    public void removeProfile(@NonNull Profile profile) {
        this.removeProfileWithIdentifier(profile.getIdentifier());
    }

    /**
     * Remove a profile from the schedule given its identifier.
     * @param profileIdentifier The string identifier of the profile to remove.
     */
    public void removeProfileWithIdentifier(@NonNull String profileIdentifier) {
        int index = this.profileIdentifiers.indexOf(profileIdentifier);

        if (index >= 0) {
            this.profileIdentifiers.remove(index);
            this.profileTimes.remove(profileIdentifier);
        }
    }

    /**
     * Get all profile identifiers tracked by the schedule.
     * @return An ArrayList of profile string identifiers.
     */
    public @NonNull ArrayList<String> getProfileIdentifiers() {
        return this.profileIdentifiers != null ? this.profileIdentifiers : new ArrayList<String>();
    }

    /**
     * Get all profile identifiers that are currently scheduled.
     * @return An ArrayList of active profile identifier strings.
     */
    public @NonNull ArrayList<String> getActiveProfileIdentifiers() {
        ArrayList<String> activeProfileIdentifiers = new ArrayList<String>();

        for (String profileIdentifier : this.getProfileIdentifiers()) {
            RecurringTime profileTime = this.getProfileTimeWithIdentifier(profileIdentifier);
            Profile profile = ProfileManager.getDefaultManager().getProfileWithIdentifier(profileIdentifier);

            if (profile == null) {
                continue;
            }

//            if (!profile.getIsActive()) {
//                continue;
//            }

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
                    activeProfileIdentifiers.add(profileIdentifier);
                    break;
                }
            }
        }

        return activeProfileIdentifiers;
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
