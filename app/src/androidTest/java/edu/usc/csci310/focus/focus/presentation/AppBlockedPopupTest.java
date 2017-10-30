package edu.usc.csci310.focus.focus.presentation;

import android.content.ComponentName;
import android.content.Intent;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.usc.csci310.focus.focus.MainActivity;
import edu.usc.csci310.focus.focus.dataobjects.App;
import edu.usc.csci310.focus.focus.storage.StorageManager;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Ashley Walker on 10/28/2017.
 */

@RunWith(AndroidJUnit4.class)
public class AppBlockedPopupTest {
    @Rule
    public final ActivityTestRule<SplashScreen> rule =
            new ActivityTestRule<>(SplashScreen.class, true, false);

    // Test App
    private final App testApp1 = new App("YouTube", "com.google.android.youtube");


    @Before
    public void setUp() {

    }

    @Test
    public void testPopup() throws Exception {
        Intents.init();

        Intent intent = new Intent();

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // You need this if starting
        //  the activity from a service
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.putExtra("appName", this.testApp1.getName());

        this.rule.launchActivity(intent);

        String dialogTitleText = "Blocked " + this.testApp1.getName();
        // Check that the AppBlockedPopup is displayed.
        onView(withText(dialogTitleText)).check(matches(isDisplayed()));

//        // Click the ok button
//        onView(withText(dialogTitleText)).perform(click());
//
//        // Check that the AppBlockedPopup is no longer displayed.
//        onView(withText(dialogTitleText)).check(matches(not(isDisplayed())));

        Intents.release();
    }

}