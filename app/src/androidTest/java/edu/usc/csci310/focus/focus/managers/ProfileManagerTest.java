package edu.usc.csci310.focus.focus.managers;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import edu.usc.csci310.focus.focus.dataobjects.App;
import edu.usc.csci310.focus.focus.dataobjects.Profile;
import edu.usc.csci310.focus.focus.dataobjects.RecurringTime;
import edu.usc.csci310.focus.focus.dataobjects.Schedule;
import edu.usc.csci310.focus.focus.storage.StorageManager;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Profile Manager Test
 */
public class ProfileManagerTest {
    @Mock
    private ProfileManager testedProfileManager;
    private StorageManager mockedStorageManager = mock(StorageManager.class);
    private ScheduleManager mockedScheduleManger = mock(ScheduleManager.class);
    public ProfileManagerDelegate mockedDelegate = mock(ProfileManagerDelegate.class);
    private HashMap<String, HashMap<String, Serializable>> profileHashMap = new HashMap<String, HashMap<String, Serializable>>();
    private HashMap<String, HashMap<String, Serializable>> scheduleHashMap = new HashMap<String, HashMap<String, Serializable>>();

    private ArrayList<Profile> testProfiles = new ArrayList<>();
    private ArrayList<Schedule> testSchedules = new ArrayList<>();

    // Test Apps
    private final App testApp1 = new App("YouTube", "com.google.android.youtube");
    private final App testApp2 = new App("Gmail", "com.google.android.gmail");
    private final App testApp3 = new App("Messages", "com.google.android.messages");

    // Test Profiles
    private Profile testProfile1;
    private Profile testProfile2;
    private Profile testProfile3;

    // Test Schedules
    private Schedule testSchedule1;
    private Schedule testSchedule2;
    private Schedule testSchedule3;

    @Before
    public void init(){
        when(this.mockedScheduleManger.getAllSchedules()).thenAnswer(
                new Answer<ArrayList<Schedule>>() {
                    @Override
                    public ArrayList<Schedule> answer(InvocationOnMock invocation) {
                        Object[] args = invocation.getArguments();
                        return testSchedules;
                    }});
        when(this.mockedStorageManager.getObject(anyString(), anyString())).thenAnswer(
                new Answer<Serializable>() {
                    @Override
                    public Serializable answer(InvocationOnMock invocation) {
                        Object[] args = invocation.getArguments();
                        String group = (String)args[0];
                        String id = (String)args[1];

                        HashMap<String, Serializable> groupObjects = null;
                        if (group.equals(ProfileManager.PROFILE_GROUP_IDENTIFIER)){
                            groupObjects = profileHashMap.get(group);
                        }else if (group.equals(ScheduleManager.SCHEDULE_GROUP_IDENTIFIER)){
                            groupObjects = scheduleHashMap.get(group);
                        }

                        if (groupObjects != null) {
                            return groupObjects.get(id);
                        } else {
                            return null;
                        }
                    }});
        when(this.mockedStorageManager.getObjectsWithPrefix(anyString())).thenAnswer(
                new Answer<Serializable>() {
                    @Override
                    public Serializable answer(InvocationOnMock invocation) {
                        Object[] args = invocation.getArguments();
                        String group = (String)args[0];

                        HashMap<String, Serializable> groupObjects = null;

                        if (group.equals(ProfileManager.PROFILE_GROUP_IDENTIFIER)){
                            groupObjects = profileHashMap.get(group);
                        }else if (group.equals(ScheduleManager.SCHEDULE_GROUP_IDENTIFIER)){
                            groupObjects = scheduleHashMap.get(group);
                        }

                        ArrayList<Serializable> objectList = new ArrayList<>();
                        for (String key : groupObjects.keySet()) {
                            objectList.add(groupObjects.get(key));
                        }

                        return objectList;
                    }});

        this.testedProfileManager = new ProfileManager(this.mockedStorageManager, this.mockedDelegate, this.mockedScheduleManger);

        this.setUpProfilesAndSchedules();
    }

