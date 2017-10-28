package edu.usc.csci310.focus.focus.blockers;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import edu.usc.csci310.focus.focus.dataobjects.App;
import edu.usc.csci310.focus.focus.managers.BlockingManager;

/**
 * Block notifications from a set of apps.
 */

public class NotificationBlocker extends NotificationListenerService implements Blocker {
    private ArrayList<App> apps = new ArrayList<App>();

    private Object isBlockingMutex = new Object();
    private boolean isBlocking = false;

    private static NotificationBlocker sharedNotificationBlocker = null;

    public static NotificationBlocker getSharedNotificationBlocker() {
        return sharedNotificationBlocker;
    }

    public void setApps(@NonNull ArrayList<App> apps) {
        this.apps = apps;
    }

    public void startBlocking() {
        synchronized (this.isBlockingMutex) {
            this.isBlocking = true;
        }
    }

    public void stopBlocking() {
        synchronized (this.isBlockingMutex) {
            this.isBlocking = false;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("Got onBind from intent");

        // Add to blocking manager
        if (BlockingManager.getDefaultManager() != null) {
            BlockingManager.getDefaultManager().setNotificationBlocker(this);
        } else {
            sharedNotificationBlocker = this;
        }

        return super.onBind(intent);
    }

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();

        System.out.println("Got listener connected");
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
//        if (!this.isBlocking) {
//            return;
//        }

        System.out.println("Got notification posted: " + sbn.getPackageName());

        String packageName = sbn.getPackageName();
        App matchedApp = this.getAppWithPackageName(packageName);

        if (matchedApp != null) {
            Notification mNotification = sbn.getNotification();
            Bundle extras = mNotification.extras;
            NotificationMetadata metadata = new NotificationMetadata(extras);

            // Create a log entry
            LogEntry logEntry = new LogEntry(matchedApp, null, metadata, LogEntry.LogEntryEventType.NOTIFICATION);
            LoggingService.logEntry(logEntry);

            System.out.println("Blocked notification: " + sbn.getPackageName());

            // Remove the notification
            // DRAGON: Doesn't work
            this.cancelNotification(sbn.getPackageName(), sbn.getTag(), sbn.getId());
            this.cancelNotification(sbn.getKey());
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn){
        super.onNotificationRemoved(sbn);
        // TODO: Confirm that the notification was successfully removed here.
    }


    /** Logger interface impl. **/
    public static @NonNull ArrayList<LogEntry> getLogEntries() {
        ArrayList<LogEntry> logEntries = LoggingService.getLogEntries();
        ArrayList<LogEntry> filteredLogEntries = new ArrayList<LogEntry>();

        for (LogEntry entry : logEntries) {
            if (entry.getEventType() == LogEntry.LogEntryEventType.NOTIFICATION) {
                filteredLogEntries.add(entry);
            }
        }

        return filteredLogEntries;
    }

    public static void removeAllLogEntries() {
        LoggingService.removeAllLogEntries();
    }


    /** Utility **/
    /**
     * Get the app from the blocked `apps` set given a package name.
     * @param packageName The package name string to match against.
     * @return The app object if it exists in `apps`. Null if it does not match.
     */
    private App getAppWithPackageName(String packageName) {
        App matched = null;

        for (App app : this.apps) {
            if (app.getIdentifier().equals(packageName)) {
                matched = app;
                break;
            }
        }

        return matched;
    }
}
