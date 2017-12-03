package com.application.bazouk.smartalarm.afterAlarmRing;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.anything;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.application.bazouk.smartalarm.R;
import com.application.bazouk.smartalarm.mainactivity.SmartAlarm;

import android.app.Activity;
import android.app.Instrumentation;
import android.graphics.drawable.Drawable;
import android.icu.util.Calendar;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.ImageView;

@RunWith(AndroidJUnit4.class)
public class AlarmRingTest {

    @Rule
    public ActivityTestRule<SmartAlarm> mActivityRule =
            new ActivityTestRule<>(SmartAlarm.class);

    private Instrumentation.ActivityMonitor alarmRingActivityMonitor = getInstrumentation().addMonitor(AlarmRing.class.getName(), null, true);

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Test
    public void alarmDisplayedTest()
    {
        if(PreferenceManager.getDefaultSharedPreferences(mActivityRule.getActivity()).getBoolean(SmartAlarm.IS_GAME_ACTIVATED,false)) {
            onView(withId(R.id.checkbox)).perform(click());
        }

        while(!mActivityRule.getActivity().getAlarmsMinutes().isEmpty())
        {
            onData(anything()).inAdapterView(withId(R.id.list_alarm)).atPosition(0).perform(click());
            onView(withId(R.id.remove)).perform(click());
        }
        onView(withId(R.id.add)).perform(click());
        int hourOfAlarm;
        int minuteOfAlarm;
        if(Calendar.getInstance().get(Calendar.MINUTE)+1!=60) {
            hourOfAlarm = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            minuteOfAlarm = Calendar.getInstance().get(Calendar.MINUTE) + 1;
        }
        else if(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)!=23 &&Calendar.getInstance().get(Calendar.MINUTE)+1==60)
        {
            hourOfAlarm = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) +1 ;
            minuteOfAlarm = 0;
        }
        else
        {
            hourOfAlarm = 0;
            minuteOfAlarm = 0;
        }
        String title = "Title";
        onView(withId(R.id.hours)).perform(typeText(Integer.toString(hourOfAlarm)),
            closeSoftKeyboard());
        onView(withId(R.id.minutes)).perform(clearText());
        onView(withId(R.id.minutes)).perform(typeText(Integer.toString(minuteOfAlarm)),
            closeSoftKeyboard());
        onView(withId(R.id.editTitle)).perform(typeText(title),
                closeSoftKeyboard());
        onView(withId(R.id.save)).perform(click());
        Activity alarmRingActivity = alarmRingActivityMonitor.waitForActivity();
        String currentTimeAlarm = Integer.toString(hourOfAlarm)+":"+Integer.toString(minuteOfAlarm);
        if(hourOfAlarm<10)
        {
            currentTimeAlarm = "0"+currentTimeAlarm;
        }
        if(minuteOfAlarm<10)
        {
            currentTimeAlarm=currentTimeAlarm.substring(0,currentTimeAlarm.length()-1)+"0"+currentTimeAlarm.substring(currentTimeAlarm.length()-1,currentTimeAlarm.length());
        }
        onView(withId(R.id.time)).check(matches(withText(currentTimeAlarm)));
        onView(withId(R.id.title)).check(matches(withText(title)));
        onView(withId(R.id.stop_alarm)).check(matches(isDisplayed()));
        onView(withId(R.id.stop_alarm)).check(matches(withImageView(((ImageView)alarmRingActivity.findViewById(R.id.stop_alarm)).getDrawable())));
        onView(withId(R.id.stop_alarm)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.list_alarm)).atPosition(0).onChildView(withId(R.id.time)).check(matches(withText(currentTimeAlarm)));
        onData(anything()).inAdapterView(withId(R.id.list_alarm)).atPosition(0).perform(click());
        onView(withId(R.id.remove_alarm_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.remove)).perform(click());
    }

    private Matcher<View> withImageView(final Drawable drawable) {
        return new BoundedMatcher<View, ImageView>(ImageView.class) {
            @Override
            public boolean matchesSafely(ImageView imageView1) {
                Drawable a = imageView1.getDrawable();
                System.out.println("Here I am 1: "+a);
                System.out.println("Here I am 4: "+ Integer.toHexString((imageView1.getId())));
                Drawable b= drawable;
                System.out.println("Here I am 2: "+b);
                return imageView1.getDrawable() == drawable;
            }
            @Override
            public void describeTo(org.hamcrest.Description description) {
                description.appendText("Stop alarm");
            }
        };
    }
}
