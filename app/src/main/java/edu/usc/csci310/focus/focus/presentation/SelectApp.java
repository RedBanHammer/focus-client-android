package edu.usc.csci310.focus.focus.presentation;
/*
 * SelectAppInterfaceController Class
 *
 * Activity that displays all of the user's Android apps
 */
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import edu.usc.csci310.focus.focus.R;

public class SelectApp extends AppCompatActivity {
    private SelectAppInterface selectAppInterface;

    /*
     * Renders the list of Android apps
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_app);
    }

    /*
     * Returns whether the user selected an app
     *
     * @param App - the user selected app
     */
    private boolean didSelectApp(App app){
        return false;
    }
}
