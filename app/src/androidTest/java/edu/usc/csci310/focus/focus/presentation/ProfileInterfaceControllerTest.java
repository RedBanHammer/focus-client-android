package edu.usc.csci310.focus.focus.presentation;

import android.app.Activity;
import android.content.ComponentName;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitor;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collection;

import edu.usc.csci310.focus.focus.MainActivity;
import edu.usc.csci310.focus.focus.R;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

/**
 * Created by bdeng on 10/29/17.
 */

@RunWith(AndroidJUnit4.class)
public class ProfileInterfaceControllerTest {
    @Rule
    public IntentsTestRule<MainActivity> mActivityRule =
            new IntentsTestRule(MainActivity.class);


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





}

