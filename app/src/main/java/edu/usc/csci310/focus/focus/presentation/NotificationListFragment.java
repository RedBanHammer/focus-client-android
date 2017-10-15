package edu.usc.csci310.focus.focus.presentation;
/*
 * NotificationListFragment Activity that displays list of apps blocked after a blocking period
 */
import android.content.Intent;
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
import java.util.logging.LogManager;

import edu.usc.csci310.focus.focus.R;
import edu.usc.csci310.focus.focus.blockers.LogEntry;
import edu.usc.csci310.focus.focus.blockers.NotificationMetadata;
import edu.usc.csci310.focus.focus.dataobjects.App;
import edu.usc.csci310.focus.focus.dataobjects.Profile;
import edu.usc.csci310.focus.focus.managers.BlockingManager;

public class NotificationListFragment extends Fragment {
    private ListView listView;
    ArrayList<LogEntry> notificationLogEntries = new ArrayList<LogEntry>();
    NotificationListViewAdapter notificationListViewAdapter;

    // newInstance constructor for creating fragment with arguments
    public static NotificationListFragment newInstance(int page, String title) {
        return new NotificationListFragment();
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BlockingManager manager = BlockingManager.getDefaultManager();

        this.notificationLogEntries = manager.getNotificationLogEntries();

        // DEBUG TMP DATA
        NotificationMetadata metadata1 = new NotificationMetadata(null);
        metadata1.title = "You have a new friend request";
        metadata1.text = "Shawn sent you a friend request lol";
        metadata1.icon = "";

        this.notificationLogEntries.add(new LogEntry(
                new App("Facebook", "com.facebook.android"),
                null,
                metadata1,
                LogEntry.LogEntryEventType.NOTIFICATION
        ));


        NotificationMetadata metadata2 = new NotificationMetadata(null);
        metadata2.title = "Shawn updated his story";
        metadata2.text = "He's out frolicking his friends on this balmy Saturday evening";
        metadata2.icon = "";

        this.notificationLogEntries.add(new LogEntry(
                new App("Snapchat", "com.snapchat.android"),
                null,
                metadata2,
                LogEntry.LogEntryEventType.NOTIFICATION
        ));


        NotificationMetadata metadata3 = new NotificationMetadata(null);
        metadata3.title = "Shawn favorited your image";
        metadata3.text = "He really liked the picture of his group working on the group project inside of SAL";
        metadata3.icon = "";

        this.notificationLogEntries.add(new LogEntry(
                new App("Instagram", "com.instagram.android"),
                null,
                metadata3,
                LogEntry.LogEntryEventType.NOTIFICATION
        ));

        //setContentView(R.layout.activity_notification);
    }

    //handle UI events
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_profile_list, container, false);

        // call the views with this layout
        listView = (ListView) v.findViewById(R.id.profileListView);

        notificationListViewAdapter = new NotificationListViewAdapter(getActivity(), 0, notificationLogEntries);
        listView.setAdapter(notificationListViewAdapter);

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> a, View v, int position,
//                                    long id) {
//                Intent intent = new Intent(getActivity(), ProfileInterfaceController.class);
//                startActivityForResult(intent, 0);
//            }
//        });
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
