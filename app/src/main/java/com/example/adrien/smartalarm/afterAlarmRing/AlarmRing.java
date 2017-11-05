package com.example.adrien.smartalarm.afterAlarmRing;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.adrien.smartalarm.R;
import com.example.adrien.smartalarm.sqliteService.AbstractQuestionBaseDAO;
import com.example.adrien.smartalarm.sqliteService.CinemaDAO;
import com.example.adrien.smartalarm.sqliteService.GeographyDAO;
import com.example.adrien.smartalarm.sqliteService.HistoryDAO;
import com.example.adrien.smartalarm.sqliteService.MusicDAO;
import com.example.adrien.smartalarm.sqliteService.Question;
import com.example.adrien.smartalarm.sqliteService.SportsDAO;
import com.example.adrien.smartalarm.smartalarm.SmartAlarm;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class AlarmRing extends AppCompatActivity {
	private TextView titleView = null;
	private MediaPlayer mediaPlayer;
	private boolean isAlarmStopped;
	private int numberOfQuestions;
	private DialogNewGame dialogNewGame;
	private Uri uriImage;

	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

		setContentView(R.layout.alarm_ring);
		TextView timeView = (TextView) findViewById(R.id.time);
		titleView = (TextView) findViewById(R.id.title);
		timeView.setText(getIntent().getStringExtra("time"));
		titleView.setText(getIntent().getStringExtra("title"));

		isAlarmStopped = false;
		multiColorViewFlashing(timeView);

		if (!titleView.getText().toString().isEmpty()) {
			Thread titleMoveThread = new Thread(new Runnable() {
				@Override
				public void run() {
					Handler titleMoveHandler = new Handler(Looper.getMainLooper());
                    final LinearLayout secondLayout = (LinearLayout)findViewById(R.id.second_layout);
					while (!isAlarmStopped) {
                        if(secondLayout.getWidth()!=0) {
                            titleMoveHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (titleView.getX() < secondLayout.getWidth()) {
                                        titleView.setX(titleView.getX() + 10);
                                    } else {
                                        titleView.setX(-titleView.getMeasuredWidth());
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
				}
			});
			titleMoveThread.start();

			multiColorViewFlashing(findViewById(R.id.background_title));
		}

		String sound = (getIntent().getStringExtra("sound"));
		if(sound.equals(getResources().getString(R.string.alarm1))) {
			mediaPlayer = MediaPlayer.create(this, R.raw.alarm1);
		}
		else if(sound.equals(getResources().getString(R.string.alarm2))) {
			mediaPlayer = MediaPlayer.create(this, R.raw.alarm2);
		}
		else if(sound.equals(getResources().getString(R.string.alarm3))) {
			mediaPlayer = MediaPlayer.create(this, R.raw.alarm3);
		}
		else if(sound.equals(getResources().getString(R.string.alarm4))) {
			mediaPlayer = MediaPlayer.create(this, R.raw.alarm4);
		}
		else if(sound.equals(getResources().getString(R.string.alarm5))) {
			mediaPlayer = MediaPlayer.create(this, R.raw.alarm5);
		}
		else if(sound.equals(getResources().getString(R.string.alarm6))) {
			String uriSoundString = PreferenceManager.getDefaultSharedPreferences(this).getString(SmartAlarm.URI_SOUND, null);
			mediaPlayer = (uriSoundString != null) ? MediaPlayer.create(this, Uri.parse(uriSoundString)) : null;
		}

        if (mediaPlayer != null) {
			mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
        ImageView stopAlarm = (ImageView) findViewById(R.id.stop_alarm);
		if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean(SmartAlarm.IS_GAME_ACTIVATED,false)) {
			stopAlarm.setImageResource(R.drawable.ic_stop_alarm);
		}
		stopAlarm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (PreferenceManager.getDefaultSharedPreferences(AlarmRing.this).getBoolean(SmartAlarm.IS_GAME_ACTIVATED,false)) {
					AbstractQuestionBaseDAO categoryDAO = chooseCategory();
					numberOfQuestions = PreferenceManager.getDefaultSharedPreferences(AlarmRing.this).getInt(SmartAlarm.NUMBER_OF_QUESTIONS,1);
					categoryDAO.open();
					List<Question> questions = categoryDAO.select(numberOfQuestions,PreferenceManager.getDefaultSharedPreferences(AlarmRing.this).getString(SmartAlarm.LEVEL,getResources().getString(R.string.easy)));
					categoryDAO.close();
					Collections.shuffle(questions);
					dialogNewGame = new DialogNewGame(AlarmRing.this, questions, mediaPlayer, AlarmRing.this);
					dialogNewGame.show();
				} else {
					mediaPlayer.stop();
					finish();
				}
				isAlarmStopped = true;
			}
		});

		String uriImageString = PreferenceManager.getDefaultSharedPreferences(this).getString(SmartAlarm.URI_IMAGE,null);
		uriImage = (uriImageString!=null)?Uri.parse(uriImageString):null;
		if (uriImage != null) {
			ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},2);
		}
	}

	private AbstractQuestionBaseDAO chooseCategory() {
		List<AbstractQuestionBaseDAO> abstractBaseDAOList = Arrays.asList(new CinemaDAO(this), new GeographyDAO(this),
				new HistoryDAO(this), new MusicDAO(this), new SportsDAO(this));
		String category = PreferenceManager.getDefaultSharedPreferences(this).getString(SmartAlarm.CATEGORY,"");
		if(category.equals(getResources().getString(R.string.category_cinema))) {
			return abstractBaseDAOList.get(0);
		}
		else if(category.equals(getResources().getString(R.string.category_geography))) {
			return abstractBaseDAOList.get(1);
		}
		else if(category.equals(getResources().getString(R.string.category_history))) {
			return abstractBaseDAOList.get(2);
		}
		else if(category.equals(getResources().getString(R.string.category_music))) {
			return abstractBaseDAOList.get(3);
		}
		else if(category.equals(getResources().getString(R.string.category_sports))) {
			return abstractBaseDAOList.get(4);
		}
		else {
			return abstractBaseDAOList.get(new Random().nextInt(5));
		}
	}

	private void multiColorViewFlashing(final View v) {
		Thread viewMultiColorThread = new Thread(new Runnable() {
			@Override
			public void run() {
				Handler multiColorHandler = new Handler(Looper.getMainLooper());
				while (!isAlarmStopped) {
					if (v instanceof LinearLayout) {
						multiColorHandler.postDelayed(new Runnable() {
							@Override
							public void run() {
								v.setBackgroundColor(ContextCompat.getColor(AlarmRing.this, R.color.red));
							}
						}, 200);
						multiColorHandler.postDelayed(new Runnable() {
							@Override
							public void run() {
								v.setBackgroundColor(ContextCompat.getColor(AlarmRing.this, R.color.blue));
							}
						}, 400);
						multiColorHandler.postDelayed(new Runnable() {
							@Override
							public void run() {
								v.setBackgroundColor(ContextCompat.getColor(AlarmRing.this, R.color.orange));
							}
						}, 600);
						multiColorHandler.postDelayed(new Runnable() {
							@Override
							public void run() {
								v.setBackgroundColor(ContextCompat.getColor(AlarmRing.this, R.color.black));
							}
						}, 800);
						try {
							Thread.sleep(800);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					} else if (v instanceof TextView) {
						multiColorHandler.postDelayed(new Runnable() {
							@Override
							public void run() {
								((TextView) v).setTextColor(ContextCompat.getColor(AlarmRing.this, R.color.red));
							}
						}, 200);
						multiColorHandler.postDelayed(new Runnable() {
							@Override
							public void run() {
								((TextView) v).setTextColor(ContextCompat.getColor(AlarmRing.this, R.color.blue));
							}
						}, 400);
						multiColorHandler.postDelayed(new Runnable() {
							@Override
							public void run() {
								((TextView) v).setTextColor(ContextCompat.getColor(AlarmRing.this, R.color.orange));
							}
						}, 600);
						multiColorHandler.postDelayed(new Runnable() {
							@Override
							public void run() {
								((TextView) v).setTextColor(ContextCompat.getColor(AlarmRing.this, R.color.black));
							}
						}, 800);
						try {
							Thread.sleep(800);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
		viewMultiColorThread.start();
	}

	public int getNumberOfQuestions() {
		return numberOfQuestions;
	}


	 @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
	 @Override public void onRequestPermissionsResult(int requestCode, @NonNull String permission [], @NonNull int[] grantResult) {
		 switch(requestCode) {
			 case 2:
			 if(grantResult.length>0 && grantResult[0]==PackageManager.PERMISSION_GRANTED)
			 {
				 InputStream inputStream = null;
				 try {
					 inputStream = getContentResolver().openInputStream(uriImage);
				 } catch (FileNotFoundException e) {
					 e.printStackTrace();
				 }
				 BitmapFactory.Options option = new BitmapFactory.Options();
				 Bitmap bitmapImage = BitmapFactory.decodeStream(inputStream, null, option);
				 findViewById(R.id.main_layout).setBackground(new BitmapDrawable(getResources(), bitmapImage));
			 }
		 }
	 }

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == DialogNewGame.CODE_DIALOG_BACK) {
			if (resultCode == RESULT_OK) {
				dialogNewGame.show();
			}
		}
	}

	@Override
	public void onBackPressed() {
		mediaPlayer.stop();
		super.onBackPressed();
	}
}
