package com.example.adrien.smartalarm;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import java.util.List;

public class DialogRemove extends AbstractDialogAddOrRemove {
    private Button remove = null;
    private final int position;
    private String hour;
    private String minute;

    public DialogRemove(@NonNull Context context, SmartAlarm main_activity, int position, String hour, String minute) {
        super(context,main_activity);
        this.hour=hour;
        this.minute=minute;
        this.position=position;
    }

    @Override
    protected void onCreate(Bundle savedInstance) {
        setContentView(R.layout.dialog_remove);
        super.onCreate(savedInstance);

        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String time = hours.getText().toString()+":"+minutes.getText().toString();
                String title = editTitle.getText().toString();
                main_activity.changeAlarm(time , title, position, Integer.parseInt(hours.getText().toString()), Integer.parseInt(minutes.getText().toString()));
                Runnable activateAlarm = new ActivateAlarm(main_activity,position,list_tone.getSelectedItem().toString(),main_activity.getUriImage(),title);
                Thread threadAlarm = new Thread(activateAlarm);
                main_activity.getListThreadAlarms().set(position,activateAlarm);
                threadAlarm.start();
                DialogRemove.this.dismiss();
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
