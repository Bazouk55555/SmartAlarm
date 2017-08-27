package com.example.adrien.smartalarm.mainActivity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
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

import com.example.adrien.smartalarm.AfterAlarmRing.BackgroundService;
import com.example.adrien.smartalarm.R;
import com.example.adrien.smartalarm.SQliteService.Alarm;
import com.example.adrien.smartalarm.SQliteService.AlarmBaseDAO;

import java.util.ArrayList;
import java.util.Calendar;
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
    //private List<Runnable> listThreadAlarms;
    AlarmManager alarmManager;
    private Uri uriImage;
    private DialogAddImage dialogAddImage;
    private MenuItem takeOffImageMenuItem;
    private Uri uriSound;
    private DialogAddSound dialogAddSound;
    private MenuItem takeOffSoundMenuItem;
    private boolean isAlarmSix = false;
    private CheckBox activateGame = null;
    private String category = "Random Category";
    private CategoryDialog categoryDialog=null;
    private int numberOfQuestions=0;
    private NumberQuestionsDialog numberQuestionsDialog=null;


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

        //listThreadAlarms = new ArrayList<>();
        alarmManager = (AlarmManager)getSystemService(Activity.ALARM_SERVICE);
        alarmsHours = new ArrayList<>();
        alarmsMinutes = new ArrayList<>();
        alarmsTitle = new ArrayList<>();
        alarmsSound = new ArrayList<>();
        alarmsActivated = new ArrayList<>();

        AlarmBaseDAO alarmBaseDAO = new AlarmBaseDAO(this);
        alarmBaseDAO.open();
        List<Alarm>alarmList = alarmBaseDAO.select();
        if(!alarmList.isEmpty()) {
            System.out.println("je suis la frero!!!");
            for(Alarm alarm: alarmList) {
                System.out.println("Nouvelle alarme avec ID="+alarm.getId()+" et time="+alarm.getTime());
                alarmsHours.add(alarm.getHour());
                alarmsMinutes.add(alarm.getMinute());
                alarmsTitle.add(alarm.getTitle());
                alarmsSound.add(alarm.getSound());
                alarmsActivated.add(alarm.getActivated());
                HashMap<String, String> mapOfTheNewAlarm = new HashMap<>();
                mapOfTheNewAlarm.put("alarm", alarm.getTime());
                mapOfTheNewAlarm.put("title", alarm.getTitle());
                listMapOfEachAlarm.add(mapOfTheNewAlarm);
                adapter_alarms.notifyDataSetChanged();
                HashMap<String, Integer> mapOfTheAlarmDrawable = new HashMap<>();
                if(alarm.getActivated()) {
                    mapOfTheAlarmDrawable.put("alarm_drawable", R.drawable.alarm_on);
                }
                else
                {
                    mapOfTheAlarmDrawable.put("alarm_drawable", R.drawable.alarm_off);
                }
                listMapOfActivates.add(mapOfTheAlarmDrawable);
                adapter_activates.notifyDataSetChanged();
                Runnable activateAlarm = new ActivateAlarm(this,(int)alarm.getId()-1,"alarm"+(alarm.getSound()+1),alarm.getTitle());
                //Thread threadAlarm = new Thread(activateAlarm);
                //listThreadAlarms.add(activateAlarm);
                //threadAlarm.start();
            }
            alarmBaseDAO.close();
        }

        list_view_activates.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ImageView activatedImageView = (ImageView) view.findViewById(R.id.activate);
                final View alarmView = list_view_alarms.getChildAt(position);
                if(alarmsActivated.get(position)){
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
                break;
            case R.id.takeof_sound:
                uriSound=null;
                isAlarmSix=false;
                takeOffSoundMenuItem.setEnabled(false);
                break;
            case R.id.category_of_question:
                if(categoryDialog==null)
                {
                 categoryDialog=new CategoryDialog(this,this);
                }
                categoryDialog.show();
                break;
            case R.id.number_of_question:
                if(numberQuestionsDialog==null)
                {
                    numberQuestionsDialog=new NumberQuestionsDialog(this,this);
                }
                numberQuestionsDialog.show();
                break;
        }
        return true;
    }

    public void setNewAlarm(String time,String title, int hour, int minute, int soundSelected)
    {
        alarmsHours.add(hour);
        alarmsMinutes.add(minute);
        alarmsTitle.add(title);
        alarmsSound.add(soundSelected);

        AlarmBaseDAO alarmBaseDAO = new AlarmBaseDAO(this);
        alarmBaseDAO.open();
        alarmBaseDAO.add(new Alarm(alarmsHours.size()-1,hour,minute,time,title,soundSelected,true));

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

        AlarmBaseDAO alarmBaseDAO = new AlarmBaseDAO(this);
        alarmBaseDAO.open();
        alarmBaseDAO.update(new Alarm(position+1,hour,minute,time,title,soundSelected,true));

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

        AlarmBaseDAO alarmBaseDAO = new AlarmBaseDAO(this);
        alarmBaseDAO.open();
        System.out.println("Before remove");
        alarmBaseDAO.remove(position+1, alarmsHours.size()-position);

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

    //public List<Runnable> getListThreadAlarms()
    //{
    //    return listThreadAlarms;
    //}

    public void setAlarmManager(int index,String sound,String title)
    {
        Intent intent_to_alarm_ring=new Intent(this,BackgroundService.class);
        String hour = (alarmsHours.get(index)>=0 && alarmsHours.get(index)<10)? "0" + alarmsHours.get(index) : "" + alarmsHours.get(index);
        String minute = (alarmsMinutes.get(index)>=0 && alarmsMinutes.get(index)<10)? "0" + alarmsMinutes.get(index) : "" + alarmsMinutes.get(index);
        String time = hour+":"+minute;
        intent_to_alarm_ring.putExtra("time",time);
        intent_to_alarm_ring.putExtra("title",title);
        intent_to_alarm_ring.putExtra("uri_image",uriImage);
        intent_to_alarm_ring.putExtra("uri_sound",uriSound);
        intent_to_alarm_ring.putExtra("sound",sound);
        intent_to_alarm_ring.putExtra("activate_game",activateGame.isChecked());
        System.out.println("JE SUIS LA JE SUIS PAS MORT!!!");
        if(category!=null) {
            intent_to_alarm_ring.putExtra("category", category);
        }
        if(numberOfQuestions!=0) {
            intent_to_alarm_ring.putExtra("number_of_questions", numberOfQuestions);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                index, intent_to_alarm_ring, PendingIntent.FLAG_CANCEL_CURRENT);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, alarmsMinutes.get(index));
        cal.set(Calendar.HOUR, alarmsHours.get(index));
        System.out.println(cal);
        if(cal.getTime().before(Calendar.getInstance().getTime()))
        {
            cal.add(Calendar.DAY_OF_YEAR,1);
        }
        alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                pendingIntent);
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

    public void setCategory(String category)
    {
        this.category=category;
        numberQuestionsDialog = new NumberQuestionsDialog(this,this);
    }

    public String getCategory()
    {
        return category;
    }

    public void setNumberOfQuestions(int numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }

    public int getNumberOfQuestions() {
        return numberOfQuestions;
    }

    @Override
    public void onBackPressed()
    {
        System.out.println("Before onBackPressed");
        /*for(int i=0;i<listThreadAlarms.size();i++)
        {
            ((ActivateAlarm)listThreadAlarms.get(i)).setContinueThread(false);
        }*/
        super.onBackPressed();
    }

    @Override
    public void onStop()
    {
        System.out.println("STOP!!!!!!!!!!!!");
        super.onStop();
    }

    @Override
    public void onDestroy()
    {
        for(int i=0;i<alarmsActivated.size();i++)
        {
            alarmsActivated.set(i,false);
        }
        super.onDestroy();
    }

    @Override
    public void finish()
    {
        super.finish();
        System.out.println("FINISH!!!!");
    }
}