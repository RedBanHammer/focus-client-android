package edu.usc.csci310.focus.focus.presentation.schedule;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

import edu.usc.csci310.focus.focus.R;
import edu.usc.csci310.focus.focus.dataobjects.Profile;
import edu.usc.csci310.focus.focus.dataobjects.Schedule;
import edu.usc.csci310.focus.focus.managers.ScheduleManager;

/**
 * Created by Briana on 10/6/17.
 */

public class ScheduleListViewAdapter extends ArrayAdapter<Schedule> {
    protected ArrayList<Schedule> schedules;

    /**
     * Update the data set and reload the view.
     * @param schedules The new list of schedules to display.
     */
    public void setSchedules(@NonNull ArrayList<Schedule> schedules) {
        this.schedules.clear();

        // Filter schedules to not include timer schedules
        ArrayList<Schedule> filteredSchedules = new ArrayList<Schedule>();
        for (Schedule schedule : schedules) {
            if (schedule.getProfileIdentifiers().size() == 0) {
                filteredSchedules.add(schedule);
            } else {
                String profileName = schedule.getProfileIdentifiers().get(0);

                if (!schedule.getName().equals(profileName + Schedule.TIMER_SCHEDULE_POSTFIX)) {
                    filteredSchedules.add(schedule);
                }
            }
        }

        this.schedules.addAll(filteredSchedules);
        this.notifyDataSetChanged();
    }

    public ScheduleListViewAdapter(Activity activity, int id, ArrayList<Schedule> objects) {
        super(activity, R.layout.schedule_list_item, objects);
        schedules = objects;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        // Get the data item for this position
        Schedule schedule = schedules.get(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder = null; // view lookup cache stored in tag
        //create new row view if null
        if (view == null) {
            // If there's no view to re-use, inflate a brand new view for row
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.schedule_list_item, parent, false);
            viewHolder = new ViewHolder(view);
            // Cache the viewHolder object inside the fresh view
            view.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) view.getTag();
        }
        // Populate the data from the data object via the viewHolder object
        // into the template view.
        viewHolder.scheduleName.setText(schedule.getName());

        //set the toggle button listener
        ToggleButton toggle = (ToggleButton) view.findViewById(R.id.toggle_schedule_button);
        toggle.setOnCheckedChangeListener(null);
        toggle.setChecked(schedule.getIsActive());

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Schedule schedule = schedules.get(position);
                schedule.setIsActive(isChecked);
                ScheduleManager.getDefaultManager().setSchedule(schedule);
            }
        });

        // Return the completed view to render on screen
        return view;
    }

    // View lookup cache that populates the listview
    public class ViewHolder {
        TextView scheduleName;

        //ArrayList of ImageViews w/ app icons
        public ViewHolder(View v) {
            scheduleName = (TextView) v.findViewById(R.id.schedule_list_name);
        }
    }
}