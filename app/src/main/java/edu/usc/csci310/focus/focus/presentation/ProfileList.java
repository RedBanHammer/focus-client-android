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

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

import edu.usc.csci310.focus.focus.R;
import edu.usc.csci310.focus.focus.dataobjects.Profile;
import edu.usc.csci310.focus.focus.managers.BlockingManager;
import edu.usc.csci310.focus.focus.managers.ProfileManager;

public class ProfileList extends Fragment {
    private ListView listView;
    ArrayList<Profile> profiles;
    FloatingActionButton addProfileButton;
    ProfileListViewAdapter profileListViewAdapter;
    public final static String PROFILE_ARRAYLIST = "profile_arraylist";
    // newInstance constructor for creating fragment with arguments
    public static ProfileList newInstance(int page, String title, ArrayList<Profile> profiles) {
        ProfileList profileListFragment = new ProfileList();
        Bundle args = new Bundle();
//        args.putInt("someInt", page);
//        args.putString("someTitle", title);
        args.putSerializable(PROFILE_ARRAYLIST, profiles);
        profileListFragment.setArguments(args);
        return profileListFragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profiles = (ArrayList<Profile>)getArguments().getSerializable(PROFILE_ARRAYLIST);
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
                intent.putExtra("PROFILE", (Serializable) profiles.get(position));
                startActivityForResult(intent, 0);
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        profileListViewAdapter.setProfiles(ProfileManager.getDefaultManager().getAllProfiles());
    }
}
