package edu.usc.csci310.focus.focus.presentation.schedule;


import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.usc.csci310.focus.focus.MainActivity;
import edu.usc.csci310.focus.focus.R;
import edu.usc.csci310.focus.focus.managers.BlockingManager;
import edu.usc.csci310.focus.focus.managers.ProfileManager;
import edu.usc.csci310.focus.focus.managers.ScheduleManager;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.isSelected;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ScheduleListActivityTest {

    private final String TEST_PROFILE_NAME_1 = "Test Profile 1";
    private final String TEST_PROFILE_NAME_2 = "Test Profile 2";

    private final String TEST_SCHEDULE_NAME_1 = "Test Schedule 1";
    private final String TEST_SCHEDULE_NAME_2 = "Test Schedule 2";

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
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() {
        // Remove all profiles and schedules before starting
        BlockingManager.createBlockingManagerWithContext(this.mActivityTestRule.getActivity().getApplicationContext());
        ScheduleManager.getDefaultManager().removeAllSchedules();
        ProfileManager.getDefaultManager().removeAllProfiles();

        // Swipe to schedule tab
        ViewInteraction viewPager = onView(
                allOf(ViewMatchers.withId(R.id.viewPager), isDisplayed()));
        viewPager.perform(swipeLeft());

        onView(isRoot()).perform(waitFor(1000));
    }

    @After
    public void tearDown() {
        // Remove all profiles and schedules before starting
        ScheduleManager.getDefaultManager().removeAllSchedules();
        ProfileManager.getDefaultManager().removeAllProfiles();
    }

    @Test
    public void testCreateNamedSchedule() {
        // Click create FAB
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.addScheduleButton),
                        withParent(withId(R.id.scheduleListLayout)),
                        isDisplayed()));
        floatingActionButton.perform(click());

        // Enter schedule name in popup
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.create_schedule_name), isDisplayed()));
        appCompatEditText.perform(replaceText(TEST_SCHEDULE_NAME_1), closeSoftKeyboard());

        // Click ok button in popup
        ViewInteraction appCompatButton = onView(
                allOf(withId(android.R.id.button1), withText("OK")));
        appCompatButton.perform(scrollTo(), click());

        // Check that schedule appeared in list
        ViewInteraction textView2 = onView(
                allOf(withId(R.id.schedule_list_name), withText(TEST_SCHEDULE_NAME_1),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        0),
                                0),
                        isDisplayed()));
        textView2.check(matches(withText(TEST_SCHEDULE_NAME_1)));
    }

    @Test
    public void testCreateScheduleBlankName() {
        // Click create FAB
        ViewInteraction floatingActionButton2 = onView(
                allOf(withId(R.id.addScheduleButton),
                        withParent(withId(R.id.scheduleListLayout)),
                        isDisplayed()));
        floatingActionButton2.perform(click());

        // Click ok without entering schedule name
        ViewInteraction appCompatButton2 = onView(
                allOf(withId(android.R.id.button1), withText("OK")));
        appCompatButton2.perform(scrollTo(), click());

        onView(isRoot()).perform(waitFor(500));

        // Check that error popup is shown
        onView(withText("Incomplete Form")).check(matches(isDisplayed()));

        // Click okay in error popup
        ViewInteraction appCompatButton3 = onView(
                allOf(withId(android.R.id.button1), withText("Okay")));
        appCompatButton3.perform(scrollTo(), click());
    }

    @Test
    public void testOpenScheduleDetails() {
        this.testCreateNamedSchedule();

        // Click first schedule in list
        ViewInteraction linearLayout = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.scheduleListView),
                                withParent(withId(R.id.scheduleListLayout))),
                        0),
                        isDisplayed()));
        linearLayout.perform(click());

        // Check that the displayed schedule name matches the expected schedule name
        ViewInteraction textView = onView(
                allOf(withId(R.id.schedule_name), withText(TEST_SCHEDULE_NAME_1)));
        textView.check(matches(withText(TEST_SCHEDULE_NAME_1)));
    }

    @Test
    public void testEditScheduleName() {
        this.testOpenScheduleDetails();

        // Click edit name button
        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.edit_schedule_name_button), isDisplayed()));
        appCompatButton4.perform(click());

        // Set schedule name in popup
        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.newNameText), isDisplayed()));
        appCompatEditText2.perform(replaceText(TEST_SCHEDULE_NAME_2), closeSoftKeyboard());

        // Press ok button in popup
        ViewInteraction appCompatButton5 = onView(
                allOf(withId(R.id.positiveButton), isDisplayed()));
        appCompatButton5.perform(click());

        // Check that the displayed schedule name was changed in details view
        ViewInteraction textView = onView(
                allOf(withId(R.id.schedule_name), withText(TEST_SCHEDULE_NAME_2)));
        textView.check(matches(withText(TEST_SCHEDULE_NAME_2)));

        // Check that the displayed schedule name was changed in schedule list
        pressBack();
        ViewInteraction textView2 = onView(
                allOf(withId(R.id.schedule_list_name), withText(TEST_SCHEDULE_NAME_2),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        0),
                                0),
                        isDisplayed()));
        textView2.check(matches(withText(TEST_SCHEDULE_NAME_2)));
    }

    @Test
    public void testEditScheduleNameCanceled() {
        this.testOpenScheduleDetails();

        ViewInteraction appCompatButton6 = onView(
                allOf(withId(R.id.edit_schedule_name_button), isDisplayed()));
        appCompatButton6.perform(click());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.newNameText), isDisplayed()));
        appCompatEditText3.perform(replaceText(TEST_SCHEDULE_NAME_2), closeSoftKeyboard());

        ViewInteraction appCompatButton7 = onView(
                allOf(withId(R.id.negativeButton), withText("Cancel"), isDisplayed()));
        appCompatButton7.perform(click());

        // Check that the displayed schedule name was NOT changed in details view
        ViewInteraction textView = onView(
                allOf(withId(R.id.schedule_name), withText(TEST_SCHEDULE_NAME_1)));
        textView.check(matches(withText(TEST_SCHEDULE_NAME_1)));

        // Check that the displayed schedule name was NOT changed in schedule list
        pressBack();
        ViewInteraction textView2 = onView(
                allOf(withId(R.id.schedule_list_name), withText(TEST_SCHEDULE_NAME_1),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        0),
                                0),
                        isDisplayed()));
        textView2.check(matches(withText(TEST_SCHEDULE_NAME_1)));
    }

    @Test
    public void testEditScheduleBlankName() {
        this.testOpenScheduleDetails();

        ViewInteraction appCompatButton6 = onView(
                allOf(withId(R.id.edit_schedule_name_button), isDisplayed()));
        appCompatButton6.perform(click());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.newNameText), isDisplayed()));
        appCompatEditText3.perform(replaceText(""), closeSoftKeyboard());

        // Press ok button in popup
        ViewInteraction appCompatButton5 = onView(
                allOf(withId(R.id.positiveButton), isDisplayed()));
        appCompatButton5.perform(click());

        // Check that the name error popup shows
        onView(withText("Invalid Name")).check(matches(isDisplayed()));

        // Dismiss error popup
        ViewInteraction appCompatButton10 = onView(
                allOf(withId(android.R.id.button1), withText("Okay")));
        appCompatButton10.perform(scrollTo(), click());

        // Check that the displayed schedule name was NOT changed in details view
        ViewInteraction textView = onView(
                allOf(withId(R.id.schedule_name), withText(TEST_SCHEDULE_NAME_1)));
        textView.check(matches(withText(TEST_SCHEDULE_NAME_1)));

        // Check that the displayed schedule name was NOT changed in schedule list
        pressBack();
        ViewInteraction textView2 = onView(
                allOf(withId(R.id.schedule_list_name), withText(TEST_SCHEDULE_NAME_1),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        0),
                                0),
                        isDisplayed()));
        textView2.check(matches(withText(TEST_SCHEDULE_NAME_1)));
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
                allOf(withId(R.id.checkBox1), withText("YouTube"),
                        withParent(childAtPosition(
                                withId(R.id.appListView),
                                0)),
                        isDisplayed()));
        appCompatCheckBox.perform(click());

        ViewInteraction appCompatCheckBox2 = onView(
                allOf(withId(R.id.checkBox1), withText("Google App"),
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

        ViewInteraction viewPager4 = onView(
                allOf(withId(R.id.viewPager), isDisplayed()));
        viewPager4.perform(swipeLeft());

        onView(isRoot()).perform(waitFor(1000));
    }

    @Test
    public void testSelectProfileToBeScheduled() {
        this.createTestProfileWithName(TEST_PROFILE_NAME_1);
        this.testCreateNamedSchedule();

        // Select first schedule in schedule list
        ViewInteraction linearLayout2 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.scheduleListView),
                                withParent(withId(R.id.scheduleListLayout))),
                        0),
                        isDisplayed()));
        linearLayout2.perform(click());

        // Click add button in menu bar
        ViewInteraction actionMenuItemView2 = onView(
                allOf(withId(R.id.add_profile_button), isDisplayed()));
        actionMenuItemView2.perform(click());

        // Click profile selection spinner
        ViewInteraction appCompatSpinner = onView(
                allOf(withId(R.id.profile_spinner), isDisplayed()));
        appCompatSpinner.perform(click());

        // Select test profile
        ViewInteraction appCompatCheckedTextView = onView(
                allOf(withId(android.R.id.text1), withText(TEST_PROFILE_NAME_1), isDisplayed()));
        appCompatCheckedTextView.perform(click());

        // Check that the test profile is now selected
        ViewInteraction textView = onView(
                allOf(withId(android.R.id.text1), withText(TEST_PROFILE_NAME_1),
                        childAtPosition(
                                allOf(withId(R.id.profile_spinner),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.RelativeLayout.class),
                                                1)),
                                0),
                        isDisplayed()));
        textView.check(matches(withText(TEST_PROFILE_NAME_1)));
    }

    @Test
    public void testSetProfileDaysOfWeek() {
        this.testSelectProfileToBeScheduled();

        // Select days of the week
        ViewInteraction appCompatCheckBox3 = onView(
                allOf(withId(R.id.monday), withText("M"), isDisplayed()));
        appCompatCheckBox3.perform(click());
        appCompatCheckBox3.check(matches(isChecked()));

        ViewInteraction appCompatCheckBox4 = onView(
                allOf(withId(R.id.wednesday), withText("W"), isDisplayed()));
        appCompatCheckBox4.perform(click());
        appCompatCheckBox4.check(matches(isChecked()));

        ViewInteraction appCompatCheckBox5 = onView(
                allOf(withId(R.id.friday), withText("F"), isDisplayed()));
        appCompatCheckBox5.perform(click());
        appCompatCheckBox5.check(matches(isChecked()));
    }

    @Test
    public void testSetProfileStartTime() {
        this.testSetProfileDaysOfWeek();

        // Click start time picker
        ViewInteraction appCompatButton7 = onView(
                allOf(withId(R.id.start_time_button),
                        withParent(withId(R.id.start_time_layout)),
                        isDisplayed()));
        appCompatButton7.perform(click());

        // Click ok button in start time picker
        ViewInteraction appCompatButton8 = onView(
                allOf(withId(android.R.id.button1), withText("OK")));
        appCompatButton8.perform(scrollTo(), click());
    }

    @Test
    public void testSetInvalidProfileDuration() {
        this.testSetProfileStartTime();

        // Check lower bound
        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.hours_field),
                        withParent(withId(R.id.duration_layout)),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("0"), closeSoftKeyboard());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.mins_field),
                        withParent(withId(R.id.duration_layout)),
                        isDisplayed()));
        appCompatEditText6.perform(replaceText("9"), closeSoftKeyboard());

        // Make sure keyboard is closed
        closeSoftKeyboard();

        // Attempt to add profile to schedule
        ViewInteraction appCompatButton9 = onView(
                allOf(withId(R.id.add_profile), withText("Add"), isDisplayed()));
        appCompatButton9.perform(click());

        // Check duration error popup shows
        onView(withText("Invalid Duration")).check(matches(isDisplayed()));

        // Click okay in popup
        ViewInteraction appCompatButton10 = onView(
                allOf(withId(android.R.id.button1), withText("Okay")));
        appCompatButton10.perform(scrollTo(), click());


        // Check upper bound (hours)
        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.hours_field),
                        withParent(withId(R.id.duration_layout)),
                        isDisplayed()));
        appCompatEditText7.perform(replaceText("11"), closeSoftKeyboard());

        ViewInteraction appCompatEditText8 = onView(
                allOf(withId(R.id.mins_field),
                        withParent(withId(R.id.duration_layout)),
                        isDisplayed()));
        appCompatEditText8.perform(replaceText("0"), closeSoftKeyboard());

        // Make sure keyboard is closed
        closeSoftKeyboard();

        // Attempt to add profile to schedule
        ViewInteraction appCompatButton11 = onView(
                allOf(withId(R.id.add_profile), withText("Add"), isDisplayed()));
        appCompatButton9.perform(click());

        // Check duration error popup shows
        onView(withText("Invalid Duration")).check(matches(isDisplayed()));

        // Click okay in popup
        ViewInteraction appCompatButton12 = onView(
                allOf(withId(android.R.id.button1), withText("Okay")));
        appCompatButton10.perform(scrollTo(), click());


        // Check upper bound (minutes)
        ViewInteraction appCompatEditText9 = onView(
                allOf(withId(R.id.hours_field),
                        withParent(withId(R.id.duration_layout)),
                        isDisplayed()));
        appCompatEditText9.perform(replaceText("10"), closeSoftKeyboard());

        ViewInteraction appCompatEditText10 = onView(
                allOf(withId(R.id.mins_field),
                        withParent(withId(R.id.duration_layout)),
                        isDisplayed()));
        appCompatEditText10.perform(replaceText("1"), closeSoftKeyboard());

        // Make sure keyboard is closed
        closeSoftKeyboard();

        // Attempt to add profile to schedule
        ViewInteraction appCompatButton13 = onView(
                allOf(withId(R.id.add_profile), withText("Add"), isDisplayed()));
        appCompatButton9.perform(click());

        // Check duration error popup shows
        onView(withText("Invalid Duration")).check(matches(isDisplayed()));

        // Click okay in popup
        ViewInteraction appCompatButton14 = onView(
                allOf(withId(android.R.id.button1), withText("Okay")));
        appCompatButton10.perform(scrollTo(), click());
    }

    @Test
    public void testSetValidProfileDuration() {
        this.testSetProfileStartTime();

        // Enter upper bound
        ViewInteraction appCompatEditText11 = onView(
                allOf(withId(R.id.hours_field),
                        withParent(withId(R.id.duration_layout)),
                        isDisplayed()));
        appCompatEditText11.perform(replaceText("1"), closeSoftKeyboard());

        ViewInteraction appCompatEditText12 = onView(
                allOf(withId(R.id.mins_field),
                        withParent(withId(R.id.duration_layout)),
                        isDisplayed()));
        appCompatEditText12.perform(replaceText("15"), closeSoftKeyboard());

        closeSoftKeyboard();

        // Add profile to schedule
        ViewInteraction appCompatButton15 = onView(
                allOf(withId(R.id.add_profile), withText("Add"), isDisplayed()));
        appCompatButton15.perform(click());

        // Check that week view is now shown
        ViewInteraction view = onView(withId(R.id.weekView));
        view.check(matches(isDisplayed()));
    }

    @Test
    public void testDeleteScheduleCanceled() {
        this.testOpenScheduleDetails();

        // Click delete schedule button
        ViewInteraction appCompatButton19 = onView(
                allOf(withId(R.id.delete_schedule_button), isDisplayed()));
        appCompatButton19.perform(click());

        // Check that confirm delete popup is shown
        onView(withText("Confirm Delete")).check(matches(isDisplayed()));

        // Click cancel in popup
        ViewInteraction appCompatButton20 = onView(
                allOf(withId(android.R.id.button2), withText("Cancel")));
        appCompatButton20.perform(scrollTo(), click());

        // Check that week view is still shown
        ViewInteraction view = onView(
                allOf(withId(R.id.weekView),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        view.check(matches(isDisplayed()));
    }

    @Test
    public void testDeleteSchedule() {
        this.testOpenScheduleDetails();

        // Click delete schedule button
        ViewInteraction appCompatButton21 = onView(
                allOf(withId(R.id.delete_schedule_button), isDisplayed()));
        appCompatButton21.perform(click());

        // Check that confirm delete popup is shown
        onView(withText("Confirm Delete")).check(matches(isDisplayed()));

        // Click delete button in popup
        ViewInteraction appCompatButton22 = onView(
                allOf(withId(android.R.id.button1), withText("Delete")));
        appCompatButton22.perform(scrollTo(), click());
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
