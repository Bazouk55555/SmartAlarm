package com.example.adrien.smartalarm.mainActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.adrien.smartalarm.R;

public class CategoryDialog extends Dialog {

	private RadioGroup categoryRadioGroup;
	private Button oKButton;
	private SmartAlarm smartAlarm;

	public CategoryDialog(@NonNull Context context, SmartAlarm smartAlarm) {
		super(context);
		this.smartAlarm = smartAlarm;
	}

	protected void onCreate(Bundle savedInstance) {
		setContentView(R.layout.dialog_category_question);
		super.onCreate(savedInstance);
		setCanceledOnTouchOutside(false);

		oKButton = (Button) findViewById(R.id.ok);
		categoryRadioGroup = (RadioGroup) findViewById(R.id.category_radio_group);
		switch(smartAlarm.getCategory())
		{
			case "Cinema":
				categoryRadioGroup.check(R.id.category1);
				break;
			case "Geography":
				categoryRadioGroup.check(R.id.category2);
				break;
			case "History":
				categoryRadioGroup.check(R.id.category3);
				break;
			case "Music":
				categoryRadioGroup.check(R.id.category4);
				break;
			case "Sports":
				categoryRadioGroup.check(R.id.category5);
				break;
			default:
				categoryRadioGroup.check(R.id.category6);
				break;
		}
		oKButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int radioButtonSelectedId = categoryRadioGroup.getCheckedRadioButtonId();
				smartAlarm.setCategory(((RadioButton) findViewById(radioButtonSelectedId)).getText().toString());
				dismiss();
			}
		});
	}
}
