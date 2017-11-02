package com.example.adrien.smartalarm.smartalarm;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.adrien.smartalarm.R;

import java.util.ArrayList;
import java.util.List;

public class DialogAdd extends AbstractDialogAddOrRemove {

	public DialogAdd(@NonNull Context context, SmartAlarm smartAlarm) {
		super(context, smartAlarm);
	}

	@Override
	protected void onCreate(Bundle savedInstance) {

		requestWindowFeature(Window.FEATURE_NO_TITLE);
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
				List<String> alarmsTimeList = new ArrayList<>();
				for (int i = 0; i < smartAlarm.getAlarmsHours().size(); i++) {
					alarmsTimeList.add(String.valueOf(smartAlarm.getAlarmsHours().get(i))
							+ String.valueOf(smartAlarm.getAlarmsMinutes().get(i)));
				}
				if (alarmsTimeList.contains(String.valueOf(hour) + String.valueOf(minute))) {
					alertAlarmInDouble();
				}
				else
				{
					String title = editTitle.getText().toString();
					int newPosition = smartAlarm.getPositionNewAlarm(hour,minute);
					smartAlarm.setNewAlarm(newPosition,time, title, hour,
							minute, listTone.getSelectedItemPosition(),true);
					smartAlarm.setAlarmManager(newPosition,
							listTone.getSelectedItem().toString(), title);
					DialogAdd.this.dismiss();
				}
			}
		});
	}
}
