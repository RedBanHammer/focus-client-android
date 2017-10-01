package edu.usc.csci310.focus.focus.presentation;
/*
 * Activity that shows a list of all Schedules
 */
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import edu.usc.csci310.focus.focus.R;

public class ScheduleList extends AppCompatActivity {

    /*
     * Renders the schedule list view
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_list);
    }
}
