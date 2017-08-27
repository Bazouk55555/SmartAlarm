package com.example.adrien.smartalarm.SQliteService;

public class Alarm {

    public long id;
    private int hour;
    private int minute;
    private String time;
    private String title;
    private int sound;
    private boolean isActivated;

    public Alarm(long id,int hour,int minute,String time,String title,int sound,boolean isActivated) {
        this.id = id;
        this.hour= hour;
        this.minute = minute;
        this.time = time;
        this.title = title;
        this.sound = sound;
        this.isActivated = isActivated;
    }

    public long getId()
    {
        return id;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public String getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }

    public int getSound() {
        return sound;
    }

    public boolean getActivated()
    {
        return isActivated;
    }

}
