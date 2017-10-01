package edu.usc.csci310.focus.focus.presentation;
/*
 * Notification Activity that displays list of apps blocked after a blocking period
 */
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import edu.usc.csci310.focus.focus.R;

public class Notification extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
    }
}
