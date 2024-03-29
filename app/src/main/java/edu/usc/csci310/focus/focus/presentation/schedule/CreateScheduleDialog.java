package edu.usc.csci310.focus.focus.presentation.schedule;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import edu.usc.csci310.focus.focus.R;
import edu.usc.csci310.focus.focus.dataobjects.App;
import edu.usc.csci310.focus.focus.dataobjects.Profile;
import edu.usc.csci310.focus.focus.dataobjects.Schedule;
import edu.usc.csci310.focus.focus.managers.ScheduleManager;

import java.util.ArrayList;
import java.util.jar.Attributes;

public class CreateScheduleDialog extends DialogFragment implements TextView.OnEditorActionListener {

    private EditText mEditText;

    // Empty constructor required for DialogFragment
    public CreateScheduleDialog() {}

    public static CreateScheduleDialog newInstance(String name) {
        
        Bundle args = new Bundle();
        CreateScheduleDialog fragment = new CreateScheduleDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_create_schedule_dialog, null);

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Positive response
                        if (mEditText.getText().toString().equals("")){
                            showFormCompletionError("Incomplete Form", "Valid name is needed for the schedule");
                        } else if(duplicateName()){
                            showFormCompletionError("Invalid Name", "Name already exists");
                        } else {
                            sendBackResult();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // negative response
                        // do nothing
                    }
                });

        Dialog dialog = builder.create();

        mEditText = (EditText) view.findViewById(R.id.create_schedule_name);

        // set this instance as callback for editor action
        mEditText.setOnEditorActionListener(this);
        mEditText.requestFocus();

        //dialog.getWindow().setSoftInputMode(
          //      WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setTitle("Provide a name for this schedule");
        return dialog;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        return true;
    }
    // Defines the listener interface
    public interface EditNameDialogListener {
        void onFinishEditDialog(String inputText);
    }
    private boolean duplicateName(){
        ArrayList<Schedule> schedules = ScheduleManager.getDefaultManager().getAllSchedules();
        for (Schedule sched: schedules){
            if (sched.getName().equals(mEditText.getText().toString())){
                return true;
            }
        }
        return false;
    }
    // Call this method to send the data back to the parent fragment
    public void sendBackResult() {
        // Notice the use of `getTargetFragment` which will be set when the dialog is displayed
        EditNameDialogListener listener = (EditNameDialogListener) getTargetFragment();
        listener.onFinishEditDialog(mEditText.getText().toString());
        dismiss();
    }

    private void showFormCompletionError(String title, String message) {
        new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which) {

                    }

                })
                .show();
    }

}
