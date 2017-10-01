package edu.usc.csci310.focus.focus.presentation;

import edu.usc.csci310.focus.focus.dataobjects.Profile;

/**
 * SelectProfileInterfaceControllerDelegate Class
 */

public interface SelectProfileInterface {
    boolean controllerDidSelectProfile(SelectProfile selectProfile, Profile profile);
}
