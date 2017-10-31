package edu.usc.csci310.focus.focus.managers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import edu.usc.csci310.focus.focus.dataobjects.Schedule;
import edu.usc.csci310.focus.focus.storage.StorageManager;


/**
 * Schedule Manager object. Handles events for schedules.
 */

public class ScheduleManager {
    public static final String SCHEDULE_GROUP_IDENTIFIER = "Schedules";

    private static ScheduleManager defaultManager = new ScheduleManager();


    public static @NonNull ScheduleManager getDefaultManager() {
        return defaultManager;
    }

    public WeakReference<ScheduleManagerDelegate> delegate;

    private StorageManager storageManager = StorageManager.getDefaultManager();

    /*
    * Constructor for Schedule Manager
    * */
    private ScheduleManager(){

    }

    public ScheduleManager(StorageManager storageManager, ScheduleManagerDelegate delegate) {
        this.storageManager = storageManager;
        this.delegate = new WeakReference<ScheduleManagerDelegate>(delegate);
    }

    /*
    * setSchedule method, takes in a Schedule object
    * */
    public void setSchedule(@NonNull Schedule schedule)
    {
        if (schedule == null) {
            return;
        }

        storageManager.setObject(schedule, SCHEDULE_GROUP_IDENTIFIER, schedule.getIdentifier());

        ScheduleManagerDelegate delegateRef = this.delegate.get();
        if (delegateRef != null) {
            delegateRef.managerDidUpdateSchedule(this, schedule);
        }
    }

    /**
     * Remove a schedule object.
     * @param schedule The schedule object to remove.
     */
    public void removeSchedule(@Nullable Schedule schedule) {
        if (schedule == null) {
            return;
        }

        this.removeScheduleWithIdentifier(schedule.getIdentifier());
    }

    /**
     * Remove a schedule given its identifier.
     * @param scheduleIdentifier The string identifier of the schedule to remove.
     */
    public void removeScheduleWithIdentifier(@NonNull String scheduleIdentifier)
    {
        if (scheduleIdentifier == null) {
            return;
        }

        Schedule removedSchedule = this.getScheduleWithIdentifier(scheduleIdentifier);

        if (removedSchedule != null) {
            this.storageManager.removeObject(SCHEDULE_GROUP_IDENTIFIER, scheduleIdentifier);

            ScheduleManagerDelegate delegateRef = this.delegate.get();
            if (delegateRef != null) {
                delegateRef.managerDidRemoveSchedule(this, removedSchedule);
            }
        }
    }

    /**
     * Purge all saved schedules.
     */
    public void removeAllSchedules() {
        this.storageManager.removeObjectsWithPrefix(SCHEDULE_GROUP_IDENTIFIER);

        ScheduleManagerDelegate delegateRef = this.delegate.get();
        if (delegateRef != null) {
            delegateRef.managerDidRemoveSchedule(this, null);
        }
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
        ArrayList<Schedule> allSchedules = this.getAllSchedules();

        for (Schedule schedule : allSchedules) {
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
    public @Nullable Schedule getScheduleWithName(@NonNull String name)
    {
        if (name == null) {
            return null;
        }

        ArrayList<Serializable> serials = storageManager.getObjectsWithPrefix(SCHEDULE_GROUP_IDENTIFIER);
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
    public @Nullable Schedule getScheduleWithIdentifier(@NonNull String identifier)
    {
        if (identifier == null) {
            return null;
        }

        return storageManager.getObject(SCHEDULE_GROUP_IDENTIFIER, identifier);
    }

    /*
    * getAllSchedules method, returns an array of all Schedule objects
    * */
    public @NonNull ArrayList<Schedule> getAllSchedules()
    {
        ArrayList<Serializable> serials = storageManager.getObjectsWithPrefix(SCHEDULE_GROUP_IDENTIFIER);
        ArrayList<Schedule> schedules = new ArrayList<Schedule>();

        for (Serializable obj : serials) {
            if (obj != null) {
                schedules.add((Schedule) obj);
            }
        }

        return schedules;
    }
}
