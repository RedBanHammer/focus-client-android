package edu.usc.csci310.focus.focus.dataobjects;

import android.os.CountDownTimer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import edu.usc.csci310.focus.focus.managers.ProfileManager;
import edu.usc.csci310.focus.focus.managers.ScheduleManager;
import edu.usc.csci310.focus.focus.presentation.ProfileList;

/**
 * Created by Ashley Walker on 10/20/2017.
 */

public class Timer implements Serializable {
    private static final long serialVersionUID = 1L;

    private Profile profile;
    private long minutes;
    private CountDownTimer countdown;
    private ScheduleManager scheduleManager = ScheduleManager.getDefaultManager();
    private ProfileManager profileManager = ProfileManager.getDefaultManager();

    public Timer(Profile profile) {
        this.minutes = 0;
        this.profile = profile;
    }

    public Timer(Profile profile, ProfileManager pm, ScheduleManager sm) {
        this.minutes = 0;
        this.profile = profile;
        this.profileManager = pm;
        this.scheduleManager = sm;
    }

    /**
     * Set the duration of the timer and begin counting down.
     * @param mins The time in minutes that the timer should be active for.
     */
    public void setTime(long mins) {
        this.minutes = mins;
    }

    /**
     * Start the timer.
     */
    public void start() {
        long ms = this.minutes * 60 * 1000;
        this.profile.setIsActive(true);
        this.profileManager.setProfile(this.profile);

        String name = this.profile.getIdentifier() + Schedule.TIMER_SCHEDULE_POSTFIX;
        final Schedule createdSchedule = new Schedule(name);

        // so, get the day and time
        RecurringTime timer = new RecurringTime();
        Calendar now = Calendar.getInstance();
        long nowMinutes = (now.get(Calendar.HOUR_OF_DAY) * 60) + now.get(Calendar.MINUTE);
        timer.addTime(now.get(Calendar.DAY_OF_WEEK)-1, nowMinutes, this.minutes);

        // add profile to schedule
        createdSchedule.addProfile(this.profile.getIdentifier(), timer);

        // set repeat to false, active to true
        createdSchedule.setIsActive(true);

        // setSchedule with ScheduleManager
        this.scheduleManager.setSchedule(createdSchedule);

        this.countdown = new CountDownTimer(ms, 1000) {

            public void onTick(long millisUntilFinished) {
                // profile has been turned on, we just wait
                // countdown time manually, show to user
                // TODO: Use android broadcast notification intent channel instead of static vars
                ProfileList.profileList.render();
            }

            public void onFinish() {
                stop();
            }
        }.start();
    }

    /**
     * Stop the timer by setting the number of remaining minutes to 0.
     */
    public void stop() {
        this.minutes = 0;
        this.countdown.cancel();
        this.countdown = null;

        this.profile.setIsActive(false);
        this.profileManager.setProfile(profile);

        // find schedule with profile.name + " Timer"
        Schedule s = this.scheduleManager.
                getScheduleWithName(this.profile.getIdentifier() + Schedule.TIMER_SCHEDULE_POSTFIX);
        // Remove the schedule
        this.scheduleManager.removeSchedule(s);
    }

    /**
     * Whether the timer is currently counting down.
     * @return YES if the timer is counting down, NO otherwise.
     */
    public boolean isRunning() {
        return (this.countdown != null);
    }

}