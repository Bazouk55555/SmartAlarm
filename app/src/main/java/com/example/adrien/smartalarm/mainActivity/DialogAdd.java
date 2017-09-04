package com.example.adrien.smartalarm.mainActivity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;

import com.example.adrien.smartalarm.R;

public class DialogAdd extends AbstractDialogAddOrRemove {

	public DialogAdd(@NonNull Context context, SmartAlarm main_activity) {
		super(context, main_activity);
	}

	@Override
	protected void onCreate(Bundle savedInstance) {
		setContentView(R.layout.dialog_add);
		super.onCreate(savedInstance);

		save = (Button) findViewById(R.id.save);
		save.setOnClickListener(new View.OnClickListener() {
			@RequiresApi(api = Build.VERSION_CODES.KITKAT)
			@Override
			public void onClick(View v) {
				String time = getTimeBeforeSave();
				int hour = Integer.parseInt(hours.getText().toString());
				int minute = Integer.parseInt(minutes.getText().toString());
				if(smartAlarm.getAlarmsHours().contains(hour) && smartAlarm.getAlarmsMinutes().contains(minute)) {
					alertAlarmInDouble();
				}
				else
				{
					String title = editTitle.getText().toString();
					smartAlarm.setNewAlarm(time, title, hour,
							minute, list_tone.getSelectedItemPosition());
					smartAlarm.setAlarmManager(smartAlarm.getAlarmsMinutes().size() - 1,
							list_tone.getSelectedItem().toString(), title);
					DialogAdd.this.dismiss();
				}
			}
		});
	}
}
