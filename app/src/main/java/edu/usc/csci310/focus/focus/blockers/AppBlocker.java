package edu.usc.csci310.focus.focus.blockers;

import android.app.ActivityManager;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import edu.usc.csci310.focus.focus.dataobjects.App;

/**
 * Block a set of apps from opening.
 */

public class AppBlocker extends Thread implements Blocker, Logger {
    private ArrayList<App> apps = new ArrayList<App>();

    private Object isBlockingMutex = new Object();
    private boolean isBlocking = false;

    private LoggingService loggingService = new LoggingService();

    private Context context;

    public AppBlocker(Context context) {
        this.context = context;
    }

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
    public void run() {
        super.run();

        while (true) {
            // If blocking, monitor system apps
            synchronized (this.isBlockingMutex) {
                if (this.isBlocking) {
                    String packageName = this.getRecentAppPackageName(this.context);

                    for (App app : this.apps) {
                        if (packageName.equals(app.getIdentifier())) {
                            // Create a log entry
                            LogEntry logEntry = new LogEntry(app, null, null, LogEntry.LogEntryEventType.OPEN);
                            this.loggingService.logEntry(logEntry);

                            // Block the app by bringing Focus into the foreground with info.
                            this.bringToForeground();

                            break;
                        }
                    }
                }
            }

            Thread.yield();
        }
    }

    public void bringToForeground() {
        // TODO: App blocked info screen class
        Intent intent = new Intent(context, SplashScreen.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // You need this if starting
        //  the activity from a service
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        // TODO: startActivity(intent);
    }

    /** Logger interface impl. **/
    public ArrayList<LogEntry> getLogEntries() {
        return this.loggingService.getLogEntries();
    }

    /** Utility **/
    public String getRecentAppPackageName(Context context) {
        String topPackageName = "";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager mUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);

            long time = System.currentTimeMillis();

            UsageEvents usageEvents = mUsageStatsManager.queryEvents(time - 1000 * 30, System.currentTimeMillis() + (10 * 1000));
            UsageEvents.Event event = new UsageEvents.Event();
            while (usageEvents.hasNextEvent()) {
                usageEvents.getNextEvent(event);
            }

            if (event != null && !TextUtils.isEmpty(event.getPackageName()) && event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
//                if (AndroidUtils.isRecentActivity(event.getClassName())) {
//                    return event.getClassName();
//                }
                return event.getPackageName();
            } else {
                topPackageName = "";
            }
        } else {
            ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;

//            if (AndroidUtils.isRecentActivity(componentInfo.getClassName())) {
//                return componentInfo.getClassName();
//            }

            topPackageName = componentInfo.getPackageName();
        }

        return topPackageName;
    }
}
