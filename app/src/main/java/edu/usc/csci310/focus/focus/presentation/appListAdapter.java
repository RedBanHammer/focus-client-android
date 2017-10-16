package edu.usc.csci310.focus.focus.presentation;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
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
import java.util.List;

import edu.usc.csci310.focus.focus.R;
import edu.usc.csci310.focus.focus.dataobjects.App;

/**
 * THIS IS TO SELECT APPS AND SEND TO BACKEND
 */

public class appListAdapter extends ArrayAdapter<App>{
    private ArrayList<App> sendAppList = new ArrayList<>();
    private ArrayList<CheckBox> checkboxes = new ArrayList<>();
    Context context;

    public appListAdapter(Context context, ArrayList<App> app) {
        super(context, 0, app);
        this.context = context;
    }
    Intent intent;


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        App app = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.app_list_layout, parent, false);
        }
        // Lookup view for data population
        final CheckBox appName = (CheckBox) convertView.findViewById(R.id.checkBox1);
        ImageView appImage = (ImageView) convertView.findViewById(R.id.appImage);
        // Populate the data into the template view using the data object
        appName.setText(app.getName());
        Drawable icon = null;
        try {
            icon = context.getPackageManager().getApplicationIcon(app.getIdentifier());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        appImage.setImageDrawable(icon);
        appName.setTag(app);

        //set checkboxes from array
        appName.setChecked(this.sendAppList.contains(app));

        //checkbox listener
        appName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    checkboxes.add(appName);
                    //get the app
                    App taggedApp = (App) appName.getTag();
                    //store it to send to next activity (Profile interface?)
                    if(!sendAppList.contains(taggedApp))
                    {
                        sendAppList.add(taggedApp);

                    }

                }
                else if (!isChecked)
                {
                    App taggedApp = (App) appName.getTag();
                    if(sendAppList.contains(taggedApp))
                    {
                        sendAppList.remove(sendAppList.indexOf(taggedApp));

                    }
                }
            }
        });


        // Return the completed view to render on screen
        return convertView;
    }

    public ArrayList<App> getAppList()
    {
        return sendAppList;
    }

    public void clearCheckboxes() {
        for(CheckBox a : checkboxes)
        {
            a.setChecked(false);
        }


    }
}
