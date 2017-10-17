package edu.usc.csci310.focus.focus.presentation.schedule;
/*
* AddProfileToSchedule Class
*
* Activity that allows user to create a new ScheduleInterfaceController
*/
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;

import edu.usc.csci310.focus.focus.R;
import edu.usc.csci310.focus.focus.dataobjects.Profile;
import edu.usc.csci310.focus.focus.dataobjects.Schedule;
import edu.usc.csci310.focus.focus.managers.ProfileManager;

public class AddProfileToSchedule extends AppCompatActivity implements TimePickerFragment.OnCompleteListener{
    public static final String DAYCB = "daycb";
    public static final String HOURS = "hours";
    public static final String MINS = "mins";
    public static final String START_HOUR = "start_hour";
    public static final String START_MIN = "start_mins";
    public static final String SELECTED_PROFILE = "selected_profile";


    private CheckBox [] daysCB = new CheckBox[7];
    private Boolean [] didCheckBoxes = new Boolean[7];
    private Spinner profileSpinner;
    private Button startTimeButton;
    private EditText hourText, minText;
    private Button addProfileButton;

    private ArrayList<Profile> profiles;
    private Profile selectedProfile;
    private int hours;
    private int mins;
    private int startHours;
    private int startMins;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_profile_to_schedule);
        for (int i = 0; i < this.didCheckBoxes.length; i++){
            this.didCheckBoxes[i] = false;
        }
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

        this.profiles = ProfileManager.getDefaultManager().getAllProfiles();
        Intent i = getIntent();
        Schedule schedule = (Schedule) i.getSerializableExtra(ScheduleInterfaceController.SCHEDULE);
        ArrayList<String> profileIDs = schedule.getProfileIdentifiers();
        String[] profileNames = new String[profiles.size()];
        for (int index=0; index<profiles.size(); index++) {
            profileNames[index] = profiles.get(index).getName();
        }
//
//        ArrayList<Profile> profileList = new ArrayList<Profile>();
//        for (int p=0; p<profiles.size(); p++){
//            profileList.add(profiles.get(p));
//        }
//        for (int index=0; index<profileIDs.size(); index++){
//            boolean filter = false;
//            for (int j =0; j<profileList.size(); j++){
//                //filter
//                if (profileList.get(j).getIdentifier().equals(profileIDs.get(index))){
//                    profileList.remove(j);
//                }
//            }
//        }
//        String[] profileNames = new String[profileList.size()];
//
//        for (int q=0; q<profileNames.length; q++){
//            profileNames[q] = profileList.get(q).getName();
//        }
        ArrayAdapter adapter = new ArrayAdapter(AddProfileToSchedule.this, android.R.layout.simple_spinner_item, profileNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        profileSpinner.setAdapter(adapter);

        profileSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedProfile = profiles.get(i);
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
        addProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProfile();
            }
        });
    }

    //checks to see whether user has successfully added profile
    public void addProfile(){
        try {
            hours = Integer.parseInt(hourText.getText().toString());
            mins = Integer.parseInt(minText.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            this.showNumberFormatError();
            return;
        }

        if (!this.isDurationValid()) {
            this.showNumberFormatError();
            return;
        }

        if (this.didCompleteFields()){
            Intent i = new Intent();
            i.putExtra(DAYCB, didCheckBoxes);
            i.putExtra(HOURS, hours);
            i.putExtra(MINS, mins);
            i.putExtra(START_HOUR, startHours);
            i.putExtra(START_MIN, startMins);
            i.putExtra(SELECTED_PROFILE, this.selectedProfile);
            setResult(Activity.RESULT_OK, i);
            finish();
        } else {
            this.showFormCompletionError();
        }
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
        return (hours>=0) && (hours<=10) &&
                ((mins==0) || (mins<=59));
    }

    public boolean didCompleteFields() {
        return (selectedProfile!=null);
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
        String time = (hourOfDay%12 + ":" + minute + " " + ((hourOfDay>=12) ? "PM" : "AM"));
        startHours = hourOfDay;
        startMins = minute;
        startTimeButton.setText(time);
    }
}
