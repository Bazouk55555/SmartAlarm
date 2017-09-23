package com.example.adrien.smartalarm.mainActivity;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.adrien.smartalarm.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class CategoryDialogTest {

    @Rule
    public ActivityTestRule<SmartAlarm> mActivityRule =
            new ActivityTestRule<>(SmartAlarm.class);

    @Test
    public void categoryChecked()
    {
        onView(withId(R.id.game)).perform(click());
        onView(withText(mActivityRule.getActivity().getResources().getString(R.string.choose_category_menu))).perform(click());
        onView(withId(R.id.category2)).perform(click());
        onView(withId(R.id.category2)).check(matches(isChecked()));
        onView(withId(R.id.ok)).perform(click());
    }
}
