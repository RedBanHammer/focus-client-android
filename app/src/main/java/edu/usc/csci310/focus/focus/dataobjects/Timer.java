package edu.usc.csci310.focus.focus.dataobjects;

import android.os.CountDownTimer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import edu.usc.csci310.focus.focus.managers.ProfileManager;
import edu.usc.csci310.focus.focus.managers.ScheduleManager;

/**
 * Created by Ashley Walker on 10/20/2017.
 */

public class Timer implements Serializable {
    private static final long serialVersionUID = 1L;

    private Profile profile;
    private long minutes;
    private CountDownTimer countdown;

    public Timer(Profile profile) {
        this.minutes = 0;
        this.profile = profile;
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
        long ms = minutes * 60 * 1000;
        profile.setIsActive(true);
        ProfileManager.getDefaultManager().setProfile(profile);

        String name = profile.getName() + " Timer";
        final Schedule createdSchedule = new Schedule(name);

        // so, get the day and time
        RecurringTime timer = new RecurringTime();
        Calendar now = Calendar.getInstance();
        long nowMinutes = (now.get(Calendar.HOUR_OF_DAY) * 60) + now.get(Calendar.MINUTE);
        timer.addTime(now.get(Calendar.DAY_OF_WEEK)-1, nowMinutes, minutes);

        // add profile to schedule
        createdSchedule.addProfile(profile.getIdentifier(), timer);

        // set repeat to false, active to true
        createdSchedule.setIsActive(true);

        // setSchedule with ScheduleManager
        ScheduleManager.getDefaultManager().setSchedule(createdSchedule);

        this.countdown = new CountDownTimer(ms, 1000) {

            public void onTick(long millisUntilFinished) {
                // profile has been turned on, we just wait
                // TODO: countdown time manually, show to user
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

        profile.setIsActive(false);
        ProfileManager.getDefaultManager().setProfile(profile);

        // find schedule with profile.name + " Timer"
        Schedule s = ScheduleManager.getDefaultManager().
                getScheduleWithName(profile.getName() + " Timer");
        // delete it
        ScheduleManager.getDefaultManager().removeSchedule(s);
    }

}