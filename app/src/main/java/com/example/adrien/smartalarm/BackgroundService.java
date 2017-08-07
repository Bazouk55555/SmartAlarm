package com.example.adrien.smartalarm;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

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
        intentToRingAlarm.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentToRingAlarm);
    }
}
