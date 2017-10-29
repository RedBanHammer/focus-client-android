package edu.usc.csci310.focus.focus.managers;

import android.app.Notification;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import edu.usc.csci310.focus.focus.blockers.AppBlocker;
import edu.usc.csci310.focus.focus.blockers.NotificationBlocker;
import edu.usc.csci310.focus.focus.dataobjects.App;
import edu.usc.csci310.focus.focus.dataobjects.Profile;
import edu.usc.csci310.focus.focus.dataobjects.RecurringTime;
import edu.usc.csci310.focus.focus.dataobjects.Schedule;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test Blocking Manager public API methods.
 */
public class BlockingManagerTest {
    private ScheduleManager mockedScheduleManager = mock(ScheduleManager.class);
    private ProfileManager mockedProfileManager = mock(ProfileManager.class);
    private AppBlocker mockedAppBlocker = mock(AppBlocker.class);
    private NotificationBlocker mockedNotificationBlocker = mock(NotificationBlocker.class);
    private BlockingManager testedBlockingManager = null;

    // Test Apps
    private App testApp1 = new App("YouTube", "com.google.android.youtube"); // Blocked
    private App testApp2 = new App("Gmail", "com.google.android.gmail"); // Blocked
    private App testApp3 = new App("Messages", "com.google.android.messages"); // Not blocked

    // Test Profiles
    private Profile testProfile1; // Actively scheduled during test, 2 apps
    private Profile testProfile2; // Not actively scheduled during test, 2 apps
    private Profile testProfile3; // Actively scheduled during test, 0 apps
    private Profile testProfile4; // Actively scheduled during test, 1 app (overlap with tp1)
    private HashMap<String, Profile> profileHashMap;

    private HashSet<App> expectedBlockedApps;
    private ArrayList<Schedule> mockedActiveSchedules;

    private BlockingManagerLogEntryDelegate mockedLogEntryDelegate;

    @Before
    public void setUp() {
        // Set up profile manager

        this.setUpProfiles();

        this.setUpSchedules();

        this.setUpBlockedApps();

        // Mock schedule manager
        when(this.mockedScheduleManager.getActiveSchedules()).thenReturn(this.mockedActiveSchedules);
        when(this.mockedProfileManager.getProfileWithIdentifier(anyString())).thenAnswer(
                new Answer<Profile>() {
                    @Override
                    public Profile answer(InvocationOnMock invocation) {
                        Object[] args = invocation.getArguments();
                        String identifier = (String)args[0];
                        return profileHashMap.get(identifier);
                    }});

        this.testedBlockingManager = new BlockingManager(this.mockedScheduleManager, this.mockedProfileManager);

        // Mock blocking modules
        this.testedBlockingManager.setAppBlocker(this.mockedAppBlocker);
        this.testedBlockingManager.setNotificationBlocker(this.mockedNotificationBlocker);

        // Mock log entry delegate
        this.mockedLogEntryDelegate = mock(BlockingManagerLogEntryDelegate.class);
        this.testedBlockingManager.setLogEntryDelegate(this.mockedLogEntryDelegate);
    }

    private void setUpProfiles() {
        this.testProfile1 = new Profile("Test Profile 1");
        this.testProfile2 = new Profile("Test Profile 2");
        this.testProfile3 = new Profile("Test Profile 3");
        this.testProfile4 = new Profile("Test Profile 4");

        this.profileHashMap = new HashMap<>();
        this.profileHashMap.put(this.testProfile1.getIdentifier(), this.testProfile1);
        this.profileHashMap.put(this.testProfile2.getIdentifier(), this.testProfile2);
        this.profileHashMap.put(this.testProfile3.getIdentifier(), this.testProfile3);
        this.profileHashMap.put(this.testProfile4.getIdentifier(), this.testProfile4);

        // Add apps to mocked profiles
        this.testProfile1.addApp(testApp1);
        this.testProfile1.addApp(testApp2);

        this.testProfile2.addApp(testApp1);
        this.testProfile2.addApp(testApp2);
        this.testProfile2.addApp(testApp3);

        this.testProfile4.addApp(testApp1);
    }

