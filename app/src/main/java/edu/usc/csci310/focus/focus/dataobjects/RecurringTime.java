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
}
