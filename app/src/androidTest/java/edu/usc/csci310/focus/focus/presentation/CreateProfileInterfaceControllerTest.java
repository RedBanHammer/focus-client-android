package edu.usc.csci310.focus.focus.presentation;

import android.content.ComponentName;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.usc.csci310.focus.focus.MainActivity;
import edu.usc.csci310.focus.focus.R;
import edu.usc.csci310.focus.focus.dataobjects.App;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.*;

/**
 * Created by bdeng on 10/29/17.
 */
@RunWith(AndroidJUnit4.class)
public class CreateProfileInterfaceControllerTest {
    @Rule
    public IntentsTestRule<MainActivity> mActivityRule =
            new IntentsTestRule(MainActivity.class);


    @Test
    public void testOpenCreateNewProfile() {
        //click the add new profile button
        onView(withId(R.id.addProfileButton)).perform(click());
        //check activity_create_profile is pulled up
        intended(hasComponent(new ComponentName(getTargetContext(), CreateProfileInterfaceController.class)));

    }

    @Test
    public void testSetProfileName(){
        //type in the profilename2 box, press enter, check that the name stays
        onView(withId(R.id.addProfileButton)).perform(click());
        onView(withId(R.id.profileName2))
                .perform(typeText("newProfileName"), closeSoftKeyboard());
        onView(withId(R.id.chooseAppButton)).perform(click());
        onData(instanceOf(App.class))
                .atPosition(4)
                .onChildView(withId(R.id.checkBox1))
                .perform(click());
        onView(withId(R.id.selectAppButton)).perform(click());
        onView(withId(R.id.submitProfileButton)).perform(click());
        onView(withId(R.id.profile_list_name)).check(matches(withText("newProfileName")));

    }

    @Test
    public void testProfileEmptyName() {
        onView(withId(R.id.addProfileButton)).perform(click());
        onView(withId(R.id.profileName2)).perform(closeSoftKeyboard());
        onView(withId(R.id.submitProfileButton)).perform(click());
        //dialog is expected to pop up
        onView(withText("You entered an invalid name for the profile")).check(matches(isDisplayed()));
        onView(withId(android.R.id.button1)).perform(click());

    }


    @Test
    public void testSetApps() {
        //test that the apps selected show up in the view
        onView(withId(R.id.addProfileButton)).perform(click());
        onView(withId(R.id.profileName2))
                .perform(typeText("newProfileName"), closeSoftKeyboard());
        onView(withId(R.id.chooseAppButton)).perform(click());
        onData(instanceOf(App.class))
                .atPosition(6)  //messages
                .onChildView(withId(R.id.checkBox1))
                .perform(click());
        onView(withId(R.id.selectAppButton)).perform(click());
        //check that the app (messages) shows up in the app section
        onView(withId(R.id.appName2)).check(matches(withText("Messages")));

    }

    @Test
    public void testCreateProfileWithoutApps() {
        onView(withId(R.id.addProfileButton)).perform(click());
        onView(withId(R.id.profileName2))
                .perform(typeText("newProfileName"), closeSoftKeyboard());
        onView(withId(R.id.submitProfileButton)).perform(click());
        onView(withText("Please select at least one application")).check(matches(isDisplayed()));
        onView(withId(android.R.id.button1)).perform(click());

    }

    @Test
    public void testCreateProfileEntireProcess() {
        onView(withId(R.id.addProfileButton)).perform(click());
        onView(withId(R.id.profileName2))
                .perform(typeText("newProfileName"), closeSoftKeyboard());
        onView(withId(R.id.chooseAppButton)).perform(click());
        onData(instanceOf(App.class))
                .atPosition(6)  //messages
                .onChildView(withId(R.id.checkBox1))
                .perform(click());
        onView(withId(R.id.selectAppButton)).perform(click());
        onView(withId(R.id.submitProfileButton)).perform(click());
        //now click on the newly made profile
        onView(withId(R.id.profile_list_name)).perform(click());
        //check the name is correct and the apps are correct
        onView(withId(R.id.profileName)).check(matches(withText("newProfileName")));
        onView(withId(R.id.appName2)).check(matches(withText("Messages")));


    }





}