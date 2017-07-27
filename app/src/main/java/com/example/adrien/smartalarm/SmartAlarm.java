package com.example.adrien.smartalarm;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.adrien.smartalarm.R.color.blue;
import static com.example.adrien.smartalarm.R.color.black;

public class SmartAlarm extends AppCompatActivity {

    private DialogAdd dialog_add;
    private DialogRemove dialogRemove;
    private ListView list_view_alarms;
    private ListView list_view_activates;
    private List<HashMap<String, String>> listMapOfEachAlarm;
    private List<String> listActivates;
    private SimpleAdapter adapter_alarms;
    private ArrayAdapter adapter_activates;
    private List<Boolean> alarmsActivated;
    private List<Integer> alarmsHours;
    private List<Integer> alarmsMinutes;
    private List<Runnable> listThreadAlarms;
    private Uri uriImage;
    private DialogAddImage dialogAddImage;
    private MenuItem takeOffImagesMenuItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_alarm);

        list_view_alarms=(ListView)findViewById(R.id.list_alarm);
        list_view_activates=(ListView)findViewById(R.id.list_activate);
        listMapOfEachAlarm = new ArrayList<>();
        listActivates = new ArrayList<>();
        adapter_alarms = new SimpleAdapter(this,listMapOfEachAlarm, R.layout.item_alarm, new String[] {"alarm", "title"},
                new int[] {R.id.time, R.id.title });
        adapter_activates = new ArrayAdapter(this, R.layout.item_activate, listActivates);
        list_view_alarms.setAdapter(adapter_alarms);
        list_view_activates.setAdapter(adapter_activates);

        listThreadAlarms = new ArrayList<>();
        alarmsHours = new ArrayList<>();
        alarmsMinutes = new ArrayList<>();

        alarmsActivated = new ArrayList<>();
        list_view_activates.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final TextView activatedTextView = (TextView)view.findViewById(R.id.activate);
                if(activatedTextView.getCurrentTextColor()==getResources().getColor(blue)){
                    activatedTextView.setTextColor(getResources().getColor(black));
                    activatedTextView.setText("Activate");
                    alarmsActivated.set(position,false);
                }
                else{
                    activatedTextView.setTextColor(getResources().getColor(blue));
                    activatedTextView.setText("Deactivate");
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
                dialogRemove=new DialogRemove(SmartAlarm.this,SmartAlarm.this,position,alarmsHours.get(position).toString(),alarmsMinutes.get(position).toString());
                dialogRemove.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        takeOffImagesMenuItem = menu.findItem(R.id.takeof_image);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.add:
                dialog_add=new DialogAdd(this,this);
                dialog_add.show();
                break;
            case R.id.add_sound:
                DialogAddSound dialogAddSound = new DialogAddSound(this);
                dialogAddSound.show();
                dialog_add.getAlarms().add("TEST");
                dialog_add.setCounterAlarmSound(dialog_add.getCounterAlarmSound()+1);
                break;
            case R.id.add_image:
                dialogAddImage = new DialogAddImage(this,this);
                dialogAddImage.show();
                break;
            case R.id.takeof_image:
                uriImage=null;
                takeOffImagesMenuItem.setEnabled(false);
        }
        return true;
    }

    public void setNewAlarm(String time,String title, int hour, int minute)
    {
        alarmsHours.add(hour);
        alarmsMinutes.add(minute);
        HashMap<String,String> mapOfTheNewAlarm = new HashMap<>();
        mapOfTheNewAlarm.put("alarm", time);
        mapOfTheNewAlarm.put("title", title);
        listMapOfEachAlarm.add(mapOfTheNewAlarm);
        adapter_alarms.notifyDataSetChanged();

        alarmsActivated.add(true);
        listActivates.add("Deactivate");
        adapter_activates.notifyDataSetChanged();
    }

    public void changeAlarm(String time, String title, int position, int hour, int minute)
    {
        alarmsHours.set(position,hour);
        alarmsMinutes.set(position,minute);
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
        listMapOfEachAlarm.remove(position);
        adapter_alarms.notifyDataSetChanged();

        alarmsActivated.remove(position);
        listActivates.remove(position);
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

    public void setUriImage(Uri uriImage)
    {
        this.uriImage=uriImage;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        System.out.println("HERE");
        if(requestCode==3){
            dialogAddImage.setUriDialog(data.getData());
        }
    }

    public MenuItem getTakeOffImagesMenuItem()
    {
        return takeOffImagesMenuItem;
    }
}