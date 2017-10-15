package edu.usc.csci310.focus.focus.blockers;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import java.io.Serializable;
import java.util.ArrayList;

import edu.usc.csci310.focus.focus.dataobjects.App;

/**
 * Block notifications from a set of apps.
 */

public class NotificationBlockingService extends NotificationListenerService implements Blocker, Logger {
    private ArrayList<App> apps = new ArrayList<App>();

    private Object isBlockingMutex = new Object();
    private boolean isBlocking = false;

    private LoggingService loggingService = new LoggingService();

    public Context context;

    public void setApps(ArrayList<App> apps) {
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
        return super.onBind(intent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification notification) {
        if (!this.isBlocking) {
            return;
        }

        String packageName = notification.getPackageName();
        App matchedApp = this.getAppWithPackageName(packageName);

        if (matchedApp != null) {
            Notification mNotification = notification.getNotification();
            Bundle extras = mNotification.extras;
            NotificationMetadata metadata = new NotificationMetadata(extras);

            // Create a log entry
            LogEntry logEntry = new LogEntry(matchedApp, null, metadata, LogEntry.LogEntryEventType.NOTIFICATION);
            this.loggingService.logEntry(logEntry);

            // Remove the notification
            this.cancelNotification(notification.getPackageName(), notification.getTag(), notification.getId());
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification notification){
        // TODO: Confirm that the notification was successfully removed here.
    }


    /** Logger interface impl. **/
    public ArrayList<LogEntry> getLogEntries() {
        return this.loggingService.getLogEntries();
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
