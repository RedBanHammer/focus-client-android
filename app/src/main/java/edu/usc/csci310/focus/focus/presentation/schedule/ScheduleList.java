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
import edu.usc.csci310.focus.focus.managers.ProfileManager;
import edu.usc.csci310.focus.focus.managers.ScheduleManager;

public class ScheduleList extends Fragment implements CreateScheduleDialog.EditNameDialogListener {
    public final static String SCHEDULE_LIST = "edu.usc.csci310.focus.focus.presentation.schedule_list";
    public final static String PROFILE_LIST = "edu.usc.csci310.focus.focus.presentation.profile_list";
    public final static String SCHEDULE_LIST_ITEM = "edu.usc.csci310.focus.focus.presentation.schedule_list_item";
    public final static String PROFILES = "edu.usc.csci310.focus.focus.presentation.profiles";

    private ListView listView;
    ArrayList<Schedule> schedules;
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
//        schedules = ScheduleManager.getDefaultManager().getAllSchedules();
        this.updateSchedules();
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
        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.activity_schedule_list, container, false);
        // call the views with this layout
        listView = (ListView)v.findViewById(R.id.scheduleListView);

        this.scheduleListViewAdapter = new ScheduleListViewAdapter(getActivity(), 0, this.schedules);
        listView.setAdapter(scheduleListViewAdapter);
        this.addScheduleButton = (FloatingActionButton) v.findViewById(R.id.addScheduleButton);

        // create new schedule
        this.addScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditDialog();
            }
        });
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position,
                                    long id) {
                intent = new Intent(getActivity(), ScheduleInterfaceController.class);
                intent.putExtra(SCHEDULE_LIST_ITEM, schedules.get(position));
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
    public void onFinishEditDialog(String name) {
        Schedule createdSchedule = new Schedule(name);
        schedules.add(createdSchedule);
        ScheduleManager.getDefaultManager().setSchedule(createdSchedule);

        scheduleListViewAdapter.setSchedules(schedules);
    }

    @Override
    public void onResume() {
        super.onResume();

        this.updateSchedules();
    }

    /**
     * Update the internal schedule list by pulling it from the Schedule Manager
     */
    private void updateSchedules() {
        ArrayList<Schedule> newSchedules = ScheduleManager.getDefaultManager().getAllSchedules();

        // Filter schedules to not include timer schedules
        ArrayList<Schedule> filteredSchedules = new ArrayList<Schedule>();
        for (Schedule schedule : newSchedules) {
            ArrayList<String> identifiers = schedule.getScheduledProfileIdentifiers();
            if (identifiers.size() == 0) {
                filteredSchedules.add(schedule);
            } else {
                String profileName = identifiers.get(0);

                if (!schedule.getName().equals(profileName + Schedule.TIMER_SCHEDULE_POSTFIX)) {
                    filteredSchedules.add(schedule);
                }
            }
        }

        if (this.scheduleListViewAdapter != null) {
            this.scheduleListViewAdapter.setSchedules(filteredSchedules);
        }

        this.schedules = filteredSchedules;
    }
}
