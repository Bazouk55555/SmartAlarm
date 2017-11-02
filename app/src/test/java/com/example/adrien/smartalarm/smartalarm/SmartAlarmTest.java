package com.example.adrien.smartalarm.smartalarm;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.example.adrien.smartalarm.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class SmartAlarmTest {
	private SmartAlarm smartAlarm;

	@Before
	public void setUp() throws Exception {
		smartAlarm = Robolectric.setupActivity(SmartAlarm.class);
		smartAlarm.setNewAlarm(0, "01:01", "Title", 1, 1, 1, true);
	}

	@Test
	public void setNewAlarmTest() {
		assertEquals(1, smartAlarm.getAlarmsMinutes().size());
	}

	@Test
	public void getAlarmsMinutesTest() {
		assertEquals((Integer) 1, smartAlarm.getAlarmsMinutes().get(0));
	}

	@Test
	public void getAlarmsHoursTest() {
		assertEquals((Integer) 1, smartAlarm.getAlarmsHours().get(0));
	}

	@Test
	public void getAlarmsActivatedTest() {
		assertEquals((Boolean) true, smartAlarm.getAlarmsActivated().get(0));
	}

	@Test
	public void setUriImageTest() {
		Uri uriImageExpected = new Uri.Builder().appendPath("Path").build();
		smartAlarm.setUriImage(uriImageExpected);
		assertEquals(uriImageExpected.getEncodedPath(), PreferenceManager
				.getDefaultSharedPreferences(smartAlarm.getBaseContext()).getString(SmartAlarm.URI_IMAGE, "wrong URI"));
	}

	@Test
	public void setUriSoundTest() {

		Uri uriSoundExpected = new Uri.Builder().appendPath("Path").build();
		smartAlarm.setUriSound(uriSoundExpected);
        assertEquals(uriSoundExpected.getEncodedPath(), PreferenceManager
                .getDefaultSharedPreferences(smartAlarm.getBaseContext()).getString(SmartAlarm.URI_SOUND, "wrong URI"));
	}

	@Test
	public void setIsAlarmSixTest() {
		smartAlarm.setIsAlarmSix(false);
        assertEquals(false, PreferenceManager
                .getDefaultSharedPreferences(smartAlarm.getBaseContext()).getBoolean(SmartAlarm.IS_ALARM_SIX, true));
	}

	@Test
	public void getAndSetCategoryTest() {
		String categoryExpected = "Sports";
		smartAlarm.setCategory(categoryExpected);
        assertEquals(categoryExpected, PreferenceManager
                .getDefaultSharedPreferences(smartAlarm.getBaseContext()).getString(SmartAlarm.CATEGORY, "wrong category"));
	}

	@Test
	public void getAndSetLevelTest() {
		String levelExpected = "Medium";
		smartAlarm.setLevel(levelExpected);
        assertEquals(levelExpected, PreferenceManager
                .getDefaultSharedPreferences(smartAlarm.getBaseContext()).getString(SmartAlarm.LEVEL, "wrong level"));
	}

	@Test
	public void getAndSetNumberOfQuestionsTest() {
		int numberOfQuestionsExpected = 5;
		smartAlarm.setNumberOfQuestions(numberOfQuestionsExpected);
        assertEquals(numberOfQuestionsExpected, PreferenceManager
                .getDefaultSharedPreferences(smartAlarm.getBaseContext()).getInt(SmartAlarm.NUMBER_OF_QUESTIONS, 0));
	}

	@Test
	public void removeAlarmTest() {
		smartAlarm.setNewAlarm(1, "02:02", "Title", 2, 2, 2, true);
		smartAlarm.removeAlarm(1);
		assertEquals(1, smartAlarm.getAlarmsMinutes().size());
	}

	public void getPositionNewAlarmTest()
    {
        smartAlarm.setNewAlarm(1, "02:02", "Title2", 2, 2, 2, true);
        assertEquals(1,smartAlarm.getPositionNewAlarm(1,30));
    }
}
