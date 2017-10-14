package edu.usc.csci310.focus.focus.blockers;

import java.util.ArrayList;

import edu.usc.csci310.focus.focus.dataobjects.App;

/**
 * Base blocker interface that Blockers should conform to.
 */

abstract interface Blocker {
    /**
     * Set the list of apps to block by this blocker module.
     * @param apps An ArrayList of apps that should be blocked.
     */
    public void setApps(ArrayList<App> apps);

    /**
     * Start blocking apps.
     */
    public void startBlocking();

    /**
     * Stop blocking apps.
     */
    public void stopBlocking();
}
