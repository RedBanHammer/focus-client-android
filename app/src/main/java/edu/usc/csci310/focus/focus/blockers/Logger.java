package edu.usc.csci310.focus.focus.blockers;

import java.util.ArrayList;

/**
 * Interface for retrieving logs.
 */

interface Logger {
    public ArrayList<LogEntry> getLogEntries();
}
