package edu.usc.csci310.focus.focus.presentation;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitor;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.Until;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collection;

import edu.usc.csci310.focus.focus.MainActivity;
import edu.usc.csci310.focus.focus.R;
import edu.usc.csci310.focus.focus.managers.BlockingManager;
import edu.usc.csci310.focus.focus.managers.ProfileManager;
import edu.usc.csci310.focus.focus.managers.ScheduleManager;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.*;

/**
 * Created by bdeng on 10/29/17.
 */

@RunWith(AndroidJUnit4.class)
public class ProfileInterfaceControllerTest {
    private String NOTIFICATION_TITLE = "Active Profile";
    private final String TEST_PROFILE_NAME_1 = "Test Profile 1";
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

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

    @Before
    public void setUp() {
        // Remove all profiles and schedules before starting
        BlockingManager.createBlockingManagerWithContext(this.mActivityTestRule.getActivity().getApplicationContext());
        ProfileManager.getDefaultManager().removeAllProfiles();

        this.createTestProfileWithName(TEST_PROFILE_NAME_1);

        Intents.init();
    }

    private static void createTestProfileWithName(String profileName) {
        ViewInteraction viewPager3 = onView(
                allOf(withId(R.id.viewPager), isDisplayed()));
        viewPager3.perform(swipeRight());

        onView(isRoot()).perform(waitFor(1000));

        ViewInteraction floatingActionButton2 = onView(
                allOf(withId(R.id.addProfileButton),
                        withParent(withId(R.id.listLayout)),
                        isDisplayed()));
        floatingActionButton2.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.profileName2), isDisplayed()));
        appCompatEditText2.perform(replaceText(profileName), closeSoftKeyboard());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.chooseAppButton), withText("Add Apps"), isDisplayed()));
        appCompatButton4.perform(click());

        ViewInteraction appCompatCheckBox = onView(
                allOf(withId(R.id.checkBox1),
                        withParent(childAtPosition(
                                withId(R.id.appListView),
                                0)),
                        isDisplayed()));
        appCompatCheckBox.perform(click());

        ViewInteraction appCompatCheckBox2 = onView(
                allOf(withId(R.id.checkBox1),
                        withParent(childAtPosition(
                                withId(R.id.appListView),
                                3)),
                        isDisplayed()));
        appCompatCheckBox2.perform(click());

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(R.id.selectAppButton), withText("Done"), isDisplayed()));
        appCompatButton5.perform(click());

        ViewInteraction appCompatButton6 = onView(
                allOf(withId(R.id.submitProfileButton), withText("Done"), isDisplayed()));
        appCompatButton6.perform(click());
    }

    @After
    public void tearDown() {
        // Remove all profiles and schedules before starting
        ProfileManager.getDefaultManager().removeAllProfiles();

        Intents.release();
    }


    @Test
    public void testOpenProfile() {
        //click the add new profile button
        onView(withId(R.id.profile_list_name)).perform(click());
        intended(hasComponent(new ComponentName(getTargetContext(), ProfileInterfaceController.class)));
    }

    @Test
    public void testOpenEditProfile() {
        onView(withId(R.id.profile_list_name)).perform(click());
        onView(withId(R.id.changeNameButton)).perform((click()));

        mActivityTestRule.launchActivity(new Intent());
        intended(hasComponent(CreateProfileInterfaceController.class.getName()));
    }

    @Test
    public void testEditName() {
        onView(withId(R.id.profile_list_name)).perform(click());
        onView(withId(R.id.changeNameButton)).perform((click()));
        onView(withId(R.id.profileName2)).perform(replaceText("changedName"), closeSoftKeyboard());
        onView(withId(R.id.submitProfileButton)).perform(click());
        //check that the name changed
        onView(withId(R.id.profileName)).check(matches(withText("changedName")));
    }

    @Test
    public void testNotificationOnToggle() {
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        ///click the toggle
        onView(withId(R.id.toggle_profile_button)).perform(click());
        onView(withId(R.id.startButton)).perform(click());
        device.openNotification();
        device.wait(Until.hasObject(By.text(NOTIFICATION_TITLE)), 10000);
        UiObject2 title = device.findObject(By.text(NOTIFICATION_TITLE));
        assertEquals(NOTIFICATION_TITLE, title.getText());
        title.click();
        device.wait(Until.hasObject(By.text(MainActivity.class.getName())), 10000);
    }


    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }


}

