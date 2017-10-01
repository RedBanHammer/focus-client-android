package edu.usc.csci310.focus.focus.dataobjects;
import java.util.Date;

/**
 * Holds a start time and stop time.
 */

public class TimeInterval {
    public Date start = null;
    public Date end = null;

    public TimeInterval(Date start, Date end) {
        this.start = start;
        this.end = end;
    }
}
