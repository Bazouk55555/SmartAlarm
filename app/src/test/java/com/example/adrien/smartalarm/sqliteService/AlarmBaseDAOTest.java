package com.example.adrien.smartalarm.sqliteService;

import android.support.constraint.BuildConfig;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.List;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class AlarmBaseDAOTest {

    private AlarmBaseDAO alarmBaseDAO= new AlarmBaseDAO(RuntimeEnvironment.application);

    @Before
    public void setUp() throws Exception {
        alarmBaseDAO.open();
    }

    @After
    public void tearDown() throws Exception {
        alarmBaseDAO.close();
    }

    @Test
    public void AddManyAlarmsTest()
    {
        alarmBaseDAO.add(new Alarm(1,1,1, "01:01", "Title",1,true));
        alarmBaseDAO.add(new Alarm(2,3,1, "03:01", "Title",1,true));
        alarmBaseDAO.add(new Alarm(3,2,2, "02:02", "Title",1,true));
        alarmBaseDAO.add(new Alarm(4,4,23, "04:23", "Title",1,true));
        alarmBaseDAO.add(new Alarm(5,13,13, "13:13", "Title bis",1,false));
        assertEquals(5, alarmBaseDAO.select().size());
    }

    @Test
    public void AddOnAlarmAndSelectTest()
    {
        Alarm alarmExpected = new Alarm(1,1,1, "01:01", "Title",1,true);
        alarmBaseDAO.add(alarmExpected);
        List<Alarm> listAlarm = alarmBaseDAO.select();
        assertEquals(1, listAlarm.size());
        assertEquals(alarmExpected,listAlarm.get(0));
    }

    @Test
    public void RemoveTest()
    {
        alarmBaseDAO.add(new Alarm(1,1,1, "01:01", "Title",1,true));
        alarmBaseDAO.add(new Alarm(2,3,1, "03:01", "Title",1,true));
        alarmBaseDAO.add(new Alarm(3,2,2, "02:02", "Title",1,true));
        alarmBaseDAO.remove(2,1);
        List<Alarm> listAlarm = alarmBaseDAO.select();
        assertEquals(2, listAlarm.size());
    }

    @Test
    public void updateActivationTest()
    {
        alarmBaseDAO.add(new Alarm(1,1,1, "01:01", "Title",1,true));
        alarmBaseDAO.updateActivation(1,1,false);
        List<Alarm> listAlarm = alarmBaseDAO.select();
        assertEquals(1, listAlarm.size());
        assertEquals(false, listAlarm.get(0).getActivated());
    }
}