    private void setUpProfilesAndSchedules() {
        testProfile1 = new Profile("Test Profile 1");
        testProfile1.setIsActive(true);

        testProfile2 = new Profile("Test Profile 2");
        testProfile2.setIsActive(true);

        testProfile3 = new Profile("Test Profile 3");
        testProfile3.setIsActive(false);

        this.testProfiles.add(testProfile1);
        this.testProfiles.add(testProfile2);
        this.testProfiles.add(testProfile3);

        testSchedule1 = new Schedule("Test Schedule 1");
        testSchedule2 = new Schedule("Test Schedule 2");
        testSchedule3 = new Schedule("Test Schedule 3");

        testSchedule1.addProfile(testProfile1.getIdentifier(), new RecurringTime());
        testSchedule2.addProfile(testProfile2.getIdentifier(), new RecurringTime());
        testSchedule3.addProfile(testProfile3.getIdentifier(), new RecurringTime());

        this.testSchedules.add(testSchedule1);
        this.testSchedules.add(testSchedule2);
        this.testSchedules.add(testSchedule3);

        HashMap<String, Serializable> profileMap = new HashMap<>();
        profileMap.put(testProfile1.getIdentifier(), testProfile1);
        profileMap.put(testProfile2.getIdentifier(), testProfile2);
        profileMap.put(testProfile3.getIdentifier(), testProfile3);

        HashMap<String, Serializable> scheduleMap = new HashMap<>();
        scheduleMap.put(testSchedule1.getIdentifier(), testSchedule1);
        scheduleMap.put(testSchedule2.getIdentifier(), testSchedule2);
        scheduleMap.put(testSchedule3.getIdentifier(), testSchedule3);

        this.scheduleHashMap.put(ScheduleManager.SCHEDULE_GROUP_IDENTIFIER, scheduleMap);
        this.profileHashMap.put(ProfileManager.PROFILE_GROUP_IDENTIFIER, profileMap);
    }

    @Test
    public void testSetGetProfileWithIdentifier() throws Exception {
        Profile expectedProfile = this.testProfiles.get(0);
        this.testedProfileManager.getAllProfiles().add(expectedProfile);

        Profile testProfile = new Profile("Test Profile 1");

        expectedProfile.addApp(testApp1);

        this.testedProfileManager.setProfile(expectedProfile);
        Profile Profile = this.testedProfileManager.getProfileWithIdentifier(expectedProfile.getIdentifier());

        assertEquals(expectedProfile, Profile);
    }

    @Test
    public void testSetGetProfileWithName() throws Exception {
        Profile expectedProfile = this.testProfiles.get(0);
        this.testedProfileManager.getAllProfiles().add(expectedProfile);

        Profile testProfile = new Profile("Test Profile 1");

        expectedProfile.addApp(testApp1);
        expectedProfile.addApp(testApp2);

        // Test setting a Profile
        this.testedProfileManager.setProfile(expectedProfile);
        verify(this.mockedStorageManager, atLeastOnce()).setObject(expectedProfile, ProfileManager.PROFILE_GROUP_IDENTIFIER, expectedProfile.getIdentifier());

        // Test getting a Profile
        Profile Profile = this.testedProfileManager.getProfileWithName(expectedProfile.getName());
        assertEquals(expectedProfile, Profile);
    }

    @Test
    public void testRemoveProfileWithIdentifier() throws Exception {
        Profile expectedProfile = this.testProfiles.get(0);
        this.testedProfileManager.removeProfileWithIdentifier(expectedProfile.getIdentifier());

        verify(this.mockedStorageManager, atLeastOnce()).removeObject(ProfileManager.PROFILE_GROUP_IDENTIFIER, expectedProfile.getIdentifier());
    }

    @Test
    public void testRemoveProfile() throws Exception {
        Profile expectedProfile = this.testProfiles.get(0);
        this.testedProfileManager.removeProfile(expectedProfile);

        verify(this.mockedStorageManager, atLeastOnce()).removeObject(ProfileManager.PROFILE_GROUP_IDENTIFIER, expectedProfile.getIdentifier());
    }

    @Test
    public void testGetAllProfiles() throws Exception {
        assertEquals(new HashSet<>(this.testProfiles), new HashSet<>(this.testedProfileManager.getAllProfiles()));
    }

}