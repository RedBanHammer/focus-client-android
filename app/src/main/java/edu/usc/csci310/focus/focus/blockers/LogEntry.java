package edu.usc.csci310.focus.focus.blockers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import edu.usc.csci310.focus.focus.dataobjects.App;
import edu.usc.csci310.focus.focus.dataobjects.Profile;

/**
 * Holds information about an app open or notification block event.
 */

public class LogEntry implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum LogEntryEventType {
        NOTIFICATION,
        OPEN,
    }

    private App app;
    private ArrayList<Profile> profiles = new ArrayList<Profile>();
    private String metadata;
    private LogEntryEventType eventType;
    private Date timestamp;

    public App getApp() {
        return this.app;
    }

    public ArrayList<Profile> getProfiles() {
        return this.profiles;
    }

    public String getMetadata() {
        return this.metadata;
    }

    public LogEntryEventType getEventType() {
        return this.eventType;
    }

    public LogEntry(App app, ArrayList<Profile> profiles, String metadata, LogEntryEventType eventType) {
        this.app = app;
        this.profiles = profiles;
        this.metadata = metadata;
        this.eventType = eventType;

        this.timestamp = new Date();
    }
}
