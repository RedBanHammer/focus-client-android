package edu.usc.csci310.focus.focus.managers;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.core.deps.guava.util.concurrent.ExecutionError;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;

import java.io.File;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.util.ArrayList;

import edu.usc.csci310.focus.focus.dataobjects.RecurringTime;
import edu.usc.csci310.focus.focus.dataobjects.Schedule;
import edu.usc.csci310.focus.focus.storage.StorageManager;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;

/**
 * Created by Briana on 10/29/17.
 */
public class ScheduleManagerTest {
    @Mock
    private ScheduleManager scheduleManager;
    private StorageManager storageManager = mock(StorageManager.class);
    public ScheduleManagerDelegate delegate = mock(ScheduleManagerDelegate.class);

    @Test
    public void getScheduleManager() throws Exception {
        ScheduleManager expectedManager = ScheduleManager.getDefaultManager();
        assertEquals(expectedManager, ScheduleManager.getDefaultManager());
    }

    @Before
    public void init(){
        scheduleManager = new ScheduleManager(storageManager, delegate);
    }

    @Test
    public void setSchedule() throws Exception {
        Schedule newSchedule = new Schedule("Schedule Test");
        scheduleManager.getAllSchedules().add(newSchedule);
        newSchedule.addProfile("111-111", new RecurringTime());
        scheduleManager.setSchedule(newSchedule);
        ArrayList<Schedule> schedules = scheduleManager.getAllSchedules();
        Schedule sched = scheduleManager.getScheduleWithIdentifier(newSchedule.getIdentifier());
        assertEquals(sched.getIdentifier(), newSchedule.getIdentifier());
    }

    @Test
    public void removeSchedule() throws Exception {
        ArrayList<String> profileIDs = new ArrayList<>();
        Schedule newSchedule = new Schedule(profileIDs, "Schedule 1");
        scheduleManager.getAllSchedules().add(newSchedule);
        scheduleManager.removeSchedule(newSchedule);
        assertEquals(scheduleManager.getScheduleWithIdentifier(newSchedule.getIdentifier()), null);
    }

    @Test
    public void removeScheduleWithIdentifier() throws Exception {
        ArrayList<String> profileIDs = new ArrayList<>();
        Schedule newSchedule = new Schedule(profileIDs, "Schedule 1");
        scheduleManager.getAllSchedules().add(newSchedule);
        scheduleManager.removeScheduleWithIdentifier(newSchedule.getIdentifier());
        assertEquals(scheduleManager.getScheduleWithIdentifier(newSchedule.getIdentifier()), null);
    }

    @Test
    public void getActiveSchedules() throws Exception {
        ArrayList<Schedule> expectedActiveSchedules = new ArrayList<Schedule>();
        scheduleManager.getAllSchedules().add(new Schedule("Schedule 1"));
        scheduleManager.getAllSchedules().add(new Schedule("Schedule 2"));
        scheduleManager.getAllSchedules().add(new Schedule("Schedule 3"));
        scheduleManager.getAllSchedules().add(new Schedule("Schedule 4"));
        ArrayList<Schedule> allSchedules = scheduleManager.getAllSchedules();

        for (Schedule schedule : allSchedules) {
            if (schedule.getIsActive()) {
                expectedActiveSchedules.add(schedule);
            }
        }
        assertEquals(expectedActiveSchedules, scheduleManager.getActiveSchedules());
    }

    @Test
    public void getScheduleWithName() throws Exception {
        ArrayList<String> profileIDs = new ArrayList<>();
        Schedule newSchedule = new Schedule(profileIDs, "Schedule 1");
        scheduleManager.getAllSchedules().add(newSchedule);
        Schedule expectedSchedule = scheduleManager.getScheduleWithName(newSchedule.getName());
        assertEquals(expectedSchedule.getIdentifier(), newSchedule.getIdentifier());
    }

    @Test
    public void getScheduleWithIdentifier() throws Exception {
        ArrayList<String> profileIDs = new ArrayList<>();
        Schedule newSchedule = new Schedule(profileIDs, "Schedule 1");
        scheduleManager.getAllSchedules().add(newSchedule);
        Schedule expectedSchedule = scheduleManager.getScheduleWithName(newSchedule.getIdentifier());
        assertEquals(expectedSchedule.getIdentifier(), newSchedule.getIdentifier());
    }

    @Test
    public void getAllSchedules() throws Exception {
        ArrayList<Schedule> schedules = scheduleManager.getAllSchedules();
        assertEquals(schedules, scheduleManager.getAllSchedules());
    }

}