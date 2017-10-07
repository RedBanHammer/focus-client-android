package edu.usc.csci310.focus.focus.presentation;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import edu.usc.csci310.focus.focus.R;
import edu.usc.csci310.focus.focus.dataobjects.Profile;
import edu.usc.csci310.focus.focus.dataobjects.Schedule;

/**
 * Created by Briana on 10/6/17.
 */

public class ScheduleListViewAdapter extends ArrayAdapter<Schedule> {
    protected ArrayList<Schedule> schedules;
    protected LayoutInflater inflater;

    public ScheduleListViewAdapter(Activity activity, int textViewResourceId, ArrayList<Schedule> objects){
        super(activity, textViewResourceId, objects);
        schedules= objects;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView ==null){
            convertView = inflater.inflate(R.layout.activity_schedule_list, parent, false);
        }
        Schedule sc = schedules.get(position);
        TextView tv = (TextView)convertView.findViewById(R.id.schedule_list_name);
        tv.setText(sc.getName());
        return convertView;
    }
}