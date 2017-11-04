package edu.usc.csci310.focus.focus.dataobjects;

import java.io.Serializable;

/**
 * A serializable version of Pair for profiles contained within a schedule.
 */

public class ScheduledProfile implements Serializable {
    private static final long serialVersionUID = 4L;

    public String identifier;
    public RecurringTime time;

    public ScheduledProfile(String identifier, RecurringTime time) {
        this.identifier = identifier;
        this.time = time;
    }
}
