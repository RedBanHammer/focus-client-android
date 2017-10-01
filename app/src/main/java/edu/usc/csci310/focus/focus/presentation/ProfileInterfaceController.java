package edu.usc.csci310.focus.focus.presentation;
/*
 * ProfileInterfaceController Class
 *
 * Activity that shows an edit ProfileInterfaceController page
 */
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import edu.usc.csci310.focus.focus.R;

public class ProfileInterfaceController extends AppCompatActivity {
//  private Profile profile;

    /*
     * renders a profile edit page
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }

    /*
     * @param: name - new name of profile
     * @return true if user changed profile name; otherwise, false
     */
    private boolean didChangeProfileName(String name){
        return false;
    }

    /*
     * Returns whether the user removed the profile or not
     */
    private boolean didRemoveProfile(){
        return false;
    }

    /*
     * Returns whether the user added an app or not
     */
    private boolean didAddApp(){
        return false;
    }
}
