package edu.usc.csci310.focus.focus.managers;

import java.lang.ref.WeakReference;

import edu.usc.csci310.focus.focus.dataobjects.Profile;

/**
 * Profile Manager. Controls activities of profiles.
 */

public class ProfileManager {
    private static ProfileManager defaultManager = new ProfileManager();

    public static ProfileManager getDefaultManager() {
        return defaultManager;
    }

    public WeakReference<ProfileManagerDelegate> delegate;

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

    }

    /*
    * removeProfile() method. Takes in a Profile object as parameter.
    */
    public void removeProfile(Profile profile)
    {

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
        return null;
    }

    /*
    * getProfileWithIdentifier() method. Takes in a string with identifier,
    * returns the corresponding Profile object.
    */
    public Profile getProfileWithIdentifier(String identifier)
    {
        return null;
    }

    /*
    * getProfilesWithNames() method. Takes in a string with profile name,
    * returns the corresponding Profile objects in an array.
    */
    public Profile[] getProfilesWithNames(String appName)
    {
        return null;
    }

    /*
    * getProfileWithIdentifiers() method. Takes in a string with profile name,
    * returns the corresponding Profile objects in an array.
    */
    public Profile[] getProfileWithIdentifiers(String appBundleID)
    {
        return null;
    }
    /*
    * getAllProfiles() method. Returns every Profile object in a Profile array
    */
    public Profile[] getAllProfiles()
    {
        return null;
    }
}
