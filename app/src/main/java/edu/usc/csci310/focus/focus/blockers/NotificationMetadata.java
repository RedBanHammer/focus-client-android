package edu.usc.csci310.focus.focus.blockers;

import android.app.Notification;
import android.os.Bundle;

import java.io.Serializable;

/**
 * Allows notification extra information to be serialized.
 */

public class NotificationMetadata implements Serializable {
    private static final long serialVersionUID = 1L;

    public String title;
    public String text;
    public String icon;

    public NotificationMetadata(Bundle bundle) {
        if (bundle != null) {
            this.title = bundle.getString(Notification.EXTRA_TITLE);
            this.text = bundle.getString(Notification.EXTRA_TEXT);
            this.icon = bundle.getString(Notification.EXTRA_LARGE_ICON);
        }
    }
}
