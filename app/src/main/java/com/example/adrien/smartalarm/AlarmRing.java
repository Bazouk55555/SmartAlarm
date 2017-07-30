package com.example.adrien.smartalarm;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class AlarmRing extends AppCompatActivity {
    private TextView timeView=null;
    private TextView titleView=null;
    private Button stopAlarm=null;
    private RelativeLayout mainLayout=null;
    private Uri uriImage;
    private MediaPlayer mediaPlayer;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_ring);
        timeView = (TextView) findViewById(R.id.time);
        titleView = (TextView) findViewById(R.id.title);
        timeView.setText(getIntent().getStringExtra("time"));
        titleView.setText(getIntent().getStringExtra("title"));
        switch(getIntent().getStringExtra("sound")) {
            case "alarm1":
                mediaPlayer = MediaPlayer.create(this, R.raw.alarm1);
                break;
            case "alarm6":
                mediaPlayer = new MediaPlayer();
                Uri uriSound = getIntent().getParcelableExtra("uri_sound");
                try {
                    mediaPlayer.setDataSource(getApplicationContext(), uriSound);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }

        mediaPlayer.start();
        stopAlarm = (Button)findViewById(R.id.stop_alarm);
        stopAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
            }
        });

        mainLayout = (RelativeLayout) findViewById(R.id.main_layout);

        uriImage=getIntent().getParcelableExtra("uri_image");
        if(uriImage!=null)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},2);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permission [], int[] grantResult)
    {
        switch(requestCode)
        {
            case 2:
                if(grantResult.length>0 && grantResult[0]== PackageManager.PERMISSION_GRANTED)
                {
                    InputStream inputStream=null;
                    try {
                        inputStream = getContentResolver().openInputStream(uriImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    BitmapFactory.Options option= new BitmapFactory.Options();
                    Bitmap bitmapImage = BitmapFactory.decodeStream(inputStream, null, option);
                    mainLayout.setBackgroundDrawable(new BitmapDrawable(getResources(),bitmapImage));
                }
        }
    }
}
