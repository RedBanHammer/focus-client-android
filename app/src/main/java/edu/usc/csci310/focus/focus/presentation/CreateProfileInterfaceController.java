package edu.usc.csci310.focus.focus.presentation;
/*
* CreateProfileInterfaceController Class
*
* Activity that allows user to create a new ProfileInterfaceController
*/
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
    private Profile profileIfEditing;
    String PROFILE_TO_EDIT="PROFILE_TO_EDIT";
    EditText tv;
    ListView listView;

    public CreateProfileInterfaceController(){
        //constructor....??
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        //initialize the profile name box
        tv = (EditText) findViewById(R.id.profileName2);
        //initialize the app displaying box
        appList = new ArrayList<>();
        appAdapter = new appViewAdapter(this, appList);
        listView = (ListView) findViewById(R.id.createProfileListView);
        listView.setAdapter(appAdapter);

        //set onclick for the apps so that they can be deleted when tapped
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, final int position,
                                    long id) {
                //delete the app selected
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CreateProfileInterfaceController.this)
                        .setTitle("Remove App")
                        .setMessage("Remove this app from list?")
                        .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                appList.remove(position);
                                appAdapter.notifyDataSetChanged();
                                //reloadAppList();
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.show();
            }
        });

        Intent in = getIntent();
        profileIfEditing = (Profile) in.getSerializableExtra(PROFILE_TO_EDIT);
        if(profileIfEditing != null)
        {
            tv.setText(profileIfEditing.getName());
            appList.addAll(profileIfEditing.getApps());
            appAdapter.notifyDataSetChanged();

            //reloadAppList();
        }

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
                //first check if the name is empty
                if(tv.getText().toString().matches(""))
                {
                    //they didn't enter a name
                    errorDialog();
                }
                //now check if applist is empty
                else if(appList.isEmpty())
                {
                    appErrorDialog();
                }
                else
                {
                    //if we are creating a new profile and not editing an old one
                    if(profileIfEditing == null) {
                        profileIfEditing = new Profile(tv.getText().toString());
                    }
                    profileIfEditing.setName(tv.getText().toString());
                    profileIfEditing.setApps(appList);
                    //pass info to manager
                    ProfileManager.getDefaultManager().setProfile(profileIfEditing);
                    finish();
                }

            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data!=null){
            if (requestCode == 10) {
                if(resultCode == Activity.RESULT_OK){
                    //Retrieve selected apps, add these to existing appLIst
                    ArrayList<App> temp = (ArrayList<App>) data.getSerializableExtra(SelectApp.SELECTED_APPS);
                    for(App a : temp)
                    {
                        if(!appList.contains(a))
                        {
                            appList.add(a);
                        }
                    }

                    //String result = data.getStringExtra(SelectApp.SELECTED_APPS);
                    appAdapter.notifyDataSetChanged();
                    //reloadAppList();
                }

            }
        }

    }//onActivityResult

    private void errorDialog() {
        new android.app.AlertDialog.Builder(this)
                .setTitle("Invalid name")
                .setMessage("You entered an invalid name for the profile")
                .setPositiveButton("Okay", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which) {

                    }

                })
                .show();
    }


    private void appErrorDialog() {
        new android.app.AlertDialog.Builder(this)
                .setTitle("Invalid Selection")
                .setMessage("Please select at least one application")
                .setPositiveButton("Okay", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which) {

                    }

                })
                .show();
    }

    /*
     * Determines whether the user has completed the form to create a new ProfileInterfaceController.
     *
     * @return true if user completed form; otherwise, false
     */
    private boolean didCompleteForm(){
        return false;
    }

    public void reloadAppList() {
        appAdapter = new appViewAdapter(this, appList);
        listView = (ListView) findViewById(R.id.createProfileListView);
        listView.setAdapter(appAdapter);

    }



}
