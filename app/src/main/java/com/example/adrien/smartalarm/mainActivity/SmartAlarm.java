package com.example.adrien.smartalarm.mainActivity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.adrien.smartalarm.AfterAlarmRing.AlarmRing;
import com.example.adrien.smartalarm.R;
import com.example.adrien.smartalarm.SQliteService.Alarm;
import com.example.adrien.smartalarm.SQliteService.AlarmBaseDAO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SmartAlarm extends AppCompatActivity {

	private DialogAdd dialogAdd;
	private DialogRemove dialogRemove;
	private List<HashMap<String, String>> listMapOfEachAlarm;
	private List<HashMap<String, Integer>> listMapOfActivates;
	private SimpleAdapter adapterAlarms;
	private SimpleAdapter adapterActivates;
	private List<Boolean> alarmsActivated;
	private List<Integer> alarmsHours;
	private List<Integer> alarmsMinutes;
	private List<String> alarmsTitle;
	private List<Integer> alarmsSound;
	private AlarmManager alarmManager;
	private Uri uriImage;
	private DialogAddImage dialogAddImage;
	private MenuItem takeOffImageMenuItem;
	private Uri uriSound;
	private DialogAddSound dialogAddSound;
	private MenuItem takeOffSoundMenuItem;
	private boolean isAlarmSix = false;
	private CheckBox activateGame = null;
	//private IsGameActivated isGameActivated;
	private static boolean isActivated;
	private String category = "Random Category";
	private CategoryDialog categoryDialog = null;
	private String level = "Easy";
	private LevelDialog levelDialog = null;
	private int numberOfQuestions = 1;
	private NumberQuestionsDialog numberQuestionsDialog = null;

	@RequiresApi(api = Build.VERSION_CODES.KITKAT)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_smart_alarm);

		activateGame = (CheckBox) findViewById(R.id.checkbox);
		//isGameActivated = new IsGameActivated(false);
		activateGame.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked==true)
				{
					isActivated=true;
					//isGameActivated.setIsGameActivated(true);
					//b = Boolean.TRUE;
					//System.out.println("Value of b after setting to TRUE: "+b);
				}
				else
				{
					isActivated=false;
					//isGameActivated.setIsGameActivated(false);
					//b= Boolean.FALSE;
					//System.out.println("Value of b after setting to FALSE: "+b);
				}
			}
		});

		dialogAdd = new DialogAdd(this, this);
		final ListView listViewAlarms = (ListView) findViewById(R.id.list_alarm);
		final ListView listViewActivates = (ListView) findViewById(R.id.list_activate);
		listMapOfEachAlarm = new ArrayList<>();
		listMapOfActivates = new ArrayList<>();

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
			listMapOfEachAlarm.add(mapOfTheNewAlarm);
			HashMap<String, Integer> mapOfTheAlarmDrawable = new HashMap<>();
            if (alarm.getActivated()) {
                mapOfTheAlarmDrawable.put("alarm_drawable", R.drawable.alarm_on);
            } else {
                mapOfTheAlarmDrawable.put("alarm_drawable", R.drawable.alarm_off);
            }
			listMapOfActivates.add(mapOfTheAlarmDrawable);
            cancelAnAlarmManager((int)alarm.getId()-1);
		}
		alarmBaseDAO.close();

        adapterAlarms = new SimpleAdapterWithBackgroundChanged(this, listMapOfEachAlarm, R.layout.item_alarm,
                new String[]{"alarm", "title"}, new int[]{R.id.time, R.id.title});
        adapterActivates = new SimpleAdapterWithBackgroundChanged(this, listMapOfActivates, R.layout.item_activate,
                new String[]{"alarm_drawable"}, new int[]{R.id.activate});
        listViewAlarms.setAdapter(adapterAlarms);
        listViewActivates.setAdapter(adapterActivates);
        listViewAlarms.requestLayout();
        listViewActivates.requestLayout();

		listViewActivates.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageView activatedImageView = (ImageView) view.findViewById(R.id.activate);
                View alarmView = listViewAlarms.getChildAt(position);
                AlarmBaseDAO alarmBaseDAO = new AlarmBaseDAO(SmartAlarm.this);
                alarmBaseDAO.open();
                if (alarmsActivated.get(position)) {
                    activatedImageView.setImageResource(R.drawable.alarm_off);
                    activatedImageView.setBackgroundColor(getResources().getColor(R.color.dark));
                    alarmView.setBackgroundColor(getResources().getColor(R.color.dark));
                    alarmsActivated.set(position, false);
                    cancelAnAlarmManager(position);
                    alarmBaseDAO.updateActivation(position, false);
                } else {
                    activatedImageView.setImageResource(R.drawable.alarm_on);
                    activatedImageView.setBackgroundColor(getResources().getColor(R.color.bright));
                    alarmView.setBackgroundColor(getResources().getColor(R.color.bright));
                    alarmsActivated.set(position, true);
                    setAlarmManager(position, "alarm" + (alarmsSound.get(position) + 1), alarmsTitle.get(position));
                    alarmBaseDAO.updateActivation(position, true);
                }
                alarmBaseDAO.close();
			}
		});

		listViewAlarms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, final int position, long id) {
				dialogRemove = new DialogRemove(SmartAlarm.this, SmartAlarm.this, position,
						alarmsHours.get(position).toString(), alarmsMinutes.get(position).toString(),
						alarmsTitle.get(position), alarmsSound.get(position));
				if (isAlarmSix) {
					dialogRemove.getAlarms().add("alarm6");
				}
				dialogRemove.show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		takeOffImageMenuItem = menu.findItem(R.id.takeof_image);
		takeOffSoundMenuItem = menu.findItem(R.id.takeof_sound);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.add :
				dialogAdd = new DialogAdd(this, this);
				if (isAlarmSix) {
					dialogAdd.getAlarms().add("alarm6");
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
				uriImage = null;
				takeOffImageMenuItem.setEnabled(false);
				break;
			case R.id.takeof_sound :
				uriSound = null;
				isAlarmSix = false;
				takeOffSoundMenuItem.setEnabled(false);
				break;
			case R.id.category_of_question :
				if (categoryDialog == null) {
					categoryDialog = new CategoryDialog(this, this);
				}
				categoryDialog.show();
				break;
			case R.id.level_of_question :
				if (levelDialog == null) {
					levelDialog = new LevelDialog(this, this);
				}
				levelDialog.show();
				break;
			case R.id.number_of_question :
				if (numberQuestionsDialog == null) {
					System.out.println("JE SUIS ICI MAIS JE SAIS PAS PQ!!!!!");
					numberQuestionsDialog = new NumberQuestionsDialog(this, this);
				}
				numberQuestionsDialog.show();
				break;
			default :
				break;
		}
		return true;
	}

	public void setNewAlarm(String time, String title, int hour, int minute, int soundSelected) {
		alarmsHours.add(hour);
		alarmsMinutes.add(minute);
		alarmsTitle.add(title);
		alarmsSound.add(soundSelected);
		alarmsActivated.add(true);

		AlarmBaseDAO alarmBaseDAO = new AlarmBaseDAO(this);
		alarmBaseDAO.open();
		alarmBaseDAO.add(new Alarm(alarmsHours.size(), hour, minute, time, title, soundSelected, true));

		HashMap<String, String> mapOfTheNewAlarm = new HashMap<>();
		mapOfTheNewAlarm.put("alarm", time);
		mapOfTheNewAlarm.put("title", title);
		listMapOfEachAlarm.add(mapOfTheNewAlarm);
		adapterAlarms.notifyDataSetChanged();

		HashMap<String, Integer> mapOfTheAlarmDrawable = new HashMap<>();
		mapOfTheAlarmDrawable.put("alarm_drawable", R.drawable.alarm_on);
		listMapOfActivates.add(mapOfTheAlarmDrawable);
		adapterActivates.notifyDataSetChanged();
	}

	public void changeAlarm(String time, String title, int position, int hour, int minute, int soundSelected) {
		alarmsHours.set(position, hour);
		alarmsMinutes.set(position, minute);
		alarmsTitle.set(position, title);
		alarmsSound.set(position, soundSelected);

		AlarmBaseDAO alarmBaseDAO = new AlarmBaseDAO(this);
		alarmBaseDAO.open();
		alarmBaseDAO.update(new Alarm(position + 1, hour, minute, time, title, soundSelected, true));

		HashMap<String, String> mapOfTheNewAlarm = new HashMap<>();
		mapOfTheNewAlarm.put("alarm", time);
		mapOfTheNewAlarm.put("title", title);
		listMapOfEachAlarm.set(position, mapOfTheNewAlarm);
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

		listMapOfEachAlarm.remove(position);
		adapterAlarms.notifyDataSetChanged();

		alarmsActivated.remove(position);
		listMapOfActivates.remove(position);
		adapterActivates.notifyDataSetChanged();
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

	@RequiresApi(api = Build.VERSION_CODES.KITKAT)
	public void setAlarmManager(int index, String sound, String title) {
		Intent intentToAlarmRing = new Intent(this, AlarmRing.class);
		String hour = (alarmsHours.get(index) >= 0 && alarmsHours.get(index) < 10)
				? "0" + alarmsHours.get(index)
				: "" + alarmsHours.get(index);
		String minute = (alarmsMinutes.get(index) >= 0 && alarmsMinutes.get(index) < 10)
				? "0" + alarmsMinutes.get(index)
				: "" + alarmsMinutes.get(index);
		String time = hour + ":" + minute;
		intentToAlarmRing.putExtra("time", time);
		intentToAlarmRing.putExtra("title", title);
		intentToAlarmRing.putExtra("uri_image", uriImage);
		intentToAlarmRing.putExtra("uri_sound", uriSound);
		intentToAlarmRing.putExtra("sound", sound);
		//intentToAlarmRing.setExtrasClassLoader(IsGameActivated.class.getClassLoader());
		//System.out.println("Value of isgame before putting in intent: "+isGameActivated.getIsGameActivated());
		//intentToAlarmRing.putExtra("activate_game", isGameActivated);
		intentToAlarmRing.putExtra("category", category);
		intentToAlarmRing.putExtra("level", level);
		intentToAlarmRing.putExtra("number_of_questions", numberOfQuestions);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, index, intentToAlarmRing,
				PendingIntent.FLAG_CANCEL_CURRENT);
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, alarmsMinutes.get(index));
		cal.set(Calendar.HOUR_OF_DAY, alarmsHours.get(index));
		if (cal.getTime().before(Calendar.getInstance().getTime())) {
			cal.add(Calendar.DAY_OF_YEAR, 1);
		}

		alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
	}

	public void setUriImage(Uri uriImage) {
		this.uriImage = uriImage;
	}

	public Uri getUriImage() {
		return uriImage;
	}

	public void setUriSound(Uri uriSound) {
		this.uriSound = uriSound;
	}

	public Uri getUriSound() {
		return uriSound;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == DialogAddImage.AUTHORIZATION_IMAGE && resultCode == Activity.RESULT_OK) {
			dialogAddImage.setUriDialog(data.getData());
			if (dialogAddImage.getUriDialog() != null) {
				dialogAddImage.setImageOk();
			}
		} else if (requestCode == DialogAddSound.AUTHORIZATION_SOUND && resultCode == Activity.RESULT_OK) {
			dialogAddSound.setUriDialog(data.getData());
			if (dialogAddSound.getUriDialog() != null) {
				dialogAddSound.setSoundOk();
			}
		}
	}

	public MenuItem getTakeOffImageMenuItem() {
		return takeOffImageMenuItem;
	}

	public MenuItem getTakeOffSoundMenuItem() {
		return takeOffSoundMenuItem;
	}

	public void setIsAlarmSix(boolean isAlarmSix) {
		this.isAlarmSix = isAlarmSix;
	}

	public boolean getIsAlarmSix() {
		return isAlarmSix;
	}

	public DialogAdd getDialogAddImage() {
		return dialogAdd;
	}

	public void setCategory(String category) {
		this.category = category;
		numberQuestionsDialog = new NumberQuestionsDialog(this, this);
	}

	public String getCategory() {
		return category;
	}

	public void setLevel(String level) {
		this.level = level;
		numberQuestionsDialog = new NumberQuestionsDialog(this, this);
	}

	public String getLevel() {
		return level;
	}

	public void setNumberOfQuestions(int numberOfQuestions) {
		this.numberOfQuestions = numberOfQuestions;
	}

	public int getNumberOfQuestions() {
		return numberOfQuestions;
	}

	@RequiresApi(api = Build.VERSION_CODES.KITKAT)
	public void removeAlarmManager(int position) {
		for (int i = position; i < alarmsHours.size() - 1; i++) {
			Intent intentToAlarmRing = new Intent(this, AlarmRing.class);
			String hour = (alarmsHours.get(i + 1) >= 0 && alarmsHours.get(i + 1) < 10)
					? "0" + alarmsHours.get(i + 1)
					: "" + alarmsHours.get(i + 1);
			String minute = (alarmsMinutes.get(i + 1) >= 0 && alarmsMinutes.get(i + 1) < 10)
					? "0" + alarmsMinutes.get(i + 1)
					: "" + alarmsMinutes.get(i + 1);
			String time = hour + ":" + minute;
			intentToAlarmRing.putExtra("time", time);
			intentToAlarmRing.putExtra("title", alarmsTitle.get(i + 1));
			intentToAlarmRing.putExtra("uri_image", uriImage);
			intentToAlarmRing.putExtra("uri_sound", uriSound);
			intentToAlarmRing.putExtra("sound", "alarm" + (alarmsSound.get(i + 1) + 1));
			//intentToAlarmRing.putExtra("activate_game", isGameActivated);
			//intentToAlarmRing.putExtra("category", category);
			//intentToAlarmRing.putExtra("level", level);
			intentToAlarmRing.putExtra("number_of_questions", numberOfQuestions);
			PendingIntent pendingIntent = PendingIntent.getActivity(this, i, intentToAlarmRing,
					PendingIntent.FLAG_CANCEL_CURRENT);
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MINUTE, alarmsMinutes.get(i + 1));
			cal.set(Calendar.HOUR_OF_DAY, alarmsHours.get(i + 1));

			if (cal.getTime().before(Calendar.getInstance().getTime())) {
				cal.add(Calendar.DAY_OF_YEAR, 1);
			}

			alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
		}
        cancelAnAlarmManager(alarmsHours.size()-1);
	}

	private void cancelAnAlarmManager(int position)
    {
        Intent intentToAlarmRing = new Intent(SmartAlarm.this, AlarmRing.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(SmartAlarm.this, position, intentToAlarmRing,
                PendingIntent.FLAG_CANCEL_CURRENT);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, alarmsMinutes.get(position));
        cal.set(Calendar.HOUR_OF_DAY, alarmsHours.get(position));
        if (cal.getTime().before(Calendar.getInstance().getTime())) {
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }
        alarmManager.cancel(pendingIntent);
    }

    private class SimpleAdapterWithBackgroundChanged extends SimpleAdapter
    {
        private SimpleAdapterWithBackgroundChanged(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            convertView = super.getView(position, convertView,  parent);
            if(!alarmsActivated.isEmpty()) {
                if (!alarmsActivated.get(position)) {
                    convertView.setBackgroundColor(getResources().getColor(R.color.dark));
                } else {
                    convertView.setBackgroundColor(getResources().getColor(R.color.bright));
                }
            }
            return convertView;
        }
    }

    public static boolean getIsActivated()
	{
		return isActivated;
	}
}