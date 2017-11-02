package com.example.adrien.smartalarm.mainActivity;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.adrien.smartalarm.R;
import com.example.adrien.smartalarm.smartalarm.SmartAlarm;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

@RunWith(AndroidJUnit4.class)
public class DialogRemoveTest {

	@ClassRule
	public static ActivityTestRule<SmartAlarm> mActivityClassRule = new ActivityTestRule<>(SmartAlarm.class);

	@BeforeClass
	public static void setupBeforeClass()
	{
		while(!mActivityClassRule.getActivity().getAlarmsMinutes().isEmpty())
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
		onView(withId(R.id.list_tone)).perform(click());
		onData(anything()).inRoot(isPlatformPopup()).atPosition(0).perform(click());
		onView(withId(R.id.save)).perform(click());
	}

	/*@AfterClass
	public static void setupAfterClass(){
		while(!mActivityClassRule.getActivity().getAlarmsMinutes().isEmpty())
		{
			onData(anything()).inAdapterView(withId(R.id.list_alarm)).atPosition(0).perform(click());
			onView(withId(R.id.remove)).perform(click());
		}
	}*/

	@Rule
	public ActivityTestRule<SmartAlarm> mActivityRule = new ActivityTestRule<>(SmartAlarm.class);

	@Before
	public void setupBeforeTest()
	{
		onData(anything()).inAdapterView(withId(R.id.list_alarm)).atPosition(0).perform(click());
	}

	@After
	public void setupAfterTest(){
		onView(withId(R.id.cancel)).perform(click());
	}

	@Test
	public void checkIfDialogIsSetUp()
	{
		onView(withId(R.id.hours)).check(matches(withText("01")));
		onView(withId(R.id.minutes)).check(matches(withText("01")));
		onView(withId(R.id.editTitle)).check(matches(withText("Title")));
		onView(withId(R.id.list_tone)).check(matches(withSpinnerText(containsString(mActivityRule.getActivity().getResources().getString(R.string.alarm1)))));
	}

	@Test
	public void hoursTest() {
		onView(withId(R.id.hours)).perform(typeText("03"), closeSoftKeyboard());
		onView(withId(R.id.hours)).check(matches(withText("03")));
		onView(withId(R.id.arrow_up1)).perform(click());
		onView(withId(R.id.hours)).check(matches(withText("04")));
		onView(withId(R.id.arrow_down1)).perform(click());
		onView(withId(R.id.hours)).check(matches(withText("03")));
		onView(withId(R.id.hours)).perform(typeText("43"), closeSoftKeyboard());
		onView(withId(R.id.hours)).check(matches(withText("4")));
		onView(withId(R.id.minutes)).perform(typeText("1"), closeSoftKeyboard());
		onView(withId(R.id.hours)).check(matches(withText("04")));
		onView(withId(R.id.hours)).perform(typeText("1"), closeSoftKeyboard());
		onView(withId(R.id.hours)).perform(clearText());
		onView(withId(R.id.minutes)).perform(typeText("1"), closeSoftKeyboard());
		onView(withId(R.id.hours)).check(matches(withText("00")));
	}

	@Test
	public void minutesTest() {
		onView(withId(R.id.minutes)).perform(typeText("03"), closeSoftKeyboard());
		onView(withId(R.id.minutes)).check(matches(withText("03")));
		onView(withId(R.id.arrow_up2)).perform(click());
		onView(withId(R.id.minutes)).check(matches(withText("04")));
		onView(withId(R.id.arrow_down2)).perform(click());
		onView(withId(R.id.minutes)).check(matches(withText("03")));
		onView(withId(R.id.minutes)).perform(typeText("63"), closeSoftKeyboard());
		onView(withId(R.id.minutes)).check(matches(withText("6")));
		onView(withId(R.id.hours)).perform(typeText("1"), closeSoftKeyboard());
		onView(withId(R.id.minutes)).check(matches(withText("06")));
		onView(withId(R.id.minutes)).perform(typeText("1"), closeSoftKeyboard());
		onView(withId(R.id.minutes)).perform(clearText());
		onView(withId(R.id.hours)).perform(typeText("1"), closeSoftKeyboard());
		onView(withId(R.id.minutes)).check(matches(withText("00")));
	}

	@Test
	public void titleTest() {
		onView(withId(R.id.editTitle)).perform(clearText());
		onView(withId(R.id.editTitle)).perform(typeText("Title"), closeSoftKeyboard());
		onView(withId(R.id.editTitle)).check(matches(withText("Title")));
	}

	@Test
	public void toneTest() {
		onView(withId(R.id.list_tone)).perform(click());
		onData(anything()).inRoot(isPlatformPopup()).atPosition(1).perform(click());
		onView(withId(R.id.list_tone)).check(matches(withSpinnerText(containsString(mActivityRule.getActivity().getResources().getString(R.string.alarm2)))));
	}
}
