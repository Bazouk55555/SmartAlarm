package com.example.adrien.smartalarm.adrien.mainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.adrien.smartalarm.R;
import com.example.adrien.smartalarm.adrien.SmartAlarm;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

@RunWith(AndroidJUnit4.class)
public class NumberQuestionsDialogTest {

	@Rule
	public ActivityTestRule<SmartAlarm> mActivityRule = new ActivityTestRule<>(SmartAlarm.class);

	@Test
	public void numberOfQuestionsTest() {
		onView(withId(R.id.game)).perform(click());
		onView(withText(mActivityRule.getActivity().getResources().getString(R.string.number_of_questions_menu))).perform(click());
		onView(withId(R.id.number_of_questions)).perform(replaceText("3"));
		onView(withId(R.id.number_of_questions)).check(matches(withText("3")));
        onView(withId(R.id.arrow_up1)).perform(click());
        onView(withId(R.id.number_of_questions)).check(matches(withText("4")));
        onView(withId(R.id.arrow_down1)).perform(click());
        onView(withId(R.id.number_of_questions)).check(matches(withText("3")));
		onView(withId(R.id.ok)).perform(click());
	}
}
