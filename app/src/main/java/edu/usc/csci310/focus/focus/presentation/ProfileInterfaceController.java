package edu.usc.csci310.focus.focus.presentation;
/*
 * ProfileInterfaceController Class
 *
 * Activity that shows an edit ProfileInterfaceController page
 */
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import edu.usc.csci310.focus.focus.dataobjects.Profile;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import edu.usc.csci310.focus.focus.R;
import edu.usc.csci310.focus.focus.dataobjects.App;

import java.util.ArrayList;

public class ProfileInterfaceController extends AppCompatActivity {
  private Profile profile;

    public void ProfileInterfaceController(Profile profile)
    {
        this.profile = profile;
    }

    /*
     * renders a profile edit page
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //LOAD APP ICONS
        /*try {
            loadAppIcons();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }*/

        appListAdapter appAdapter = new appListAdapter(this, profile.getApps());

        ListView listView = (ListView) findViewById(R.id.listviewprofile);
        listView.setAdapter(appAdapter);

        TextView tv = (TextView)findViewById(R.id.profileName);
        tv.setText(profile.getName());

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

    private void loadAppIcons() throws PackageManager.NameNotFoundException {
        for (int i = 0; i < profile.getApps().size(); i++) {
            App temp = profile.getApps().get(i);
            Drawable appIcon = getPackageManager().getApplicationIcon(temp.getIdentifier());
            temp.setIcon(appIcon);
        }

    }
}
