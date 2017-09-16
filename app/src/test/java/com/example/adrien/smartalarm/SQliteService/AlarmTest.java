package com.example.adrien.smartalarm.SQliteService;

import org.junit.Test;

import static org.junit.Assert.*;

public class AlarmTest {

    private Alarm alarm = new Alarm(1, 1, 1, "01:01", "Title", 1, true);

    @Test
    public void getIdTest() throws Exception {
        int idExpected =1;
        assertEquals(idExpected, alarm.getId());
    }

    @Test
    public void getHourTest() throws Exception {
        int hourExpected = 1;
        assertEquals(hourExpected, alarm.getHour());
    }

    @Test
    public void getMinuteTest() throws Exception {
        int minuteExpected = 1;
        assertEquals(minuteExpected, alarm.getMinute());
    }

    @Test
    public void getTimeTest() throws Exception {
        String timeExpected = "01:01";
        assertEquals(timeExpected, alarm.getTime());
    }

    @Test
    public void getTitleTest() throws Exception {
        String titleExpected = "Title";
        assertEquals(titleExpected, alarm.getTitle());
    }

    @Test
    public void getSoundTest() throws Exception {
        int soundExpected = 1;
        assertEquals(soundExpected, alarm.getSound());
    }

    @Test
    public void getAlarmActivatedTest() throws Exception {
        assertEquals(true, alarm.getActivated());
    }
}