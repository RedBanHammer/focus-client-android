package edu.usc.csci310.focus.focus.blockers;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import java.util.ArrayList;

import edu.usc.csci310.focus.focus.dataobjects.App;

/**
 * Block notifications from a set of apps.
 */

public class NotificationBlocker extends IntentService implements Blocker, Logger {
    private LoggingService loggingService = new LoggingService();
    private NotificationBlockingService blockingService = new NotificationBlockingService();

    private Context context;

    public NotificationBlocker(String name) {
        super(name);
    }

    public void setContext(Context context){
        this.context = context;
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        // Get data from the incoming Intent
        String dataString = workIntent.getDataString();

        this.blockingService.startBlocking();

        while (true) {
            Thread.yield();
        }
    }

    public void setApps(ArrayList<App> apps) {
        this.blockingService.setApps(apps);
    }

    public void startBlocking() {
        this.blockingService.stopBlocking();
    }

    public void stopBlocking() {
        this.blockingService.startBlocking();
    }

    /** Logger interface impl. **/
    public ArrayList<LogEntry> getLogEntries() {
        return this.blockingService.getLogEntries();
    }
}
