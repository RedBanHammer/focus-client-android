package edu.usc.csci310.focus.focus.presentation;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;

import edu.usc.csci310.focus.focus.dataobjects.App;
import edu.usc.csci310.focus.focus.dataobjects.Profile;
import edu.usc.csci310.focus.focus.dataobjects.Schedule;
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

        for (Schedule s : schedules) {
            for (String pIdentifier : s.getActiveProfileIdentifiers()) {
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

        String message = appName + " is currently being blocked by the following schedule" +
                (blockingSchedules.size() != 1 ? "s" : "") + ":\n\n";

        for (String s : blockingSchedules) {
            message = message + "• " + s + "\n";
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
