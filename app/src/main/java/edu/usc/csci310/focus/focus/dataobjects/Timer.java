package edu.usc.csci310.focus.focus.dataobjects;

import android.os.CountDownTimer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.usc.csci310.focus.focus.managers.ProfileManager;

/**
 * Created by Ashley Walker on 10/20/2017.
 */

public class Timer implements Serializable {
    private static final long serialVersionUID = 1L;

    private Profile profile;

    private boolean isOn = false;
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
        this.isOn = true;
    }

    /**
     * Start the timer.
     */
    public void start() {
        long ms = minutes * 60 * 1000;
        profile.setIsActive(true);

        this.countdown = new CountDownTimer(ms, 1000) {

            public void onTick(long millisUntilFinished) {
                // profile has been turned on, we just wait
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
    }

}