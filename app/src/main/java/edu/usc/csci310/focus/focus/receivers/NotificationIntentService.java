package edu.usc.csci310.focus.focus.receivers;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;

import edu.usc.csci310.focus.focus.MainActivity;
import edu.usc.csci310.focus.focus.R;

/**
 * Created by bdeng on 11/20/17.
 */

public class NotificationIntentService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    private final String channelId = "csci310-focus";
    private final int mNotificationId = 2;

    public NotificationIntentService() {
        super(" ");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Notification.Builder builder = null;
        if (android.os.Build.VERSION.SDK_INT >= 26) {
            builder = new Notification.Builder(this, channelId);
        }
        else
        {
            builder = new Notification.Builder(this);
        }
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        builder.setContentTitle("Weekly Usage");
        builder.setContentText("View your usage stats for this week");
        builder.setSmallIcon(R.drawable.ic_focus);
        Intent onClickIntent = new Intent(this, MainActivity.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        onClickIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        builder.setContentIntent(resultPendingIntent);
        notificationManager.notify(mNotificationId, builder.build());
    }
}
