package edu.usc.csci310.focus.focus.dataobjects;

import android.support.annotation.NonNull;

import java.util.Calendar;

/**
 * Container for achievements earned by the user.
 */

public class AchievementStat extends NamedObject {
    private static final long serialVersionUID = 1L;

    private Calendar timestamp = Calendar.getInstance();

    public AchievementStat(@NonNull String identifier) {
        super("achievement-stat", identifier);
        this.timestamp = Calendar.getInstance();
    }

    public @NonNull Calendar getTimestamp() {
        return this.timestamp;
    }
}
