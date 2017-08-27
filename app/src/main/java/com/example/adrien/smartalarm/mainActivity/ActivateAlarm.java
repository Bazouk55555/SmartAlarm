package com.example.adrien.smartalarm.mainActivity;

import android.content.Intent;

import com.example.adrien.smartalarm.AfterAlarmRing.BackgroundService;

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
    }

    public void run(){
        System.out.println("ALARM ACTIVATED WITH POSITION: "+index);
        while(continueThread) {
            int hourOfTheDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            int minuteOfTheDay = Calendar.getInstance().get(Calendar.MINUTE);
            if (continueThread && smartAlarm.getAlarmsActivated().get(index) && smartAlarm.getAlarmsHours().get(index) == hourOfTheDay && smartAlarm.getAlarmsMinutes().get(index) == minuteOfTheDay) {
                Intent intent_to_alarm_ring=new Intent(smartAlarm,BackgroundService.class);
                String hour = (smartAlarm.getAlarmsHours().get(index)>=0 && smartAlarm.getAlarmsHours().get(index)<10)? "0" + smartAlarm.getAlarmsHours().get(index) : "" + smartAlarm.getAlarmsHours().get(index);
                String minute = (smartAlarm.getAlarmsMinutes().get(index)>=0 && smartAlarm.getAlarmsMinutes().get(index)<10)? "0" + smartAlarm.getAlarmsMinutes().get(index) : "" + smartAlarm.getAlarmsMinutes().get(index);
                String time = hour+":"+minute;
                intent_to_alarm_ring.putExtra("time",time);
                intent_to_alarm_ring.putExtra("title",title);
                intent_to_alarm_ring.putExtra("uri_image",smartAlarm.getUriImage());
                intent_to_alarm_ring.putExtra("uri_sound",smartAlarm.getUriSound());
                intent_to_alarm_ring.putExtra("sound",sound);
                intent_to_alarm_ring.putExtra("activate_game",smartAlarm.isActivateGame());
                System.out.println("JE SUIS LA JE SUIS PAS MORT!!!");
                if(smartAlarm.getCategory()!=null) {
                    intent_to_alarm_ring.putExtra("category", smartAlarm.getCategory());
                }
                if(smartAlarm.getNumberOfQuestions()!=0) {
                    intent_to_alarm_ring.putExtra("number_of_questions", smartAlarm.getNumberOfQuestions());
                }
                smartAlarm.startService(intent_to_alarm_ring);
                continueThread=false;
            }
        }
        System.out.println("ALARM DEACTIVATED WITH POSITION: "+index);
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
