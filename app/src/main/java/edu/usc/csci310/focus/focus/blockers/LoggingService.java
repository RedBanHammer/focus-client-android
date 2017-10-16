package edu.usc.csci310.focus.focus.blockers;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

import edu.usc.csci310.focus.focus.storage.StorageManager;

/**
 * Component for blockers to perform app entry logging and retrieval.
 */

class LoggingService {
    public static final String sLoggerGroupIdentifier = "log-entries";

    public LoggingService() {

    }

    /**
     * Log an entry to disk.
     * @param entry The entry to log.
     */
    public void logEntry(LogEntry entry) {
        StorageManager manager = StorageManager.getDefaultManager();
        UUID uuid = UUID.randomUUID();

        manager.setObject(entry, sLoggerGroupIdentifier, uuid.toString());
    }

    /**
     * Get previously logged entries.
     * @return An array of LogEntries.
     */
    public @NonNull ArrayList<LogEntry> getLogEntries() {
        StorageManager manager = StorageManager.getDefaultManager();

        ArrayList<Serializable> rawLogs = manager.getObjectsWithPrefix(sLoggerGroupIdentifier);

        ArrayList<LogEntry> logs = new ArrayList<LogEntry>();
        for (Serializable log : rawLogs) {
            logs.add((LogEntry)log);
        }

        return logs;
    }

    public void removeAllLogEntries() {
        StorageManager.getDefaultManager().removeObjectsWithPrefix(sLoggerGroupIdentifier);
    }
}
