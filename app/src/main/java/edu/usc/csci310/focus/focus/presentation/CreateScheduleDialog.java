package edu.usc.csci310.focus.focus.presentation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import edu.usc.csci310.focus.focus.R;
import java.util.jar.Attributes;

public class CreateScheduleDialog extends DialogFragment implements TextView.OnEditorActionListener {

    private EditText mEditText;

    public interface NameListener {
        void onFinishUserDialog(String user);
    }
    // Empty constructor required for DialogFragment
    public CreateScheduleDialog() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_schedule_dialog, container);
        mEditText = (EditText) view.findViewById(R.id.schedule_name);

        // set this instance as callback for editor action
        mEditText.setOnEditorActionListener(this);
        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getDialog().setTitle("Provide a name for this schedule");

        return view;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        // Return input text to activity
        NameListener activity = (NameListener) getActivity();
        activity.onFinishUserDialog(mEditText.getText().toString());
        this.dismiss();
        return true;
    }
}
