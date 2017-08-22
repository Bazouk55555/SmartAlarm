package com.example.adrien.smartalarm.AfterAlarmRing;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.example.adrien.smartalarm.R;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ImagesGame extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.images_game);
        List<Integer> imagesBackgroundList;
        if(getIntent().getBooleanExtra("hasWon",false)) {
            imagesBackgroundList = Arrays.asList(R.drawable.image_background_won1,R.drawable.image_background_won2,R.drawable.image_background_won3,R.drawable.image_background_won4,R.drawable.image_background_won5);
            findViewById(R.id.images_game).setBackgroundResource(imagesBackgroundList.get(new Random().nextInt(5)));
        }
        else
        {
            imagesBackgroundList = Arrays.asList(R.drawable.image_background_lost1,R.drawable.image_background_lost2,R.drawable.image_background_lost3,R.drawable.image_background_lost4,R.drawable.image_background_lost5);
            findViewById(R.id.images_game).setBackgroundResource(imagesBackgroundList.get(new Random().nextInt(5)));
        }
        Handler handlerGoBack = new Handler(getMainLooper());
        handlerGoBack.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent result = new Intent();
                setResult(RESULT_OK, result);
                finish();
            }
        },3000);
    }

    @Override
    public void onBackPressed()
    {
    }
}
