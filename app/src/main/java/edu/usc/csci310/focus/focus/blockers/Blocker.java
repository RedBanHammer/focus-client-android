package edu.usc.csci310.focus.focus.blockers;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashSet;

import edu.usc.csci310.focus.focus.dataobjects.App;

/**
 * Base blocker interface that Blockers should conform to.
 */

public abstract interface Blocker {
    /**
     * Set the list of apps to block by this blocker module.
     * @param apps A set of apps that should be blocked.
     */
    public void setApps(@NonNull HashSet<App> apps);

    /**
     * Start blocking apps.
     */
    public void startBlocking();

    /**
     * Stop blocking apps.
     */
    public void stopBlocking();
}
