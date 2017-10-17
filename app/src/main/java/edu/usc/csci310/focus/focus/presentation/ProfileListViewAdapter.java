package edu.usc.csci310.focus.focus.presentation;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;


import java.util.ArrayList;

import edu.usc.csci310.focus.focus.R;
import edu.usc.csci310.focus.focus.dataobjects.Profile;
import edu.usc.csci310.focus.focus.managers.ProfileManager;

/**
 * Created by Briana on 10/6/17.
 */

public class ProfileListViewAdapter extends ArrayAdapter<Profile> {
    protected ArrayList<Profile> profiles;

    /**
     * Update the data set and reload the view.
     * @param profiles The new list of profiles to display.
     */
    public void setProfiles(@NonNull ArrayList<Profile> profiles) {
        this.profiles.clear();
        this.profiles.addAll(profiles);
        this.notifyDataSetChanged();
    }

    public ProfileListViewAdapter(Activity activity, int id, ArrayList<Profile> objects){
        super(activity, 0, objects);
        this.profiles = objects;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        // Get the data item for this position
        Profile profile = profiles.get(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder = null; // view lookup cache stored in tag
        //create new row view if null
        if (view == null) {
            // If there's no view to re-use, inflate a brand new view for row
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.profile_list_item, parent, false);
            viewHolder = new ViewHolder(view);
            // Cache the viewHolder object inside the fresh view
            view.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) view.getTag();
        }
        // Populate the data from the data object via the viewHolder object
        // into the template view.
        viewHolder.profileName.setText(profile.getName());

        //set the toggle button listener
        ToggleButton toggle = (ToggleButton) view.findViewById(R.id.toggle_profile_button);
        toggle.setOnCheckedChangeListener(null);
        //toggle.setChecked(profile.getIsActive());

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Profile profile = profiles.get(position);
                profile.setIsActive(isChecked);
                ProfileManager.getDefaultManager().setProfile(profile);
            }
        });

        // Return the completed view to render on screen
        return view;
    }

    // View lookup cache that populates the listview
    public class ViewHolder {
        TextView profileName;
        //ArrayList of ImageViews w/ app icons
        public ViewHolder (View v){
            profileName = (TextView)v.findViewById(R.id.profile_list_name);
        }
    }
}