    private void setUpSchedules() {
        // Current time
        Calendar c = Calendar.getInstance();
        long now = c.getTimeInMillis();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long passed = now - c.getTimeInMillis();
        long minuteIndex = passed / 1000 / 60;
        int dayIndex = c.get(Calendar.DAY_OF_WEEK) - 1;

        // Now
        RecurringTime time1 = new RecurringTime();
        time1.addTime(dayIndex, minuteIndex, new Long(30));

        // Not now
        RecurringTime time2 = new RecurringTime();
        time2.addTime(dayIndex, minuteIndex+30, new Long(30));

        // Active schedule with profiles
        Schedule activeSchedule1 = new Schedule("Active Test Schedule 1");
        activeSchedule1.setIsActive(true);
        activeSchedule1.addProfile(this.testProfile1.getIdentifier(), time1);
        activeSchedule1.addProfile(this.testProfile2.getIdentifier(), time2);
        activeSchedule1.addProfile(this.testProfile3.getIdentifier(), time1);
        activeSchedule1.addProfile(this.testProfile4.getIdentifier(), time1);

        // Active schedule with no profiles
        Schedule activeSchedule2 = new Schedule("Active Test Schedule 2");
        activeSchedule2.setIsActive(true);

        this.mockedActiveSchedules = new ArrayList<Schedule>();
        this.mockedActiveSchedules.add(activeSchedule1);
        this.mockedActiveSchedules.add(activeSchedule2);
    }

    private void setUpBlockedApps() {
        // Set expected blocked apps
        this.expectedBlockedApps = new HashSet<App>();
        this.expectedBlockedApps.add(this.testApp1);
        this.expectedBlockedApps.add(this.testApp2);
    }

    @Test
    public void testManagerInitializesDelegates() throws Exception {
        // ScheduleManager delegate should be the tested BlockingManager.
        assertEquals(this.testedBlockingManager, this.mockedScheduleManager.delegate.get());

        // ProfileManager delegate should be the tested BlockingManager.
        assertEquals(this.testedBlockingManager, this.mockedProfileManager.delegate.get());
    }

    @Test
    public void testManagerUpdatesBlockedAppsOnProfileUpdate() throws Exception {
        // Trigger a profile update
        this.testedBlockingManager.managerDidUpdateProfile(this.mockedProfileManager, this.testProfile1);

        // The blocking manager should have updated the apps for both its blockers.
        verify(this.mockedAppBlocker, atLeastOnce()).setApps(this.expectedBlockedApps);
        verify(this.mockedNotificationBlocker, atLeastOnce()).setApps(this.expectedBlockedApps);
    }

    @Test
    public void testManagerUpdatesBlockedAppsOnProfileRemove() throws Exception {
        // Trigger a profile update
        this.testedBlockingManager.managerDidRemoveProfile(this.mockedProfileManager, this.testProfile1);

        // The blocking manager should have updated the apps for both its blockers.
        verify(this.mockedAppBlocker, atLeastOnce()).setApps(this.expectedBlockedApps);
        verify(this.mockedNotificationBlocker, atLeastOnce()).setApps(this.expectedBlockedApps);
    }

    @Test
    public void testManagerUpdatesBlockedAppsOnScheduleUpdate() throws Exception {
        // Trigger a profile update
        this.testedBlockingManager.managerDidUpdateSchedule(this.mockedScheduleManager, this.mockedActiveSchedules.get(0));

        // The blocking manager should have updated the apps for both its blockers.
        verify(this.mockedAppBlocker, atLeastOnce()).setApps(this.expectedBlockedApps);
        verify(this.mockedNotificationBlocker, atLeastOnce()).setApps(this.expectedBlockedApps);
    }

    @Test
    public void testManagerUpdatesBlockedAppsOnScheduleRemove() throws Exception {
        // Trigger a profile update
        this.testedBlockingManager.managerDidRemoveSchedule(this.mockedScheduleManager, this.mockedActiveSchedules.get(0));

        // The blocking manager should have updated the apps for both its blockers.
        verify(this.mockedAppBlocker, atLeastOnce()).setApps(this.expectedBlockedApps);
        verify(this.mockedNotificationBlocker, atLeastOnce()).setApps(this.expectedBlockedApps);
    }

    @Test
    public void testManagerLogEntryDelegate() throws Exception {
        // Test that the log entry delegate receives callbacks.
        this.testedBlockingManager.didUpdateLogEntries();
        verify(this.mockedLogEntryDelegate, atLeastOnce()).blockingManagerDidUpdateLogEntries(this.testedBlockingManager);
    }
}