package edu.usc.csci310.focus.focus.presentation;
/*
 * Activity that shows a list of all Schedules
 */
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import edu.usc.csci310.focus.focus.R;
import edu.usc.csci310.focus.focus.dataobjects.Profile;
import edu.usc.csci310.focus.focus.dataobjects.Schedule;

public class ScheduleList extends Fragment {
    public final static String PROFILE_LIST = "edu.usc.csci310.focus.focus.presentation.profile_list";
    private ListView listView;
    ArrayList<Schedule> schedules;
    ArrayList<Profile> profiles;
    FloatingActionButton addScheduleButton;
    ScheduleListViewAdapter scheduleListViewAdapter;
    Intent intent;
    // newInstance constructor for creating fragment with arguments
    public static ScheduleList newInstance(int page, String title) {
        ScheduleList scheduleListFragment = new ScheduleList();
        Bundle args = new Bundle();
//        args.putInt("someInt", page);
//        args.putString("someTitle", title);
        scheduleListFragment.setArguments(args);
        return scheduleListFragment;
    }

    /*
     * Renders the schedule list view
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        profiles = new ArrayList<Profile>();
        profiles.add(new Profile("Profile1"));
        schedules = new ArrayList<Schedule>();
        schedules.add(new Schedule(profiles,"Schedule1"));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // call the views with this layout
        listView = (ListView)getActivity().findViewById(R.id.scheduleListView);

        scheduleListViewAdapter = new ScheduleListViewAdapter(getActivity(), 0, schedules);
        listView.setAdapter(scheduleListViewAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_schedule_list, container, false);

        addScheduleButton = (FloatingActionButton) v.findViewById(R.id.addScheduleButton);

        // add new profile
        addScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getActivity(), CreateScheduleInterfaceController.class);
                intent.putExtra(PROFILE_LIST, profiles);
                startActivityForResult(intent, 0);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position,
                                    long id) {
                intent = new Intent(getActivity(), ScheduleInterfaceController.class);
                startActivityForResult(intent, 0);
            }
        });
        return v;
    }
}
