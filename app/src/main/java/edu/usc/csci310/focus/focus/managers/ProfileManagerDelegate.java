package edu.usc.csci310.focus.focus.managers;

import edu.usc.csci310.focus.focus.dataobjects.Profile;

/**
 * Delegate for ProfileManager. Handles profile object update events.
 */

abstract interface ProfileManagerDelegate {
    /**
     * Called when the manager updates a profile object.
     * @param manager The ProifileManager that is sending the event.
     * @param profile The profile whose data was updated.
     */
    public void managerDidUpdateProfile(ProfileManager manager, Profile profile);

    /**
     * Called when the manager deletes a profile object.
     * @param manager The ProifileManager that is sending the event.
     * @param profile The profile that was deleted.
     */
    public void managerDidRemoveProfile(ProfileManager manager, Profile profile);
}
