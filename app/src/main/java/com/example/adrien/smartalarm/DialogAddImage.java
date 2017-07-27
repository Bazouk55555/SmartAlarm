package com.example.adrien.smartalarm;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

public class DialogAddImage extends Dialog {

    public static int AUTHORIZATION_IMAGE=1;
    private Button cancel = null;
    private Button ok = null;
    private Button lookForImage = null;
    private SmartAlarm smartAlarm;
    private Uri uriDialog;

    public DialogAddImage(@NonNull Context context, SmartAlarm smartAlarm) {
        super(context);
        this.smartAlarm=smartAlarm;
    }

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.dialog_add_image);

        cancel = (Button)findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uriDialog=null;
                DialogAddImage.this.dismiss();
            }
        });

        ok = (Button)findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smartAlarm.setUriImage(uriDialog);
                if(uriDialog!=null) {
                    smartAlarm.getTakeOffImageMenuItem().setEnabled(true);
                }
                DialogAddImage.this.dismiss();
            }
        });

        lookForImage = (Button) findViewById(R.id.button_image);
        lookForImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                smartAlarm.startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI),AUTHORIZATION_IMAGE);
            }
        });
    }

    public void setUriDialog(Uri uriDialog)
    {
        this.uriDialog=uriDialog;
    }
}
