package edu.usc.csci310.focus.focus.blockers;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import edu.usc.csci310.focus.focus.MainActivity;
import edu.usc.csci310.focus.focus.dataobjects.App;
import edu.usc.csci310.focus.focus.managers.BlockingManager;

/**
 * Block notifications from a set of apps.
 */

public class NotificationBlocker extends IntentService implements Blocker, Logger {
    private LoggingService loggingService = new LoggingService();
//    private NotificationBlockingService blockingService = new NotificationBlockingService();

    public NotificationBlocker(String name) {
        super(name);
    }

    public NotificationBlocker() {
        super("NotificationBlocker");
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        // Set the blocking manager
        BlockingManager.getDefaultManager().setNotificationBlocker(this);

//        this.blockingService.startBlocking();

        this.run();
    }

    private void run() {
        while (true) {
            try {
                if (this.apps.size() > 0) {
                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        mNotificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE);
                    }
                } else {
                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        mNotificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL);
                    }
                }

                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private ArrayList<App> apps = new ArrayList<App>();
    public void setApps(@NonNull ArrayList<App> apps) {
        this.apps = apps;
//        this.blockingService.setApps(apps);
    }

    public void startBlocking() {
//        this.blockingService.stopBlocking();
    }

    public void stopBlocking() {
//        this.blockingService.startBlocking();
    }

    /** Logger interface impl. **/
    public @NonNull ArrayList<LogEntry> getLogEntries() {
//        return this.blockingService.getLogEntries();
        return new ArrayList<LogEntry>();
    }

    public void removeAllLogEntries() {
//        this.blockingService.removeAllLogEntries();
    }
}
