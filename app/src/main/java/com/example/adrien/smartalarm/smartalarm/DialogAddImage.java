package com.example.adrien.smartalarm.smartalarm;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.example.adrien.smartalarm.R;

public class DialogAddImage extends Dialog {

	private SmartAlarm smartAlarm;
	private Uri uriDialog;

	public DialogAddImage(@NonNull Context context, SmartAlarm smartAlarm) {
		super(context);
		this.smartAlarm = smartAlarm;
	}

	@Override
	protected void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_add_image);
		setCanceledOnTouchOutside(false);

		Button cancel = (Button) findViewById(R.id.cancel);
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				uriDialog = null;
				DialogAddImage.this.dismiss();
			}
		});

		Button ok = (Button) findViewById(R.id.ok);
		ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				smartAlarm.setUriImage(uriDialog);
				if (uriDialog != null) {
					smartAlarm.getTakeOffImageMenuItem().setEnabled(true);
				}
				smartAlarm.setPicture(true);
				DialogAddImage.this.dismiss();
			}
		});

		Button lookForImage = (Button) findViewById(R.id.button_image);
		lookForImage.setOnClickListener(new View.OnClickListener() {
			@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
			@Override
			public void onClick(View v) {
				ActivityCompat.requestPermissions(smartAlarm, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
						SmartAlarm.AUTHORIZATION_IMAGE);
			}
		});
	}

	public void setUriDialog(Uri uriDialog) {
		this.uriDialog = uriDialog;
	}

	public Uri getUriDialog() {
		return uriDialog;
	}

	public void setImageOk() {
		if (uriDialog != null) {
			((ImageView) findViewById(R.id.image_loaded)).setImageResource(R.drawable.ic_loaded);
		}
	}
}
