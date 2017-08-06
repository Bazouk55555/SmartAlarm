package com.example.adrien.smartalarm;

import android.content.Intent;
import android.net.Uri;

import java.util.Calendar;

public class ActivateAlarm implements Runnable{
    private SmartAlarm smartAlarm;
    private int index;
    private String sound;
    private String title;
    private boolean continueThread = true;

    public ActivateAlarm(SmartAlarm smartAlarm, int index, String sound,String title)
    {
        this.smartAlarm=smartAlarm;
        this.index=index;
        this.sound=sound;
        this.title=title;
        System.out.println("HAHA:"+sound);
    }

    public void run(){
        while(continueThread) {
            int hourOfTheDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            int minuteOfTheDay = Calendar.getInstance().get(Calendar.MINUTE);
            if (continueThread && smartAlarm.getAlarmsActivated().get(index) && smartAlarm.getAlarmsHours().get(index) == hourOfTheDay && smartAlarm.getAlarmsMinutes().get(index) == minuteOfTheDay) {
                Intent intent_to_alarm_ring=new Intent(smartAlarm,AlarmRing.class);
                String hour = (smartAlarm.getAlarmsHours().get(index)>=0 && smartAlarm.getAlarmsHours().get(index)<10)? "0" + smartAlarm.getAlarmsHours().get(index) : "" + smartAlarm.getAlarmsHours().get(index);
                String minute = (smartAlarm.getAlarmsMinutes().get(index)>=0 && smartAlarm.getAlarmsMinutes().get(index)<10)? "0" + smartAlarm.getAlarmsMinutes().get(index) : "" + smartAlarm.getAlarmsMinutes().get(index);
                String time = hour+":"+minute;
                intent_to_alarm_ring.putExtra("time",time);
                intent_to_alarm_ring.putExtra("title",title);
                intent_to_alarm_ring.putExtra("uri_image",smartAlarm.getUriImage());
                intent_to_alarm_ring.putExtra("uri_sound",smartAlarm.getUriSound());
                intent_to_alarm_ring.putExtra("sound",sound);
                System.out.println("HERE 1");
                smartAlarm.startActivity(intent_to_alarm_ring);
                continueThread=false;
            }
        }
    }

    public void setIndex(int index)
    {
        this.index=index;
    }

    public int getIndex()
    {
        return index;
    }

    public void setContinueThread(boolean continueThread)
    {
        this.continueThread=continueThread;
    }
}
