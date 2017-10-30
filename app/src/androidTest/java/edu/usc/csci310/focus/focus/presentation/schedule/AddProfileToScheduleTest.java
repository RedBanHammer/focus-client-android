package edu.usc.csci310.focus.focus.presentation.schedule;

import org.junit.Test;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.runner.RunWith;

import edu.usc.csci310.focus.focus.R;

import static android.support.test.espresso.Espresso.onView;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Add Profile To Schedule Tests
 */
@RunWith(AndroidJUnit4.class)
public class AddProfileToScheduleTest {
    @Test
    public void testDidCompleteFields() throws Exception {
        // whether profile was selected
    }

    @Rule
    public ActivityTestRule<AddProfileToSchedule> testAddProfile =
            new ActivityTestRule<>(AddProfileToSchedule.class);
    @Test
    public void addProfile() throws Exception {

    }

}