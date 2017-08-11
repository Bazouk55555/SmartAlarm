package com.example.adrien.smartalarm;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DialogNewGame extends Dialog {

    private TextView questionTextView = null;
    private EditText answerEditText = null;
    private Button oKButton = null;
    private MediaPlayer mediaPlayer;
    private String answer;
    private AlarmRing alarmRing;

    public DialogNewGame(@NonNull Context context, String question, String answer, MediaPlayer mediaPlayer, AlarmRing alarmRing) {
        super(context);
        setContentView(R.layout.dialog_start_game);

        this.questionTextView = (TextView) findViewById(R.id.question);
        this.questionTextView.setText(question);
        this.answer = answer;
        this.mediaPlayer = mediaPlayer;
        this.alarmRing = alarmRing;
    }

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        answerEditText = (EditText) findViewById(R.id.answer);
        oKButton = (Button) findViewById(R.id.ok);

        oKButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(answerEditText.getText().toString().equals(answer))
                {
                    mediaPlayer.stop();
                    DialogNewGame.this.dismiss();
                    alarmRing.onBackPressed();
                }
            }
        });
    }
}