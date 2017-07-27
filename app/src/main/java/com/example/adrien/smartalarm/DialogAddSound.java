package com.example.adrien.smartalarm;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

public class DialogAddSound extends Dialog {

    private Button cancel = null;

    public DialogAddSound(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.dialog_add_sound);

        cancel = (Button)findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogAddSound.this.dismiss();
            }
        });
    }
}
