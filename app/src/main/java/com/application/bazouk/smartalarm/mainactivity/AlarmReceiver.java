package com.application.bazouk.smartalarm.mainactivity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.application.bazouk.smartalarm.afterAlarmRing.AlarmRing;
import com.application.bazouk.smartalarm.R;
import com.application.bazouk.smartalarm.sqliteService.Alarm;
import com.application.bazouk.smartalarm.sqliteService.AlarmBaseDAO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()))
        {
            setAlarmManager(context);
        }
    }

    private void setAlarmManager(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);
        List<Integer> alarmsHours = new ArrayList<>();
        List<Integer> alarmsMinutes = new ArrayList<>();
        List<String> alarmsTitle = new ArrayList<>();
        List<Integer> alarmsSound = new ArrayList<>();
        AlarmBaseDAO alarmBaseDAO = new AlarmBaseDAO(context);
        alarmBaseDAO.open();
        List<Alarm> alarmList = alarmBaseDAO.select();
        for (Alarm alarm : alarmList) {
            if(alarm.getActivated()) {
                alarmsHours.add(alarm.getHour());
                alarmsMinutes.add(alarm.getMinute());
                alarmsTitle.add(alarm.getTitle());
                alarmsSound.add(alarm.getSound());
            }
        }
        alarmBaseDAO.close();
        for(int i =0; i<alarmsHours.size();i++) {
            Intent intentToAlarmRing = new Intent(context, AlarmRing.class);
            intentToAlarmRing.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            String hour = getStringTimeFromAnIndexOfListOfNumber(alarmsHours, i);
            String minute = getStringTimeFromAnIndexOfListOfNumber(alarmsMinutes, i);
            String time = hour + ":" + minute;
            intentToAlarmRing.putExtra("time", time);
            intentToAlarmRing.putExtra("title", alarmsTitle.get(i));
            intentToAlarmRing.putExtra("sound", chooseAlarmSoundFromNumber(alarmsSound.get(i),context));
            PendingIntent pendingIntent = PendingIntent.getActivity(context, Integer.parseInt(hour + minute),
                    intentToAlarmRing, PendingIntent.FLAG_CANCEL_CURRENT);
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MINUTE, alarmsMinutes.get(i));
            cal.set(Calendar.HOUR_OF_DAY, alarmsHours.get(i));
            if (cal.getTime().before(Calendar.getInstance().getTime())) {
                cal.add(Calendar.DAY_OF_YEAR, 1);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
            }
            else
            {
                alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
            }
        }
    }

    private String getStringTimeFromAnIndexOfListOfNumber(List <Integer> numbers, int index) {
        return (numbers.get(index) >= 0 && numbers.get(index) < 10)
                ? "0" + numbers.get(index)
                : "" + numbers.get(index);
    }

    private String chooseAlarmSoundFromNumber(int number,Context context) {
        switch (number) {
            case 0:
                return context.getResources().getString(R.string.alarm1);
            case 1:
                return context.getResources().getString(R.string.alarm2);
            case 2:
                return context.getResources().getString(R.string.alarm3);
            case 3:
                return context.getResources().getString(R.string.alarm4);
            case 4:
                return context.getResources().getString(R.string.alarm5);
            case 5:
                return context.getResources().getString(R.string.alarm6);
            default:
                return context.getResources().getString(R.string.alarm1);
        }
    }
}
