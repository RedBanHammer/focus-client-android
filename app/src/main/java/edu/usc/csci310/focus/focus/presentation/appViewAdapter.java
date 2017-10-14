package edu.usc.csci310.focus.focus.presentation;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

import edu.usc.csci310.focus.focus.R;
import edu.usc.csci310.focus.focus.dataobjects.App;

/**
 * THIS ONE IS JUST TO DISPLAY APPS. NO CHECKBOXES
 */

public class appViewAdapter extends ArrayAdapter<App> {

    public appViewAdapter(Context context, ArrayList<App> app) {
        super(context, 0, app);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        App app = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.app_view_layout, parent, false);
        }
        // Lookup view for data population
        TextView appName = (TextView) convertView.findViewById(R.id.appName2);
        ImageView appImage = (ImageView) convertView.findViewById(R.id.appImage2);
        // Populate the data into the template view using the data object
        appName.setText(app.getName());
        appImage.setBackground(app.getIcon());


        // Return the completed view to render on screen
        return convertView;
    }
}

