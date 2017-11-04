package edu.usc.csci310.focus.focus.presentation;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Pair;

import java.util.ArrayList;

import edu.usc.csci310.focus.focus.dataobjects.App;
import edu.usc.csci310.focus.focus.dataobjects.Profile;
import edu.usc.csci310.focus.focus.dataobjects.RecurringTime;
import edu.usc.csci310.focus.focus.dataobjects.Schedule;
import edu.usc.csci310.focus.focus.dataobjects.ScheduledProfile;
import edu.usc.csci310.focus.focus.managers.ProfileManager;
import edu.usc.csci310.focus.focus.managers.ScheduleManager;


/**
 * Created by Ashley Walker on 10/14/2017.
 */

public class AppBlockedPopup extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String appName = getActivity().getIntent().getExtras().getString("appName");

        ArrayList<Schedule> schedules = ScheduleManager.getDefaultManager().getActiveSchedules();
        ArrayList<String> blockingSchedules = new ArrayList<String>();

        ArrayList<Profile> profiles = ProfileManager.getDefaultManager().getAllProfiles();
        ArrayList<String> blockingProfiles = new ArrayList<String>();

        for (Schedule s : schedules) {
            ArrayList<ScheduledProfile> scheduledProfiles = s.getScheduledProfiles();
            if (scheduledProfiles.size() == 0) {
                continue;
            }

            String profileIdentifier = scheduledProfiles.get(0).identifier;

            if (s.getName().equals(profileIdentifier + Schedule.TIMER_SCHEDULE_POSTFIX)) {
                continue;
            }

            for (String pIdentifier : s.getActiveProfileIdentifiers(ProfileManager.getDefaultManager())) {
                Profile p = ProfileManager.getDefaultManager().getProfileWithIdentifier(pIdentifier);
                for (App a : p.getApps()) {
                    if (a.getName().equals(appName) && !blockingSchedules.contains(s.getName())) {
                        blockingSchedules.add(s.getName());
                        continue;
                    }
                }
                continue;
            }
        }

        for (Profile p : profiles) {
            for (App a : p.getApps()) {
                if (a.getName().equals(appName) && !blockingProfiles.contains(p.getName())) {
                    blockingProfiles.add(p.getName());
                    continue;
                }
            }
        }

        String message = appName + " is currently being blocked by ";

        if (blockingSchedules.size() > 0) {
            message = message + "the following schedule" +
                    (blockingSchedules.size() != 1 ? "s" : "") + ":\n\n";

            for (String s : blockingSchedules) {
                message = message + "• " + s + "\n";
            }
            if (blockingProfiles.size() > 0) {
                message = message + "\nand ";
            }
        }

        if (blockingProfiles.size() > 0) {
            message = message + "the following profile" +
                    (blockingProfiles.size() != 1 ? "s" : "") + ":\n\n";

            for (String p : blockingProfiles) {
                message = message + "• " + p + "\n";
            }
        }

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Blocked " + appName)
                .setMessage(message)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Positive response
                        getActivity().finish();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

}
