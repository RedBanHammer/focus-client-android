package edu.usc.csci310.focus.focus.presentation.schedule;

import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ListView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.usc.csci310.focus.focus.MainActivity;
import edu.usc.csci310.focus.focus.R;
import edu.usc.csci310.focus.focus.dataobjects.Schedule;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;

import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.*;

/**
 * Schedule Interface Controller Tests
 */
@RunWith(AndroidJUnit4.class)
public class ScheduleInterfaceControllerTest {

    /**
     * Perform action of waiting for a specific time.
     *
     * @source https://stackoverflow.com/a/35924943
     */
    public static ViewAction waitFor(final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "Wait for " + millis + " milliseconds.";
            }

            @Override
            public void perform(UiController uiController, final View view) {
                uiController.loopMainThreadForAtLeast(millis);
            }
        };
    }


    @Rule
    public ActivityTestRule<MainActivity> activityTestRule =
            new ActivityTestRule<>(MainActivity.class);
    @Rule
    public ActivityTestRule<ScheduleInterfaceController> scheduleTestRule =
            new ActivityTestRule<>(ScheduleInterfaceController.class);

    @Before
    public void init() {
        onView(withId(R.id.viewPager)).check(matches(isDisplayed()));
        onView(withId(R.id.viewPager)).perform(swipeLeft());
        onView(isRoot()).perform(waitFor(1000));
    }

    @Test
    public void testCreateScheduleWithName() throws Exception {
        onView(withId(R.id.addScheduleButton)).perform(click());
        onView(withText("OK")).check(matches(isDisplayed()));
        onView(withId(R.id.create_schedule_name)).check(matches(isDisplayed()));
        onView(withId(R.id.create_schedule_name)).perform(typeText("Schedule 100"));

        onView(isRoot()).perform(waitFor(500));

        onView(withId(android.R.id.button1)).perform(click());
    }

    @Test
    public void testCreateScheduleWithoutName() throws Exception {
        onView(withId(R.id.addScheduleButton)).perform(click());
        onView(withText("OK")).check(matches(isDisplayed()));
        onView(withId(R.id.create_schedule_name)).check(matches(isDisplayed()));

        onView(isRoot()).perform(waitFor(500));

        onView(withId(android.R.id.button1)).perform(click());
        onView(withText("Valid name is needed for the schedule")).check(matches(isDisplayed()));
    }

    @Test
    public void testOpenSchedule() throws Exception {
        testCreateScheduleWithName();
        onView(isRoot()).perform(waitFor(1500));
        final int[] counts = new int[2];
        onView(withId(R.id.scheduleListView)).check(matches(new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View view) {
                ListView listView = (ListView) view;

                counts[0] = listView.getCount();
                return true;
            }

            @Override
            public void describeTo(Description description) {

            }
        }));
        onData(anything()).inAdapterView(withId(R.id.scheduleListView)).atPosition(counts[0]-1).perform(click());
        onView(isRoot()).perform(waitFor(500));

        onView(withId(R.id.schedule_name)).check(matches(withText("Schedule 100")));
    }

    @Test
    public void testDayWeekView() throws Exception {
        testCreateScheduleWithName();
        onView(isRoot()).perform(waitFor(1500));
        onData(anything()).inAdapterView(withId(R.id.scheduleListView)).atPosition(0).perform(click());
        onView(isRoot()).perform(waitFor(500));
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(isRoot()).perform(waitFor(500));
        onData(withText("Day view")).check(matches(isDisplayed()));
//        onView(isRoot()).perform(waitFor(500));
//        onData(withText("Week view")).perform(click());
//        onView(isRoot()).perform(waitFor(500));
    }

    @Test
    public void testOpenAddProfile() throws Exception {
        onData(anything()).inAdapterView(withId(R.id.scheduleListView)).atPosition(0).perform(click());
        onView(isRoot()).perform(waitFor(500));
        onView(withId(R.id.add_profile_button)).perform(click());
        onView(isRoot()).perform(waitFor(500));
        onView(withId(R.id.profile_spinner)).check(matches(isDisplayed()));
    }
}