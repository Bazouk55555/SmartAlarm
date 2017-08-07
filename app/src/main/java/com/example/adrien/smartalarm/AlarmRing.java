package com.example.adrien.smartalarm;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class AlarmRing extends AppCompatActivity {
    private TextView timeView=null;
    private TextView titleView=null;
    private Button stopAlarm=null;
    private RelativeLayout mainLayout=null;
    private Uri uriImage;
    private MediaPlayer mediaPlayer;
    private PowerManager.WakeLock wl;
    Thread multiColorThread;
    boolean isAlarmStopped;
    Thread titleMoveThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Tag");
        wl.acquire();
        this.getWindow().setFlags(
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        setContentView(R.layout.alarm_ring);
        timeView = (TextView) findViewById(R.id.time);
        titleView = (TextView) findViewById(R.id.title);

        isAlarmStopped=false;
        multiColorThread = new Thread(new Runnable(){
            @Override
            public void run() {
                Handler multiColorHandler = new Handler(Looper.getMainLooper());
                while(!isAlarmStopped)
                {
                    multiColorHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            timeView.setTextColor(getResources().getColor(R.color.red));
                        }
                    },200);
                    multiColorHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            timeView.setTextColor(getResources().getColor(R.color.blue));
                        }
                    },400);
                    multiColorHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            timeView.setTextColor(getResources().getColor(R.color.orange));
                        }
                    },600);
                    multiColorHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            timeView.setTextColor(getResources().getColor(R.color.black));
                        }
                    },800);
                    try {
                        Thread.sleep(800);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        multiColorThread.start();

        titleMoveThread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("STEP 1");
                Handler titleMoveHandler = new Handler(Looper.getMainLooper());
                while(!isAlarmStopped)
                {
                    titleMoveHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(titleView.getX()<findViewById(R.id.second_layout).getWidth()) {
                                titleView.setX(titleView.getX()+10);
                            }
                            else
                            {
                                titleView.setX(-titleView.getTextSize());
                            }
                        }
                    });
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        titleMoveThread.start();

        timeView.setText(getIntent().getStringExtra("time"));
        titleView.setText(getIntent().getStringExtra("title"));
        switch(getIntent().getStringExtra("sound")) {
            case "alarm1":
                mediaPlayer = MediaPlayer.create(this, R.raw.alarm1);
                break;
            case "alarm2":
                mediaPlayer = MediaPlayer.create(this, R.raw.alarm2);
                break;
            case "alarm3":
                mediaPlayer = MediaPlayer.create(this, R.raw.alarm3);
                break;
            case "alarm4":
                mediaPlayer = MediaPlayer.create(this, R.raw.alarm4);
                break;
            case "alarm5":
                mediaPlayer = MediaPlayer.create(this, R.raw.alarm5);
                break;
            case "alarm6":
                Uri uriSound = getIntent().getParcelableExtra("uri_sound");
                mediaPlayer = mediaPlayer.create(this, uriSound);
                break;
        }

        mediaPlayer.start();
        stopAlarm = (Button)findViewById(R.id.stop_alarm);
        stopAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                isAlarmStopped = true;
            }
        });

        mainLayout = (RelativeLayout) findViewById(R.id.main_layout);

        uriImage=getIntent().getParcelableExtra("uri_image");
        if(uriImage!=null)
        {
            //ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},2);
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

    /*@Override
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
    }*/

    @Override
    protected void onStop() {
        super.onStop();
        if(wl.isHeld()) {
            wl.release();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mediaPlayer.stop();
    }

}
