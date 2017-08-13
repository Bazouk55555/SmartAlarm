package com.example.adrien.smartalarm.AfterAlarmRing;

import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.adrien.smartalarm.R;
import com.example.adrien.smartalarm.SQliteService.Question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DialogNewGame extends Dialog {

    private TextView questionTextView = null;
    private RadioGroup answerRadioGroup = null;
    private Button oKButton = null;
    private MediaPlayer mediaPlayer;
    private Question question;
    private AlarmRing alarmRing;

    public DialogNewGame(@NonNull Context context, Question question, MediaPlayer mediaPlayer, AlarmRing alarmRing) {
        super(context);
        setContentView(R.layout.dialog_start_game);

        this.questionTextView = (TextView) findViewById(R.id.question);
        this.questionTextView.setText(question.getQuestion());
        this.question = question;
        this.mediaPlayer = mediaPlayer;
        this.alarmRing = alarmRing;
    }

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        this.setCanceledOnTouchOutside(false);

        List<Integer> randomNumberList = new ArrayList<>();
        randomNumberList.add(0);
        randomNumberList.add(1);
        randomNumberList.add(2);
        randomNumberList.add(3);
        answerRadioGroup = (RadioGroup) findViewById(R.id.answer_radio_group);
        int randomNumber = new Random().nextInt(4);
        ((RadioButton) answerRadioGroup.getChildAt(randomNumberList.get(randomNumber))).setText(question.getAnswer());
        randomNumberList.remove(randomNumber);
        randomNumber = new Random().nextInt(3);
        ((RadioButton) answerRadioGroup.getChildAt(randomNumberList.get(randomNumber))).setText(question.getWrongAnswer1());
        randomNumberList.remove(randomNumber);
        randomNumber = new Random().nextInt(2);
        ((RadioButton) answerRadioGroup.getChildAt(randomNumberList.get(randomNumber))).setText(question.getWrongAnswer2());
        randomNumberList.remove(randomNumber);
        ((RadioButton) answerRadioGroup.getChildAt(randomNumberList.get(0))).setText(question.getWrongAnswer3());

        oKButton = (Button) findViewById(R.id.ok);
        oKButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton radioButtonClicked = (RadioButton)findViewById(answerRadioGroup.getCheckedRadioButtonId());
                if(radioButtonClicked.getText().equals(question.getAnswer()))
                {
                    mediaPlayer.stop();
                    DialogNewGame.this.dismiss();
                    alarmRing.onBackPressed();
                }
            }
        });
    }
}
