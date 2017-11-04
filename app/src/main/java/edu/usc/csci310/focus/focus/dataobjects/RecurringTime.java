package edu.usc.csci310.focus.focus.dataobjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * An object holding a recurring set of times during the week.
 */

public class RecurringTime implements Serializable {
    private static final long serialVersionUID = 1L;

    private ArrayList<Map<Long, Long>> times;

    public RecurringTime() {
        this.times = new ArrayList<Map<Long, Long>>();
        for (int i = 0; i < 7; i++) {
            this.times.add(new HashMap<Long, Long>());
        }
    }

    public void combineWith(RecurringTime other) {
        ArrayList<Map<Long, Long>> otherTimes = other.getTimes();

        for (int i = 0; i < Math.min(this.times.size(), otherTimes.size()); i++) {
            HashMap<Long, Long> otherTimesDaily = (HashMap) otherTimes.get(i);
            HashMap<Long, Long> ourTimesDaily = (HashMap) this.times.get(i);

            // Need to check for overlaps
            for (Long otherStartTime : otherTimesDaily.keySet()) {
                Long otherDuration = otherTimesDaily.get(otherStartTime);
                Long otherEndTime = otherStartTime + otherDuration;

                boolean hasOverlap = false;
                for (Long ourStartTime : ourTimesDaily.keySet()) {
                    Long ourEndTime = ourStartTime + ourTimesDaily.get(ourStartTime);

                    if (otherStartTime >= ourStartTime && otherStartTime <= ourEndTime) {
                        // Other starts somewhere in our time block
                        ourTimesDaily.put(ourStartTime, Math.max(otherEndTime, ourEndTime) - ourStartTime);
                        hasOverlap = true;
                        break;
                    } else if (otherEndTime >= ourStartTime && otherEndTime <= ourEndTime) {
                        // Other ends somewhere in our time block
                        // Remove previous start
                        ourTimesDaily.remove(ourStartTime);

                        Long combinedStartTime = Math.min(ourStartTime, otherStartTime);
                        ourTimesDaily.put(combinedStartTime, Math.max(otherEndTime, ourEndTime) - combinedStartTime);
                        hasOverlap = true;
                        break;
                    }
                }

                if (!hasOverlap) {
                    // Add in normally, no overlap
                    ourTimesDaily.put(otherStartTime, otherDuration);
                }
            }

            // Push to times array
            this.times.set(i, ourTimesDaily);
        }
    }

    /**
     * Add a time block to a day in the week.
     * @param dayIndex The day of the week to add the time to.
     * @param minuteIndex The time in minutes when the time block starts.
     * @param duration The duration of the time block.
     */
    public void addTime(Integer dayIndex, Long minuteIndex, Long duration) {
        Map<Long, Long> data = this.times.get(dayIndex);
        if (data == null) {
            data = new HashMap<Long, Long>();
        }
        data.put(minuteIndex, duration);
        this.times.set(dayIndex, data);
    }

    /**
     * Get all times for all days of the week.
     * @return An array of times corresponding to the day of the week and scheduled blocks.
     */
    public ArrayList<Map<Long, Long>> getTimes() {
        return this.times;
    }

    /**
     * Remove all times for a day of the week.
     * @param dayIndex The day of the week to remove times at.
     */
    public void removeTimes(Integer dayIndex) {
        this.times.set(dayIndex, new HashMap<Long, Long>());
    }

    /**
     * Remove a time starting at a specific minute on a specific day of the week.
     * @param dayIndex The day of the week.
     * @param minuteIndex The specific starting minute of the time block.
     */
    public void removeTime(Integer dayIndex, Long minuteIndex) {
        this.times.get(dayIndex).remove(minuteIndex);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof RecurringTime)) {
            return false;
        }

        RecurringTime other = (RecurringTime)obj;
        return (other.getTimes().equals(this.getTimes()));
    }

    @Override
    public int hashCode() {
        return this.getTimes().hashCode();
    }
}
