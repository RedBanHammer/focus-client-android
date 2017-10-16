package edu.usc.csci310.focus.focus.dataobjects;

import java.lang.reflect.Array;
import java.util.ArrayList;
import edu.usc.csci310.focus.focus.managers.ProfileManager;

/**
 * A profile holds a collection of blocked apps.
 */

public class Profile extends NamedObject {
    private static final long serialVersionUID = 1L;

    private ArrayList<App> apps = new ArrayList<App>();

    /**
     * Create a new profile with a specific name.
     * @param name The name of the profile.
     */
    public Profile(String name) {
        super(name);
    }

    public void setApps(ArrayList<App> apps)
    {
        this.apps = apps;
    }

    public void addApp(App app) {
        this.apps.add(app);
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
        }
    }

    public ArrayList<App> getApps() { return this.apps; }
}
