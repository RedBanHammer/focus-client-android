package edu.usc.csci310.focus.focus.dataobjects;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import edu.usc.csci310.focus.focus.managers.ProfileManager;
import edu.usc.csci310.focus.focus.managers.ScheduleManager;

import static org.junit.Assert.*;

/**
 * Created by Ashley Walker on 10/28/2017.
 */
public class TimerTest2 {
    private static Profile profile;
    private static Timer timer;

    @Before
    public void setupForEachTest() {
        //
        profile = new Profile("profileName");
        App a = new App("appName", "appIdentifier");
        profile.addApp(a);
        ProfileManager.getDefaultManager().setProfile(profile);

        timer = new Timer(profile);

        timer.setTime(10);
    }

    @Test
    public void testStart() throws Exception {
        timer.start();
        profile = ProfileManager.getDefaultManager().getProfileWithName("profileName");

        // assert profile is active
        assertTrue(profile.getIsActive());

        // assert schedule exists
        String scheduleName = profile.getIdentifier() + Schedule.TIMER_SCHEDULE_POSTFIX;
        Schedule schedule = ScheduleManager.getDefaultManager().getScheduleWithName(scheduleName);
        assertNotNull(schedule);

        // assert schedule is active
        assertTrue(schedule.getIsActive());

        // assert schedule has profile
        ArrayList<String> identifiers = schedule.getActiveProfileIdentifiers();
        Profile schedulesProfile = ProfileManager.getDefaultManager().getProfileWithIdentifier(identifiers.get(0));
        assertEquals(profile, schedulesProfile);

        // stop with that for now...
        timer.stop();
    }
}