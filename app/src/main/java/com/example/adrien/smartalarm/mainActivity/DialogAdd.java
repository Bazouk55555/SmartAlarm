package com.example.adrien.smartalarm.mainActivity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.example.adrien.smartalarm.R;

public class DialogAdd extends AbstractDialogAddOrRemove {

    public DialogAdd(@NonNull Context context, SmartAlarm main_activity) {
        super(context,main_activity);
    }

    @Override
    protected void onCreate(Bundle savedInstance) {
        setContentView(R.layout.dialog_add);
        super.onCreate(savedInstance);

        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener(){
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
                main_activity.setNewAlarm(time,title, Integer.parseInt(hours.getText().toString()),Integer.parseInt(minutes.getText().toString()),list_tone.getSelectedItemPosition());
                main_activity.setAlarmManager(main_activity.getAlarmsMinutes().size()-1,list_tone.getSelectedItem().toString(),title);
                //Runnable activateAlarm = new ActivateAlarm(main_activity,main_activity.getAlarmsMinutes().size()-1,list_tone.getSelectedItem().toString(),title);
                //Thread threadAlarm = new Thread(activateAlarm);
                //main_activity.getListThreadAlarms().add(activateAlarm);
                //threadAlarm.start();
                DialogAdd.this.dismiss();
            }
        });
    }
}
