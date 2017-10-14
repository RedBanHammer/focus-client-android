package edu.usc.csci310.focus.focus.managers;

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

    public static ScheduleManager getDefaultManager() {
        return defaultManager;
    }

    public WeakReference<ScheduleManagerDelegate> delegate;

    private StorageManager storage = StorageManager.getDefaultManager();

    /*
    * Constructor for Schedule Manager
    * */
    public void ScheduleManager(){

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

    /*
   * getSchedulesInTimeInterval method, takes in a TimeInterval, returns array of Schedule objects.
   *  ----perhaps we can use java.time class to replace a timeinterval object? will need further consideration-----
   * */
    public ArrayList<Schedule> getSchedulesInTimeInterval(TimeInterval timeinterval)
    {
        // Unsure what to do here?
        return null;
    }

    /*
    * getScheduleWithName() method. Takes in a string with schedule name,
    * returns the corresponding Schedule object.
    */
    public Schedule getScheduleWithName(String name)
    {
        ArrayList<Serializable> serials = storage.getObjectsWithPrefix("Schedules");
        ArrayList<Schedule> schedules = new ArrayList<Schedule>();

        for (Serializable o : serials) {
            schedules.add((Schedule) o);
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
    public ArrayList<Schedule> getScheduleWithIdentifier(String identifier)
    {
        return storage.getObject("Schedules", identifier);
    }

    /*
    * getAllSchedules method, returns an array of all Schedule objects
    * */
    public ArrayList<Schedule> getAllSchedules()
    {
        ArrayList<Serializable> serials = storage.getObjectsWithPrefix("Schedules");
        ArrayList<Schedule> schedules = new ArrayList<Schedule>();

        for (Serializable o : serials) {
            schedules.add((Schedule) o);
        }

        return schedules;
    }


}
