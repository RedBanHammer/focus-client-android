package edu.usc.csci310.focus.focus.dataobjects;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static org.hamcrest.CoreMatchers.is;


import static org.junit.Assert.*;

/**
 */
public class RecurringTimeTest {
    @Mock
    public RecurringTime times = new RecurringTime();

    @Test
    public void testCombineWith() throws Exception {
        int dayIndex = 1;
        int dayIndex2 = 2;
        Long minuteIndex = new Long(0);
        Long duration = new Long(60);
        RecurringTime expectedTimes = new RecurringTime();
        expectedTimes.addTime(dayIndex, minuteIndex, duration);
        RecurringTime other = new RecurringTime();
        other.addTime(dayIndex2, minuteIndex, duration);
        expectedTimes.combineWith(other);
        RecurringTime actualTimes = new RecurringTime();
        actualTimes.addTime(dayIndex, minuteIndex, duration);
        actualTimes.addTime(dayIndex2, minuteIndex, duration);
        assertEquals(expectedTimes.getTimes(), actualTimes.getTimes());
    }

    @Test
    public void testAddTime() throws Exception {
        int dayIndex = 1;
        Long minuteIndex = new Long(0);
        Long duration = new Long(60);
        RecurringTime expectedTimes = new RecurringTime();
        expectedTimes.addTime(dayIndex, minuteIndex, duration);
        assertTrue(expectedTimes.getTimes().get(dayIndex).containsKey(minuteIndex));
        assertTrue(expectedTimes.getTimes().get(dayIndex).containsValue(duration));
    }

    @Test
    public void testGetTimes() throws Exception {
        ArrayList<Map<Long, Long>> expectedTimes = times.getTimes();
        assertEquals(expectedTimes, times.getTimes());
    }

    @Test
    public void testRemoveTimes() throws Exception {
        int dayIndex = 1;
        Long min1 = new Long(0);
        Long dur1 = new Long(60);
        Long min2 = new Long(120);
        Long dur2 = new Long(30);
        RecurringTime expectedTimes = new RecurringTime();
        expectedTimes.addTime(dayIndex, min1, dur1);
        expectedTimes.addTime(dayIndex, min2, dur2);
        expectedTimes.removeTimes(dayIndex);
        assertThat(expectedTimes.getTimes().get(dayIndex).size(), is(0));
    }

    @Test
    public void testRemoveTime() throws Exception {
        int dayIndex = 1;
        Long minuteIndex = new Long(0);
        Long duration = new Long(60);
        RecurringTime expectedTimes = times;
        expectedTimes.addTime(dayIndex, minuteIndex, duration);
        expectedTimes.removeTime(dayIndex, minuteIndex);
        assertEquals(expectedTimes, times);
    }
}