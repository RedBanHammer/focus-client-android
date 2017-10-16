package edu.usc.csci310.focus.focus.presentation;
/*
 * SelectAppInterfaceController Class
 *
 * Activity that displays all of the user's Android apps
 */
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.usc.csci310.focus.focus.R;
import edu.usc.csci310.focus.focus.dataobjects.App;

public class SelectApp extends AppCompatActivity implements SelectAppInterface {
    public static final String SELECTED_APPS = "SELECTED_APPS";
    private ArrayList<App> appList = new ArrayList<>();
    private appListAdapter appAdapter;
    private Button selectAppButton;
    /*
     * Renders the list of Android apps
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_app);

        //LOAD THE APPLIST WITH EVERY APPLICATION ON THE PHONE
        List<PackageInfo> apps = getPackageManager().getInstalledPackages(0);
        int appsize = apps.size();
        for(int i = 0; i < appsize; i++)
        {
            PackageInfo packageInfo = apps.get(i);
            String name = packageInfo.applicationInfo.loadLabel(getPackageManager()).toString();
            String identifier = packageInfo.packageName;
            Drawable appIcon = null;
            try {
                appIcon = getPackageManager().getApplicationIcon(identifier);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            if(!identifier.startsWith("com.android") && !identifier.startsWith("edu.usc.csci310.focus"))
            {
                //create the app object
                App app = new App(name, identifier);
                System.out.println(identifier);

                //add the app object to the array
                appList.add(app);
            }

        }

        //load the apps into the interface
        appAdapter = new appListAdapter(this, appList);

        ListView listView = (ListView) findViewById(R.id.appListView);
        listView.setAdapter(appAdapter);

        //done with selecting apps
        selectAppButton = (Button) findViewById(R.id.selectAppButton);
        selectAppButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //this pulls back up the createprofile interface class.... MAKE SURE IT'S THE SAME ONE AS BEFORE, NOT A BLANK ONE
                Intent i = new Intent();
                ArrayList<App> send = appAdapter.getAppList();
                i.putExtra(SELECTED_APPS, send);
                //i.putExtra(SELECTED_APPS, ";lskdjfl;k");
                setResult(Activity.RESULT_OK, i);
                //SelectApp.super.onBackPressed();
                finish();
            }
        });

    }

    /*
     * Returns whether the user selected an app
     *
     * @param App - the user selected app
     */
    private boolean didSelectApp(App app){
        return false;
    }

    @Override
    public boolean controllerDidSelectApp(SelectApp selectApp, App app) {
        return false;
    }
}
