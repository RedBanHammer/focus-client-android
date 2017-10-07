package edu.usc.csci310.focus.focus.presentation;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.usc.csci310.focus.focus.R;
import edu.usc.csci310.focus.focus.dataobjects.Profile;

/**
 * Created by Briana on 10/6/17.
 */

public class ProfileListViewAdapter extends ArrayAdapter<Profile> {
    protected ArrayList<Profile> profiles;

    // View lookup cache that populates the listview
    private static class ViewHolder {
        TextView profileName;
        //ArrayList of ImageViews w/ app icons
    }

    public ProfileListViewAdapter(Activity activity, int id, ArrayList<Profile> objects){
        super(activity, R.layout.profile_list_item, objects);
        profiles= objects;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        Profile profile = profiles.get(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        //create new row view if null
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.profile_list_item, parent, false);
            viewHolder.profileName = (TextView) convertView.findViewById(R.id.profile_list_name);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data from the data object via the viewHolder object
        // into the template view.
        viewHolder.profileName.setText(profile.getName());
        // Return the completed view to render on screen
        return convertView;
    }
}