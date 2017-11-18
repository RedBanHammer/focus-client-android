package edu.usc.csci310.focus.focus.presentation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

import edu.usc.csci310.focus.focus.R;
import edu.usc.csci310.focus.focus.dataobjects.App;
import edu.usc.csci310.focus.focus.dataobjects.Profile;
import edu.usc.csci310.focus.focus.dataobjects.RecurringTime;
import edu.usc.csci310.focus.focus.dataobjects.Schedule;
import edu.usc.csci310.focus.focus.managers.ProfileManager;
import edu.usc.csci310.focus.focus.managers.ScheduleManager;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by Briana on 10/6/17.
 */

public class ProfileListViewAdapter extends ArrayAdapter<Profile> {
    private static final String PLACEHOLDER_TIMER_TEXT = "Not active";

    protected ArrayList<Profile> profiles;
    private View view;
    private Profile profile;

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
        view = convertView;
        // Get the data item for this position
        profile = profiles.get(position);

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


        // add app UX preview of apps in profile
        ArrayList<App> apps = this.profile.getApps();
        ImageView appImage = (ImageView) view.findViewById(R.id.miniApp1);

        for (int i = 0; i < apps.size(); i++) {
            // Populate the data into the template view using the data object
            Drawable icon = null;
            try {
                icon = this.getContext().getPackageManager().getApplicationIcon(apps.get(i).getIdentifier());
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            appImage.setImageDrawable(icon);

            switch(i) {
                case 0:
                    appImage = (ImageView) view.findViewById(R.id.miniApp2);
                    break;
                case 1:
                    appImage = (ImageView) view.findViewById(R.id.miniApp3);
                    break;
                case 2:
                    appImage = (ImageView) view.findViewById(R.id.miniApp4);
                    break;
                case 3:
                    appImage = (ImageView) view.findViewById(R.id.miniApp5);
                    break;
                case 4:
                    appImage = (ImageView) view.findViewById(R.id.miniApp6);
                    break;
                case 5:
                    i = apps.size(); // a way of breaking out of the for loop
                    break;
            }
        }

        Schedule timerSchedule = ScheduleManager.getDefaultManager().getScheduleWithName(this.profile.getIdentifier() + Schedule.TIMER_SCHEDULE_POSTFIX);

        if (timerSchedule != null) {
            ArrayList<Long> timesRemaining = timerSchedule.getTimesRemainingWithProfileIdentifier(profile.getIdentifier());
            if (timesRemaining.size() > 0) {
                Long minutesRemaining = timesRemaining.get(0);
                viewHolder.profileTime.setText(
                        minutesRemaining.toString() +
                                " minute" + (minutesRemaining != 1 ? "s" : "") + " left");
            } else {
                viewHolder.profileTime.setText(PLACEHOLDER_TIMER_TEXT);
            }
        } else {
            viewHolder.profileTime.setText(PLACEHOLDER_TIMER_TEXT);
        }

        //set the toggle button listener
        ToggleButton toggle = (ToggleButton) view.findViewById(R.id.toggle_profile_button);

        boolean profileIsActive = timerSchedule != null;

        toggle.setOnCheckedChangeListener(null);
        toggle.setChecked(profileIsActive);

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton buttonView, boolean isChecked) {
                Profile profile = profiles.get(position);
                profile = ProfileManager.getDefaultManager()
                        .getProfileWithIdentifier(profile.getIdentifier());
                if (isChecked) { // turning on
                    // if turning on, bring up timer dialog
                    Intent intent = new Intent(getContext(), ActivateProfileDialog.class);
                    intent.putExtra("profile", (Serializable) profile);

                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // You need this if starting
                    // the activity from a service
                    //intent.setAction(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);

                    ((Activity) getContext()).startActivityForResult(intent, 0);
                } else { // turning off
                    profile.setIsActive(false);
                    ProfileManager.getDefaultManager().setProfile(profile);

                    // find schedule with profile.name + " Timer"
                    Schedule s = ScheduleManager.getDefaultManager().
                            getScheduleWithName(profile.getIdentifier() + Schedule.TIMER_SCHEDULE_POSTFIX);
                    // delete it

                    ScheduleManager.getDefaultManager().removeSchedule(s);

                    notifyDataSetChanged();
                }
            }
        });

        // Return the completed view to render on screen
        return view;
    }

    // View lookup cache that populates the listview
    public class ViewHolder {
        TextView profileName;
        TextView profileTime;

        //ArrayList of ImageViews w/ app icons
        public ViewHolder (View v){
            this.profileName = (TextView)v.findViewById(R.id.profile_list_name);
            this.profileTime = (TextView)v.findViewById(R.id.profile_time_remaining);
        }
    }
}