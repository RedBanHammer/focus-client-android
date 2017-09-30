package edu.usc.csci310.focus.focus.dataobjects;

import java.util.ArrayList;

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
    }

    public void addApp(App app) {

    }

    public void removeAppWithName(String name) {

    }

    public void removeAppWithIdentifier(String bundleID) {

    }

    public ArrayList<App> getApps() {
        return null;
    }


    public void setIsActive(boolean flag) {
        this.isActive = flag;
    }

    public boolean getIsActive() {
        return this.isActive;
    }
}
