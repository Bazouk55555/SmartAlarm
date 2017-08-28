package com.example.adrien.smartalarm.mainActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.adrien.smartalarm.R;

public class DialogAddSound extends Dialog {

	public static final int AUTHORIZATION_SOUND = 2;
	private Button cancel = null;
	private Button ok = null;
	private Button lookForSound = null;
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
		setContentView(R.layout.dialog_add_sound);
		setCanceledOnTouchOutside(false);

		cancel = (Button) findViewById(R.id.cancel);
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				uriDialog = null;
				DialogAddSound.this.dismiss();
			}
		});

		ok = (Button) findViewById(R.id.ok);
		ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				smartAlarm.setUriSound(uriDialog);
				if (uriDialog != null) {
					smartAlarm.getTakeOffSoundMenuItem().setEnabled(true);
				}
				smartAlarm.setIsAlarmSix(false);
				smartAlarm.getDialogAddImage().getAlarms().add("alarm6");
				DialogAddSound.this.dismiss();
			}
		});

		lookForSound = (Button) findViewById(R.id.button_sound);
		lookForSound.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				smartAlarm.startActivityForResult(
						new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.INTERNAL_CONTENT_URI),
						AUTHORIZATION_SOUND);
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
