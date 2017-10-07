package edu.usc.csci310.focus.focus.presentation;
/*
* CreateScheduleInterfaceController Class
*
* Activity that allows user to create a new ScheduleInterfaceController
*/
import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import edu.usc.csci310.focus.focus.R;

public class CreateScheduleInterfaceController extends AppCompatActivity {
    private EditText name;
    private Button addButton;
    private CheckBox [] daysCB = new CheckBox[7];
    private CheckBox repeatWeekly;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_schedule);

        name = (EditText)findViewById(R.id.create_schedule_text_name);
        addButton =(Button)findViewById(R.id.create_schedule_profiles_button);
        daysCB[0] = (CheckBox)findViewById(R.id.sunday);
        daysCB[1] = (CheckBox)findViewById(R.id.monday);
        daysCB[2] = (CheckBox)findViewById(R.id.tuesday);
        daysCB[3] = (CheckBox)findViewById(R.id.wednesday);
        daysCB[4] = (CheckBox)findViewById(R.id.thursday);
        daysCB[5] = (CheckBox)findViewById(R.id.friday);
        daysCB[6] = (CheckBox)findViewById(R.id.saturday);

        //adding profiles should show dialog popup
    }
    /*
     * Determines whether the user has completed the form to create a new ScheduleInterfaceController.
     *
     * @return true if user completed form; otherwise, false
     */
    private boolean didCompleteForm(){
        return false;
    }
}
