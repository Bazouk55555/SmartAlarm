package com.example.adrien.smartalarm.adrien;

import com.example.adrien.smartalarm.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class LevelDialog extends Dialog {

	private RadioGroup levelRadioGroup;
	private SmartAlarm smartAlarm;

	public LevelDialog(@NonNull Context context, SmartAlarm smartAlarm) {
		super(context);
		this.smartAlarm = smartAlarm;
	}

	protected void onCreate(Bundle savedInstance) {

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_level_question);
		super.onCreate(savedInstance);
		setCanceledOnTouchOutside(false);

		Button oKButton = (Button) findViewById(R.id.ok);
		levelRadioGroup = (RadioGroup) findViewById(R.id.level_radio_group);
		String level = PreferenceManager.getDefaultSharedPreferences(smartAlarm).getString(SmartAlarm.LEVEL,smartAlarm.getResources().getString(R.string.easy));
		if(level.equals(smartAlarm.getResources().getString(R.string.easy))) {
			levelRadioGroup.check(R.id.easy);
		}
		else if(level.equals(smartAlarm.getResources().getString(R.string.medium))) {
			levelRadioGroup.check(R.id.medium);
		}
		else if(level.equals(smartAlarm.getResources().getString(R.string.hard))) {
			levelRadioGroup.check(R.id.hard);
		}
		oKButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int radioButtonSelectedId = levelRadioGroup.getCheckedRadioButtonId();
				smartAlarm.setLevel(((RadioButton) findViewById(radioButtonSelectedId)).getText().toString());
				dismiss();
			}
		});
	}
}
