package com.example.adrien.smartalarm.mainActivity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.View;
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

	public DialogRemove(@NonNull Context context, SmartAlarm main_activity, int position, String hour, String minute,
			String title, int soundSelected) {
		super(context, main_activity);
		this.hour = hour;
		this.minute = minute;
		this.title = title;
		this.position = position;
		this.soundSelected = soundSelected;
	}

	@Override
	protected void onCreate(Bundle savedInstance) {
		setContentView(R.layout.dialog_remove);
		super.onCreate(savedInstance);

		editTitle.setText(title);
		list_tone.setSelection(soundSelected);
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
					if (i != position) {
						alarmsTimeList.add(String.valueOf(smartAlarm.getAlarmsHours().get(i))
								+ String.valueOf(smartAlarm.getAlarmsMinutes().get(i)));
					}
				}
				if (alarmsTimeList.contains(String.valueOf(hour) + String.valueOf(minute))) {
					alertAlarmInDouble();
				} else {
					String title = editTitle.getText().toString();
					soundSelected = list_tone.getSelectedItemPosition();
					smartAlarm.changeAlarm(time, title, position, Integer.parseInt(hours.getText().toString()),
							Integer.parseInt(minutes.getText().toString()), soundSelected);
					if (smartAlarm.getAlarmsActivated().get(position)) {
						smartAlarm.setAlarmManager(position, list_tone.getSelectedItem().toString(), title);
					}
					DialogRemove.this.dismiss();
				}
			}
		});

		Button remove = (Button) findViewById(R.id.remove);
		remove.setOnClickListener(new View.OnClickListener() {
			@RequiresApi(api = Build.VERSION_CODES.KITKAT)
			@Override
			public void onClick(View v) {
				smartAlarm.removeAlarmManager(position);
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
