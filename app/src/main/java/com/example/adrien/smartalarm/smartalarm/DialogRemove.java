package com.example.adrien.smartalarm.smartalarm;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.adrien.smartalarm.R;

import java.util.ArrayList;
import java.util.List;

public class DialogRemove extends AbstractDialogAddOrRemove {
    private final int position;
    private String hour;
    private String minute;
    private String title;
    private int soundSelected;

    public DialogRemove(@NonNull Context context, SmartAlarm smartAlarm, int position, String hour, String minute,
                        String title, int soundSelected) {
        super(context, smartAlarm);
        this.hour = hour;
        this.minute = minute;
        this.title = title;
        this.position = position;
        this.soundSelected = soundSelected;
    }

    @Override
    protected void onCreate(Bundle savedInstance) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_remove);
        super.onCreate(savedInstance);

        editTitle.setText(title);
        listTone.setSelection(soundSelected);
        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time = getTimeBeforeSave();
                int hour = Integer.parseInt(hours.getText().toString());
                int minute = Integer.parseInt(minutes.getText().toString());
                List<Integer> alarmHours = smartAlarm.getAlarmsHours();
                List<Integer> alarmMinutes = smartAlarm.getAlarmsMinutes();
                List<String> alarmsTimeList = new ArrayList<>();
                for (int i = 0; i < smartAlarm.getAlarmsHours().size(); i++) {
                    if (i != position) {
                        alarmsTimeList.add(String.valueOf(alarmHours.get(i))
                                + String.valueOf(alarmMinutes.get(i)));
                    }
                }
                if (alarmsTimeList.contains(String.valueOf(hour) + String.valueOf(minute))) {
                    alertAlarmInDouble();
                } else {
                    String title = editTitle.getText().toString();
                    soundSelected = listTone.getSelectedItemPosition();
                    boolean isActivated = smartAlarm.getAlarmsActivated().get(position);
                    smartAlarm.cancelAnAlarmManager(position);
                    smartAlarm.removeAlarm(position);
                    int newPosition = smartAlarm.getPositionNewAlarm(hour, minute);
                    smartAlarm.setNewAlarm(newPosition, time, title, hour,
                            minute, listTone.getSelectedItemPosition(), isActivated);
                    if (isActivated) {
                        smartAlarm.setAlarmManager(newPosition,
                                listTone.getSelectedItem().toString(), title);
                    }
                    DialogRemove.this.dismiss();
                }
            }
        });

        Button remove = (Button) findViewById(R.id.remove);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smartAlarm.cancelAnAlarmManager(position);
                smartAlarm.removeAlarm(position);
                DialogRemove.this.dismiss();
            }
        });

        if (Integer.parseInt(hour) < 10) {
            hour = "0" + hour;
        }
        hours.setText(hour);

        if (Integer.parseInt(minute) < 10) {
            minute = "0" + minute;
        }
        minutes.setText(minute);
    }
}
