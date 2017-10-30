package edu.usc.csci310.focus.focus.presentation.schedule;
/*
* AddProfileToSchedule Class
*
* Activity that allows user to create a new ScheduleInterfaceController
*/
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import edu.usc.csci310.focus.focus.R;
import edu.usc.csci310.focus.focus.dataobjects.Profile;
import edu.usc.csci310.focus.focus.managers.ProfileManager;

public class EditProfileInSchedule extends AppCompatActivity implements TimePickerFragment.OnCompleteListener{
    public static final String DAYCB_EDIT = "daycbe";
    public static final String HOURS_EDIT = "hourse";
    public static final String MINS_EDIT = "minse";
    public static final String START_HOUR_EDIT = "start_houre";
    public static final String START_MIN_EDIT = "start_minse";
    public static final String DID_DELETE_PROFILE = "did_delete";
    public static final String PROFILE_ID = "profile_id";


    private CheckBox [] daysCB = new CheckBox[7];
    private Boolean [] didCheckBoxes = new Boolean[7];
    private TextView profileName;
    private Button startTimeButton;
    private EditText hourText, minText;
    private Button updateProfileButton, deleteProfileButton;

    private ArrayList<Profile> profiles;
    private int hours;
    private int mins;
    private int startHours;
    private int startMins;
    private String profileID;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_in_schedule);

        startTimeButton = (Button)findViewById(R.id.start_time_button);
        profileName = (TextView) findViewById(R.id.profile_name);
        hourText = (EditText) findViewById(R.id.hours_field);
        minText = (EditText) findViewById(R.id.mins_field);

        daysCB[0] = (CheckBox)findViewById(R.id.sunday);
        daysCB[1] = (CheckBox)findViewById(R.id.monday);
        daysCB[2] = (CheckBox)findViewById(R.id.tuesday);
        daysCB[3] = (CheckBox)findViewById(R.id.wednesday);
        daysCB[4] = (CheckBox)findViewById(R.id.thursday);
        daysCB[5] = (CheckBox)findViewById(R.id.friday);
        daysCB[6] = (CheckBox)findViewById(R.id.saturday);
        updateProfileButton =(Button)findViewById(R.id.update_profile);
        deleteProfileButton =(Button)findViewById(R.id.delete_profile);

        Intent i = getIntent();
        ArrayList<Map<Long, Long>> times = (ArrayList<Map<Long, Long>>) i.getSerializableExtra(ScheduleInterfaceController.PROFILE_TIME);
        String name = i.getStringExtra(ScheduleInterfaceController.PROFILE_NAME);
        profileName.setText(name);
        Calendar startTime = (Calendar) i.getSerializableExtra(ScheduleInterfaceController.START_TIME);
        Calendar endTime = (Calendar) i.getSerializableExtra(ScheduleInterfaceController.END_TIME);

        startHours = startTime.get(Calendar.HOUR_OF_DAY);
        startMins = startTime.get(Calendar.MINUTE);
        int endHours = endTime.get(Calendar.HOUR_OF_DAY);
        int endMins = endTime.get(Calendar.MINUTE);
        for (int dayOfWeek = 0; dayOfWeek < this.didCheckBoxes.length; dayOfWeek++){
            // if not checked
            if (times.get(dayOfWeek).size()==0){
                daysCB[dayOfWeek].setChecked(false);
                didCheckBoxes[dayOfWeek] = false;
            }else{
                daysCB[dayOfWeek].setChecked(true);
                didCheckBoxes[dayOfWeek] = true;
            }
        }
        int hoursDiff;
        if (endHours ==0){
            hoursDiff = 24 - startHours;
        }else{
            hoursDiff = endHours-startHours;
        }
        int minsDiff = endMins-startMins;
        if (minsDiff < 0){
            hoursDiff -=1;
            minsDiff +=60;
        }
        hourText.setText(String.valueOf(hoursDiff));
        minText.setText(String.valueOf(minsDiff));
        profileID = i.getStringExtra(ScheduleInterfaceController.PROFILE_ID);

        DateFormat df = new SimpleDateFormat("h:mm a");
        Date date = new Date(0, 0, 0, startHours, startMins);
        String time = df.format(date);

        startTimeButton.setText(time);
        this.profiles = ProfileManager.getDefaultManager().getAllProfiles();

        startTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerFragment startTimeFragment = new TimePickerFragment();
                startTimeFragment.show(getSupportFragmentManager(), "startTimePicker");
            }
        });
        hourText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                return true;
            }
        });
        minText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                return true;
            }
        });
        daysCB[0].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    didCheckBoxes[0] = true;
                }else{
                    didCheckBoxes[0] = false;
                }
            }
        });
        daysCB[1].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    didCheckBoxes[1] = true;
                }else{
                    didCheckBoxes[1] = false;
                }
            }
        });
        daysCB[2].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    didCheckBoxes[2] = true;
                }else{
                    didCheckBoxes[2] = false;
                }
            }
        });
        daysCB[3].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    didCheckBoxes[3] = true;
                }else{
                    didCheckBoxes[3] = false;
                }
            }
        });
        daysCB[4].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    didCheckBoxes[4] = true;
                }else{
                    didCheckBoxes[4] = false;
                }
            }
        });
        daysCB[5].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    didCheckBoxes[5] = true;
                }else{
                    didCheckBoxes[5] = false;
                }
            }
        });

        daysCB[6].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    didCheckBoxes[6] = true;
                }else{
                    didCheckBoxes[6] = false;
                }
            }
        });

        //add profile and return to activity
        updateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });
        //delete the event
        deleteProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteProfile();
            }
        });
    }
    public void deleteProfile(){
        Intent i = new Intent();
        i.putExtra(DID_DELETE_PROFILE, true);
        i.putExtra(PROFILE_ID, profileID);
        setResult(Activity.RESULT_OK, i);
        finish();
    }
    //checks to see whether user has successfully updated profile
    public void updateProfile(){
        if (hourText.getText().toString().equals("") && minText.getText().toString().equals("")){
            this.showFormCompletionError();
            return;
        }
        if (hourText.getText().toString().equals("")){
            hours = 0;
            try {
                mins = Integer.parseInt(minText.getText().toString());
            } catch (NumberFormatException e) {
                e.printStackTrace();
                this.showNumberFormatError();
                return;
            }
        }else if (minText.getText().toString().equals("")){
            mins = 0;
            try {
                hours = Integer.parseInt(hourText.getText().toString());
            } catch (NumberFormatException e) {
                e.printStackTrace();
                this.showNumberFormatError();
                return;
            }
        }else{
            try {
                hours = Integer.parseInt(hourText.getText().toString());
                mins = Integer.parseInt(minText.getText().toString());
            } catch (NumberFormatException e) {
                e.printStackTrace();
                this.showNumberFormatError();
                return;
            }
        }

        if (!this.isDurationValid()) {
            this.showNumberFormatError();
            return;
        }

        Intent i = new Intent();
        i.putExtra(DAYCB_EDIT, didCheckBoxes);
        i.putExtra(HOURS_EDIT, hours);
        i.putExtra(MINS_EDIT, mins);
        i.putExtra(START_HOUR_EDIT, startHours);
        i.putExtra(START_MIN_EDIT, startMins);
        i.putExtra(DID_DELETE_PROFILE, false);
        i.putExtra(PROFILE_ID, profileID);
        setResult(Activity.RESULT_OK, i);
        finish();
    }

    private void showNumberFormatError() {
        new AlertDialog.Builder(this)
                .setTitle("Invalid Duration")
                .setMessage("You entered an invalid duration for the profile.")
                .setPositiveButton("Okay", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which) {

                    }

                })
                .show();
    }

    private void showFormCompletionError() {
        new AlertDialog.Builder(this)
                .setTitle("Incomplete Form")
                .setMessage("You did not complete setting up the profile.")
                .setPositiveButton("Okay", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which) {

                    }

                })
                .show();
    }

    private boolean isDurationValid() {
        int maxMins = 10*60;
        int totalMins = hours*60 + mins;

        int maxMinutes = 1440;
        int minIndex = startHours*60+startMins;
        int duration = hours*60+mins;
        int total = minIndex + duration;
        if (total > maxMinutes) {
            return false;
        }
        return ((totalMins <= maxMins) && totalMins >=10);
    }

    /*
     * Determines whether the user has completed the form to create a new ScheduleInterfaceController.
     *
     * @return  if user completed form; otherwise, false
     */
    private void didCompleteForm(){
        return;
    }

    @Override
    public void onComplete(int hourOfDay, int minute) {
        DateFormat df = new SimpleDateFormat("h:mm a");
        Date date = new Date(0, 0, 0, hourOfDay, minute);
        String time = df.format(date);
        startHours = hourOfDay;
        startMins = minute;
        startTimeButton.setText(time);
    }
}
