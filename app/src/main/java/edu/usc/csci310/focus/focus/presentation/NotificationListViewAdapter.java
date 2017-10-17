package edu.usc.csci310.focus.focus.presentation;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.usc.csci310.focus.focus.R;
import edu.usc.csci310.focus.focus.blockers.LogEntry;
import edu.usc.csci310.focus.focus.blockers.NotificationMetadata;
import edu.usc.csci310.focus.focus.dataobjects.Profile;

/**
 * Controls rendering and display of notification list elements.
 */

class NotificationListViewAdapter extends ArrayAdapter<LogEntry> {
    protected ArrayList<LogEntry> logEntries;
    private Activity activity;

    public NotificationListViewAdapter(Activity activity, int id, ArrayList<LogEntry> objects) {
        super(activity, 0, objects);
        this.activity = activity;
        this.logEntries = objects;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        // Get the data item for this position
        LogEntry logEntry = logEntries.get(position);

        // Check if an existing view is being reused, otherwise inflate the view
        NotificationListViewAdapter.ViewHolder viewHolder = null; // view lookup cache stored in tag
        //create new row view if null
        if (view == null) {
            // If there's no view to re-use, inflate a brand new view for row
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.notification_list_item, parent, false);
            viewHolder = new NotificationListViewAdapter.ViewHolder(view);
            // Cache the viewHolder object inside the fresh view
            view.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (NotificationListViewAdapter.ViewHolder) view.getTag();
        }
        // Populate the data from the data object via the viewHolder object
        // into the template view.
        NotificationMetadata metadata = (NotificationMetadata)logEntry.getMetadata();

        if (metadata != null) {
            viewHolder.notificationTime.setText(logEntry.getTimestamp().toString() + " (" + logEntry.getApp().getName() + ")");
            viewHolder.notificationTitle.setText(metadata.title);
            viewHolder.notificationText.setText(metadata.text);
        } else {
            viewHolder.notificationTime.setText(logEntry.getTimestamp().toString());
            viewHolder.notificationTitle.setText("Blocked " + logEntry.getApp().getName());
            viewHolder.notificationText.setText(logEntry.getApp().getName() + " was blocked.");
        }
        //get icon from android system
        Drawable appIcon = null;
        try {
            appIcon = activity.getPackageManager().getApplicationIcon(logEntry.getApp().getIdentifier());
        } catch (PackageManager.NameNotFoundException e) {
            //yikes
        }
        viewHolder.notificationIcon.setImageDrawable(appIcon);

        // Return the completed view to render on screen
        return view;
    }

    // View lookup cache that populates the listview
    public class ViewHolder {
        TextView notificationTime;
        TextView notificationTitle;
        TextView notificationText;
        ImageView notificationIcon;


        // Notification UI elements
        public ViewHolder (View v){
            notificationTime = (TextView)v.findViewById(R.id.notification_time);
            notificationTitle = (TextView)v.findViewById(R.id.notification_title);
            notificationText = (TextView)v.findViewById(R.id.notification_text);
            notificationIcon = (ImageView)v.findViewById(R.id.notification_icon);
        }
    }
}
