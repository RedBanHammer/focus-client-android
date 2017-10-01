package edu.usc.csci310.focus.focus.presentation;
/*
* CreateProfileInterfaceController Class
*
* Activity that allows user to create a new ProfileInterfaceController
*/
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import edu.usc.csci310.focus.focus.R;

public class CreateProfileInterfaceController extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
    }

    /*
     * Determines whether the user has completed the form to create a new ProfileInterfaceController.
     *
     * @return true if user completed form; otherwise, false
     */
    private boolean didCompleteForm(){
        return false;
    }

}
