package com.example.adrien.smartalarm.mainActivity;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.anything;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.adrien.smartalarm.R;
import com.example.adrien.smartalarm.smartalarm.SmartAlarm;

import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;

@RunWith(AndroidJUnit4.class)
public class SmartAlarmTest {

    @Rule
    public ActivityTestRule<SmartAlarm> mActivityRule =
            new ActivityTestRule<>(SmartAlarm.class);

    @Test
    public void checkBoxTest()
    {
        if(PreferenceManager.getDefaultSharedPreferences(mActivityRule.getActivity()).getBoolean(SmartAlarm.IS_GAME_ACTIVATED,false)) {
            onView(withId(R.id.checkbox)).perform(click()).check(matches(isNotChecked()));
            onView(withId(R.id.checkbox)).perform(click()).check(matches(isChecked()));
        }
        else
        {
            onView(withId(R.id.checkbox)).perform(click()).check(matches(isChecked()));
            onView(withId(R.id.checkbox)).perform(click()).check(matches(isNotChecked()));
        }
    }

    @Test
    public void menuConfigurationTest()
    {
        onView(withId(R.id.configuration)).check(matches(isEnabled()));
        onView(withId(R.id.configuration)).perform(click());
        onView(withText(mActivityRule.getActivity().getResources().getString(R.string.add_a_sound))).perform(click());
        onView(withId(R.id.dialog_add_sound)).check(matches(isDisplayed()));
        onView(withId(R.id.cancel)).perform(click());
        onView(withId(R.id.configuration)).perform(click());
        onView(withText(mActivityRule.getActivity().getResources().getString(R.string.add_an_image))).perform(click());
        onView(withId(R.id.dialog_add_image)).check(matches(isDisplayed()));
        onView(withId(R.id.cancel)).perform(click());
    }

    @Test
    public void menuGameTest()
    {
        onView(withId(R.id.game)).check(matches(isClickable()));
        onView(withId(R.id.game)).perform(click());
        onView(withText(mActivityRule.getActivity().getResources().getString(R.string.choose_category_menu))).perform(click());
        onView(withId(R.id.category_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.ok)).perform(click());
        onView(withId(R.id.game)).perform(click());
        onView(withText(mActivityRule.getActivity().getResources().getString(R.string.number_of_questions_menu))).perform(click());
        onView(withId(R.id.number_of_question_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.ok)).perform(click());
        onView(withId(R.id.game)).perform(click());
        onView(withText(mActivityRule.getActivity().getResources().getString(R.string.level_of_questions_menu))).perform(click());
        onView(withId(R.id.level_of_question_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.ok)).perform(click());
    }

    @Test
    public void menuAddTest()
    {
        onView(withId(R.id.add)).check(matches(isClickable()));
        onView(withId(R.id.add)).perform(click());
        onView(withId(R.id.add_alarm_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.cancel)).perform(click());
    }

    @Test
    public void alarmDisplayedTest()
    {
        while(!mActivityRule.getActivity().getAlarmsMinutes().isEmpty())
        {
            onData(anything()).inAdapterView(withId(R.id.list_alarm)).atPosition(0).perform(click());
            onView(withId(R.id.remove)).perform(click());
        }
        onView(withId(R.id.add)).perform(click());
        onView(withId(R.id.hours)).perform(typeText("1"),
            closeSoftKeyboard());
        onView(withId(R.id.minutes)).perform(typeText("1"),
            closeSoftKeyboard());
        onView(withId(R.id.editTitle)).perform(typeText("Title"),
                closeSoftKeyboard());
        onView(withId(R.id.save)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.list_alarm)).atPosition(0).onChildView(withId(R.id.time)).check(matches(withText("01:01")));
        onData(anything()).inAdapterView(withId(R.id.list_alarm)).atPosition(0).onChildView(withId(R.id.title)).check(matches(withText("Title")));
        onData(anything()).inAdapterView(withId(R.id.list_alarm)).atPosition(0).check(matches(withBackgroundColor(ContextCompat.getColor(mActivityRule.getActivity(), R.color.bright))));
        onData(anything()).inAdapterView(withId(R.id.list_alarm)).atPosition(0).onChildView(withId(R.id.activate)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.list_alarm)).atPosition(0).check(matches(withBackgroundColor(ContextCompat.getColor(mActivityRule.getActivity(), R.color.dark))));
        onData(anything()).inAdapterView(withId(R.id.list_alarm)).atPosition(0).perform(click());
        onView(withId(R.id.remove_alarm_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.remove)).perform(click());
        assertEquals(0,mActivityRule.getActivity().getAlarmsHours().size());
    }

    private static Matcher<View> withBackgroundColor(final int color) {
        return new BoundedMatcher<View, LinearLayout>(LinearLayout.class) {
            @Override
            public boolean matchesSafely(LinearLayout layoutView) {
                return color == ((ColorDrawable) layoutView.getBackground()).getColor();
            }
            @Override
            public void describeTo(Description description) {
                description.appendText("with background color: ");
            }
        };
    }
}
