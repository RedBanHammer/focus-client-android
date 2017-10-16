package edu.usc.csci310.focus.focus.blockers;

import android.os.Bundle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

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
    private Serializable metadata;
    private LogEntryEventType eventType;
    private Date timestamp;
    private String identifier;

    public App getApp() {
        return this.app;
    }

    public ArrayList<Profile> getProfiles() {
        return this.profiles;
    }

    public Serializable getMetadata() {
        return this.metadata;
    }

    public LogEntryEventType getEventType() {
        return this.eventType;
    }

    public Date getTimestamp() {
        return this.timestamp;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public LogEntry(App app, ArrayList<Profile> profiles, Serializable metadata, LogEntryEventType eventType) {
        this.app = app;
        this.profiles = profiles;
        this.metadata = metadata;
        this.eventType = eventType;

        this.timestamp = new Date();
        this.identifier = (UUID.randomUUID()).toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof LogEntry)) {
            return false;
        }

        LogEntry other = (LogEntry)obj;
        return (other.getIdentifier().equals(this.getIdentifier()));
    }
}
