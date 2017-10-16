package edu.usc.csci310.focus.focus.managers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import edu.usc.csci310.focus.focus.dataobjects.Profile;
import edu.usc.csci310.focus.focus.dataobjects.Schedule;
import edu.usc.csci310.focus.focus.dataobjects.TimeInterval;
import edu.usc.csci310.focus.focus.storage.StorageManager;


/**
 * Schedule Manager object. Handles events for schedules.
 */

public class ScheduleManager {
    private static ScheduleManager defaultManager = new ScheduleManager();

    public static @NonNull ScheduleManager getDefaultManager() {
        return defaultManager;
    }

    public WeakReference<ScheduleManagerDelegate> delegate;

    private StorageManager storage = StorageManager.getDefaultManager();

    /*
    * Constructor for Schedule Manager
    * */
    private void ScheduleManager(){

    }

    /*
    * setSchedule method, takes in a Schedule object
    * */
    public void setSchedule(Schedule schedule)
    {
        ScheduleManagerDelegate delegateRef = this.delegate.get();
        delegateRef.managerDidUpdateSchedule(this, schedule);
        storage.setObject(schedule, "Schedules", schedule.getIdentifier());
    }

    /*
   * removeSchedule method, takes in a string.
   * */
    public void removeSchedule(String scheduleidentifier)
    {
        //((ScheduleManagerDelegate) delegate).managerDidRemoveSchedule(this, schedule);
        storage.removeObject("Schedules", scheduleidentifier);
    }
    /*
    * -------------------GETTERS-------------------------------
    */

    /**
     * Get all schedules that are active.
     * @return An ArrayList of active schedules.
     */
    public @NonNull ArrayList<Schedule> getActiveSchedules() {
        ArrayList<Schedule> activeSchedules = new ArrayList<Schedule>();

        for (Schedule schedule : this.getAllSchedules()) {
            if (schedule.getIsActive()) {
                activeSchedules.add(schedule);
            }
        }

        return activeSchedules;
    }

    /*
    * getScheduleWithName() method. Takes in a string with schedule name,
    * returns the corresponding Schedule object.
    */
    public @Nullable Schedule getScheduleWithName(String name)
    {
        ArrayList<Serializable> serials = storage.getObjectsWithPrefix("Schedules");
        ArrayList<Schedule> schedules = new ArrayList<Schedule>();

        for (Serializable obj : serials) {
            if (obj != null) {
                schedules.add((Schedule) obj);
            }
        }

        Schedule s = null;
        for (Schedule sched : schedules) {
            if (sched.getName().equals(name))
                s = sched;
        }

        return s;
    }

    /*
   * getSchedule method, takes in a Schedule object
   * */
    public @Nullable ArrayList<Schedule> getScheduleWithIdentifier(String identifier)
    {
        return storage.getObject("Schedules", identifier);
    }

    /*
    * getAllSchedules method, returns an array of all Schedule objects
    * */
    public @NonNull ArrayList<Schedule> getAllSchedules()
    {
        ArrayList<Serializable> serials = storage.getObjectsWithPrefix("Schedules");
        ArrayList<Schedule> schedules = new ArrayList<Schedule>();

        for (Serializable obj : serials) {
            if (obj != null) {
                schedules.add((Schedule) obj);
            }
        }

        return schedules;
    }
}
