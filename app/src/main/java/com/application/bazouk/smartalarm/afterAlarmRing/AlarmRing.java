package com.application.bazouk.smartalarm.afterAlarmRing;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.application.bazouk.smartalarm.sqliteService.AbstractQuestionBaseDAO;
import com.application.bazouk.smartalarm.sqliteService.CinemaDAO;
import com.application.bazouk.smartalarm.sqliteService.GeographyDAO;
import com.application.bazouk.smartalarm.sqliteService.MusicDAO;
import com.application.bazouk.smartalarm.sqliteService.Question;
import com.application.bazouk.smartalarm.sqliteService.SportsDAO;
import com.application.bazouk.smartalarm.R;
import com.application.bazouk.smartalarm.sqliteService.HistoryDAO;
import com.application.bazouk.smartalarm.mainactivity.SmartAlarm;

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
	private AudioManager audioManager;
	private int currentAudioMode;
	private boolean isSpeakerPhoneOn;
	private boolean isGameActivated;

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
					final LinearLayout secondLayout = (LinearLayout) findViewById(R.id.second_layout);
					while (!isAlarmStopped) {
						if (secondLayout.getWidth() != 0) {
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
		if (sound.equals(getResources().getString(R.string.alarm1))) {
			mediaPlayer = MediaPlayer.create(this, R.raw.alarm1);
		} else if (sound.equals(getResources().getString(R.string.alarm2))) {
			mediaPlayer = MediaPlayer.create(this, R.raw.alarm2);
		} else if (sound.equals(getResources().getString(R.string.alarm3))) {
			mediaPlayer = MediaPlayer.create(this, R.raw.alarm3);
		} else if (sound.equals(getResources().getString(R.string.alarm4))) {
			mediaPlayer = MediaPlayer.create(this, R.raw.alarm4);
		} else if (sound.equals(getResources().getString(R.string.alarm5))) {
			mediaPlayer = MediaPlayer.create(this, R.raw.alarm5);
		} else if (sound.equals(getResources().getString(R.string.alarm6))) {
			if (PreferenceManager.getDefaultSharedPreferences(AlarmRing.this).getBoolean(SmartAlarm.IS_ALARM_SIX,
					false)) {
				String uriSoundString = PreferenceManager.getDefaultSharedPreferences(this)
						.getString(SmartAlarm.URI_SOUND, null);
				mediaPlayer = (uriSoundString != null) ? MediaPlayer.create(this, Uri.parse(uriSoundString)) : null;
			} else {
				mediaPlayer = MediaPlayer.create(this, R.raw.alarm1);
			}
		}

		if (mediaPlayer != null) {
			audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
			currentAudioMode = audioManager.getMode();
			isSpeakerPhoneOn = audioManager.isSpeakerphoneOn();
			audioManager.setMode(AudioManager.MODE_IN_CALL);
			audioManager.setSpeakerphoneOn(true);
			mediaPlayer.setLooping(true);
			mediaPlayer.start();
		}
		ImageView stopAlarm = (ImageView) findViewById(R.id.stop_alarm);
		if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean(SmartAlarm.IS_GAME_ACTIVATED, false)) {
			stopAlarm.setImageResource(R.drawable.ic_stop_alarm);
		}
		isGameActivated = PreferenceManager.getDefaultSharedPreferences(AlarmRing.this)
				.getBoolean(SmartAlarm.IS_GAME_ACTIVATED, false);

		stopAlarm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isGameActivated) {
					AbstractQuestionBaseDAO categoryDAO = chooseCategory();
					numberOfQuestions = PreferenceManager.getDefaultSharedPreferences(AlarmRing.this)
							.getInt(SmartAlarm.NUMBER_OF_QUESTIONS, 1);
					categoryDAO.open();
					List<Question> questions = categoryDAO.select(numberOfQuestions,
							PreferenceManager.getDefaultSharedPreferences(AlarmRing.this).getString(SmartAlarm.LEVEL,
									getResources().getString(R.string.easy)));
					categoryDAO.close();
					Collections.shuffle(questions);
					dialogNewGame = new DialogNewGame(AlarmRing.this, questions, mediaPlayer, AlarmRing.this);
					dialogNewGame.show();
				} else {
					if (mediaPlayer != null) {
						mediaPlayer.stop();
					}
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
						finishAndRemoveTask();
					} else {
						finish();
					}
				}
				isAlarmStopped = true;
			}
		});

		String uriImageString = PreferenceManager.getDefaultSharedPreferences(this).getString(SmartAlarm.URI_IMAGE,
				null);
		Uri uriImage = (uriImageString != null) ? Uri.parse(uriImageString) : null;
		if (uriImage != null) {
			InputStream inputStream = null;
			try {
				inputStream = getContentResolver().openInputStream(uriImage);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			BitmapFactory.Options option = new BitmapFactory.Options();
			Bitmap bitmapImage = BitmapFactory.decodeStream(inputStream, null, option);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				findViewById(R.id.main_layout).setBackground(new BitmapDrawable(getResources(), bitmapImage));
			} else {
				findViewById(R.id.main_layout).setBackgroundDrawable(new BitmapDrawable(getResources(), bitmapImage));
			}
		}
	}

	private AbstractQuestionBaseDAO chooseCategory() {
		List<AbstractQuestionBaseDAO> abstractBaseDAOList = Arrays.asList(new CinemaDAO(this), new GeographyDAO(this),
				new HistoryDAO(this), new MusicDAO(this), new SportsDAO(this));
		String category = PreferenceManager.getDefaultSharedPreferences(this).getString(SmartAlarm.CATEGORY, "");
		if (category.equals(getResources().getString(R.string.category_cinema))) {
			return abstractBaseDAOList.get(0);
		} else if (category.equals(getResources().getString(R.string.category_geography))) {
			return abstractBaseDAOList.get(1);
		} else if (category.equals(getResources().getString(R.string.category_history))) {
			return abstractBaseDAOList.get(2);
		} else if (category.equals(getResources().getString(R.string.category_music))) {
			return abstractBaseDAOList.get(3);
		} else if (category.equals(getResources().getString(R.string.category_sports))) {
			return abstractBaseDAOList.get(4);
		} else {
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
		if (!isGameActivated) {
			super.onBackPressed();
		}
	}

	@Override
	public void finish() {
		mediaPlayer.stop();
		audioManager.setMode(currentAudioMode);
		audioManager.setSpeakerphoneOn(isSpeakerPhoneOn);
		super.finish();
	}
}
