package com.application.bazouk.smartalarm.mainactivity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.application.bazouk.smartalarm.R;

public class DialogAddSound extends Dialog {

	private SmartAlarm smartAlarm;
	private Uri uriDialog;

	public DialogAddSound(@NonNull Context context, SmartAlarm smartAlarm) {
		super(context);
		this.smartAlarm = smartAlarm;
		uriDialog = null;
	}

	@Override
	protected void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_add_sound);
		setCanceledOnTouchOutside(false);

		Button cancel = (Button) findViewById(R.id.cancel);
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				uriDialog = null;
				DialogAddSound.this.dismiss();
			}
		});

		Button ok = (Button) findViewById(R.id.ok);
		ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				smartAlarm.setUriSound(uriDialog);
				if (uriDialog != null) {
					smartAlarm.getTakeOffSoundMenuItem().setEnabled(true);
				}
				smartAlarm.setIsAlarmSix(true);
				smartAlarm.getDialogAdd().getAlarms().add("alarm6");
				DialogAddSound.this.dismiss();
			}
		});

		Button lookForSound = (Button) findViewById(R.id.button_sound);
		lookForSound.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
					ActivityCompat.requestPermissions(smartAlarm,
							new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, SmartAlarm.AUTHORIZATION_SOUND);
				} else {
					ActivityCompat.requestPermissions(smartAlarm, new String[]{}, SmartAlarm.AUTHORIZATION_SOUND);
				}
			}
		});
	}

	public void setUriDialog(Uri uriDialog) {
		this.uriDialog = uriDialog;
	}

	public Uri getUriDialog() {
		return uriDialog;
	}

	public void setSoundOk() {
		if (uriDialog != null) {
			((ImageView) findViewById(R.id.sound_loaded)).setImageResource(R.drawable.ic_loaded);
		}
	}
}
