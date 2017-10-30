package edu.usc.csci310.focus.focus.dataobjects;

import android.content.Context;
import android.os.Looper;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashMap;

import edu.usc.csci310.focus.focus.managers.ProfileManager;
import edu.usc.csci310.focus.focus.managers.ScheduleManager;

import static org.junit.Assert.*;

/**
 * Created by Ashley Walker on 10/28/2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class TimerTest {
    Context context;

    @Rule
    public TemporaryFolder tmpFileDir = new TemporaryFolder();

    @Mock
    private ProfileManager mockedProfileManager;
    private ScheduleManager mockedScheduleManager;

    private Timer testedTimer;

    private Profile testProfile1;
    private HashMap<String, Profile> profileHashMap;

    // Maps schedule name -> Schedule
    private HashMap<String, Schedule> scheduleHashMap;

    // Test Apps
    private final App testApp1 = new App("YouTube", "com.google.android.youtube");
    private final App testApp2 = new App("Gmail", "com.google.android.gmail");
    private final App testApp3 = new App("Messages", "com.google.android.messages");

    @Before
    public void setUp() {
        this.setUpProfiles();

        this.setUpManagers();
    }

    private void setUpProfiles() {
        this.testProfile1 = new Profile("Test Profile 1");
        this.testProfile1.addApp(this.testApp1);
        this.testProfile1.addApp(this.testApp2);
        this.testProfile1.addApp(this.testApp3);

        this.mockedProfileManager.setProfile(this.testProfile1);

        this.profileHashMap = new HashMap<>();
        this.profileHashMap.put(this.testProfile1.getIdentifier(), this.testProfile1);
    }

    private void setUpManagers() {
        this.mockedProfileManager = mock(ProfileManager.class);
        this.mockedScheduleManager = mock(ScheduleManager.class);

        this.scheduleHashMap = new HashMap<>();
        when(this.mockedScheduleManager.getScheduleWithName(anyString())).thenAnswer(
                new Answer<Schedule>() {
                    @Override
                    public Schedule answer(InvocationOnMock invocation) {
                        Object[] args = invocation.getArguments();
                        String name = (String)args[0];
                        return scheduleHashMap.get(name);
                    }});
        doAnswer(new Answer<Object>() {
                    @Override
                    public Object answer(InvocationOnMock invocation) {
                        Object[] args = invocation.getArguments();
                        Schedule schedule = (Schedule)args[0];

                        scheduleHashMap.put(schedule.getName(), schedule);

                        return null;
                    }}).when(this.mockedScheduleManager).setSchedule(any(Schedule.class));

        when(this.mockedProfileManager.getProfileWithIdentifier(anyString())).thenAnswer(
                new Answer<Profile>() {
                    @Override
                    public Profile answer(InvocationOnMock invocation) {
                        Object[] args = invocation.getArguments();
                        String identifier = (String)args[0];
                        return profileHashMap.get(identifier);
                    }});
    }

    @Test
    public void testStartStop() throws Exception {
        this.testedTimer = new Timer(this.testProfile1, this.mockedProfileManager, this.mockedScheduleManager);

        this.testedTimer.setTime(10);

        Looper.prepare();

        // Make sure test profile is not active before starting timer.
        assertFalse(this.testProfile1.getIsActive());

        this.testedTimer.start();

        // Check that the timer started
        assertTrue(this.testedTimer.isRunning());

        // Check that the profile is now active.
        assertNotNull(this.testProfile1);
        assertTrue(this.testProfile1.getIsActive());

        // Check that the schedule exists
        String scheduleName = this.testProfile1.getIdentifier() + Schedule.TIMER_SCHEDULE_POSTFIX;
        Schedule createdSchedule = this.mockedScheduleManager.getScheduleWithName(scheduleName);
        assertNotNull(createdSchedule);

        // Check that the created schedule is set to active
        assertTrue(createdSchedule.getIsActive());

        // Check that the created schedule has the desired test profile
        String profileIdentifier = createdSchedule.getProfileIdentifiers().get(0);
        Profile profileInCreatedSchedule = this.mockedProfileManager.getProfileWithIdentifier(profileIdentifier);
        assertEquals(this.testProfile1, profileInCreatedSchedule);

        // Stop the timer
        this.testedTimer.stop();

        // Check that the timer is stopped
        assertFalse(this.testedTimer.isRunning());
    }
}