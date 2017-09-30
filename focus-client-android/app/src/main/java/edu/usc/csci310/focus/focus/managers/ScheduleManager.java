package edu.usc.csci310.focus.focus.managers;

/**
 * Schedule Manager object. Handles events for schedules.
 */

public class ScheduleManager implements ScheduleManagerDelegate{
    /*
    * Constructor for Schedule Manager
    * */
    public void ScheduleManager(){

    }

    @Override
    /* Method from ScheduleManagerDelegate*/
    public void managerDidUpdateSchedule(ScheduleManager manager, Schedule schedule) {

    }

    @Override
    /* Method from ScheduleManagerDelegate*/
    public void managerDidRemoveSchedule(ScheduleManager manager, Schedule schedule) {

    };

    /*
    * setSchedule method, takes in a Schedule object
    * */
    public void setSchedule(Schedule schedule)
    {

    }

    /*
   * removeSchedule method, takes in a string.
   * */
    public void removeSchedule(String scheduleidentifier)
    {

    }
    /*
    * -------------------GETTERS-------------------------------
    */

    /*
   * getSchedulesInTimeInterval method, takes in a TimeInterval, returns array of Schedule objects.
   *  ----perhaps we can use java.time class to replace a timeinterval object? will need further consideration-----
   * */
    public Schedule[] getSchedulesInTimeInterval(TimeInterval timeinterval)
    {
        return null;
    }
    /*
   * getSchedule method, takes in a Schedule object
   * */
    public Schedule[] getSchedule(String scheduleidentifier)
    {
        return null;
    }

    /*
    * getAllSchedules method, returns an array of all Schedule objects
    * */
    public Schedule[] getAllSchedules()
    {
        return null;
    }


}
