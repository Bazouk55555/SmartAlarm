package com.example.adrien.smartalarm.mainActivity;

import com.example.adrien.smartalarm.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class LevelDialog extends Dialog {

	private RadioGroup levelRadioGroup;
	private Button oKButton;
	private SmartAlarm smartAlarm;

	public LevelDialog(@NonNull Context context, SmartAlarm smartAlarm) {
		super(context);
		this.smartAlarm = smartAlarm;
	}

	protected void onCreate(Bundle savedInstance) {
		setContentView(R.layout.dialog_level_question);
		super.onCreate(savedInstance);
		setCanceledOnTouchOutside(false);

		oKButton = (Button) findViewById(R.id.ok);
		levelRadioGroup = (RadioGroup) findViewById(R.id.level_radio_group);
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
