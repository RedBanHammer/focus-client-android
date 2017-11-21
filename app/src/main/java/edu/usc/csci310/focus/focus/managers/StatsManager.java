package edu.usc.csci310.focus.focus.managers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import edu.usc.csci310.focus.focus.dataobjects.AchievementStat;
import edu.usc.csci310.focus.focus.dataobjects.Profile;
import edu.usc.csci310.focus.focus.dataobjects.ProfileStat;
import edu.usc.csci310.focus.focus.dataobjects.StreakStat;
import edu.usc.csci310.focus.focus.storage.StorageManager;

/**
 * Handles storing and retrieving statistics.
 */

public class StatsManager {
    private static StatsManager defaultManager = new StatsManager();
    public static final String STATS_GROUP_IDENTIFIER = "profile-stats";

    public static final String STREAKS_GROUP_IDENTIFIER = "streaks";
    public static final String DAILY_STREAK_IDENTIFIER = "daily-streaks";
    public static final String WEEKLY_STREAK_IDENTIFIER = "weekly-streaks";
    public static final String MONTHLY_STREAK_IDENTIFIER = "monthly-streaks";
    public static final String YEARLY_STREAK_IDENTIFIER = "yearly-streaks";

    public static final String ACHIEVEMENT_GROUP_IDENTIFIER = "achievements";



    public WeakReference<StatsManagerDelegate> delegate;

    public static @NonNull StatsManager getDefaultManager() {
        return defaultManager;
    }

    public void setDelegate(StatsManagerDelegate delegate){
        this.delegate = new WeakReference<StatsManagerDelegate>(delegate);
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


    /** Profile Statistics **/
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
        storageManager.removeObjectsWithPrefix(STATS_GROUP_IDENTIFIER);
    }

    /**
     * Remove a specific stats object given its profile identifier.
     * @param identifier The identifier of the profile (and stats object) to remove.
     */
    public void removeProfileStatWithIdentifier(@NonNull String identifier) {
        storageManager.removeObject(STATS_GROUP_IDENTIFIER, identifier);

        StatsManagerDelegate delegateRef = delegate.get();
        if (delegateRef != null) {
            delegateRef.managerDidRemoveProfileStat(this);
        }
    }





    /** Streak Statistics **/

    /**
     * Set the daily streak to a specific value and save it to disk.
     * @param count The value to set the daily streak stat to.
     */
    public void setDailyStreak(@NonNull Integer count) {
        StreakStat stat = storageManager.getObject(STREAKS_GROUP_IDENTIFIER, DAILY_STREAK_IDENTIFIER);
        if (stat != null) {
            stat.setCount(count);
        } else {
            stat = new StreakStat(DAILY_STREAK_IDENTIFIER, count);
        }

        storageManager.setObject(stat, STREAKS_GROUP_IDENTIFIER, DAILY_STREAK_IDENTIFIER);
    }

    /**
     * Get the current daily streak value.
     * @return An Integer representing the current daily streak.
     */
    public Integer getDailyStreak() {
        StreakStat stat = storageManager.getObject(STREAKS_GROUP_IDENTIFIER, DAILY_STREAK_IDENTIFIER);
        if (stat != null) {
            return stat.getCount();
        } else {
            return 0;
        }
    }

    /**
     * Increment the daily streak stat object by one and save it to disk.
     */
    public void incrementDailyStreak() {
        StreakStat stat = storageManager.getObject(STREAKS_GROUP_IDENTIFIER, YEARLY_STREAK_IDENTIFIER);

        if (stat != null &&
                (Calendar.getInstance().compareTo(stat.getTimestamp()) <= 0 ||
                        Calendar.getInstance().getTimeInMillis() - stat.getTimestamp().getTimeInMillis() < 24*60*60*1000)) {
            return;
        }

        Integer streak = getDailyStreak() + 1;
        setDailyStreak(streak);
    }




    /**
     * Set the weekly streak to a specific value and save it to disk.
     * @param count The value to set the weekly streak stat to.
     */
    public void setWeeklyStreak(@NonNull Integer count) {
        StreakStat stat = storageManager.getObject(STREAKS_GROUP_IDENTIFIER, WEEKLY_STREAK_IDENTIFIER);
        if (stat != null) {
            stat.setCount(count);
        } else {
            stat = new StreakStat(WEEKLY_STREAK_IDENTIFIER, count);
        }

        storageManager.setObject(stat, STREAKS_GROUP_IDENTIFIER, WEEKLY_STREAK_IDENTIFIER);
    }

    /**
     * Get the current weekly streak value.
     * @return An Integer representing the current weekly streak.
     */
    public Integer getWeeklyStreak() {
        StreakStat stat = storageManager.getObject(STREAKS_GROUP_IDENTIFIER, WEEKLY_STREAK_IDENTIFIER);
        if (stat != null) {
            return stat.getCount();
        } else {
            return 0;
        }
    }

    /**
     * Increment the weekly streak stat object by one and save it to disk.
     */
    public void incrementWeeklyStreak() {
        StreakStat stat = storageManager.getObject(STREAKS_GROUP_IDENTIFIER, WEEKLY_STREAK_IDENTIFIER);

        if (stat != null &&
                (Calendar.getInstance().compareTo(stat.getTimestamp()) <= 0 ||
                        daysBetween(Calendar.getInstance(), stat.getTimestamp()) < 7)) {
            return;
        }

        Integer streak = getWeeklyStreak() + 1;
        setWeeklyStreak(streak);
    }





