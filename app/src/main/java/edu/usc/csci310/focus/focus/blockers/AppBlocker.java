package edu.usc.csci310.focus.focus.blockers;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import edu.usc.csci310.focus.focus.MainActivity;
import edu.usc.csci310.focus.focus.dataobjects.App;
import edu.usc.csci310.focus.focus.dataobjects.Profile;
import edu.usc.csci310.focus.focus.managers.BlockingManager;
import edu.usc.csci310.focus.focus.managers.ProfileManager;
import edu.usc.csci310.focus.focus.presentation.SplashScreen;

/**
 * Block a set of apps from opening.
 */

public class AppBlocker extends IntentService implements Blocker {
    private HashSet<App> apps = new HashSet<App>();

    private Object isBlockingMutex = new Object();
    private boolean isBlocking = false;

    public AppBlocker(String name) {
        super(name);
    }

    public AppBlocker() {
        super("AppBlocker");
    }


    @Override
    protected void onHandleIntent(Intent workIntent) {
        // Set the blocking manager
        BlockingManager.getDefaultManager().setAppBlocker(this);

        this.run();
    }

    public void setApps(@NonNull HashSet<App> apps) {
        this.apps = apps;
    }

    public void startBlocking() {
        System.out.println("Going to set blocking to true for app blocker");
        synchronized (this.isBlockingMutex) {
            this.isBlocking = true;
            System.out.println("Set blocking to true for app blocker");
        }
    }

    public void stopBlocking() {
        synchronized (this.isBlockingMutex) {
            this.isBlocking = false;
        }
    }

    public void run() {
        while (true) {
            // If blocking, monitor system apps
//            synchronized (this.isBlockingMutex) {
//                if (this.isBlocking) {
                    String packageName = this.getRecentAppPackageName(this);

                    for (App app : this.apps) {
                        if (packageName.equals(app.getIdentifier())) {
                            // Create a log entry
                            LogEntry logEntry = new LogEntry(app, null, null, LogEntry.LogEntryEventType.OPEN);
                            LoggingService.logEntry(logEntry);

                            // Block the app by bringing Focus into the foreground with info.
                            this.bringToForeground(app);

                            break;
                        }
                    }

//                }
//            }

//            Thread.yield();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void bringToForeground(App app) {
        Intent intent = new Intent(MainActivity.mainActivityContext, SplashScreen.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // You need this if starting
        //  the activity from a service
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.putExtra("appName", app.getName());

        MainActivity.mainActivityContext.startActivity(intent);
    }

    /** Logger interface impl. **/
    public static ArrayList<LogEntry> getLogEntries() {
        ArrayList<LogEntry> logEntries = LoggingService.getLogEntries();
        ArrayList<LogEntry> filteredLogEntries = new ArrayList<LogEntry>();

        for (LogEntry entry : logEntries) {
            if (entry.getEventType() == LogEntry.LogEntryEventType.OPEN) {
                filteredLogEntries.add(entry);
            }
        }

        return filteredLogEntries;
    }

    public static void removeAllLogEntries() {
        LoggingService.removeAllLogEntries();
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
