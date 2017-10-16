package edu.usc.csci310.focus.focus.presentation.schedule;
/*
 * Activity that shows a list of all Schedules
 */
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

public class ScheduleList extends Fragment implements CreateScheduleDialog.EditNameDialogListener {
    public final static String PROFILE_LIST = "edu.usc.csci310.focus.focus.presentation.schedule_list";
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /*
     * Renders the schedule list view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_schedule_list, container, false);
        // call the views with this layout
        listView = (ListView)v.findViewById(R.id.scheduleListView);
        profiles = new ArrayList<Profile>();
        profiles.add(new Profile("Profile1"));
        schedules = new ArrayList<Schedule>();
        schedules.add(new Schedule(profiles,"Schedule1"));
        scheduleListViewAdapter = new ScheduleListViewAdapter(getActivity(), 0, schedules);
        listView.setAdapter(scheduleListViewAdapter);
        addScheduleButton = (FloatingActionButton) v.findViewById(R.id.addScheduleButton);

        // create new schedule
        addScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditDialog();
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
    // Call this method to launch the edit dialog
    private void showEditDialog() {
        FragmentManager fm = getFragmentManager();
        CreateScheduleDialog editNameDialogFragment = CreateScheduleDialog.newInstance("new schedule");
        // SETS the target fragment for use later when sending results
        editNameDialogFragment.setTargetFragment(ScheduleList.this, 0);
        editNameDialogFragment.show(fm, "new schedule");
    }

    // This is called when the dialog is completed and the results have been passed
    @Override
    public void onFinishEditDialog(String data) {
        schedules.add(new Schedule(null, data));
        scheduleListViewAdapter.notifyDataSetChanged();
    }


}