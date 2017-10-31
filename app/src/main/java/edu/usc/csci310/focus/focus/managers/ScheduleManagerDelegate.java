package edu.usc.csci310.focus.focus.managers;

import android.support.annotation.Nullable;

import edu.usc.csci310.focus.focus.dataobjects.Schedule;


/**
 * Delegate for ScheduleManager
 */

abstract interface ScheduleManagerDelegate {

    /*
    * managerDidUpdateSchedule method. Takes in a ScheduleManager object and a Schedule object
    */
    public void managerDidUpdateSchedule(ScheduleManager manager, Schedule schedule);

    /*
    * managerDidRemoveSchedule method. Takes in a ScheduleManager object and a Schedule object
    */
    public void managerDidRemoveSchedule(ScheduleManager manager, @Nullable Schedule schedule);
}
