package edu.usc.csci310.focus.focus.presentation;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import edu.usc.csci310.focus.focus.MainActivity;

/**
 * Created by Ashley Walker on 10/14/2017.
 */

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DialogFragment newFragment = new AppBlockedPopup();
        newFragment.show(getFragmentManager(), "App Blocked");
    }

}
