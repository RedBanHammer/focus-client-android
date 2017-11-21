package edu.usc.csci310.focus.focus.dataobjects;

import android.support.annotation.NonNull;

import java.util.Calendar;

/**
 * Container for achievements earned by the user.
 */

public class AchievementStat extends NamedObject {
    private static final long serialVersionUID = 1L;
    private String name;
    private String description;
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private Calendar timestamp = Calendar.getInstance();

    public AchievementStat(@NonNull String identifier) {
        super("achievement-stat", identifier);
        this.timestamp = Calendar.getInstance();
    }

    public @NonNull Calendar getTimestamp() {
        return this.timestamp;
    }
}
