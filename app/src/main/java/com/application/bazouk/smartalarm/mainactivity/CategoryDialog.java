package com.application.bazouk.smartalarm.mainactivity;

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

import com.application.bazouk.smartalarm.R;

public class CategoryDialog extends Dialog {

	private RadioGroup categoryRadioGroup;
	private SmartAlarm smartAlarm;
	private boolean isSerieOfDialogs;

	public CategoryDialog(@NonNull Context context, SmartAlarm smartAlarm, boolean isSerieOfDialogs) {
		super(context);
		this.smartAlarm = smartAlarm;
		this.isSerieOfDialogs = isSerieOfDialogs;
	}

	protected void onCreate(Bundle savedInstance) {

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_category_question);
		super.onCreate(savedInstance);
		setCanceledOnTouchOutside(false);

		Button oKButton = (Button) findViewById(R.id.ok);
		categoryRadioGroup = (RadioGroup) findViewById(R.id.category_radio_group);
		String category = PreferenceManager.getDefaultSharedPreferences(smartAlarm).getString(SmartAlarm.CATEGORY,"");
		if(category.equals(smartAlarm.getResources().getString(R.string.category_cinema))){
			categoryRadioGroup.check(R.id.category1);
		}
		else if(category.equals(smartAlarm.getResources().getString(R.string.category_geography))) {
			categoryRadioGroup.check(R.id.category2);
		}
		else if(category.equals(smartAlarm.getResources().getString(R.string.category_history))) {
			categoryRadioGroup.check(R.id.category3);
		}
		else if(category.equals(smartAlarm.getResources().getString(R.string.category_music))) {
			categoryRadioGroup.check(R.id.category4);
		}
		else if(category.equals(smartAlarm.getResources().getString(R.string.category_sports))) {
			categoryRadioGroup.check(R.id.category5);
		}
		else {
			categoryRadioGroup.check(R.id.category6);
		}
		oKButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int radioButtonSelectedId = categoryRadioGroup.getCheckedRadioButtonId();
				smartAlarm.setCategory(((RadioButton) findViewById(radioButtonSelectedId)).getText().toString());
				dismiss();
				if(isSerieOfDialogs)
				{
					smartAlarm.chooseLevelOfQuestions(true);
				}
			}
		});
	}

	public void setSerieOfDialogs(boolean isSerieOfDialogs)
	{
		this.isSerieOfDialogs = isSerieOfDialogs;
	}
}
