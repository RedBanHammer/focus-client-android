package edu.usc.csci310.focus.focus;

import android.app.TabActivity;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Created by Briana on 10/29/17.
 */
@RunWith(AndroidJUnit4.class)
public class MyPagerAdapterTest {
    @Rule
    public ActivityTestRule<MainActivity> testTabActivity =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void test(){

    }
}