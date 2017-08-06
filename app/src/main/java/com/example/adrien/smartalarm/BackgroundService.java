package com.example.adrien.smartalarm;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

public class BackgroundService extends IntentService {

    public BackgroundService() {
        super("");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Intent intentToRingAlarm = new Intent();
        startActivity(intent);
    }
}
