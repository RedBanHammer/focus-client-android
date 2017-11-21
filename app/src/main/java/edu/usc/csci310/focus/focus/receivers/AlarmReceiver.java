package edu.usc.csci310.focus.focus.receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by bdeng on 11/20/17.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        //Toast.makeText(context, "Repeating Alarm worked.", Toast.LENGTH_LONG).show();

        Intent intent1 = new Intent(context, NotificationIntentService.class);
        context.startService(intent1);

    }
}

