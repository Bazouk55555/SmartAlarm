/*
package com.example.adrien.smartalarm;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAa
public class DialogAdd1 extends Dialog {
    SmartAlarm main_activity;
    private Spinner list_tone=null;
    private Button cancel=null;
    private Button save=null;
    private EditText hours=null;
    private EditText minutes=null;
    private EditText editTitle= null;
    private ImageView arrow_up1=null;
    private ImageView arrow_up2=null;
    private ImageView arrow_down1=null;
    private ImageView arrow_down2=null;
    List<String> alarms = new ArrayList<String>();
    private int counterAlarmSound;

    public DialogAdd1(@NonNull Context context, SmartAlarm main_activity) {
        super(context);
        this.main_activity=main_activity;
    }

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.dialog_add);

        list_tone = (Spinner) findViewById(R.id.list_tone);
        counterAlarmSound=0;
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
                DialogAdd1.this.dismiss();
            }
        });

        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String time = hours.getText().toString()+":"+DialogAdd1.this.minutes.getText().toString();
                String title = editTitle.getText().toString();
                main_activity.setNewAlarm(time,title, Integer.parseInt(hours.getText().toString()),Integer.parseInt(minutes.getText().toString()));
                Runnable activateAlarm = new ActivateAlarm(main_activity,main_activity.getAlarmsMinutes().size()-1,list_tone.getSelectedItem().toString(),title);
                Thread threadAlarm = new Thread(activateAlarm);
                main_activity.getListThreadAlarms().add(activateAlarm);
                threadAlarm.start();
                DialogAdd1.this.dismiss();
            }
        });

        editTitle = (EditText) findViewById(R.id.editTitle);

        hours = (EditText) findViewById(R.id.hours);
        hours.addTextChangedListener(new TextWatcherTime(2,3,hours));

        minutes = (EditText) findViewById(R.id.minutes);
        minutes.addTextChangedListener(new TextWatcherTime(6,-1,minutes));

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

    public void setCounterAlarmSound(int counterAlarmSound)
    {
        this.counterAlarmSound=counterAlarmSound;
    }

    public int getCounterAlarmSound()
    {
        return this.counterAlarmSound++;
    }

    public List<String>getAlarms()
    {
        return alarms;
    }

    private class TextWatcherTime implements TextWatcher
    {
        private int firstLimitTime;
        private int secondLimitTime;
        private EditText editText;
        private boolean canModifyText=true;
        private int start;

        public TextWatcherTime(int firstLimitTime, int secondLimitTime, EditText editText)
        {
            this.firstLimitTime=firstLimitTime;
            this.secondLimitTime=secondLimitTime;
            this.editText=editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            this.start=start;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(canModifyText == true) {
                if (s.toString().length() == 3) {
                    canModifyText = false;
                    editText.setText(s.toString().substring(0, 2));
                    canModifyText = true;
                }
                if(s.toString().length() == 2)
                {
                    canModifyText = false;
                    if(start==0)
                    {
                        if(Character.getNumericValue(s.toString().charAt(start))>firstLimitTime|| (Character.getNumericValue(s.toString().charAt(start))==firstLimitTime && Character.getNumericValue(s.toString().charAt(1))>secondLimitTime))
                        {
                            editText.setText(s.toString().substring(1, 2));
                        }
                    }
                    if(start==1)
                    {
                        if(Character.getNumericValue(s.toString().charAt(0))>firstLimitTime|| (Character.getNumericValue(s.toString().charAt(0))==firstLimitTime && Character.getNumericValue(s.toString().charAt(start))>secondLimitTime))
                        {
                            editText.setText(s.toString().substring(0, 1));
                        }
                    }
                    canModifyText=true;
                }
            }
        }
    }
}
*/
