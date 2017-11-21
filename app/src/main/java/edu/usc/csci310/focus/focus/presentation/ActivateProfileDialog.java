package edu.usc.csci310.focus.focus.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import edu.usc.csci310.focus.focus.R;
import edu.usc.csci310.focus.focus.dataobjects.Profile;
import edu.usc.csci310.focus.focus.dataobjects.Timer;
import edu.usc.csci310.focus.focus.managers.ProfileManager;


/**
 * Created by Ashley Walker on 10/22/2017.
 */

public class ActivateProfileDialog extends AppCompatActivity {

    private Profile profile;
    Button cancelButton;
    Button startButton;
    TextView tv;
    NumberPicker hoursPicker;
    NumberPicker minutesPicker;
    long minutes;
    Timer timer;

    public void setProfile(Profile profile) {
        this.profile = profile;
        this.timer = new Timer(this.profile);
        this.renderProfileInfo();
    }

    public void ActivateProfileDialog(Profile profile)
    {
        this.setProfile(profile);

    }

    /*
     * Renders an activate profile view
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_activate);

        //get the profile information passed through CreateProfileInterfaceController matches
        Intent intent = getIntent();
        this.profile = (Profile) intent.getSerializableExtra("profile");

        this.renderProfileInfo();

        // Initialize hours and minutes pickers
        this.hoursPicker = (NumberPicker) findViewById(R.id.hoursPicker);
        this.hoursPicker.setMinValue(0);
        this.hoursPicker.setMaxValue(10);

        this.minutesPicker = (NumberPicker) findViewById(R.id.minutesPicker);
        this.minutesPicker.setMinValue(10);
        this.minutesPicker.setMaxValue(59);

        this.hoursPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (newVal == 10) {
                    minutesPicker.setMinValue(0);
                    minutesPicker.setValue(0);
                    minutesPicker.setEnabled(false);
                } else {
                    if (newVal == 0) minutesPicker.setMinValue(10);
                    else minutesPicker.setMinValue(0);
                    minutesPicker.setEnabled(true);
                }
            }
        });


        //initialize edit profile name button
        this.startButton = (Button) findViewById(R.id.startButton);
        this.startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hrs = hoursPicker.getValue();
                int mins = minutesPicker.getValue();
                minutes = mins + (hrs * 60);

                //package profile as a schedule using Timer
                timer.setTime(minutes);
                timer.start();

                Intent returnIntent = new Intent();
                setResult(RESULT_OK, returnIntent);
                finish();
            }

        });

        this.cancelButton = (Button) findViewById(R.id.cancelButton);
        this.cancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(RESULT_CANCELED, returnIntent);
                finish();
            }
        });

    }

    private void renderProfileInfo() {
        tv = (TextView)findViewById(R.id.profileName);
        tv.setText(profile.getName());
    }

    @Override
    public void onResume() {
        super.onResume();

        Profile newProfile = ProfileManager.getDefaultManager().getProfileWithIdentifier(this.profile.getIdentifier());
        this.setProfile(newProfile);
        renderProfileInfo();
    }

}
