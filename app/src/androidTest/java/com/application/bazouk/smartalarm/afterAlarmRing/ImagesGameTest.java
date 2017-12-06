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
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.application.bazouk.smartalarm.R;
import com.application.bazouk.smartalarm.mainactivity.SmartAlarm;

import android.app.Activity;
import android.app.Instrumentation;
import android.icu.util.Calendar;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

@RunWith(AndroidJUnit4.class)
public class ImagesGameTest {

    @Rule
    public ActivityTestRule<SmartAlarm> mActivityRule =
            new ActivityTestRule<>(SmartAlarm.class);

    private Instrumentation.ActivityMonitor alarmRingActivityMonitor = getInstrumentation().addMonitor(AlarmRing.class.getName(), null, false);
    private Instrumentation.ActivityMonitor imagesGameActivityMonitor = getInstrumentation().addMonitor(ImagesGame.class.getName(), null, false);

    private final int NUMBER_OF_QUESTIONS_TESTED = 3;

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
        onView(withId(R.id.number_of_questions)).perform(typeText(String.valueOf(NUMBER_OF_QUESTIONS_TESTED)),
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
        alarmRingActivityMonitor.waitForActivity();
        onView(withId(R.id.stop_alarm)).perform(click());
        onView(withId(R.id.dialog_game_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.answer_radio_button1)).perform(click());
        Activity imagesGameActivity;
        int score = 0;
        int scoreBufferBeforeAnswer;
        String scoreText;
        String scoreString;
        for(int i =0;i<NUMBER_OF_QUESTIONS_TESTED-1;i++) {
            onView(withId(R.id.ok)).perform(click());
            imagesGameActivity = imagesGameActivityMonitor.getLastActivity();
            scoreBufferBeforeAnswer = score;
            scoreText = ((TextView)imagesGameActivity.findViewById(R.id.score)).getText().toString();
            int j = scoreText.length()-2;
            scoreString ="";
            while(scoreText.charAt(j)>='0' && scoreText.charAt(j)<='9')
            {
                scoreString = scoreText.charAt(j) + scoreString;
                j--;
            }
            score = Integer.parseInt(scoreString);
            while(((TextView)imagesGameActivity.findViewById(R.id.comment)).getText().toString().equals(""))
            {
            }
            if(scoreBufferBeforeAnswer<score)
            {
                onView(withId(R.id.comment)).check(matches(withText(imagesGameActivity.getResources().getString(R.string.good_job))));
            }
            else
            {
                onView(withId(R.id.comment)).check(matches(withText(imagesGameActivity.getResources().getString(R.string.still_sleepy))));
            }
            onView(withId(R.id.images_game)).perform(click());
            onView(withId(R.id.dialog_game_layout)).check(matches(isDisplayed()));
        }
        onView(withId(R.id.ok)).perform(click());
        imagesGameActivity = imagesGameActivityMonitor.getLastActivity();
        scoreText = ((TextView)imagesGameActivity.findViewById(R.id.score)).getText().toString();
        int j = scoreText.length()-2;
        scoreString ="";
        while(scoreText.charAt(j)>='0' && scoreText.charAt(j)<='9')
        {
            scoreString = scoreText.charAt(j) + scoreString;
            j--;
        }
        score = Integer.parseInt(scoreString);
        while(((TextView)imagesGameActivity.findViewById(R.id.comment)).getText().toString().equals(""))
        {
        }
        if(score < 35)
        {
            onView(withId(R.id.comment)).check(matches(withText(imagesGameActivity.getResources().getString(R.string.go_back_to_bed))));
        }
        else if(score < 65)
        {
            onView(withId(R.id.comment)).check(matches(withText(imagesGameActivity.getResources().getString(R.string.take_a_coffee))));
        }
        else
        {
            onView(withId(R.id.comment)).check(matches(withText(imagesGameActivity.getResources().getString(R.string.ready_to_go_to_the_gym))));
        }
        onView(withId(R.id.images_game)).perform(click());
    }
}
