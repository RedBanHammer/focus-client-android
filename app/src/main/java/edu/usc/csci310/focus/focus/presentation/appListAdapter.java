package edu.usc.csci310.focus.focus.presentation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.usc.csci310.focus.focus.R;
import edu.usc.csci310.focus.focus.dataobjects.App;

/**
 * Created by bdeng on 10/6/17.
 */

public class appListAdapter extends ArrayAdapter<App>{

    public appListAdapter(Context context, ArrayList<App> app) {
        super(context, 0, app);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        App app = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.app_list_layout, parent, false);
        }
        // Lookup view for data population
        TextView appName = (TextView) convertView.findViewById(R.id.appName);
        ImageView appImage = (ImageView) convertView.findViewById(R.id.appImage);
        // Populate the data into the template view using the data object
        appName.setText(app.getName());
        appImage.setBackground(app.getIcon());

        //get image method from app?

        // Return the completed view to render on screen
        return convertView;
    }
}
