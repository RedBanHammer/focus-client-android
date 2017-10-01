package edu.usc.csci310.focus.focus.presentation;
/*
 * Activity that shows a list of all Profiles
 */
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import edu.usc.csci310.focus.focus.R;

public class ProfileList extends AppCompatActivity {

    /*
     * Renders the profile list view
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_list);
    }
}
