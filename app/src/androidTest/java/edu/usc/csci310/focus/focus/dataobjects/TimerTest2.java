package edu.usc.csci310.focus.focus.dataobjects;

import android.content.Context;
import android.os.Looper;
import android.support.test.InstrumentationRegistry;
import android.test.mock.MockContext;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

import java.io.File;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import edu.usc.csci310.focus.focus.managers.ProfileManager;
import edu.usc.csci310.focus.focus.managers.ScheduleManager;
import edu.usc.csci310.focus.focus.storage.StorageManager;

import static org.junit.Assert.*;

/**
 * Created by Ashley Walker on 10/28/2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class TimerTest2 {
    private static Timer timer;
    Context context;

    @Rule
    public TemporaryFolder tmpFileDir = new TemporaryFolder();

    @Mock
    private ProfileManager mockedProfileManager = ProfileManager.getDefaultManager();
    private ScheduleManager mockedScheduleManger = ScheduleManager.getDefaultManager();

    @Test
    public void testStart() throws Exception {
        Profile profile = new Profile("profileName");
        App a = new App("appName", "appIdentifier");
        profile.addApp(a);
        this.mockedProfileManager.setProfile(profile);

        timer = new Timer(profile, this.mockedProfileManager, this.mockedScheduleManger);

        timer.setTime(10);

        Looper.prepare();

        timer.start();

        profile = this.mockedProfileManager.getProfileWithIdentifier(profile.getIdentifier());

        // assert profile is active
        assertNotNull(profile);
        assertTrue(profile.getIsActive());

        // assert schedule exists
        String scheduleName = profile.getIdentifier() + Schedule.TIMER_SCHEDULE_POSTFIX;
        //ArrayList<Serializable> serials = StorageManager.getDefaultManagerWithContext(context).getObjectsWithPrefix("Schedules");
        ArrayList<Schedule> schedules = this.mockedScheduleManger.getAllSchedules();
        /*
        for (Serializable obj : serials) {
            if (obj != null) {
                schedules.add((Schedule) obj);
            }
        }
        */
        Schedule schedule = null;
        for (Schedule s : schedules) {
            if (s.getName().equals(scheduleName)) {
                schedule = s;
                break;
            }
        }
        assertNotNull(schedule);

        // assert schedule is active
        assertTrue(schedule.getIsActive());

        // assert schedule has profile
        ArrayList<String> identifiers = schedule.getActiveProfileIdentifiers(this.mockedProfileManager);
        Profile schedulesProfile = this.mockedProfileManager.getProfileWithIdentifier(identifiers.get(0));
        assertEquals(profile, schedulesProfile);

        // stop with that for now...
        timer.stop();
    }
}