package com.example.adrien.smartalarm.mainActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.adrien.smartalarm.R;
import com.example.adrien.smartalarm.SQliteService.AbstractBaseDAO;
import com.example.adrien.smartalarm.SQliteService.Question;
import com.example.adrien.smartalarm.SQliteService.SportsDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SmartAlarm extends AppCompatActivity {

    private DialogAdd dialog_add;
    private DialogRemove dialogRemove;
    private ListView list_view_alarms;
    private ListView list_view_activates;
    private List<HashMap<String, String>> listMapOfEachAlarm;
    private List<HashMap<String, Integer>> listMapOfActivates;
    private SimpleAdapter adapter_alarms;
    private SimpleAdapter adapter_activates;
    private List<Boolean> alarmsActivated;
    private List<Integer> alarmsHours;
    private List<Integer> alarmsMinutes;
    private List<String> alarmsTitle;
    private List<Integer> alarmsSound;
    private List<Runnable> listThreadAlarms;
    private Uri uriImage;
    private DialogAddImage dialogAddImage;
    private MenuItem takeOffImageMenuItem;
    private Uri uriSound;
    private DialogAddSound dialogAddSound;
    private MenuItem takeOffSoundMenuItem;
    private boolean isAlarmSix = false;
    private CheckBox activateGame = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_alarm);

        activateGame = (CheckBox) findViewById(R.id.checkbox);

        dialog_add=new DialogAdd(this,this);
        list_view_alarms=(ListView)findViewById(R.id.list_alarm);
        list_view_activates=(ListView)findViewById(R.id.list_activate);
        listMapOfEachAlarm = new ArrayList<>();
        listMapOfActivates = new ArrayList<>();
        adapter_alarms = new SimpleAdapter(this,listMapOfEachAlarm, R.layout.item_alarm, new String[] {"alarm", "title"},
                new int[] {R.id.time, R.id.title });
        adapter_activates = new SimpleAdapter(this,listMapOfActivates, R.layout.item_activate, new String[] {"alarm_drawable"},
                new int[] {R.id.activate});
        list_view_alarms.setAdapter(adapter_alarms);
        list_view_activates.setAdapter(adapter_activates);

        listThreadAlarms = new ArrayList<>();
        alarmsHours = new ArrayList<>();
        alarmsMinutes = new ArrayList<>();
        alarmsTitle = new ArrayList<>();
        alarmsSound = new ArrayList<>();

        alarmsActivated = new ArrayList<>();
        list_view_activates.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ImageView activatedImageView = (ImageView) view.findViewById(R.id.activate);
                final View alarmView = list_view_alarms.getChildAt(position);
                if(alarmsActivated.get(position)==true){
                    activatedImageView.setImageResource(R.drawable.alarm_off);
                    activatedImageView.setBackgroundColor(getResources().getColor(R.color.dark));
                    alarmView.setBackgroundColor(getResources().getColor(R.color.dark));
                    alarmsActivated.set(position,false);
                }
                else{
                    activatedImageView.setImageResource(R.drawable.alarm_on);
                    activatedImageView.setBackgroundColor(getResources().getColor(R.color.bright));
                    alarmView.setBackgroundColor(getResources().getColor(R.color.bright));
                    alarmsActivated.set(position,true);
                }
            }
        });

        list_view_alarms.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view,
                                    final int position,
                                    long id) {
                dialogRemove=new DialogRemove(SmartAlarm.this,SmartAlarm.this,position,alarmsHours.get(position).toString(),alarmsMinutes.get(position).toString(),alarmsTitle.get(position),alarmsSound.get(position));
                if(isAlarmSix ==true)
                {
                    dialogRemove.getAlarms().add("alarm6");
                }
                dialogRemove.show();
            }
        });

        Question question = new Question(0, "Quel est le nom de la fille de Sonia", "3Iboulboulah", "Anna", "3Anna", "Boulboulah");
        SportsDAO sportsDAO = new SportsDAO(this);
        ((SportsDAO)sportsDAO).open();
        ((SportsDAO)sportsDAO).add(question);
        sportsDAO.close();
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
        switch(item.getItemId())
        {
            case R.id.add:
                dialog_add= new DialogAdd(this,this);
                if(isAlarmSix ==true)
                {
                    dialog_add.getAlarms().add("alarm6");
                }
                dialog_add.show();

                break;
            case R.id.add_sound:
                dialogAddSound = new DialogAddSound(this,this);
                dialogAddSound.show();
                break;
            case R.id.add_image:
                dialogAddImage = new DialogAddImage(this,this);
                dialogAddImage.show();
                break;
            case R.id.takeof_image:
                uriImage=null;
                takeOffImageMenuItem.setEnabled(false);
            case R.id.takeof_sound:
                uriSound=null;
                isAlarmSix=false;
                takeOffSoundMenuItem.setEnabled(false);
        }
        return true;
    }

    public void setNewAlarm(String time,String title, int hour, int minute, int soundSelected)
    {
        alarmsHours.add(hour);
        alarmsMinutes.add(minute);
        alarmsTitle.add(title);
        alarmsSound.add(soundSelected);

        HashMap<String,String> mapOfTheNewAlarm = new HashMap<>();
        mapOfTheNewAlarm.put("alarm", time);
        mapOfTheNewAlarm.put("title", title);
        listMapOfEachAlarm.add(mapOfTheNewAlarm);
        adapter_alarms.notifyDataSetChanged();

        alarmsActivated.add(true);
        HashMap<String,Integer> mapOfTheAlarmDrawable = new HashMap<>();
        mapOfTheAlarmDrawable.put("alarm_drawable", R.drawable.alarm_on);
        listMapOfActivates.add(mapOfTheAlarmDrawable);
        adapter_activates.notifyDataSetChanged();
    }

    public void changeAlarm(String time, String title, int position, int hour, int minute, int soundSelected)
    {
        alarmsHours.set(position,hour);
        alarmsMinutes.set(position,minute);
        alarmsTitle.set(position,title);
        alarmsSound.set(position,soundSelected);
        HashMap<String,String> mapOfTheNewAlarm = new HashMap<>();
        mapOfTheNewAlarm.put("alarm", time);
        mapOfTheNewAlarm.put("title", title);
        listMapOfEachAlarm.set(position,mapOfTheNewAlarm);
        adapter_alarms.notifyDataSetChanged();
    }

    public void removeAlarm(int position)
    {
        alarmsHours.remove(position);
        alarmsMinutes.remove(position);
        alarmsTitle.remove(position);
        alarmsSound.remove(position);
        listMapOfEachAlarm.remove(position);
        adapter_alarms.notifyDataSetChanged();

        alarmsActivated.remove(position);
        listMapOfActivates.remove(position);
        adapter_activates.notifyDataSetChanged();
    }

    public List<Integer> getAlarmsHours()
    {
        return alarmsHours;
    }

    public List<Integer> getAlarmsMinutes()
    {
        return alarmsMinutes;
    }

    public List<Boolean> getAlarmsActivated() {
        return alarmsActivated;
    }

    public List<Runnable> getListThreadAlarms()
    {
        return listThreadAlarms;
    }

    public Uri getUriImage()
    {
        return uriImage;
    }

    public Uri getUriSound()
    {
        return uriSound;
    }

    public void setUriImage(Uri uriImage)
    {
        this.uriImage=uriImage;
    }

    public void setUriSound(Uri uriSound)
    {
        this.uriSound=uriSound;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode==DialogAddImage.AUTHORIZATION_IMAGE && resultCode== Activity.RESULT_OK){
            dialogAddImage.setUriDialog(data.getData());
            if(dialogAddImage.getUriDialog()!=null) {
                dialogAddImage.setImageOk();
            }
        }
        else if(requestCode==DialogAddSound.AUTHORIZATION_SOUND && resultCode== Activity.RESULT_OK){
            dialogAddSound.setUriDialog(data.getData());
            if(dialogAddSound.getUriDialog()!=null)
            {
                dialogAddSound.setSoundOk();
            }
        }
    }

    public MenuItem getTakeOffImageMenuItem()
    {
        return takeOffImageMenuItem;
    }

    public MenuItem getTakeOffSoundMenuItem()
    {
        return takeOffSoundMenuItem;
    }

    public void setIsAlarmSix(boolean isAlarmSix)
    {
        this.isAlarmSix=isAlarmSix;
    }

    public DialogAdd getDialogAddImage()
    {
        return dialog_add;
    }

    public boolean isActivateGame()
    {
        return activateGame.isChecked();
    }
}