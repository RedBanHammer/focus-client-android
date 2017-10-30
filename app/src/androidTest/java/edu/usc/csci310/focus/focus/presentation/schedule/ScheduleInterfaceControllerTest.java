package edu.usc.csci310.focus.focus.presentation.schedule;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.usc.csci310.focus.focus.MainActivity;
import edu.usc.csci310.focus.focus.R;

import static android.support.test.espresso.Espresso.onView;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

/**
 * Schedule Interface Controller Tests
 */
@RunWith(AndroidJUnit4.class)
public class ScheduleInterfaceControllerTest {
    @Rule
    public ActivityTestRule<MainActivity> testScheduleInterfaceController =
            new ActivityTestRule<>(MainActivity.class);
    @Before
    public void init(){
        onView(withId(R.id.viewPager)).check(matches(isDisplayed()));
        onView(withId(R.id.viewPager)).perform(swipeLeft());
        onView(withId(R.id.addScheduleButton)).perform(click());
    }
    @Test
    public void testCreateSchedule() throws Exception {
        onView(withText("OK")).check(matches(isDisplayed()));
        onView(withId(R.id.create_schedule_name)).check(matches(isDisplayed()));

//        perform(typeText("Schedule 1"));
//        onView(withId(android.R.id.button1)).perform(click());
    }

    @Test
    public void testOpenAddProfile() throws Exception{
        onView(withId(R.id.add_profile)).perform(click());
        onView(withId(R.id.profile_spinner)).check(matches(isDisplayed()));
    }
}