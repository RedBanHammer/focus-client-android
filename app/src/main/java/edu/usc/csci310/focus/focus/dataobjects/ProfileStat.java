package edu.usc.csci310.focus.focus.dataobjects;

import android.support.annotation.NonNull;

import java.util.Calendar;
import java.util.HashMap;

/**
 * Holds statistics for profiles. The identifier of a profile stat object matches the
 * identifier of the profile it holds statistics for.
 */

public class ProfileStat extends NamedObject {
    private static final long serialVersionUID = 1L;

    // Map of start time to duration (in min) of time intervals where the user didn't
    // open any apps in the profile
    HashMap<Calendar, Long> focusedIntervals = new HashMap<>();

    /**
     * Create a container with a specific name and identifier.
     * @param identifier The string identifier of the profile to hold a reference to.
     */
    public ProfileStat(String identifier) {
        super("profile-stats", identifier);
    }


    /**
     * Add a new focused interval to the internal map.
     * @param startTime
     * @param duration
     */
    public void addFocusedInterval(@NonNull Calendar startTime, @NonNull Long duration) {
        focusedIntervals.put(startTime, duration);
    }

    /**
     * Get the map of time intervals where the user didn't open any apps in the profile.
     * @return
     */
    public HashMap<Calendar, Long> getFocusedIntervals() {
        return focusedIntervals;
    }

    /**
     * Get the map of time intervals where the user didn't open any apps in the profile within a
     * specific time interval.
     * @param startTime
     * @param duration
     * @return
     */
    public HashMap<Calendar, Long> getFocusedIntervalsInInterval(Calendar startTime, Long duration) {
        HashMap<Calendar, Long> filteredFocusedIntervals = new HashMap<>();

        Calendar endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.MINUTE, duration.intValue());

        for (Calendar intervalStart : focusedIntervals.keySet()) {
            Long intervalDuration = focusedIntervals.get(intervalStart);
            // DRAGON: UI queries for intervals *started* within a time frame, rather than intervals
            // that fit within a time frame.
//            Calendar intervalEnd = (Calendar)intervalStart.clone();
//            intervalEnd.add(Calendar.MINUTE, intervalDuration.intValue());

            if (startTime.compareTo(intervalStart) <= 0 && endTime.compareTo(intervalStart) >= 0) {
                // Interval time is within this focused interval.
                filteredFocusedIntervals.put(intervalStart, intervalDuration);
            }

        }

        return filteredFocusedIntervals;
    }

    /**
     * Called when the user opens a blocked app within the profile to remove a previously-created
     * focused interval.
     */
    public void invalidateFocusInterval() {
        // Go through focusedIntervals and check if the current calendar matches.
        Calendar now = Calendar.getInstance();

        HashMap<Calendar, Long> tmpFocusedIntervals = new HashMap<>();

        for (Calendar intervalStart : focusedIntervals.keySet()) {
            Long duration = focusedIntervals.get(intervalStart);
            Calendar intervalEnd = (Calendar) intervalStart.clone();
            intervalEnd.add(Calendar.MINUTE, duration.intValue());

            if (now.compareTo(intervalStart) >= 0 && now.compareTo(intervalEnd) <= 0) {
                // Current time is within this focused interval.
                continue;
            }

            tmpFocusedIntervals.put(intervalStart, duration);
        }

        focusedIntervals = tmpFocusedIntervals;
    }
}
