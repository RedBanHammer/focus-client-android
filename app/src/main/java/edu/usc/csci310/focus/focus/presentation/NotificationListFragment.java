package edu.usc.csci310.focus.focus.presentation;
/*
 * NotificationListFragment Activity that displays list of apps blocked after a blocking period
 */
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.LogManager;

import edu.usc.csci310.focus.focus.MainActivity;
import edu.usc.csci310.focus.focus.R;
import edu.usc.csci310.focus.focus.blockers.LogEntry;
import edu.usc.csci310.focus.focus.blockers.NotificationMetadata;
import edu.usc.csci310.focus.focus.dataobjects.App;
import edu.usc.csci310.focus.focus.dataobjects.Profile;
import edu.usc.csci310.focus.focus.managers.BlockingManager;
import edu.usc.csci310.focus.focus.managers.BlockingManagerLogEntryDelegate;

import static android.R.drawable.ic_menu_delete;

public class NotificationListFragment extends Fragment implements BlockingManagerLogEntryDelegate {
    private ListView listView;
    ArrayList<LogEntry> notificationLogEntries = new ArrayList<LogEntry>();
    NotificationListViewAdapter notificationListViewAdapter;
    FloatingActionButton clearNotificationsButton;

    // newInstance constructor for creating fragment with arguments
    public static NotificationListFragment newInstance(int page, String title) {
        return new NotificationListFragment();
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BlockingManager.getDefaultManager().setLogEntryDelegate(this);
    }

    private void updateLogEntries() {
        BlockingManager manager = BlockingManager.getDefaultManager();
        this.notificationLogEntries = manager.getNotificationLogEntries();
        Collections.reverse(this.notificationLogEntries); // Make reverse chronological
    }

    public void render() {
        this.updateLogEntries();

        this.notificationListViewAdapter.updateLogEntries(this.notificationLogEntries);
    }

    @Override
    public void onResume() {
        super.onResume();

        this.render();
    }

    public void blockingManagerDidUpdateLogEntries(BlockingManager blockingManager) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                render();
            }
        });
    }

    //handle UI events
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_profile_list, container, false);

        // call the views with this layout
        this.listView = (ListView) v.findViewById(R.id.profileListView);

        this.updateLogEntries();

        this.notificationListViewAdapter = new NotificationListViewAdapter(getActivity(), 0, this.notificationLogEntries);
        this.listView.setAdapter(this.notificationListViewAdapter);

        this.clearNotificationsButton = (FloatingActionButton) v.findViewById(R.id.addProfileButton);
        this.clearNotificationsButton.setImageResource(android.R.drawable.ic_menu_delete);
        this.clearNotificationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //clear notifications in storage
                BlockingManager.getDefaultManager().clearAllNotificationLogEntries();

                //clear notifications on the view
                notificationLogEntries.clear();
                notificationListViewAdapter.notifyDataSetChanged();
            }
        });

        return v;
    }

    /** Utility **/
    private ArrayList<LogEntry> filterLogEntries(ArrayList<LogEntry> logEntries) {
        ArrayList<LogEntry> filtered = new ArrayList<LogEntry>();

        for (LogEntry entry : logEntries) {
            if (entry.getEventType() == LogEntry.LogEntryEventType.NOTIFICATION) {
                filtered.add(entry);
            }
        }

        return filtered;
    }
}
