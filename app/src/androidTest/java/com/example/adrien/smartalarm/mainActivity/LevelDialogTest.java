package com.example.adrien.smartalarm.mainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.adrien.smartalarm.R;
import com.example.adrien.smartalarm.smartalarm.SmartAlarm;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

@RunWith(AndroidJUnit4.class)
public class LevelDialogTest {

	@Rule
	public ActivityTestRule<SmartAlarm> mActivityRule = new ActivityTestRule<>(SmartAlarm.class);

	@Test
	public void levelOfQuestionsTest() {
		onView(withId(R.id.game)).perform(click());
		onView(withText(mActivityRule.getActivity().getResources().getString(R.string.level_of_questions_menu))).perform(click());
		onView(withId(R.id.medium)).perform(click());
		onView(withId(R.id.medium)).check(matches(isChecked()));
		onView(withId(R.id.ok)).perform(click());
	}
}
