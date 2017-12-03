package com.application.bazouk.smartalarm.afterAlarmRing;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.application.bazouk.smartalarm.R;
import com.application.bazouk.smartalarm.mainactivity.SmartAlarm;

import android.app.PendingIntent;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

@RunWith(AndroidJUnit4.class)
public class DialogNewGameTest {

    @Rule
    public ActivityTestRule<SmartAlarm> mActivityRule =
            new ActivityTestRule<>(SmartAlarm.class);

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Test
    public void alarmDisplayedTest()
    {
        if(!PreferenceManager.getDefaultSharedPreferences(mActivityRule.getActivity()).getBoolean(SmartAlarm.IS_GAME_ACTIVATED,false)) {
            onView(withId(R.id.checkbox)).perform(click());
        }

        onView(withId(R.id.game)).perform(click());
        onView(withText(mActivityRule.getActivity().getResources().getString(R.string.number_of_questions_menu))).perform(click());
        onView(withId(R.id.number_of_questions)).perform(clearText());
        onView(withId(R.id.number_of_questions)).perform(typeText("1"),
                closeSoftKeyboard());
        onView(withId(R.id.ok)).perform(click());

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
        onView(withId(R.id.hours)).perform(typeText(Integer.toString(hourOfAlarm)),
                closeSoftKeyboard());
        onView(withId(R.id.minutes)).perform(clearText());
        onView(withId(R.id.minutes)).perform(typeText(Integer.toString(minuteOfAlarm)),
                closeSoftKeyboard());
        onView(withId(R.id.save)).perform(click());
        while(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)!=hourOfAlarm || Calendar.getInstance().get(Calendar.MINUTE)!=minuteOfAlarm+1)
        {
        }
        onView(withId(R.id.stop_alarm)).perform(click());
        onView(withId(R.id.dialog_game_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.answer_radio_button1)).perform(click());
        onView(withId(R.id.ok)).perform(click());
        //Check here if intent is launched
    }

    private boolean isAlarmSet(int hour, int minute) {
        Intent intent = new Intent(mActivityRule.getActivity(),AlarmRing.class);
        PendingIntent service = PendingIntent.getService(
                mActivityRule.getActivity().getApplicationContext(),
                Integer.parseInt(String.valueOf(hour) + String.valueOf(minute)),
                intent,
                PendingIntent.FLAG_NO_CREATE
        );
        return service != null;
    }
}
