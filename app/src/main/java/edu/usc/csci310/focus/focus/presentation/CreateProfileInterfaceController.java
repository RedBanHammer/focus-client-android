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

import edu.usc.csci310.focus.focus.R;
import edu.usc.csci310.focus.focus.dataobjects.App;
import edu.usc.csci310.focus.focus.dataobjects.Profile;

public class CreateProfileInterfaceController extends AppCompatActivity {
    private ArrayList<App> appList;
    private appViewAdapter appAdapter;

    public CreateProfileInterfaceController(){
        //constructor....??
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        //hook up the select app button to pull up the select app activity
        Button button = (Button) findViewById(R.id.chooseAppButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start NewActivity.class
                Intent myIntent = new Intent(CreateProfileInterfaceController.this, SelectApp.class);
                startActivityForResult(myIntent, 0);
            }
        });


        //hook up done button to pull up the newly created profile's interface
        Button done = (Button) findViewById(R.id.submitProfileButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // PASS INFO TO THE PROFILEINTERFACECONTROLLER....
                TextView tv = (TextView) findViewById(R.id.profileName2);
                Profile profile = new Profile(tv.toString());
                profile.setApps(appList);
                Intent intent = new Intent(CreateProfileInterfaceController.this, ProfileInterfaceController.class);
                intent.putExtra("PROFILE", (Serializable) profile);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 0) {
            if(resultCode == Activity.RESULT_OK){
                appList = (ArrayList<App>) data.getSerializableExtra("SELECTED_APPS");
                reloadAppList();
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
