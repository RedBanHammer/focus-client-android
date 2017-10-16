package edu.usc.csci310.focus.focus.presentation;
/*
 * ProfileInterfaceController Class
 *
 * Activity that shows an edit ProfileInterfaceController page
 */
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;

import edu.usc.csci310.focus.focus.MainActivity;
import edu.usc.csci310.focus.focus.dataobjects.Profile;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import edu.usc.csci310.focus.focus.R;
import edu.usc.csci310.focus.focus.dataobjects.App;
import edu.usc.csci310.focus.focus.managers.ProfileManager;

import java.util.ArrayList;

public class ProfileInterfaceController extends AppCompatActivity {
    private Profile profile;
    Button deleteButton;
    Button editNameButton;
    String SAVE_NAME= "Save Changes";
    String CANCEL="Cancel";
    String TITLE="Edit Name";
    String PROFILE_TO_EDIT="PROFILE_TO_EDIT";
    EditText text;
    Dialog editNameDialog;
    Button posButton;
    TextView tv;

    public void setProfile(Profile profile) {
        this.profile = profile;
        this.renderProfileInfo();
    }

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

        //get the profile information passed through CreateProfileInterfaceController
        //MAKE SURE THIS MATCHES HOW WE RECEIVE THE PROFILE FROM THE MAIN MENU
        Intent intent = getIntent();
        this.profile = (Profile) intent.getSerializableExtra("PROFILE");

        appViewAdapter appAdapter = new appViewAdapter(this, profile.getApps());

        ListView listView = (ListView) findViewById(R.id.listviewprofile);
        listView.setAdapter(appAdapter);

        this.renderProfileInfo();

        //initialize delete profile button
        deleteButton = (Button) findViewById(R.id.deleteButton);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //remove the profile from storage manager
                ProfileManager.getDefaultManager().removeProfile(profile);
                Intent intent = new Intent(ProfileInterfaceController.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //initialize edit profile name button
        editNameButton = (Button) findViewById(R.id.changeNameButton);
        editNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //package info and send to createprofileinterfacecontroller to modify
                Intent send = new Intent(ProfileInterfaceController.this, CreateProfileInterfaceController.class);
                send.putExtra(PROFILE_TO_EDIT, profile);
                startActivity(send);


                //create a new dialog
                /*
                editNameDialog = new Dialog(ProfileInterfaceController.this);
                editNameDialog.setContentView(R.layout.edit_profile_name);
                editNameDialog.setTitle(TITLE);

                text = (EditText) editNameDialog.findViewById(R.id.newNameText);
                posButton = (Button) editNameDialog.findViewById(R.id.positiveButton);
                posButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //send new name to profile manager
                        String newName = text.getText().toString();
                        profile.setName(newName);
                        ProfileManager.getDefaultManager().setProfile(profile);

                        //exit the dialog, reload the name
                        tv.setText(newName);
                        editNameDialog.dismiss();

                    }
                });

                Button negButton = (Button) editNameDialog.findViewById(R.id.negativeButton);
                negButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //dismiss dialog
                        editNameDialog.dismiss();

                    }
                });

                editNameDialog.show(); */
            }

        });

    }

    private void renderProfileInfo() {
        tv = (TextView)findViewById(R.id.profileName);
        tv.setText(profile.getName());
    }

    @Override
    public void onResume() {
        super.onResume();

        Profile newProfile = ProfileManager.getDefaultManager().getProfileWithIdentifier(this.profile.getIdentifier());
        this.setProfile(newProfile);
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
