package com.example.adrien.smartalarm.mainActivity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;

import com.example.adrien.smartalarm.R;

import java.util.List;

public class DialogRemove extends AbstractDialogAddOrRemove {
    private Button remove = null;
    private final int position;
    private String hour;
    private String minute;
    private String title;
    private int soundSelected;

    public DialogRemove(@NonNull Context context, SmartAlarm main_activity, int position, String hour, String minute, String title, int soundSelected) {
        super(context,main_activity);
        this.hour=hour;
        this.minute=minute;
        this.title=title;
        this.position=position;
        this.soundSelected=soundSelected;
    }

    @Override
    protected void onCreate(Bundle savedInstance) {
        setContentView(R.layout.dialog_remove);
        super.onCreate(savedInstance);

        editTitle.setText(title);
        list_tone.setSelection(soundSelected);
        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                checkBeforeSave();
                String hour = hours.getText().toString();
                String minute = minutes.getText().toString();
                if (hour.length()==1) {
                    hour = "0" + hour;
                }
                if (minute.length()==1) {
                    minute = "0" + minute;
                }
                String time = hour+":"+minute;
                String title = editTitle.getText().toString();
                soundSelected=list_tone.getSelectedItemPosition();
                main_activity.changeAlarm(time , title, position, Integer.parseInt(hours.getText().toString()), Integer.parseInt(minutes.getText().toString()), soundSelected);
                main_activity.setAlarmManager(position,list_tone.getSelectedItem().toString(),title);
                DialogRemove.this.dismiss();
            }
        });

        remove = (Button) findViewById(R.id.remove);
        remove.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                main_activity.removeAlarmManager(position);
                main_activity.removeAlarm(position);
                DialogRemove.this.dismiss();
            }
        });

        if(Integer.parseInt(hour)<10)
        {
            hour="0"+hour;
        }
        hours.setText(hour);

        if(Integer.parseInt(minute)<10)
        {
            minute="0"+minute;
        }
        minutes.setText(minute);
    }
}
