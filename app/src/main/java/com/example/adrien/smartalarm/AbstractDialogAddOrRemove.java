package com.example.adrien.smartalarm;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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

public class AbstractDialogAddOrRemove extends Dialog {
    protected SmartAlarm main_activity;
    protected Spinner list_tone=null;
    protected Button cancel=null;
    protected Button save=null;
    protected EditText hours=null;
    protected EditText minutes=null;
    protected EditText editTitle= null;
    protected ImageView arrow_up1=null;
    protected ImageView arrow_up2=null;
    protected ImageView arrow_down1=null;
    protected ImageView arrow_down2=null;
    protected List<String> alarms = new ArrayList<String>();
    Handler redArrowForShortTime;
    //protected Thread integerLessThanTenThread;

    public AbstractDialogAddOrRemove(@NonNull Context context, SmartAlarm main_activity) {
        super(context);
        this.main_activity=main_activity;
        alarms.add("alarm1");
        alarms.add("alarm2");
        alarms.add("alarm3");
        alarms.add("alarm4");
        alarms.add("alarm5");
    }

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        list_tone = (Spinner) findViewById(R.id.list_tone);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(main_activity, android.R.layout.simple_spinner_dropdown_item, alarms);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        list_tone.setAdapter(adapter);

        cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AbstractDialogAddOrRemove.this.dismiss();
            }
        });

        editTitle = (EditText) findViewById(R.id.editTitle);

        hours = (EditText) findViewById(R.id.hours);
        hours.addTextChangedListener(new TextWatcherTime(2,3,hours));

        /*integerLessThanTenThread= new Thread(){
            @Override
            public void run()
            {
                while(true) {
                    if (hours.getText().toString().length()!=2 && !hours.isSelected()) {
                        hours.setText("0"+hours.getText().toString());
                    }
                    if (minutes.getText().toString().length()!=2 && !minutes.isSelected()) {
                        minutes.setText("0"+minutes.getText().toString());
                    }
                }
            }
        };
        integerLessThanTenThread.start();*/

        minutes = (EditText) findViewById(R.id.minutes);
        minutes.addTextChangedListener(new TextWatcherTime(6,-1,minutes));

        redArrowForShortTime= new Handler();

        arrow_up1 = (ImageView) findViewById(R.id.arrow_up1);
        arrow_up1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrow_up1.setImageResource(R.drawable.ic_arrow_up_red);
                int number =(Integer.parseInt(hours.getText().toString())+1)%24;
                String text_number= String.valueOf(number);
                if(number>-1 && number<10)
                {
                    text_number="0"+text_number;
                }
                hours.setText(text_number);
                redArrowForShortTime.postDelayed(new redArrowRunnable(arrow_up1, R.drawable.ic_arrow_up),150);
            }
        });

        arrow_down1 = (ImageView) findViewById(R.id.arrow_down1);
        arrow_down1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrow_down1.setImageResource(R.drawable.ic_arrow_down_red);
                int number = ((Integer.parseInt(hours.getText().toString())-1)+24)%24;
                String text_number= String.valueOf(number);
                if(number>-1 && number<10)
                {
                    text_number="0"+text_number;
                }
                hours.setText(text_number);
                redArrowForShortTime.postDelayed(new redArrowRunnable(arrow_down1, R.drawable.ic_arrow_down),150);
            }
        });

        arrow_up2 = (ImageView) findViewById(R.id.arrow_up2);
        arrow_up2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrow_up2.setImageResource(R.drawable.ic_arrow_up_red);
                int number = (Integer.parseInt(minutes.getText().toString())+1)%60;
                String text_number= String.valueOf(number);
                if(number>-1 && number<10)
                {
                    text_number="0"+text_number;
                }
                minutes.setText(text_number);
                redArrowForShortTime.postDelayed(new redArrowRunnable(arrow_up2, R.drawable.ic_arrow_up),150);
            }
        });

        arrow_down2 = (ImageView) findViewById(R.id.arrow_down2);
        arrow_down2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrow_down2.setImageResource(R.drawable.ic_arrow_down_red);
                int number = ((Integer.parseInt(minutes.getText().toString())-1)+60)%60;
                String text_number= String.valueOf(number);
                if(number>-1 && number<10)
                {
                    text_number="0"+text_number;
                }
                minutes.setText(text_number);
                redArrowForShortTime.postDelayed(new redArrowRunnable(arrow_down2, R.drawable.ic_arrow_down),150);
            }
        });
    }

    public List<String> getAlarms()
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
            if(canModifyText) {
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
                        boolean goToNextStep = true;
                        if(Character.getNumericValue(s.toString().charAt(0))>firstLimitTime|| (Character.getNumericValue(s.toString().charAt(0))==firstLimitTime && Character.getNumericValue(s.toString().charAt(start))>secondLimitTime))
                        {
                            editText.setText(s.toString().substring(0, 1));
                            goToNextStep=false;
                        }
                        if(goToNextStep==true)
                        {
                            if(editText==hours){
                                minutes.requestFocus();
                            }
                            else if (editText==minutes){
                                findViewById(R.id.editTitle).requestFocus();
                            }
                        }
                    }
                    canModifyText=true;
                }
            }
        }
    }

    private class redArrowRunnable implements Runnable {

        ImageView imageView;
        int drawableArrow;

        public redArrowRunnable(ImageView imageView, int drawableArrow)
        {
            this.imageView=imageView;
            this.drawableArrow=drawableArrow;
        }

        @Override
        public void run() {
            imageView.setImageResource(drawableArrow);
        }
    }
}
