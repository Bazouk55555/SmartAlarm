package com.example.adrien.smartalarm.AfterAlarmRing;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.adrien.smartalarm.AfterAlarmRing.AlarmRing;

public class BackgroundService extends IntentService {

    public BackgroundService() {
        super("a service");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        System.out.println("HERE 2");
        Intent intentToRingAlarm = new Intent(this, AlarmRing.class);
        intentToRingAlarm.putExtra("time",intent.getStringExtra("time"));
        intentToRingAlarm.putExtra("title",intent.getStringExtra("title"));
        intentToRingAlarm.putExtra("uri_image",intent.getParcelableExtra("uri_image"));
        intentToRingAlarm.putExtra("uri_sound",intent.getParcelableExtra("uri_sound"));
        intentToRingAlarm.putExtra("sound",intent.getStringExtra("sound"));
        intentToRingAlarm.putExtra("category",intent.getStringExtra("category"));
        intentToRingAlarm.putExtra("number_of_questions",intent.getIntExtra("number_of_questions",5));
        intentToRingAlarm.putExtra("activate_game",intent.getBooleanExtra("activate_game",false));
        intentToRingAlarm.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentToRingAlarm);
    }
}