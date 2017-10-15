package edu.usc.csci310.focus.focus.presentation;
/*
* CreateProfileInterfaceController Class
*
* Activity that allows user to create a new ProfileInterfaceController
*/
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.usc.csci310.focus.focus.MainActivity;
import edu.usc.csci310.focus.focus.R;
import edu.usc.csci310.focus.focus.dataobjects.App;
import edu.usc.csci310.focus.focus.dataobjects.Profile;
import edu.usc.csci310.focus.focus.managers.ProfileManager;

public class CreateProfileInterfaceController extends AppCompatActivity {
    private ArrayList<App> appList;
    private appViewAdapter appAdapter;
    private Button button, done;
    public CreateProfileInterfaceController(){
        //constructor....??
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        //hook up the select app button to pull up the select app activity
        button = (Button) findViewById(R.id.chooseAppButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start NewActivity.class
                Intent myIntent = new Intent(CreateProfileInterfaceController.this, SelectApp.class);
                startActivityForResult(myIntent, 10);
            }
        });


        //hook up done button to pull up the main activity
        done = (Button) findViewById(R.id.submitProfileButton);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // PASS INFO TO THE PROFILEINTERFACECONTROLler
                TextView tv = (TextView) findViewById(R.id.profileName2);
                Profile profile = new Profile(tv.getText().toString());
                profile.setApps(appList);

                //pass info to manager
                ProfileManager.getDefaultManager().setProfile(profile);

                //notify profileList

                Intent intent = new Intent(CreateProfileInterfaceController.this, MainActivity.class);
               // intent.putExtra("PROFILE", (Serializable) profile);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data!=null){
            if (requestCode == 10) {
                if(resultCode == Activity.RESULT_OK){
                    appList = (ArrayList<App>) data.getSerializableExtra(SelectApp.SELECTED_APPS);
                    //String result = data.getStringExtra(SelectApp.SELECTED_APPS);
                    reloadAppList();
                }

            }
        }

    }//onActivityResult

    /*
     * Determines whether the user has completed the form to create a new ProfileInterfaceController.
     *
     * @return true if user completed form; otherwise, false
     */
    private boolean didCompleteForm(){
        return false;
    }

    public void reloadAppList() {
        //fill the applist when apps are checked and returned.
        appAdapter = new appViewAdapter(this, appList);
        ListView listView = (ListView) findViewById(R.id.createProfileListView);
        listView.setAdapter(appAdapter);
    }

}
