package edu.usc.csci310.focus.focus.dataobjects;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import edu.usc.csci310.focus.focus.managers.ProfileManager;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by bdeng on 10/29/17.
 */
public class ScheduleTest {
    private Schedule testSchedule;
    private Profile testProfile;
    String schedName = "testSchedule";
    String profileName = "testProfile";
    RecurringTime time;
    Integer day = 1;
    Long start = 720L;
    Long duration = 30L;
    RecurringTime time1;
    long minuteIndex;
    private ProfileManager mockedProfileManager = mock(ProfileManager.class);
    private HashMap<String, Profile> profileHashMap;



    @Before
    public void setUp() {
        testSchedule = new Schedule(schedName);
        testProfile = new Profile(profileName);
        time = new RecurringTime();
        time.addTime(day, start, duration);

        profileHashMap = new HashMap<>();
        profileHashMap.put(testProfile.getIdentifier(), testProfile);

        // Current time
        Calendar c = Calendar.getInstance();
        long now = c.getTimeInMillis();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long passed = now - c.getTimeInMillis();
        minuteIndex = passed / 1000 / 60;
        int dayIndex = c.get(Calendar.DAY_OF_WEEK) - 1;

        // Now
        time1 = new RecurringTime();
        time1.addTime(dayIndex, minuteIndex, new Long(30));

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
    public void addProfile() throws Exception {
        testSchedule.addProfile(testProfile.getIdentifier(), time);

        boolean success = testSchedule.getProfileIdentifiers().contains(testProfile.getIdentifier());
        assertEquals(true, success);
    }


    @Test
    public void removeProfileWithIdentifier() throws Exception {
        testSchedule.addProfile(testProfile.getIdentifier(), time);
        testSchedule.removeProfileWithIdentifier(testProfile.getIdentifier());

        boolean success = testSchedule.getProfileIdentifiers().contains(testProfile.getIdentifier());
        assertEquals(false, success);
    }

    @Test
    public void removeNonExistentProfileWithIdentifier() throws Exception {
        testSchedule.addProfile(testProfile.getIdentifier(), time);
        ArrayList<String> expectedIds = testSchedule.getProfileIdentifiers();
        testSchedule.removeProfileWithIdentifier("dummy-id");

        ArrayList<String> resultIds = testSchedule.getProfileIdentifiers();
        assertEquals(expectedIds, resultIds);
    }

    @Test
    public void getProfileIdentifiers() throws Exception {
        testSchedule.addProfile(testProfile.getIdentifier(), time);
        ArrayList<String> expectedList = new ArrayList<>();
        expectedList.add(testProfile.getIdentifier());
        ArrayList<String> resultList = testSchedule.getProfileIdentifiers();

        assertEquals(expectedList, resultList);
    }

    //this one accesses profile manager..... doesnt work because of files. needs tweaking
    @Test
    public void getActiveProfileIdentifiers() throws Exception {
        testSchedule.addProfile(testProfile.getIdentifier(), time1);
        ArrayList<String> expectedList = new ArrayList<String>();
        expectedList.add(testProfile.getIdentifier());
        ArrayList<String> resultList = testSchedule.getActiveProfileIdentifiers(mockedProfileManager);

        assertEquals(expectedList, resultList);
    }

    //how to get the remaining time on my side?
    @Test
    public void getTimeRemainingWithProfileIdentifier() throws Exception {
        //time remaining
        //now-start = minutes passed
        //duration - minutes passed = time remaining
        testSchedule.setIsActive(true);
        testSchedule.addProfile(testProfile.getIdentifier(), time1);
        long expected = 30l;

        long result = testSchedule.getTimeRemainingWithProfileIdentifier(testProfile.getIdentifier());
        assertEquals(expected, result);

    }

    @Test
    public void getTimeRemainingNonExistentProfile() throws Exception {
        long result = testSchedule.getTimeRemainingWithProfileIdentifier("dummy-id");
        assertEquals(0, result);

    }

    @Test
    public void getProfileTimes() throws Exception {
        Map<String, RecurringTime> expectedMap = new HashMap<>();
        expectedMap.put(testProfile.getIdentifier(), time);
        testSchedule.addProfile(testProfile.getIdentifier(), time);

        Map<String, RecurringTime> resultMap = testSchedule.getProfileTimes();
        assertEquals(expectedMap, resultMap);
    }

    @Test
    public void getProfileTimeWithIdentifier() throws Exception {
        testSchedule.addProfile(testProfile.getIdentifier(), time);
        RecurringTime resultTime = testSchedule.getProfileTimeWithIdentifier(testProfile.getIdentifier());

        assertEquals(time, resultTime);
    }


}