package edu.usc.csci310.focus.focus.dataobjects;

import java.lang.reflect.Array;
import java.util.ArrayList;
import edu.usc.csci310.focus.focus.managers.ProfileManager;

/**
 * A profile holds a collection of blocked apps.
 */

public class Profile extends NamedObject {
    private ArrayList<App> apps = new ArrayList<App>();
    private boolean isActive;

    /**
     * Create a new profile with a specific name.
     * @param name The name of the profile.
     */
    public Profile(String name) {
        super(name);
        ProfileManager.getDefaultManager().setProfile(this);
    }

    public void setApps(ArrayList<App> apps)
    {
        this.apps = apps;
    }

    public void addApp(App app) {
        this.apps.add(app);
        ProfileManager.getDefaultManager().setProfile(this);
    }

    public void removeAppWithName(String name) {
        int index = -1;
        for (int i = 0; i < apps.size(); i++) {
            if (this.apps.get(i).getName().equalsIgnoreCase(name)) {
                index = i;
            }
        }
        if (index >= 0) {
            this.apps.remove(index);
            ProfileManager.getDefaultManager().setProfile(this);
        }
    }

    public void removeAppWithIdentifier(String bundleID) {
        int index = -1;
        for (int i = 0; i < apps.size(); i++) {
            if (this.apps.get(i).getIdentifier().equals(bundleID)) {
                index = i;
            }
        }
        if (index >= 0) {
            this.apps.remove(index);
            ProfileManager.getDefaultManager().setProfile(this);
        }
    }

    public ArrayList<App> getApps() { return this.apps; }

    public void setIsActive(boolean flag) {
        this.isActive = flag;
        ProfileManager.getDefaultManager().setProfile(this);
    }

    public boolean getIsActive() { return this.isActive; }
}
