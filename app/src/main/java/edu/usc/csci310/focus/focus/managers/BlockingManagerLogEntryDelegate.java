package edu.usc.csci310.focus.focus.managers;

/**
 * Callbacks for when the log entries change
 */

public interface BlockingManagerLogEntryDelegate {
    public void blockingManagerDidUpdateLogEntries(BlockingManager blockingManager);
}
