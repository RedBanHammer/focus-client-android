package edu.usc.csci310.focus.focus.managers;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import edu.usc.csci310.focus.focus.dataobjects.Profile;
import edu.usc.csci310.focus.focus.dataobjects.ProfileStat;
import edu.usc.csci310.focus.focus.storage.StorageManager;

/**
 * Handles storing and retrieving statistics.
 */

public class StatsManager {
    private static StatsManager defaultManager = new StatsManager();
    public static final String STATS_GROUP_IDENTIFIER = "profile-stats";

    public WeakReference<StatsManagerDelegate> delegate;

    public static @NonNull StatsManager getDefaultManager() {
        return defaultManager;
    }

    private StorageManager storageManager = StorageManager.getDefaultManager();

    /**
     * Constructor.
     */
    private StatsManager() {
        //constructor
    }
    public StatsManager(@NonNull StorageManager storageManager) {
        this.storageManager = storageManager;
    }

    /**
     * Forcefully set the app usage statistics for a profile. Overwrites previously saved stats.
     * @param obj A profile statistics container object.
     */
    public void setProfileStat(@NonNull ProfileStat obj) {
        storageManager.setObject(obj, STATS_GROUP_IDENTIFIER, obj.getIdentifier());

        if (delegate != null) {
            StatsManagerDelegate delegateRef = delegate.get();
            if (delegateRef != null) {
                delegateRef.managerDidUpdateProfileStat(this, obj);
            }
        }
    }

    /**
     * Add a focused interval into storage given a profile identifier. If no previous stats
     * existed for the profile, the manager creates a new one.
     * @param profileIdentifier
     * @param startTime
     * @param duration
     */
    public void addFocusedIntervalWithProfileIdentifier(@NonNull String profileIdentifier,
                                                        @NonNull Calendar startTime,
                                                        @NonNull Long duration) {
        ProfileStat stat = (ProfileStat) storageManager.getObject(STATS_GROUP_IDENTIFIER, profileIdentifier);
        if (stat == null) {
            // Create a new stats object.
            stat = new ProfileStat(profileIdentifier);
            stat.addFocusedInterval(startTime, duration);
        } else {
            // Combine with the previous stat object.
            stat.addFocusedInterval(startTime, duration);
        }

        // Save to disk.
        setProfileStat(stat);
    }

    /**
     * Invalidate any focused intervals for a profile stat object. Does nothing if no stats object
     * currently exists.
     * @param profileIdentifier The identifier of the profile to invalidate the focused interval from.
     */
    public void invalidateFocusedIntervalWithProfileIdentifier(@NonNull String profileIdentifier) {
        ProfileStat stat = (ProfileStat) storageManager.getObject(STATS_GROUP_IDENTIFIER, profileIdentifier);

        if (stat != null) {
            stat.invalidateFocusInterval();
            setProfileStat(stat);
        }
    }

    /**
     * Get all saved profile statistics containers with all unfiltered data.
     * @return An ArrayList of profile statistics containers.
     */
    public @NonNull ArrayList<ProfileStat> getAllProfileStats() {
        ArrayList<Serializable> rawProfileStats = storageManager.getObjectsWithPrefix(STATS_GROUP_IDENTIFIER);
        ArrayList<ProfileStat> profileStats = new ArrayList<>();

        for (Serializable rawProfileStat : rawProfileStats) {
            ProfileStat profileStat = (ProfileStat) rawProfileStat;
            Profile profile = ProfileManager.getDefaultManager().getProfileWithIdentifier(profileStat.getIdentifier());

            if (profile != null) {
                profileStats.add(profileStat);
            }
        }

        return profileStats;
    }

    /**
     * Remove all profile statistics from storage.
     */
    public void removeAllProfileStats() {
        storageManager.getDefaultManager().removeObjectsWithPrefix(STATS_GROUP_IDENTIFIER);
    }

    /**
     * Remove a specific stats object given its profile identifier.
     * @param identifier The identifier of the profile (and stats object) to remove.
     */
    public void removeProfileStatWithIdentifier(@NonNull String identifier) {
        storageManager.getDefaultManager().removeObject(STATS_GROUP_IDENTIFIER, identifier);

        StatsManagerDelegate delegateRef = delegate.get();
        if (delegateRef != null) {
            delegateRef.managerDidRemoveProfileStat(this);
        }
    }
}
