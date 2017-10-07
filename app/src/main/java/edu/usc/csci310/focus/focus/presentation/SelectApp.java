package edu.usc.csci310.focus.focus.presentation;
/*
 * SelectAppInterfaceController Class
 *
 * Activity that displays all of the user's Android apps
 */
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import edu.usc.csci310.focus.focus.R;
import edu.usc.csci310.focus.focus.dataobjects.App;

public class SelectApp extends AppCompatActivity implements SelectAppInterface {
    private ArrayList<App> appList;
    /*
     * Renders the list of Android apps
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_app);

        //LOAD THE APPLIST WITH EVERY APPLICATION ON THE PHONE
        List<PackageInfo> apps = getPackageManager().getInstalledPackages(0);
        for(int i = 0; i < apps.size(); i++)
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

            //create the app object
            App app = new App(name, identifier);
            app.setIcon(appIcon);

            //add the app object to the array
            appList.add(app);
        }

        //load the apps into the interface
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
