package com.application.bazouk.smartalarm.mainactivity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.application.bazouk.smartalarm.afterAlarmRing.AlarmRing;
import com.application.bazouk.smartalarm.sqliteService.Alarm;
import com.application.bazouk.smartalarm.R;
import com.application.bazouk.smartalarm.sqliteService.AlarmBaseDAO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SmartAlarm extends AppCompatActivity {

	public static final String IS_GAME_ACTIVATED = "game activated";
	public static final String URI_SOUND = "uri sound";
	public static final String URI_IMAGE = "uri image";
	public static final String IS_ALARM_SIX = "alarm six";
	public static final String IS_PICTURE_PRESENT = "picture present";
	public static final String LEVEL = "level";
	public static final String NUMBER_OF_QUESTIONS = "number of questions";
	public static final String CATEGORY = "category";
	public static final int AUTHORIZATION_IMAGE = 1;
	public static final int AUTHORIZATION_SOUND = 2;

	private AbstractDialogAddOrRemove dialogAdd;
	private AbstractDialogAddOrRemove dialogRemove;
	private List<HashMap<String, String>> listMapOfEachAlarm;
	private SimpleAdapter adapterAlarms;
	private List<Boolean> alarmsActivated;
	private List<Integer> alarmsHours;
	private List<Integer> alarmsMinutes;
	private List<String> alarmsTitle;
	private List<Integer> alarmsSound;
	private AlarmManager alarmManager;
	private DialogAddImage dialogAddImage;
	private MenuItem takeOffImageMenuItem;
	private DialogAddSound dialogAddSound;
	private MenuItem takeOffSoundMenuItem;
	private CategoryDialog categoryDialog = null;
	private LevelDialog levelDialog = null;
	private NumberQuestionsDialog numberQuestionsDialog = null;
	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_smart_alarm);

		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		editor = preferences.edit();
		CheckBox activateGame = (CheckBox) findViewById(R.id.checkbox);
		activateGame.setChecked(preferences.getBoolean(IS_GAME_ACTIVATED, false));
		activateGame.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					editor.putBoolean(IS_GAME_ACTIVATED, true);
					editor.apply();
				} else {
					editor.putBoolean(IS_GAME_ACTIVATED, false);
					editor.apply();
				}
			}
		});

		dialogAdd = new DialogAdd(this, this);
		final ListView listViewAlarms = (ListView) findViewById(R.id.list_alarm);
		listMapOfEachAlarm = new ArrayList<>();

		alarmManager = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);
		alarmsHours = new ArrayList<>();
		alarmsMinutes = new ArrayList<>();
		alarmsTitle = new ArrayList<>();
		alarmsSound = new ArrayList<>();
		alarmsActivated = new ArrayList<>();

		AlarmBaseDAO alarmBaseDAO = new AlarmBaseDAO(this);
		alarmBaseDAO.open();
		List<Alarm> alarmList = alarmBaseDAO.select();
		for (Alarm alarm : alarmList) {
			alarmsHours.add(alarm.getHour());
			alarmsMinutes.add(alarm.getMinute());
			alarmsTitle.add(alarm.getTitle());
			alarmsSound.add(alarm.getSound());
			alarmsActivated.add(alarm.getActivated());
			HashMap<String, String> mapOfTheNewAlarm = new HashMap<>();
			mapOfTheNewAlarm.put("alarm", alarm.getTime());
			mapOfTheNewAlarm.put("title", alarm.getTitle());
			if (alarm.getActivated()) {
				mapOfTheNewAlarm.put("alarm_drawable", Integer.toString(R.drawable.alarm_on));
			} else {
				mapOfTheNewAlarm.put("alarm_drawable", Integer.toString(R.drawable.alarm_off));
			}
			listMapOfEachAlarm.add(mapOfTheNewAlarm);
		}
		alarmBaseDAO.close();

		adapterAlarms = new SimpleAdapterWithBackgroundChanged(this, listMapOfEachAlarm, R.layout.item_alarm,
				new String[]{"alarm", "title", "alarm_drawable"}, new int[]{R.id.time, R.id.title, R.id.activate});
		listViewAlarms.setAdapter(adapterAlarms);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		takeOffImageMenuItem = menu.findItem(R.id.takeof_image);
		takeOffSoundMenuItem = menu.findItem(R.id.takeof_sound);
		if (preferences.getBoolean(IS_ALARM_SIX, false)) {
			takeOffSoundMenuItem.setEnabled(true);
		} else {
			takeOffSoundMenuItem.setEnabled(false);
		}

		if (preferences.getBoolean(IS_PICTURE_PRESENT, false)) {
			takeOffImageMenuItem.setEnabled(true);
		} else {
			takeOffImageMenuItem.setEnabled(false);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.add :
				dialogAdd = new DialogAdd(this, this);
				if (preferences.getBoolean(IS_ALARM_SIX, false)) {
					dialogAdd.getAlarms().add(getResources().getString(R.string.alarm6));
				}
				dialogAdd.show();
				break;
			case R.id.add_sound :
				dialogAddSound = new DialogAddSound(this, this);
				dialogAddSound.show();
				break;
			case R.id.add_image :
				dialogAddImage = new DialogAddImage(this, this);
				dialogAddImage.show();
				break;
			case R.id.takeof_image :
				takeOffImageMenuItem.setEnabled(false);
				setUriImage(null);
				setPicture(false);
				break;
			case R.id.takeof_sound :
				takeOffSoundMenuItem.setEnabled(false);
				setUriSound(null);
				setIsAlarmSix(false);
				boolean isAnAlarmSixExisting = false;
				AlarmBaseDAO alarmBaseDAO = new AlarmBaseDAO(this);
				alarmBaseDAO.open();
				for (int i = 0; i < alarmsSound.size(); i++) {
					if (alarmsSound.get(i) == 5) {
						isAnAlarmSixExisting = true;
						alarmsSound.set(i, 0);
						alarmBaseDAO.updateSoundAlarm(i, 0);
					}
				}
				if (isAnAlarmSixExisting) {
					AlertDialog.Builder builder;
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
						builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
					} else {
						builder = new AlertDialog.Builder(this);
					}
					builder.setTitle(getResources().getString(R.string.alarm_six_took_off))
							.setMessage(getResources().getString(R.string.alarm_six_took_off_message))
							.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
								}
							}).setIcon(android.R.drawable.ic_dialog_alert).show();
				}
				alarmBaseDAO.close();
				break;
			case R.id.all_the_set_up :
				chooseCategoryOfQuestions(true);
				break;
			case R.id.category_of_question :
				chooseCategoryOfQuestions(false);
				break;
			case R.id.level_of_question :
				chooseLevelOfQuestions(false);
				break;
			case R.id.number_of_question :
				chooseNumberOfQuestions();
				break;
			default :
				break;
		}
		return true;
	}

	public void setNewAlarm(int position, String time, String title, int hour, int minute, int soundSelected,
			boolean isActivated) {
		alarmsHours.add(position, hour);
		alarmsMinutes.add(position, minute);
		alarmsTitle.add(position, title);
		alarmsSound.add(position, soundSelected);
		alarmsActivated.add(position, isActivated);

		AlarmBaseDAO alarmBaseDAO = new AlarmBaseDAO(this);
		alarmBaseDAO.open();
		alarmBaseDAO.add(new Alarm(alarmsHours.size(), hour, minute, time, title, soundSelected, isActivated));
		alarmBaseDAO.close();

		HashMap<String, String> mapOfTheNewAlarm = new HashMap<>();
		mapOfTheNewAlarm.put("alarm", time);
		mapOfTheNewAlarm.put("title", title);
		if (alarmsActivated.get(position)) {
			mapOfTheNewAlarm.put("alarm_drawable", Integer.toString(R.drawable.alarm_on));
		} else {
			mapOfTheNewAlarm.put("alarm_drawable", Integer.toString(R.drawable.alarm_off));
		}
		listMapOfEachAlarm.add(position, mapOfTheNewAlarm);
		adapterAlarms.notifyDataSetChanged();

	}

	public void removeAlarm(int position) {
		alarmsHours.remove(position);
		alarmsMinutes.remove(position);
		alarmsTitle.remove(position);
		alarmsSound.remove(position);

		AlarmBaseDAO alarmBaseDAO = new AlarmBaseDAO(this);
		alarmBaseDAO.open();
		alarmBaseDAO.remove(position + 1, alarmsHours.size() - position);
		alarmBaseDAO.close();

		listMapOfEachAlarm.remove(position);
		adapterAlarms.notifyDataSetChanged();

		alarmsActivated.remove(position);
	}

	public List<Integer> getAlarmsMinutes() {
		return alarmsMinutes;
	}

	public List<Integer> getAlarmsHours() {
		return alarmsHours;
	}

	public List<Boolean> getAlarmsActivated() {
		return alarmsActivated;
	}

	public void setAlarmManager(int index, String sound, String title) {
		Intent intentToAlarmRing = new Intent(this, AlarmRing.class);
		intentToAlarmRing.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK );
		String hour = getStringTimeFromAnIndexOfListOfNumber(alarmsHours, index);
		String minute = getStringTimeFromAnIndexOfListOfNumber(alarmsMinutes, index);
		String time = hour + ":" + minute;
		intentToAlarmRing.putExtra("time", time);
		intentToAlarmRing.putExtra("title", title);
		intentToAlarmRing.putExtra("sound", sound);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, Integer.parseInt(hour + minute),
				intentToAlarmRing, PendingIntent.FLAG_CANCEL_CURRENT);
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, alarmsMinutes.get(index));
		cal.set(Calendar.HOUR_OF_DAY, alarmsHours.get(index));
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

	public void setUriImage(Uri uriImage) {
		editor.putString(URI_IMAGE, String.valueOf(uriImage));
		editor.apply();
	}

	public void setUriSound(Uri uriSound) {
		editor.putString(URI_SOUND, String.valueOf(uriSound));
		editor.apply();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == AUTHORIZATION_IMAGE && resultCode == Activity.RESULT_OK) {
			dialogAddImage.setUriDialog(data.getData());
			if (dialogAddImage.getUriDialog() != null) {
				dialogAddImage.setImageOk();
			}
		} else if (requestCode == AUTHORIZATION_SOUND && resultCode == Activity.RESULT_OK) {
			dialogAddSound.setUriDialog(data.getData());
			if (dialogAddSound.getUriDialog() != null) {
				dialogAddSound.setSoundOk();
			}
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String permission[], @NonNull int[] grantResult) {
		switch (requestCode) {
			case AUTHORIZATION_IMAGE :
				if (grantResult.length > 0 && grantResult[0] == PackageManager.PERMISSION_GRANTED) {
					startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI),
							AUTHORIZATION_IMAGE);
				}
				break;
			case AUTHORIZATION_SOUND :
				if (grantResult.length > 0 && grantResult[0] == PackageManager.PERMISSION_GRANTED) {
					startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.INTERNAL_CONTENT_URI),
							AUTHORIZATION_SOUND);
				}
				break;
		}
	}

	public MenuItem getTakeOffImageMenuItem() {
		return takeOffImageMenuItem;
	}

	public MenuItem getTakeOffSoundMenuItem() {
		return takeOffSoundMenuItem;
	}

	public void setIsAlarmSix(boolean isAlarmSix) {
		editor.putBoolean(IS_ALARM_SIX, isAlarmSix);
		editor.apply();
	}

	public void setPicture(boolean isPicturePresent) {
		editor.putBoolean(IS_PICTURE_PRESENT, isPicturePresent);
		editor.apply();
	}

	public AbstractDialogAddOrRemove getDialogAdd() {
		return dialogAdd;
	}

	public void setCategory(String category) {
		numberQuestionsDialog = new NumberQuestionsDialog(this, this);
		editor.putString(CATEGORY, category);
		editor.apply();
	}

	public void setLevel(String level) {
		numberQuestionsDialog = new NumberQuestionsDialog(this, this);
		editor.putString(LEVEL, level);
		editor.apply();
	}

	public void setNumberOfQuestions(int numberOfQuestions) {
		editor.putInt(NUMBER_OF_QUESTIONS, numberOfQuestions);
		editor.apply();
	}

	public void cancelAnAlarmManager(int index) {
		Intent intentToAlarmRing = new Intent(SmartAlarm.this, AlarmRing.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(SmartAlarm.this,
				Integer.parseInt(getStringTimeFromAnIndexOfListOfNumber(alarmsHours, index)
						+ getStringTimeFromAnIndexOfListOfNumber(alarmsMinutes, index)),
				intentToAlarmRing, PendingIntent.FLAG_CANCEL_CURRENT);
		alarmManager.cancel(pendingIntent);
	}

	private String getStringTimeFromAnIndexOfListOfNumber(List<Integer> numbers, int index) {
		return (numbers.get(index) >= 0 && numbers.get(index) < 10)
				? "0" + numbers.get(index)
				: "" + numbers.get(index);
	}

	private class SimpleAdapterWithBackgroundChanged extends SimpleAdapter {
		private SimpleAdapterWithBackgroundChanged(Context context, List<? extends Map<String, ?>> data, int resource,
				String[] from, int[] to) {
			super(context, data, resource, from, to);
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final View convertViewToReturn = super.getView(position, convertView, parent);
			if (!alarmsActivated.isEmpty()) {
				if (!alarmsActivated.get(position)) {
					convertViewToReturn.setBackgroundColor(ContextCompat.getColor(SmartAlarm.this, R.color.dark));
				} else {
					convertViewToReturn.setBackgroundColor(ContextCompat.getColor(SmartAlarm.this, R.color.bright));
				}
			}
			for (int i = 0; i < 2; i++) {
				((ViewGroup) convertViewToReturn).getChildAt(i).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialogRemove = new DialogRemove(SmartAlarm.this, SmartAlarm.this, position,
								alarmsHours.get(position).toString(), alarmsMinutes.get(position).toString(),
								alarmsTitle.get(position), alarmsSound.get(position));
						if (preferences.getBoolean(IS_ALARM_SIX, false)) {
							dialogRemove.getAlarms().add(getResources().getString(R.string.alarm6));
						}
						dialogRemove.show();
					}
				});
			}

			convertViewToReturn.findViewById(R.id.activate).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					AlarmBaseDAO alarmBaseDAO = new AlarmBaseDAO(SmartAlarm.this);
					alarmBaseDAO.open();
					if (alarmsActivated.get(position)) {
						((ImageView) v).setImageResource(R.drawable.alarm_off);
						convertViewToReturn.setBackgroundColor(ContextCompat.getColor(SmartAlarm.this, R.color.dark));
						alarmsActivated.set(position, false);
						cancelAnAlarmManager(position);
						alarmBaseDAO.updateActivation(alarmsHours.get(position), alarmsMinutes.get(position), false);
					} else {
						((ImageView) v).setImageResource(R.drawable.alarm_on);
						convertViewToReturn.setBackgroundColor(ContextCompat.getColor(SmartAlarm.this, R.color.bright));
						alarmsActivated.set(position, true);
						setAlarmManager(position, chooseAlarmSoundFromNumber(alarmsSound.get(position)), alarmsTitle.get(position));
						alarmBaseDAO.updateActivation(alarmsHours.get(position), alarmsMinutes.get(position), true);
					}
					alarmBaseDAO.close();
				}
			});
			return convertViewToReturn;
		}
	}

	public int getPositionNewAlarm(int hour, int min) {
		int i = 0;
		int numberOfAlarm = alarmsHours.size();
		while (i < numberOfAlarm && hour > alarmsHours.get(i)) {
			i++;
		}
		if (i < alarmsHours.size() && hour == alarmsHours.get(i)) {
			while (i < numberOfAlarm && hour == alarmsHours.get(i) && min > alarmsMinutes.get(i)) {
				i++;
			}
		}
		return i;
	}

	private void chooseCategoryOfQuestions(boolean isSerieOfDialogs) {
		if (categoryDialog == null) {
			categoryDialog = new CategoryDialog(this, this, isSerieOfDialogs);
		}
		else
		{
			categoryDialog.setSerieOfDialogs(isSerieOfDialogs);
		}
		categoryDialog.show();
	}
	public void chooseNumberOfQuestions() {
		if (numberQuestionsDialog == null) {
			numberQuestionsDialog = new NumberQuestionsDialog(this, this);
		}
		numberQuestionsDialog.show();
	}
	public void chooseLevelOfQuestions(boolean isSerieOfDialogs) {
		if (levelDialog == null) {
			levelDialog = new LevelDialog(this, this, isSerieOfDialogs);
		}
		else
		{
			levelDialog.setSerieOfDialogs(isSerieOfDialogs);
		}
		levelDialog.show();
	}

	private String chooseAlarmSoundFromNumber(int number)
	{
		switch(number)
		{
			case 0:
				return getResources().getString(R.string.alarm1);
			case 1:
				return getResources().getString(R.string.alarm2);
			case 2:
				return getResources().getString(R.string.alarm3);
			case 3:
				return getResources().getString(R.string.alarm4);
			case 4:
				return getResources().getString(R.string.alarm5);
			case 5:
				return getResources().getString(R.string.alarm6);
			default:
				return getResources().getString(R.string.alarm1);
		}
	}
}