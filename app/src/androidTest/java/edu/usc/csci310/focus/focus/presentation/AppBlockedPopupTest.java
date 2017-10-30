package edu.usc.csci310.focus.focus.presentation;

import android.content.Context;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.test.mock.MockContext;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;

import edu.usc.csci310.focus.focus.R;
import edu.usc.csci310.focus.focus.dataobjects.App;
import edu.usc.csci310.focus.focus.dataobjects.Profile;
import edu.usc.csci310.focus.focus.dataobjects.RecurringTime;
import edu.usc.csci310.focus.focus.dataobjects.Schedule;
import edu.usc.csci310.focus.focus.dataobjects.Timer;
import edu.usc.csci310.focus.focus.managers.ProfileManager;
import edu.usc.csci310.focus.focus.managers.ScheduleManager;
import edu.usc.csci310.focus.focus.storage.StorageManager;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Ashley Walker on 10/28/2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class AppBlockedPopupTest {

    Context context;

    @Rule
    public TemporaryFolder tmpFileDir = new TemporaryFolder();

    @Mock
    private ProfileManager mockedProfileManager;
    private ScheduleManager mockedScheduleManager;

    private AppBlockedPopup testedABP;

    private Profile testProfile1;
    private HashMap<String, Profile> profileHashMap;

    private Schedule testSchedule1;
    // Maps schedule name -> Schedule
    private HashMap<String, Schedule> scheduleHashMap;

    // Test Apps
    private final App testApp1 = new App("YouTube", "com.google.android.youtube");
    private final App testApp2 = new App("Gmail", "com.google.android.gmail");
    private final App testApp3 = new App("Messages", "com.google.android.messages");

    @Before
    public void setUp() {
        this.setUpManagers();
        this.setUpProfiles();
        this.setUpSchedule();
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

    private void setUpSchedule() {
        this.testSchedule1 = new Schedule("Test Schedule 1");
        RecurringTime rt = new RecurringTime();
        Calendar c = Calendar.getInstance();
        rt.addTime(c.get(Calendar.DAY_OF_WEEK)-1,
                (long)(c.get(Calendar.HOUR)*60 + c.get(Calendar.MINUTE)),
                (long) 10);
        this.testSchedule1.addProfile(testProfile1.getIdentifier(), rt);

        this.mockedScheduleManager.setSchedule(this.testSchedule1);

        this.scheduleHashMap = new HashMap<>();
        this.scheduleHashMap.put(this.testSchedule1.getIdentifier(), this.testSchedule1);
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
    public void onCreateDialog() throws Exception {
        context = InstrumentationRegistry.getInstrumentation().getContext();
        StorageManager testedManager = StorageManager.getDefaultManagerWithContext(context);
        File baseDir = this.tmpFileDir.newFolder("app-blocked-popup-test");

        Bundle savedInstanceState = new Bundle();
        savedInstanceState.putString("profile", "profileName");
        boolean containsKey = savedInstanceState.containsKey("profile");
        assertTrue(containsKey);

        testedABP = new AppBlockedPopup();
        testedABP.onCreateDialog(savedInstanceState);

        String message = testedABP.getDialog().toString();
        String expected = "appName is being blocked by the following schedule:\n\n• scheduleName\n";
        assertEquals(expected, message);
    }

}