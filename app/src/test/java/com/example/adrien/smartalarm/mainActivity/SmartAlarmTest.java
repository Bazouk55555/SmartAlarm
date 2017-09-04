package com.example.adrien.smartalarm.mainActivity;
import android.net.Uri;

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
        smartAlarm.setNewAlarm("01:01","Title",1,1,1);
    }

    @Test
    public void setNewAlarmTest()
    {
        assertEquals(1,smartAlarm.getAlarmsMinutes().size());
    }

    @Test
    public void getAlarmsMinutesTest()
    {
        assertEquals((Integer)1,smartAlarm.getAlarmsMinutes().get(0));
    }

    @Test
    public void getAlarmsHoursTest()
    {
        assertEquals((Integer)1,smartAlarm.getAlarmsHours().get(0));
    }

    @Test
    public void getAlarmsActivatedTest()
    {
        assertEquals((Boolean)true,smartAlarm.getAlarmsActivated().get(0));
    }

    @Test
    public void setUriImageTest()
    {
        Uri uriImageExpected = new Uri.Builder().appendPath("Path").build();
        smartAlarm.setUriImage(uriImageExpected);
        assertEquals(uriImageExpected.getEncodedPath(),smartAlarm.getUriImage().getEncodedPath());
    }

    @Test
    public void setUriSoundTest()
    {

        Uri uriSoundExpected = new Uri.Builder().appendPath("Path").build();
        smartAlarm.setUriSound(uriSoundExpected);
        assertEquals(uriSoundExpected.getEncodedPath(),smartAlarm.getUriSound().getEncodedPath());
    }

    @Test
    public void setIsAlarmSixTest()
    {
        assertEquals(false,smartAlarm.getIsAlarmSix());
    }

    /*@Test
    public void setCategoryTest()
    {

    }

    @Test
    public void getCategoryTest()
    {

    }

    @Test
    public void getNumberOfQuestionsTest()
    {

    }

    @Test
    public void setNewAlarmTest()
    {

    }

    @Test
    public void changeAlarmTest()
    {

    }

    @Test
    public void removeAlarmTest()
    {

    }*/
}
