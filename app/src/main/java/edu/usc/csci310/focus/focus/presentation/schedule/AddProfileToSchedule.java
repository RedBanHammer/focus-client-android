package edu.usc.csci310.focus.focus.presentation.schedule;
/*
* AddProfileToSchedule Class
*
* Activity that allows user to create a new ScheduleInterfaceController
*/
import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
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
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;

import edu.usc.csci310.focus.focus.R;
import edu.usc.csci310.focus.focus.dataobjects.Profile;
import edu.usc.csci310.focus.focus.managers.ProfileManager;

public class AddProfileToSchedule extends AppCompatActivity implements TimePickerFragment.OnCompleteListener{
    public static final String DAYCB = "daycb";
    public static final String HOURS = "hours";
    public static final String MINS = "mins";
    public static final String START_HOUR = "start_hour";
    public static final String START_MIN = "start_mins";

    private CheckBox [] daysCB = new CheckBox[7];
    private Boolean [] didCheckBoxes = new Boolean[7];
    private Spinner profileSpinner;
    private Button startTimeButton;
    private EditText hourText, minText;
    private Button addProfileButton;
    private Button cancelButton;

    private String selectedProfile;
    private int hours;
    private int mins;
    private int startHours;
    private int startMins;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_profile_to_schedule);

        startTimeButton = (Button)findViewById(R.id.start_time_button);
        profileSpinner = (Spinner)findViewById(R.id.profile_spinner);
        startTimeButton = (Button)findViewById(R.id.start_time_button);
        hourText = (EditText) findViewById(R.id.hours_field);
        minText = (EditText) findViewById(R.id.mins_field);

        daysCB[0] = (CheckBox)findViewById(R.id.sunday);
        daysCB[1] = (CheckBox)findViewById(R.id.monday);
        daysCB[2] = (CheckBox)findViewById(R.id.tuesday);
        daysCB[3] = (CheckBox)findViewById(R.id.wednesday);
        daysCB[4] = (CheckBox)findViewById(R.id.thursday);
        daysCB[5] = (CheckBox)findViewById(R.id.friday);
        daysCB[6] = (CheckBox)findViewById(R.id.saturday);
        addProfileButton =(Button)findViewById(R.id.add_profile);
        cancelButton = (Button)findViewById(R.id.cancel_button);
        ArrayList<Profile> profiles = ProfileManager.getDefaultManager().getAllProfiles();
        String[] profileNames = new String[profiles.size()];
        for (int index=0; index<profiles.size(); index++){
            profileNames[index] = profiles.get(index).getName();
        }
        ArrayAdapter adapter = new ArrayAdapter(AddProfileToSchedule.this, android.R.layout.simple_spinner_item, profileNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        profileSpinner.setAdapter(adapter);

        profileSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedProfile = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
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
                hours = Integer.parseInt(hourText.getText().toString());
                return true;
            }
        });
//        hourText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                // TODO Auto-generated method stub
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                // TODO Auto-generated method stub
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//                hours = Integer.parseInt(hourText.getText().toString());
//            }
//        });
        minText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                mins = Integer.parseInt(minText.getText().toString());
                return true;
            }
        });
//        minText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                // TODO Auto-generated method stub
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                // TODO Auto-generated method stub
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//                mins = Integer.parseInt(minText.getText().toString());
//            }
//        });
        minText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                mins = Integer.parseInt(minText.getText().toString());
                return true;
            }
        });
        daysCB[0].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    didCheckBoxes[0] = true;
                }
            }
        });
        daysCB[1].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    didCheckBoxes[1] = true;
                }
            }
        });
        daysCB[2].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    didCheckBoxes[2] = true;
                }
            }
        });
        daysCB[3].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    didCheckBoxes[3] = true;
                }
            }
        });
        daysCB[4].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    didCheckBoxes[4] = true;
                }
            }
        });
        daysCB[5].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    didCheckBoxes[5] = true;
                }
            }
        });

        daysCB[6].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    didCheckBoxes[6] = true;
                }
            }
        });
        //cancel activity
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                setResult(Activity.RESULT_CANCELED, i);
                finish();
            }
        });
        //add profile and return to activity
        addProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProfile();
            }
        });
    }

    //checks to see whether user has successfully added profile
    public void addProfile(){
        hours = Integer.parseInt(hourText.getText().toString());
        mins = Integer.parseInt(minText.getText().toString());

        if (didCompleteFields()){
            Intent i = new Intent();
            i.putExtra(DAYCB, didCheckBoxes);
            i.putExtra(HOURS, hours);
            i.putExtra(MINS, mins);
            i.putExtra(START_HOUR, startHours);
            i.putExtra(START_MIN, startMins);
            setResult(Activity.RESULT_OK, i);
            finish();
        }
    }
    public boolean didCompleteFields(){
        return ((selectedProfile!="") && (hours>=0) && (hours<=10) && (mins>=0) && (mins<=59) &&(startHours!=0) && (startMins!=0));
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
        String time;
        int hr;
        boolean am = false;
        //AM
        if (hourOfDay<12){
            hr = hourOfDay;
            am = true;
            time = Integer.toString(hr) + ":" +Integer.toString(minute) + " AM";
        }else{//PM
            hr = hourOfDay-12;
            time = Integer.toString(hr) + ":" +Integer.toString(minute) + " PM";
        }
        startHours = hourOfDay;
        startMins = minute;
        startTimeButton.setText(time);
    }
}
