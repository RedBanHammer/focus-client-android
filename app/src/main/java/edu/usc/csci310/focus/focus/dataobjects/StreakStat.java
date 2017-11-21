package edu.usc.csci310.focus.focus.dataobjects;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Holds streak statistics.
 */

public class StreakStat extends NamedObject {
    private static final long serialVersionUID = 1L;

    private Integer count = 0;
    private Calendar timestamp = Calendar.getInstance();

    /**
     * Create a container with a specific name and identifier.
     * @param identifier The string identifier of the profile to hold a reference to.
     */
    public StreakStat(@NonNull String identifier) {
        super("streak-stat", identifier);
        this.count = 0;
        this.timestamp = Calendar.getInstance();
    }

    public StreakStat(@NonNull String identifier, @NonNull Integer count) {
        super("streak-stat", identifier);
        this.count = count;
    }

    public void setCount(@NonNull Integer count) {
        this.count = count;
    }

    public @NonNull Integer getCount() {
        return this.count;
    }

    public void setTimestamp(@NonNull Calendar timestamp) {
        this.timestamp = timestamp;
    }

    public @NonNull Calendar getTimestamp() {
        return this.timestamp;
    }
}
