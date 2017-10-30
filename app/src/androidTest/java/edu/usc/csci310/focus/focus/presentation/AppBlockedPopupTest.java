package edu.usc.csci310.focus.focus.presentation;

import android.content.ComponentName;
import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.usc.csci310.focus.focus.MainActivity;
import edu.usc.csci310.focus.focus.dataobjects.App;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;

/**
 * Created by Ashley Walker on 10/28/2017.
 */

@RunWith(AndroidJUnit4.class)
public class AppBlockedPopupTest {
    @Rule
    public IntentsTestRule<MainActivity> mActivityRule = new IntentsTestRule(MainActivity.class);

    // Test App
    private final App testApp1 = new App("YouTube", "com.google.android.youtube");

    @Before
    public void setUp() {
        Intent intent = new Intent(this.mActivityRule.getActivity(), SplashScreen.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // You need this if starting
        //  the activity from a service
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.putExtra("appName", this.testApp1.getName());

        mActivityRule.launchActivity(intent);
    }

    @Test
    public void testPopup() throws Exception {
        // Check that the AppBlockedPopup is indeed up.
        intended(hasComponent(new ComponentName(getTargetContext(), AppBlockedPopup.class)));
    }

}