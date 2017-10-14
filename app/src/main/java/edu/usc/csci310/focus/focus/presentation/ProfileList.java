package edu.usc.csci310.focus.focus.presentation;
/*
 * Activity that shows a list of all Profiles
 */
import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.usc.csci310.focus.focus.R;
import edu.usc.csci310.focus.focus.dataobjects.Profile;

public class ProfileList extends Fragment {
    private ListView listView;
    ArrayList<Profile> profiles;
    FloatingActionButton addProfileButton;
    ProfileListViewAdapter profileListViewAdapter;
    // newInstance constructor for creating fragment with arguments
    public static ProfileList newInstance(int page, String title) {
        ProfileList profileListFragment = new ProfileList();
        Bundle args = new Bundle();
//        args.putInt("someInt", page);
//        args.putString("someTitle", title);
        profileListFragment.setArguments(args);
        return profileListFragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Profile profile = new Profile("NAME");
        String name = profile.getName();
        profiles = new ArrayList<Profile>();
        profiles.add(new Profile("Profile1"));
        profiles.add(new Profile("Profile2"));
        profiles.add(new Profile("Profile3"));
        profiles.add(profile);
        profiles.add(new Profile("Profile4"));
    }

    //handle UI events
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_profile_list, container, false);

        addProfileButton = (FloatingActionButton) v.findViewById(R.id.addProfileButton);

        // call the views with this layout
        listView = (ListView) v.findViewById(R.id.profileListView);

        profileListViewAdapter = new ProfileListViewAdapter(getActivity(), 0, profiles);
        listView.setAdapter(profileListViewAdapter);
        // add new profile
        addProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreateProfileInterfaceController.class);
                startActivityForResult(intent, 0);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position,
                                    long id) {
                Intent intent = new Intent(getActivity(), ProfileInterfaceController.class);
                startActivityForResult(intent, 0);
            }
        });
        return v;
    }
}
