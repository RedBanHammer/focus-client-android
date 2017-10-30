package edu.usc.csci310.focus.focus.presentation.schedule;
/*
 * ScheduleInterfaceController Class
 *
 * Activity that shows an edit ScheduleInterfaceController page
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import edu.usc.csci310.focus.focus.R;
import edu.usc.csci310.focus.focus.dataobjects.Profile;
import edu.usc.csci310.focus.focus.dataobjects.RecurringTime;
import edu.usc.csci310.focus.focus.dataobjects.Schedule;
import edu.usc.csci310.focus.focus.managers.ProfileManager;
import edu.usc.csci310.focus.focus.managers.ScheduleManager;
import edu.usc.csci310.focus.focus.presentation.ProfileInterfaceController;

public class ScheduleInterfaceController extends AppCompatActivity implements WeekView.EventClickListener, MonthLoader.MonthChangeListener, WeekView.EventLongPressListener, WeekView.EmptyViewLongPressListener {
    public static final String PROFILE_TIME = "profile_time";
    public static final String PROFILE_NAME = "profile_name";
    public static final String START_TIME = "start_time";
    public static final String END_TIME = "end_time";
    public static final String PROFILE_ID = "PROFILE_ID";
    private static final int TYPE_DAY_VIEW = 1;
    private static final int TYPE_THREE_DAY_VIEW = 2;
    private static final int TYPE_WEEK_VIEW = 3;
    private static final String TITLE="Edit Schedule Name";
    public static final String SCHEDULE = "scheduleddd";
    private int mWeekViewType = TYPE_THREE_DAY_VIEW;
    private WeekView mWeekView;
    private TextView scheduleName;
    private Schedule schedule;
    private Button editNameButton;
    private Dialog editNameDialog;
    private Button deleteButton;
    private EditText text;
    private Button posButton;
    private Button negButton;

    private List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
    private Map<Long, String> mapID = new HashMap<Long, String>();

    /*
     * renders a schedule page
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        Intent i = getIntent();
        this.schedule = (Schedule) i.getSerializableExtra(ScheduleList.SCHEDULE_LIST_ITEM);


        // Get a reference for the week view in the layout.
        mWeekView = (WeekView) findViewById(R.id.weekView);

        // Get reference to schedule name.
        scheduleName = (TextView) findViewById(R.id.schedule_name);
        scheduleName.setText(schedule.getName());

        // Show a toast message about the touched event.
        mWeekView.setOnEventClickListener(this);
        mWeekView.setMonthChangeListener(this);

        // Set long press listener for events.
        mWeekView.setEventLongPressListener(this);

        // Set long press listener for empty view
        mWeekView.setEmptyViewLongPressListener(this);

        // Set up a date time interpreter to interpret how the date and time will be formatted in
        // the week view. This is optional.
        setupDateTimeInterpreter(false);


        this.initializeEditNameButton();
        this.initializeDeleteButton();
        //populate events list with WeekViewEvents to be displayed in the calender

        this.populateEventsList(schedule);
    }

    private void initializeEditNameButton() {
        this.editNameButton = (Button) findViewById(R.id.edit_schedule_name_button);
        this.editNameButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                editNameDialog = new Dialog(ScheduleInterfaceController.this);
                editNameDialog.setContentView(R.layout.edit_profile_name);
                editNameDialog.setTitle(TITLE);
                text = (EditText) editNameDialog.findViewById(R.id.newNameText);
                posButton = (Button) editNameDialog.findViewById(R.id.positiveButton);
                posButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newName = text.getText().toString();
                        editNameDialog.dismiss();
                        didFinishEditingScheduleName(newName);
                    }
                });

                Button negButton = (Button) editNameDialog.findViewById(R.id.negativeButton);
                negButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //dismiss dialog
                        editNameDialog.dismiss();

                    }
                });
                editNameDialog.show();
            }

        });
    }

    private void didFinishEditingScheduleName(String newName) {
        if (newName.equals("")) {
            // Don't accept blank names, show an error
            this.showInvalidNameError();
            return;
        }


        //send new name to schedule manager
        schedule.setName(newName);
        ScheduleManager.getDefaultManager().setSchedule(schedule);

        //exit the dialog, reload the name
        scheduleName.setText(newName);
    }

    private void showInvalidNameError() {
        new AlertDialog.Builder(this)
                .setTitle("Invalid Name")
                .setMessage("You cannot enter a blank name for a schedule.")
                .setPositiveButton("Okay", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which) {

                    }

                })
                .show();
    }

    private void initializeDeleteButton() {
        this.deleteButton = (Button) findViewById(R.id.delete_schedule_button);
        this.deleteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ScheduleInterfaceController.this)
                        .setTitle("Confirm Delete")
                        .setMessage("Are you sure you want to delete this schedule? This action cannot be undone.")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which) {
                                // Delete the schedule
                                ScheduleManager.getDefaultManager().removeSchedule(schedule);
                                // Close activity
                                finish();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which) {
                                // User canceled deleting
                            }
                        })
                        .show();
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.schedule_main, menu);
        return true;
    }
    //opens add ProfileToSchedule activity
    private void openAddProfileActivity(){
        Intent i = new Intent(ScheduleInterfaceController.this, AddProfileToSchedule.class);
        i.putExtra(SCHEDULE, schedule);
        startActivityForResult(i, 10);
    }

    /*
    * Take new Profile added and add it to events list that holds the WeekViewEvents
    * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        boolean dayChecked = false;
        if (data!=null){
            if (requestCode ==10 && resultCode == Activity.RESULT_OK){
                Boolean dayCB [] = (Boolean[]) data.getSerializableExtra(AddProfileToSchedule.DAYCB);

                int startHours = data.getIntExtra(AddProfileToSchedule.START_HOUR, 0);
                int startMins = data.getIntExtra(AddProfileToSchedule.START_MIN, 0);
                int hours = data.getIntExtra(AddProfileToSchedule.HOURS, 0);
                int mins = data.getIntExtra(AddProfileToSchedule.MINS, 0);

                Long maxMinutes = new Long(1440);
                Long minIndex = new Long(startHours*60+startMins);
                Long duration = new Long(hours*60+mins);
                Long total = minIndex + duration;
                RecurringTime rt = new RecurringTime();

                for (int i=0; i<dayCB.length; i++){
                    if (dayCB[i]){
                        // check if wraps to next day
//                        if (total > maxMinutes){
//                            Long timeRemainingInDay = maxMinutes-minIndex;
//                            rt.addTime(i, minIndex, timeRemainingInDay);
//                            // rt.addTime(i+1, new Long(0), duration-timeRemainingInDay);
//                        }else{ // profile time block in same day
                            rt.addTime(i, minIndex, duration);
//                        }
                        dayChecked = true;
                    }
                }

                // If none of the days were checked, schedule the profile for the current day of the week
                if (!dayChecked){
                    Calendar startTime = Calendar.getInstance();
                    int dayOfWeek = startTime.get(Calendar.DAY_OF_WEEK);
                    rt.addTime(startTime.get(Calendar.DAY_OF_WEEK)-1, minIndex , duration);
                }

                //add profile with recurring time to schedule
                Profile profile = (Profile) data.getSerializableExtra(AddProfileToSchedule.SELECTED_PROFILE);
                this.schedule.addProfile(profile.getIdentifier(), rt);
                ScheduleManager.getDefaultManager().setSchedule(this.schedule);

                this.populateEventsList(this.schedule);
            }
            //Edit profile/event in schedule
            else if (requestCode ==11){
                Boolean didDelete = data.getBooleanExtra(EditProfileInSchedule.DID_DELETE_PROFILE, false);
                if (didDelete){
                    deleteProfileFromSchedule(data);
                }else{ //updated profile
                    updateProfileInSchedule(data);
                }
            }
        }
    }

    private void deleteProfileFromSchedule(Intent data){
        String profID = data.getStringExtra(EditProfileInSchedule.PROFILE_ID);
        schedule.removeProfileWithIdentifier(profID);
        ScheduleManager.getDefaultManager().setSchedule(this.schedule);
        for (int i=0; i<events.size(); i++){
            if (mapID.get(events.get(i).getId()).equals(profID)){
                events.remove(i);
            }
        }
        this.populateEventsList(this.schedule);
    }

    private void updateProfileInSchedule(Intent data){
        String profID = data.getStringExtra(EditProfileInSchedule.PROFILE_ID);
        Boolean dayCB [] = (Boolean[]) data.getSerializableExtra(EditProfileInSchedule.DAYCB_EDIT);
        int hours = data.getIntExtra(EditProfileInSchedule.HOURS_EDIT, 0);
        int mins = data.getIntExtra(EditProfileInSchedule.MINS_EDIT, 0);

        int startHours = data.getIntExtra(EditProfileInSchedule.START_HOUR_EDIT, 0);
        int startMins = data.getIntExtra(EditProfileInSchedule.START_MIN_EDIT, 0);

        Long maxMinutes = new Long(1440);

        Long minIndex = new Long(startHours*60+startMins);
        Long duration = new Long(hours*60+mins);
        Long total = minIndex + duration;
        schedule.removeProfileWithIdentifier(profID);
        RecurringTime rt = new RecurringTime();
        boolean dayChecked = false;
        for (int i=0; i<dayCB.length; i++){
            if (dayCB[i]){
                // check if wraps to next day
//                if (total > maxMinutes){
//                    Long timeRemainingInDay = maxMinutes-minIndex;
//                    rt.addTime(i, minIndex, timeRemainingInDay);
//                    // rt.addTime(i+1, new Long(0), duration-timeRemainingInDay);
//                }else{ // profile time block in same day
                    rt.addTime(i, minIndex, duration);
//                }
                dayChecked = true;
            }
        }
        if (!dayChecked){
            Calendar startTime = Calendar.getInstance();
            rt.addTime(startTime.get(Calendar.DAY_OF_WEEK)-1, minIndex, duration);
        }
        schedule.addProfile(profID, rt);
        ScheduleManager.getDefaultManager().setSchedule(this.schedule);
        this.populateEventsList(schedule);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        setupDateTimeInterpreter(id == R.id.action_week_view);
        switch (id){
            case R.id.action_today:
                mWeekView.goToToday();
                return true;
            //adding a profile to the schedule
            case R.id.add_profile_button:
                openAddProfileActivity();
                return true;
            case R.id.action_day_view:
                if (mWeekViewType != TYPE_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_DAY_VIEW;
                    mWeekView.setNumberOfVisibleDays(1);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                }
                return true;
            case R.id.action_three_day_view:
                if (mWeekViewType != TYPE_THREE_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_THREE_DAY_VIEW;
                    mWeekView.setNumberOfVisibleDays(3);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                }
                return true;
            case R.id.action_week_view:
                if (mWeekViewType != TYPE_WEEK_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_WEEK_VIEW;
                    mWeekView.setNumberOfVisibleDays(7);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Set up a date time interpreter which will show short date values when in week view and long
     * date values otherwise.
     * @param shortDate True if the date values should be short.
     */
    private void setupDateTimeInterpreter(final boolean shortDate) {
        mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
                SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
                String weekday = weekdayNameFormat.format(date.getTime());
                SimpleDateFormat format = new SimpleDateFormat(" M/d", Locale.getDefault());

                // All android api level do not have a standard way of getting the first letter of
                // the week day name. Hence we get the first char programmatically.
                // Details: http://stackoverflow.com/questions/16959502/get-one-letter-abbreviation-of-week-day-of-a-date-in-java#answer-16959657
                if (shortDate)
                    weekday = String.valueOf(weekday.charAt(0));
                return weekday.toUpperCase() + format.format(date.getTime());
            }

            @Override
            public String interpretTime(int hour) {
                return hour > 11 ? (hour - 12) + " PM" : (hour == 0 ? "12 AM" : hour + " AM");
            }
        });
    }

    protected String getEventTitle(Calendar time) {
        return String.format("Event of %02d:%02d %s/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH)+1, time.get(Calendar.DAY_OF_MONTH));
    }


    @Override
    public void onEmptyViewLongPress(Calendar time) {
        Toast.makeText(this, "Empty view long pressed: " + getEventTitle(time), Toast.LENGTH_SHORT).show();
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

    private void populateEventsList(Schedule schedule){
        this.events.clear(); // Cleanup

        Map<String, RecurringTime> map = schedule.getProfileTimes();

        int[] colors = {
                getResources().getColor(R.color.event_color_01),
                getResources().getColor(R.color.event_color_02),
                getResources().getColor(R.color.event_color_03),
                getResources().getColor(R.color.event_color_04)
        };


        ArrayList<String> profileIdentifiers = schedule.getProfileIdentifiers();
        int profileIndex = 0;

        for (Map.Entry<String, RecurringTime> entry : map.entrySet()) {
            Profile profile = ProfileManager.getDefaultManager().getProfileWithIdentifier(profileIdentifiers.get(profileIndex));

            if (profile == null) {
                continue;
            }

            RecurringTime profileTime = schedule.getProfileTimeWithIdentifier(profile.getIdentifier());

            if (profileTime == null) { // Profile is not in schedule
                continue;
            }

            ArrayList<Map<Long, Long>> timesInWeek = profileTime.getTimes();
            int weekIndex = 0;

            for (Map<Long, Long> time : timesInWeek){
                //sun/mon/tues/wed/thurs/fri/sat/

                for (Map.Entry<Long, Long> t : time.entrySet()){
                    //t.getKey() : minuteIndex The time in minutes when the time block starts.
                    //t.getValue() : duration The duration of the time block.
                    Long minuteIndex = t.getKey();
                    int startHourOfDay = (int)(minuteIndex/60);
                    int startMinuteOfHour = (int)(minuteIndex%60);

                    Long duration = t.getValue();
                    int hours = (int)(duration/60);
                    int minutes = (int)(duration%60);

                    Calendar startTime = Calendar.getInstance();
                    startTime.set(Calendar.HOUR_OF_DAY, startHourOfDay);
                    startTime.set(Calendar.MINUTE, startMinuteOfHour);
                    startTime.set(Calendar.SECOND, 0);
                    startTime.set(Calendar.MILLISECOND, 0);
                    startTime.set(Calendar.DAY_OF_WEEK, weekIndex+1);

                    Calendar endTime = (Calendar) startTime.clone();
                    endTime.add(Calendar.HOUR, hours); // Add duration hours to startTime
                    endTime.add(Calendar.MINUTE, minutes); // Add duration minutes to startTime

                    WeekViewEvent event = new WeekViewEvent(
                            this.events.size(),
                            profile.getName(),
                            startTime,
                            endTime
                    );
                    mapID.put(new Long(this.events.size()), profile.getIdentifier());
                    event.setColor(colors[profileIndex % colors.length]);

                    this.events.add(event);
                }

                weekIndex++;
            }
            profileIndex++;
        }

        this.mWeekView.notifyDatasetChanged();
    }
    //when event is clicked
    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        String id = mapID.get(new Long(event.getId()));
        String name = event.getName();
        Calendar startTime = event.getStartTime();
        Calendar endTime = event.getEndTime();
        Intent i = new Intent(ScheduleInterfaceController.this, EditProfileInSchedule.class);
        RecurringTime rt = schedule.getProfileTimeWithIdentifier(id);

        if (rt != null) {
            ArrayList<Map<Long, Long>> times = rt.getTimes();
            i.putExtra(PROFILE_TIME, times);
            i.putExtra(PROFILE_NAME, name);
            i.putExtra(START_TIME, startTime);
            i.putExtra(END_TIME, endTime);
            i.putExtra(PROFILE_ID, id);
            startActivityForResult(i, 11);
        }
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
        Toast.makeText(this, "Long pressed event: " + event.getName(), Toast.LENGTH_SHORT).show();
    }
    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        List<WeekViewEvent> weekviewEvents = new ArrayList<WeekViewEvent>();

        for (WeekViewEvent event : this.events) {
            // Clone the event
            WeekViewEvent newEvent = new WeekViewEvent(
                    event.getId(),
                    event.getName(),
                    event.getStartTime(),
                    event.getEndTime());
            newEvent.setColor(event.getColor());

            Calendar newStartTime = (Calendar) newEvent.getStartTime().clone();
            newStartTime.set(Calendar.YEAR, newYear);
            newStartTime.set(Calendar.MONTH, newMonth-1);
            newStartTime.set(Calendar.DAY_OF_WEEK, newEvent.getStartTime().get(Calendar.DAY_OF_WEEK));
            newStartTime.set(Calendar.HOUR_OF_DAY, newEvent.getStartTime().get(Calendar.HOUR_OF_DAY));
            newStartTime.set(Calendar.MINUTE, newEvent.getStartTime().get(Calendar.MINUTE));
            newEvent.setStartTime(newStartTime);

            Calendar newEndTime = (Calendar) newEvent.getEndTime().clone();
            newEndTime.set(Calendar.YEAR, newYear);
            newEndTime.set(Calendar.MONTH, newMonth-1);
            newEndTime.set(Calendar.DAY_OF_WEEK, newEvent.getEndTime().get(Calendar.DAY_OF_WEEK));
            newEndTime.set(Calendar.HOUR_OF_DAY, newEvent.getEndTime().get(Calendar.HOUR_OF_DAY));
            newEndTime.set(Calendar.MINUTE, newEvent.getEndTime().get(Calendar.MINUTE));
            newEvent.setEndTime(newEndTime);

            weekviewEvents.add(newEvent);
        }

        return weekviewEvents;
    }


    /**
     * Triggered when the users clicks on a empty space of the calendar.
     * @param time: {@link Calendar} object set with the date and time of the clicked position on the view.
     */
//    @Override
//    public void onEmptyViewLongPress(Calendar time) {
//        once= true;
//        Date date= time.getTime();
//        Calendar startTime = Calendar.getInstance();
//        startTime.setTime(date);
//        Calendar endTime = (Calendar) startTime.clone();
//        endTime.add(Calendar.HOUR, 1);
//        endTime.set(Calendar.MONTH, startTime.get(Calendar.MONTH));
//        newEvent = new WeekViewEvent(100, getEventTitle(startTime), startTime, endTime);
//        newEvent.setColor(getResources().getColor(R.color.event_color_03));
//        getWeekView().notifyDatasetChanged();
//        Toast.makeText(this, "Empty view long pressed: " + getEventTitle(time), Toast.LENGTH_SHORT).show();
//    }
}
