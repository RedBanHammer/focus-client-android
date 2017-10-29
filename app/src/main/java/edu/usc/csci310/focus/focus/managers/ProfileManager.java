package edu.usc.csci310.focus.focus.managers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import edu.usc.csci310.focus.focus.dataobjects.App;
import edu.usc.csci310.focus.focus.dataobjects.Profile;
import edu.usc.csci310.focus.focus.dataobjects.Schedule;
import edu.usc.csci310.focus.focus.storage.StorageManager;

/**
 * Profile Manager. Controls activities of profiles.
 */

public class ProfileManager {
    private static ProfileManager defaultManager = new ProfileManager();
    public static final String PROFILE_GROUP_IDENTIFIER = "Profiles";

    public static @NonNull ProfileManager getDefaultManager() {
        return defaultManager;
    }

    public WeakReference<ProfileManagerDelegate> delegate;

    private StorageManager storageManager = StorageManager.getDefaultManager();
    private ScheduleManager scheduleManager = ScheduleManager.getDefaultManager();
    /*
     * Profile Manager constructor.
     */
    private ProfileManager() {
        //constructor
    }
    public ProfileManager(StorageManager storageManager, ProfileManagerDelegate delegate, ScheduleManager scheduleManager) {
        this.storageManager = storageManager;
        this.delegate = new WeakReference<ProfileManagerDelegate>(delegate);
        this.scheduleManager = scheduleManager;
    }
    /*
    * setProfile() method. Takes in a Profile object as parameter.
    */
    public void setProfile(Profile profile)
    {
        // update with storageManager manager
        storageManager.setObject(profile, "Profiles", profile.getIdentifier());

        // notify delegate
        ProfileManagerDelegate delegateRef = this.delegate.get();
        if (delegateRef != null) {
            delegateRef.managerDidUpdateProfile(this, profile);
        }
    }

    /*
    * removeProfile() method. Takes in a Profile object as parameter.
    */
    public void removeProfile(Profile profile)
    {
        this.removeProfileWithIdentifier(profile.getIdentifier());
    }

    /**
     * Remove a profile given its string identifier.
     * @param identifier The string identifier of the profile to remove.
     */
    public void removeProfileWithIdentifier(String identifier) {
        Profile removedProfile = this.getProfileWithIdentifier(identifier);

        if (removedProfile != null) {
            this.storageManager.removeObject(PROFILE_GROUP_IDENTIFIER, identifier);

            ArrayList<Schedule> schedules = scheduleManager.getAllSchedules();
            for (Schedule schedule : schedules) {
                schedule.removeProfileWithIdentifier(identifier);
                scheduleManager.setSchedule(schedule);
            }

            ProfileManagerDelegate delegateRef = this.delegate.get();
            if (delegateRef != null) {
                delegateRef.managerDidRemoveProfile(this, removedProfile);
            }
        }
    }

    /*
    * ----------------------------GETTERS------------------------------------
    * */

    /*
    * getProfileWithName() method. Takes in a string with profile name,
    * returns the corresponding Profile object.
    */
    public @Nullable Profile getProfileWithName(String name)
    {
        ArrayList<Serializable> serials = this.storageManager.getObjectsWithPrefix(PROFILE_GROUP_IDENTIFIER);
        ArrayList<Profile> profiles = new ArrayList<Profile>();

        for (Serializable obj : serials) {
            if (obj != null) {
                profiles.add((Profile) obj);
            }
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
    public @Nullable Profile getProfileWithIdentifier(String identifier)
    {
        return storageManager.getObject(PROFILE_GROUP_IDENTIFIER, identifier);
    }

    /*
    * getProfilesWithNames() method. Takes in a string with profile name,
    * returns the corresponding Profile objects in an array.
    */
    public @NonNull ArrayList<Profile> getProfilesWithNames(String appName)
    {
        ArrayList<Serializable> serials = storageManager.getObjectsWithPrefix(PROFILE_GROUP_IDENTIFIER);
        ArrayList<Profile> profiles = new ArrayList<Profile>();

        for (Serializable obj : serials) {
            if (obj != null) {
                profiles.add((Profile) obj);
            }
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
    public @NonNull ArrayList<Profile> getProfileWithIdentifiers(String appBundleID)
    {
        ArrayList<Serializable> serials = this.storageManager.getObjectsWithPrefix(PROFILE_GROUP_IDENTIFIER);
        ArrayList<Profile> profiles = new ArrayList<Profile>();

        for (Serializable obj : serials) {
            if (obj != null) {
                profiles.add((Profile) obj);
            }
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
    public @NonNull ArrayList<Profile> getAllProfiles()
    {
        ArrayList<Serializable> serials = this.storageManager.getObjectsWithPrefix(PROFILE_GROUP_IDENTIFIER);
        ArrayList<Profile> profiles = new ArrayList<Profile>();

        for (Serializable obj : serials) {
            if (obj != null) {
                profiles.add((Profile) obj);
            }
        }

        return profiles;
    }
}
