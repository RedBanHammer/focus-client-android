package edu.usc.csci310.focus.focus.presentation;
/*
 * ScheduleInterfaceController Class
 *
 * Activity that shows an edit ScheduleInterfaceController page
 */
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import edu.usc.csci310.focus.focus.R;

public class ScheduleInterfaceController extends AppCompatActivity {
//  private ScheduleInterfaceController schedule;

    /*
     * renders a schedule edit page
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        getIntent().getSerializableExtra(ScheduleList.PROFILE_LIST);
    }

    /*
     * Renders a daily schedule view
     *
     * @param int - day of the week
     */
    private void renderDailySchedule(int day){

    }

    /*
     * Renders a weekly schedule view
     *
     */
    private void renderWeeklySchedule(){

    }

    /*
     * Returns whether the user selected a profileInterfaceController
     *
     * @param ProfileInterfaceController - profileInterfaceController the user selects
     * @return true if the user selected a profileInterfaceController; otherwise, false
     */
    private boolean didSelectProfile(ProfileInterfaceController profileInterfaceController){
        return false;
    }

    /*
     * Returns whether the user enabled the schedule
     *
     * @return true if user enabled the schedule; otherwise, false
     */
    private boolean didToggleActiveState(boolean active){
        return false;
    }
}
