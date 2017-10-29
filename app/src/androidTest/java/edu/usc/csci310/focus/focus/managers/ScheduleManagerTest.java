package edu.usc.csci310.focus.focus.managers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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
 *
 */
public class ScheduleManagerTest {
    @Mock
    private ScheduleManager testedScheduleManager;
    private StorageManager mockedStorageManager = mock(StorageManager.class);
    public ScheduleManagerDelegate mockedDelegate = mock(ScheduleManagerDelegate.class);
    private HashMap<String, HashMap<String, Serializable>> objectHashMap = new HashMap<String, HashMap<String, Serializable>>();
    private ArrayList<Schedule> testSchedules = new ArrayList<>();

    @Test
    public void getScheduleManager() throws Exception {
        ScheduleManager expectedManager = ScheduleManager.getDefaultManager();
        assertEquals(expectedManager, ScheduleManager.getDefaultManager());
    }

    @Before
    public void init(){
        when(this.mockedStorageManager.getObject(anyString(), anyString())).thenAnswer(
                new Answer<Serializable>() {
                    @Override
                    public Serializable answer(InvocationOnMock invocation) {
                        Object[] args = invocation.getArguments();
                        String group = (String)args[0];
                        String id = (String)args[1];

                        HashMap<String, Serializable> groupObjects = objectHashMap.get(group);

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

                        HashMap<String, Serializable> groupObjects = objectHashMap.get(group);

                        ArrayList<Serializable> objectList = new ArrayList<>();
                        for (String key : groupObjects.keySet()) {
                            objectList.add(groupObjects.get(key));
                        }

                        return objectList;
                    }});

        this.testedScheduleManager = new ScheduleManager(this.mockedStorageManager, this.mockedDelegate);

        this.setUpSchedules();
    }

    private void setUpSchedules() {
        Schedule testSchedule1 = new Schedule("Test Schedule 1");
        testSchedule1.setIsActive(true);

        Schedule testSchedule2 = new Schedule("Test Schedule 2");
        testSchedule2.setIsActive(true);

        Schedule testSchedule3 = new Schedule("Test Schedule 3");
        testSchedule3.setIsActive(false);

        this.testSchedules.add(testSchedule1);
        this.testSchedules.add(testSchedule2);
        this.testSchedules.add(testSchedule3);

        HashMap<String, Serializable> objectMap = new HashMap<>();
        objectMap.put(testSchedule1.getIdentifier(), testSchedule1);
        objectMap.put(testSchedule2.getIdentifier(), testSchedule2);
        objectMap.put(testSchedule3.getIdentifier(), testSchedule3);

        this.objectHashMap.put(ScheduleManager.SCHEDULE_GROUP_IDENTIFIER, objectMap);
    }

    @Test
    public void testSetGetScheduleWithIdentifier() throws Exception {
        Schedule expectedSchedule = this.testSchedules.get(0);
        this.testedScheduleManager.getAllSchedules().add(expectedSchedule);

        Profile testProfile = new Profile("Test Profile 1");

        expectedSchedule.addProfile(testProfile.getIdentifier(), new RecurringTime());

        this.testedScheduleManager.setSchedule(expectedSchedule);
        Schedule schedule = this.testedScheduleManager.getScheduleWithIdentifier(expectedSchedule.getIdentifier());

        assertEquals(expectedSchedule, schedule);
    }

    @Test
    public void testSetGetScheduleWithName() throws Exception {
        Schedule expectedSchedule = this.testSchedules.get(0);
        this.testedScheduleManager.getAllSchedules().add(expectedSchedule);

        Profile testProfile = new Profile("Test Profile 1");

        expectedSchedule.addProfile(testProfile.getIdentifier(), new RecurringTime());

        // Test setting a schedule
        this.testedScheduleManager.setSchedule(expectedSchedule);
        verify(this.mockedStorageManager, atLeastOnce()).setObject(expectedSchedule, ScheduleManager.SCHEDULE_GROUP_IDENTIFIER, expectedSchedule.getIdentifier());

        // Test getting a schedule
        Schedule schedule = this.testedScheduleManager.getScheduleWithName(expectedSchedule.getName());
        assertEquals(expectedSchedule, schedule);
    }

    @Test
    public void testRemoveScheduleWithIdentifier() throws Exception {
        Schedule expectedSchedule = this.testSchedules.get(0);
        this.testedScheduleManager.removeScheduleWithIdentifier(expectedSchedule.getIdentifier());

        verify(this.mockedStorageManager, atLeastOnce()).removeObject(ScheduleManager.SCHEDULE_GROUP_IDENTIFIER, expectedSchedule.getIdentifier());
    }

    @Test
    public void testRemoveSchedule() throws Exception {
        Schedule expectedSchedule = this.testSchedules.get(0);
        this.testedScheduleManager.removeSchedule(expectedSchedule);

        verify(this.mockedStorageManager, atLeastOnce()).removeObject(ScheduleManager.SCHEDULE_GROUP_IDENTIFIER, expectedSchedule.getIdentifier());
    }

    @Test
    public void testGetActiveSchedules() throws Exception {
        HashSet<Schedule> expectedActiveSchedules = new HashSet<>();
        for (Schedule schedule : this.testSchedules) {
            if (schedule.getIsActive()) {
                expectedActiveSchedules.add(schedule);
            }
        }

        assertEquals(expectedActiveSchedules, new HashSet<>(this.testedScheduleManager.getActiveSchedules()));
    }

    @Test
    public void testGetAllSchedules() throws Exception {
        assertEquals(new HashSet<>(this.testSchedules), new HashSet<>(this.testedScheduleManager.getAllSchedules()));
    }

}