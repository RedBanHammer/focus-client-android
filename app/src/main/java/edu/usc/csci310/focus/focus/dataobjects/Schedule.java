package edu.usc.csci310.focus.focus.dataobjects;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;

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
    public static final String TIMER_SCHEDULE_POSTFIX = "-TIMER";

    private static final long serialVersionUID = 5L;

    private ArrayList<ScheduledProfile> scheduledProfiles = new ArrayList<>();
    private Boolean isActive = false;

    /**
     * Create a new Schedule object with a list of profiles.
     * @param scheduledProfiles The initial profile identifiers to populate the schedule with.
     * @param name The name of the schedule.
     */
    public Schedule(@NonNull ArrayList<ScheduledProfile> scheduledProfiles, @NonNull String name) {
        super(name);
        this.scheduledProfiles = scheduledProfiles;
    }

    public Schedule(@NonNull String name) {
        super(name);
        this.scheduledProfiles = new ArrayList<>();
    }

    /**
     * Add a profile to the schedule along with a recurring time entry.
     * @param profileIdentifier The identifier of the profile to include.
     * @param time The recurring times the profile should activate in the schedule.
     */
    public void addProfile(@NonNull String profileIdentifier, @NonNull RecurringTime time) {
        // Create a new pair to schedule.
        ScheduledProfile scheduledProfile = new ScheduledProfile(profileIdentifier, time);

        scheduledProfiles.add(scheduledProfile);
    }

    /**
     * Set all profiles contained within the schedule.
     * @param scheduledProfiles An ArrayList of profile identifiers to set in the schedule.
     */
    public void setScheduledProfile(@NonNull ArrayList<ScheduledProfile> scheduledProfiles) {
        this.scheduledProfiles = scheduledProfiles;
    }

    /**
     * Remove all scheduled profiles given a specific identifier.
     * @param profileIdentifier The string identifier of the profile to remove.
     */
    public void removeAllProfilesWithIdentifier(@NonNull String profileIdentifier) {
        if (profileIdentifier == null) {
            return;
        }

        ArrayList<ScheduledProfile> newScheduledProfiles = new ArrayList<>();

        for (ScheduledProfile scheduledProfile : scheduledProfiles) {
            if (!scheduledProfile.identifier.equals(profileIdentifier)) {
                newScheduledProfiles.add(scheduledProfile);
            }
        }

        scheduledProfiles = newScheduledProfiles;
    }

    /**
     * Remove a single scheduled profile given its identifier and recurring time.
     * @param profileIdentifier The string identifier of the profile to remove.
     * @param recurringTime A recurring time instance associated with the profile.
     */
    public void removeProfileWithIdentifierRecurringTime(@NonNull String profileIdentifier,
                                                         @NonNull RecurringTime recurringTime) {
        Pair<String, RecurringTime> pair = new Pair<>(profileIdentifier, recurringTime);

        int index = scheduledProfiles.indexOf(pair);
        if (index > -1) {
            removeScheduledProfileAtIndex(index);
        }
    }

    /**
     * Remove a scheduled profile given its index in the scheduled profiles array.
     * @param index The index of the scheduled profile to remove.
     */
    public void removeScheduledProfileAtIndex(@NonNull int index) {
        scheduledProfiles.remove(index);
    }

    /**
     * Get all scheduled profiles tracked by the schedule.
     * @return An ArrayList of scheduled profile pairs (identifier, recurring time).
     */
    public @NonNull ArrayList<ScheduledProfile> getScheduledProfiles() {
        return scheduledProfiles;
    }

    /**
     * Get all unique scheduled profile identifiers.
     * @return An array of the scheduled profile identifier strings.
     */
    public @NonNull ArrayList<String> getScheduledProfileIdentifiers() {
        ArrayList<String> identifiers = new ArrayList<>();

        for (ScheduledProfile scheduledProfile : scheduledProfiles) {
            if (!identifiers.contains(scheduledProfile.identifier)) {
                identifiers.add(scheduledProfile.identifier);
            }
        }

        return identifiers;
    }

    /**
     * Get all profile identifiers that are currently scheduled.
     * @param manager The Profile Manager to use to retrieve profile info.
     * @return An ArrayList of active profile identifier strings.
     */
    public @NonNull ArrayList<String> getActiveProfileIdentifiers(ProfileManager manager) {
        ArrayList<String> activeProfileIdentifiers = new ArrayList<>();

        for (ScheduledProfile scheduledProfile : scheduledProfiles) {
            String profileIdentifier = scheduledProfile.identifier;

            RecurringTime profileTime = scheduledProfile.time;
            Profile profile = manager.getProfileWithIdentifier(profileIdentifier);

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
     * Returns the amount of active time left for a currently scheduled profile.
     * @param identifier The string identifier of the profile.
     * @return An array list of the amount of active times in minutes left for a scheduled profile.
     */
    public @NonNull ArrayList<Long> getTimesRemainingWithProfileIdentifier(@NonNull String identifier) {
        ArrayList<Long> timesRemaining = new ArrayList<>();

        if (identifier == null) {
            return timesRemaining;
        }

        ArrayList<RecurringTime> profileTimes = this.getProfileTimesWithIdentifier(identifier);

        for (RecurringTime profileTime : profileTimes) {
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
                if (minutesPassed >= key && minutesPassed <= key + duration) {
                    timesRemaining.add(new Long(key + duration - minutesPassed));
                } else if (minutesPassed >= key + duration && minutesPassed <= key + duration + 2) {
                    timesRemaining.add(new Long(0));
                }
                else {
                    timesRemaining.add(new Long(-1));
                }
            }
        }

        return timesRemaining;
    }

    /**
     * Get all scheduled times for a profile given its identifier.
     * @param identifier The string identifier of a profile.
     * @return An array of recurring times if the profile is scheduled.
     */
    public @Nullable ArrayList<RecurringTime> getProfileTimesWithIdentifier(String identifier) {
        ArrayList<RecurringTime> times = new ArrayList<>();

        for (ScheduledProfile scheduledProfile : scheduledProfiles) {
            if (scheduledProfile.identifier.equals(identifier)) {
                times.add(scheduledProfile.time);
            }
        }

        return times;
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
