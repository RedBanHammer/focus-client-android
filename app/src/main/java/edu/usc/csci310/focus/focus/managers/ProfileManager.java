package edu.usc.csci310.focus.focus.managers;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import edu.usc.csci310.focus.focus.dataobjects.App;
import edu.usc.csci310.focus.focus.dataobjects.Profile;
import edu.usc.csci310.focus.focus.storage.StorageManager;

/**
 * Profile Manager. Controls activities of profiles.
 */

public class ProfileManager {
    private static ProfileManager defaultManager = new ProfileManager();

    public static ProfileManager getDefaultManager() {
        return defaultManager;
    }

    public WeakReference<ProfileManagerDelegate> delegate;

    private StorageManager storage = StorageManager.getDefaultManager();

    /*
     * Profile Manager constructor.
     */
    public void ProfileManager() {
        //constructor
    }

    /*
    * setProfile() method. Takes in a Profile object as parameter.
    */
    public void setProfile(Profile profile)
    {
        // notify delegate
        ProfileManagerDelegate delegateRef = this.delegate.get();
        delegateRef.managerDidUpdateProfile(this, profile);
        // update with storage manager
        storage.setObject(profile, "Profiles", profile.getIdentifier());
    }

    /*
    * removeProfile() method. Takes in a Profile object as parameter.
    */
    public void removeProfile(Profile profile)
    {
        ProfileManagerDelegate delegateRef = this.delegate.get();
        delegateRef.managerDidRemoveProfile(this, profile);
        storage.removeObject("Profiles", profile.getIdentifier());
    }

    /*
    * ----------------------------GETTERS------------------------------------
    * */

    /*
    * getProfileWithName() method. Takes in a string with profile name,
    * returns the corresponding Profile object.
    */
    public Profile getProfileWithName(String name)
    {
        ArrayList<Serializable> serials = storage.getObjectsWithPrefix("Profiles");
        ArrayList<Profile> profiles = new ArrayList<Profile>();

        for (Serializable o : serials) {
            profiles.add((Profile) o);
        }

        Profile p = null;
        for (Profile prof : profiles) {
            if (prof.getName().equals(name))
                p = prof;
        }

        return p;
    }

    /*
    * getProfileWithIdentifier() method. Takes in a string with identifier,
    * returns the corresponding Profile object.
    */
    public Profile getProfileWithIdentifier(String identifier)
    {
        return storage.getObject("Profiles", identifier);
    }

    /*
    * getProfilesWithNames() method. Takes in a string with profile name,
    * returns the corresponding Profile objects in an array.
    */
    public ArrayList<Profile> getProfilesWithNames(String appName)
    {
        ArrayList<Serializable> serials = storage.getObjectsWithPrefix("Profiles");
        ArrayList<Profile> profiles = new ArrayList<Profile>();

        for (Serializable o : serials) {
            profiles.add((Profile) o);
        }

        ArrayList<App> apps = new ArrayList<App>();
        ArrayList<Profile> profs = new ArrayList<Profile>();

        for (Profile s:profiles) {
            apps = s.getApps();
            for (App a : apps) {
                if (a.getName().equals(appName)) {
                    profs.add(s);
                }
            }
        }

        return profs;
    }

    /*
    * getProfileWithIdentifiers() method. Takes in a string with profile name,
    * returns the corresponding Profile objects in an array.
    */
    public ArrayList<Profile> getProfileWithIdentifiers(String appBundleID)
    {
        ArrayList<Serializable> serials = storage.getObjectsWithPrefix("Profiles");
        ArrayList<Profile> profiles = new ArrayList<Profile>();

        for (Serializable o : serials) {
            profiles.add((Profile) o);
        }

        ArrayList<App> apps = new ArrayList<App>();
        ArrayList<Profile> profs = new ArrayList<Profile>();

        for (Profile s:profiles) {
            apps = s.getApps();
            for (App a : apps) {
                if (a.getIdentifier().equals(appBundleID)) {
                    profs.add(s);
                }
            }
        }

        return profs;
    }
    /*
    * getAllProfiles() method. Returns every Profile object in a Profile array
    */
    public ArrayList<Profile> getAllProfiles()
    {
        ArrayList<Serializable> serials = storage.getObjectsWithPrefix("Profiles");
        ArrayList<Profile> profiles = new ArrayList<Profile>();

        for (Serializable o : serials) {
            profiles.add((Profile) o);
        }

        return profiles;
    }
}
