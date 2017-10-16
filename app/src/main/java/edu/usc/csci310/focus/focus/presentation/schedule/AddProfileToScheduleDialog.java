package edu.usc.csci310.focus.focus.presentation.schedule;
/*
* AddProfileToScheduleDialog Class
*
* Activity that allows user to create a new ScheduleInterfaceController
*/
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;

import edu.usc.csci310.focus.focus.R;

public class AddProfileToScheduleDialog extends DialogFragment {
    private CheckBox [] daysCB = new CheckBox[7];
    private Boolean [] didCheckBoxes = new Boolean[7];
    private Spinner profileSpinner;
    private Button startTimeButton;
    private Button durationButton;
    private Button addProfileButton;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_profile_to_schedule_dialog, container, false);
        startTimeButton = (Button)view.findViewById(R.id.start_time_button);
        profileSpinner = (Spinner)view.findViewById(R.id.profile_spinner);
        startTimeButton = (Button)view.findViewById(R.id.start_time_button);
        durationButton = (Button)view.findViewById(R.id.duration_button);

        daysCB[0] = (CheckBox)view.findViewById(R.id.sunday);
        daysCB[1] = (CheckBox)view.findViewById(R.id.monday);
        daysCB[2] = (CheckBox)view.findViewById(R.id.tuesday);
        daysCB[3] = (CheckBox)view.findViewById(R.id.wednesday);
        daysCB[4] = (CheckBox)view.findViewById(R.id.thursday);
        daysCB[5] = (CheckBox)view.findViewById(R.id.friday);
        daysCB[6] = (CheckBox)view.findViewById(R.id.saturday);
        addProfileButton =(Button)view.findViewById(R.id.add_profile);

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
//        createButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//        mEditText = (EditText) view.findViewById(R.id.schedule_name);
//
//        // set this instance as callback for editor action
//        mEditText.setOnEditorActionListener(this);
//        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getDialog().setTitle("Provide a name for this schedule");

        return view;
    }

    /*
     * Determines whether the user has completed the form to create a new ScheduleInterfaceController.
     *
     * @return  if user completed form; otherwise, false
     */
    private void didCompleteForm(){
        return;
    }
}
