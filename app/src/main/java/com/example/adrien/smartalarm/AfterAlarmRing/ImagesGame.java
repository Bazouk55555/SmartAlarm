package com.example.adrien.smartalarm.AfterAlarmRing;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.adrien.smartalarm.R;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ImagesGame extends AppCompatActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.images_game);
		final TextView scoreView = (TextView) findViewById(R.id.score);
		final TextView commentView = (TextView) findViewById(R.id.comment);
		final RelativeLayout imageGamesLayout = (RelativeLayout) findViewById(R.id.images_game);
		final boolean finalImage = getIntent().getBooleanExtra("final", false);
		imageGamesLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!finalImage) {
					Intent result = new Intent();
					setResult(RESULT_OK, result);
				}
				finish();
			}
		});
		List<Integer> imagesBackgroundList;
		int score = getIntent().getIntExtra("score", 0);
		scoreView.setText("Score: " + String.valueOf(score) + "%");
		if (!finalImage) {
			if (getIntent().getBooleanExtra("hasWon", false)) {
				imagesBackgroundList = Arrays.asList(R.drawable.image_background_won1, R.drawable.image_background_won2,
						R.drawable.image_background_won3, R.drawable.image_background_won4,
						R.drawable.image_background_won5);
				imageGamesLayout.setBackgroundResource(imagesBackgroundList.get(new Random().nextInt(5)));
				commentView.setText(getResources().getString(R.string.good_job));
			} else {
				imagesBackgroundList = Arrays.asList(R.drawable.image_background_lost1,
						R.drawable.image_background_lost2, R.drawable.image_background_lost3,
						R.drawable.image_background_lost4, R.drawable.image_background_lost5);
				imageGamesLayout.setBackgroundResource(imagesBackgroundList.get(new Random().nextInt(5)));
				commentView.setText(getResources().getString(R.string.still_sleepy));
			}
		} else {
			if (score < 35) {
				imageGamesLayout.setBackgroundResource(R.drawable.stay_on_bed);
				commentView.setText(getResources().getString(R.string.go_back_to_bed));
			} else if (score < 65) {
				imageGamesLayout.setBackgroundResource(R.drawable.take_coffee);
				commentView.setText(getResources().getString(R.string.take_a_coffee));
			} else {
				imageGamesLayout.setBackgroundResource(R.drawable.awake);
				commentView.setText(getResources().getString(R.string.ready_to_go_to_the_gym));
			}
		}
		final long beginTime = System.currentTimeMillis();
		Thread flashingTextThread = new Thread(new Runnable() {
			@Override
			public void run() {
				Handler flashingTextHandler = new Handler(Looper.getMainLooper());
				final String comment = commentView.getText().toString();
				while (System.currentTimeMillis() <= beginTime + 3000) {
					flashingTextHandler.postDelayed(new Runnable() {
						@Override
						public void run() {
							commentView.setText("");
						}
					}, 500);
					flashingTextHandler.postDelayed(new Runnable() {
						@Override
						public void run() {
							commentView.setText(comment);
						}
					}, 1000);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		flashingTextThread.start();
		Thread scoreMoveThread = new Thread(new Runnable() {
			@Override
			public void run() {
				Handler titleMoveHandler = new Handler(Looper.getMainLooper());
				while (System.currentTimeMillis() <= beginTime + 3000) {
					titleMoveHandler.post(new Runnable() {
						@Override
						public void run() {
							if (scoreView.getX() < imageGamesLayout.getWidth()) {
								scoreView.setX(scoreView.getX() + 10);
							} else {
								scoreView.setX(-scoreView.getTextSize());
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
		scoreMoveThread.start();
		Handler handlerGoBack = new Handler(getMainLooper());
		handlerGoBack.postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent result = new Intent();
				setResult(RESULT_OK, result);
				finish();
			}
		}, 3000);
	}

	@Override
	public void onBackPressed() {
	}
}
