/*
package com.example.adrien.smartalarm;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class DialogRemove1 extends Dialog {
    private SmartAlarm main_activity;
    private Spinner list_tone=null;
    private Button cancel=null;
    private Button save=null;
    private Button remove = null;
    private EditText hours=null;
    private EditText minutes=null;
    private EditText editTitle= null;
    private ImageView arrow_up1=null;
    private ImageView arrow_up2=null;
    private ImageView arrow_down1=null;
    private ImageView arrow_down2=null;
    private final int position;
    private String hour;
    private String minute;

    public DialogRemove1(@NonNull Context context, SmartAlarm main_activity, int position, String hour, String minute) {
        super(context);
        this.main_activity=main_activity;
        this.hour=hour;
        this.minute=minute;
        this.position=position;
    }

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.dialog_remove);

        list_tone = (Spinner) findViewById(R.id.list_tone);
        List<String> alarms = new ArrayList<String>();
        alarms.add("alarm1");
        alarms.add("alarm2");
        alarms.add("alarm3");
        alarms.add("alarm4");
        alarms.add("alarm5");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(main_activity, android.R.layout.simple_spinner_dropdown_item, alarms);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        list_tone.setAdapter(adapter);

        cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DialogRemove1.this.dismiss();
            }
        });

        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String time = hours.getText().toString()+":"+DialogRemove1.this.minutes.getText().toString();
                String title = editTitle.getText().toString();
                main_activity.changeAlarm(time , title, position, Integer.parseInt(hours.getText().toString()), Integer.parseInt(minutes.getText().toString()));
                List<Runnable> listThread = main_activity.getListThreadAlarms();
                ((ActivateAlarm)listThread.get(position)).setContinueThread(false);
                Runnable activateAlarm = new ActivateAlarm(main_activity,position,list_tone.getSelectedItem().toString(),title);
                Thread threadAlarm = new Thread(activateAlarm);
                listThread.set(position,activateAlarm);
                threadAlarm.start();
                DialogRemove1.this.dismiss();
            }
        });

        remove = (Button) findViewById(R.id.remove);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Runnable> listThread = main_activity.getListThreadAlarms();
                ((ActivateAlarm)listThread.get(position)).setContinueThread(false);
                listThread.remove(position);
                for(int i=position;i<listThread.size();i++)
                {
                    ((ActivateAlarm)listThread.get(i)).setIndex(((ActivateAlarm)listThread.get(i)).getIndex()-1);
                }
                main_activity.removeAlarm(position);
                DialogRemove1.this.dismiss();
            }
        });

        editTitle = (EditText) findViewById(R.id.editTitle);

        hours = (EditText) findViewById(R.id.hours);
        if(Integer.parseInt(hour)<10)
        {
            hour="0"+hour;
        }
        hours.setText(hour);
        minutes = (EditText) findViewById(R.id.minutes);
        if(Integer.parseInt(minute)<10)
        {
            minute="0"+minute;
        }
        minutes.setText(minute);

        arrow_up1 = (ImageView) findViewById(R.id.arrow_up1);
        arrow_up1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number =(Integer.parseInt(hours.getText().toString())+1)%24;
                String text_number= String.valueOf(number);
                if(number>-1 && number<10)
                {
                    text_number="0"+text_number;
                }
                hours.setText(text_number);
            }
        });

        arrow_down1 = (ImageView) findViewById(R.id.arrow_down1);
        arrow_down1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = ((Integer.parseInt(hours.getText().toString())-1)+24)%24;
                String text_number= String.valueOf(number);
                if(number>-1 && number<10)
                {
                    text_number="0"+text_number;
                }
                hours.setText(text_number);
            }
        });

        arrow_up2 = (ImageView) findViewById(R.id.arrow_up2);
        arrow_up2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = (Integer.parseInt(minutes.getText().toString())+1)%60;
                String text_number= String.valueOf(number);
                if(number>-1 && number<10)
                {
                    text_number="0"+text_number;
                }
                minutes.setText(text_number);
            }
        });

        arrow_down2 = (ImageView) findViewById(R.id.arrow_down2);
        arrow_down2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = ((Integer.parseInt(minutes.getText().toString())-1)+60)%60;
                String text_number= String.valueOf(number);
                if(number>-1 && number<10)
                {
                    text_number="0"+text_number;
                }
                minutes.setText(text_number);
            }
        });
    }
}
*/
