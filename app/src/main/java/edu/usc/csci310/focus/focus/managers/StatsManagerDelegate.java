package edu.usc.csci310.focus.focus.managers;

import edu.usc.csci310.focus.focus.dataobjects.ProfileStat;

/**
 * Delegate for the Stats Manager, used to notify when stats are updated.
 */

abstract public interface StatsManagerDelegate {
    /**
     * The manager updated the profile statistic object.
     * @param manager The calling Stats Manager object.
     * @param stat The statistic object updated by the manager.
     */
    public void managerDidUpdateProfileStat(StatsManager manager, ProfileStat stat);

    /**
     * The manager removed a profile statistic object. Can be called when no stats object was
     * actually removed from on-disk storage (but was attempted.)
     * @param manager Th calling Stats Manager object.
     */
    public void managerDidRemoveProfileStat(StatsManager manager);
}