    /**
     * Set the monthly streak to a specific value and save it to disk.
     * @param count The value to set the monthly streak stat to.
     */
    public void setMonthlyStreak(@NonNull Integer count) {
        StreakStat stat = storageManager.getObject(STREAKS_GROUP_IDENTIFIER, MONTHLY_STREAK_IDENTIFIER);
        if (stat != null) {
            stat.setCount(count);
        } else {
            stat = new StreakStat(MONTHLY_STREAK_IDENTIFIER, count);
        }

        storageManager.setObject(stat, STREAKS_GROUP_IDENTIFIER, MONTHLY_STREAK_IDENTIFIER);
    }

    /**
     * Get the current monthly streak value.
     * @return An Integer representing the current monthly streak.
     */
    public Integer getMonthlyStreak() {
        StreakStat stat = storageManager.getObject(STREAKS_GROUP_IDENTIFIER, MONTHLY_STREAK_IDENTIFIER);
        if (stat != null) {
            return stat.getCount();
        } else {
            return 0;
        }
    }

    /**
     * Increment the monthly streak stat object by one and save it to disk.
     */
    public void incrementMonthlyStreak() {
        StreakStat stat = storageManager.getObject(STREAKS_GROUP_IDENTIFIER, MONTHLY_STREAK_IDENTIFIER);

        if (stat != null &&
                (Calendar.getInstance().compareTo(stat.getTimestamp()) <= 0 ||
                        daysBetween(Calendar.getInstance(), stat.getTimestamp()) < 30)) {
            return;
        }

        Integer streak = getMonthlyStreak() + 1;
        setMonthlyStreak(streak);
    }




    /**
     * Set the yearly streak to a specific value and save it to disk.
     * @param count The value to set the yearly streak stat to.
     */
    public void setYearlyStreak(@NonNull Integer count) {
        StreakStat stat = storageManager.getObject(STREAKS_GROUP_IDENTIFIER, YEARLY_STREAK_IDENTIFIER);
        if (stat != null) {
            stat.setCount(count);
        } else {
            stat = new StreakStat(YEARLY_STREAK_IDENTIFIER, count);
        }

        storageManager.setObject(stat, STREAKS_GROUP_IDENTIFIER, YEARLY_STREAK_IDENTIFIER);
    }

    /**
     * Get the current yearly streak value.
     * @return An Integer representing the current yearly streak.
     */
    public Integer getYearlyStreak() {
        StreakStat stat = storageManager.getObject(STREAKS_GROUP_IDENTIFIER, YEARLY_STREAK_IDENTIFIER);
        if (stat != null) {
            return stat.getCount();
        } else {
            return 0;
        }
    }

    /**
     * Increment the yearly streak stat object by one and save it to disk.
     */
    public void incrementYearlyStreak() {
        StreakStat stat = storageManager.getObject(STREAKS_GROUP_IDENTIFIER, YEARLY_STREAK_IDENTIFIER);

        if (stat != null &&
                (Calendar.getInstance().compareTo(stat.getTimestamp()) <= 0 ||
                daysBetween(Calendar.getInstance(), stat.getTimestamp()) < 365)) {
            return;
        }

        Integer streak = getYearlyStreak() + 1;
        setYearlyStreak(streak);
    }





    /** Achievements **/

    /**
     * Grant the user an achievement.
     * @param identifier A string identifier uniquely identifying the achievement earned.
     */
    public void setAchievement(@NonNull String identifier) {
        AchievementStat achievement = new AchievementStat(identifier);
        storageManager.setObject(achievement, ACHIEVEMENT_GROUP_IDENTIFIER, identifier);
    }

    /**
     * Remove an achievement the user has earned. Can remove non-existent achievements.
     * @param identifier A string identifier of the achievemnt to remove.
     */
    public void removeAchievement(@NonNull String identifier) {
        storageManager.removeObject(ACHIEVEMENT_GROUP_IDENTIFIER, identifier);
    }

    /**
     * Get the achievement statistic given an achievement identifier.
     * @param identifier A string identifier uniquely identifying the achievement earned.
     * @return An AchievementStat if one exists, null if the user has not earned this achievement.
     */
    public @Nullable AchievementStat getAchievement(@NonNull String identifier) {
        return (AchievementStat) storageManager.getObject(ACHIEVEMENT_GROUP_IDENTIFIER, identifier);
    }

    /**
     * Get a list of all achievements the user earned.
     * @return An ArrayList of AchievementStats that the user earned.
     */
    public @NonNull ArrayList<AchievementStat> getAllAchievements() {
        ArrayList<Serializable> rawAchievements = storageManager.getObjectsWithPrefix(ACHIEVEMENT_GROUP_IDENTIFIER);
        ArrayList<AchievementStat> achievements = new ArrayList<>();

        for (Serializable rawAchievement : rawAchievements) {
            achievements.add((AchievementStat) rawAchievement);
        }

        return achievements;
    }



    /** Util **/
    private long daysBetween(Calendar startDate, Calendar endDate) {
        long end = endDate.getTimeInMillis();
        long start = startDate.getTimeInMillis();
        return TimeUnit.MILLISECONDS.toDays(Math.abs(end - start));
    }
}
