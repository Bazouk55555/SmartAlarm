package com.example.adrien.smartalarm.AfterAlarmRing;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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

    public static final int CODE_DIALOG_BACK =0;
    private TextView questionTextView = null;
    private RadioGroup answerRadioGroup = null;
    private Button oKButton = null;
    private MediaPlayer mediaPlayer;
    private List<Question> questions;
    private AlarmRing alarmRing;
    private int numberQuestion;
    private int numberRightQuestion=0;

    public DialogNewGame(@NonNull Context context, List<Question> questions, MediaPlayer mediaPlayer, AlarmRing alarmRing) {
        super(context);
        setContentView(R.layout.dialog_start_game);

        this.questionTextView = (TextView) findViewById(R.id.question);
        this.questionTextView.setText(questions.get(0).getQuestion());
        this.questions = questions;
        this.mediaPlayer = mediaPlayer;
        this.alarmRing = alarmRing;
        numberQuestion=0;
    }

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setCanceledOnTouchOutside(false);

        updateQuestion(0);
        oKButton = (Button) findViewById(R.id.ok);
        oKButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread answeringQuestionsThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        RadioButton radioButtonClicked = (RadioButton)findViewById(answerRadioGroup.getCheckedRadioButtonId());
                        if(numberQuestion<alarmRing.getNumberOfQuestions()) {
                            Boolean hasWon;
                            if (radioButtonClicked.getText().equals(questions.get(numberQuestion).getAnswer()))
                            {
                                numberRightQuestion++;
                                hasWon=true;
                            }
                            else
                            {
                                hasWon=false;
                            }
                            dismiss();
                            numberQuestion++;
                            Intent intentWin= new Intent(alarmRing, ImagesGame.class);
                            intentWin.putExtra("score",(int)(numberRightQuestion*100/numberQuestion));
                            if(numberQuestion<alarmRing.getNumberOfQuestions()) {
                                alarmRing.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        updateQuestion(numberQuestion);
                                    }
                                });
                                intentWin.putExtra("hasWon",hasWon);
                                alarmRing.startActivityForResult(intentWin, CODE_DIALOG_BACK);
                            }
                            else
                            {
                                mediaPlayer.stop();
                                DialogNewGame.this.dismiss();
                                intentWin.putExtra("final",true);
                                alarmRing.startActivityForResult(intentWin, CODE_DIALOG_BACK);
                                alarmRing.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        alarmRing.onBackPressed();
                                    }
                                });
                            }
                        }
                    }
                });
                answeringQuestionsThread.start();
            }
        });
    }

    private void updateQuestion(int numberQuestion)
    {
        questionTextView.setText(questions.get(numberQuestion).getQuestion());
        List<Integer> randomNumberList= new ArrayList<>();
        randomNumberList.add(0);
        randomNumberList.add(1);
        randomNumberList.add(2);
        randomNumberList.add(3);
        answerRadioGroup = (RadioGroup) findViewById(R.id.answer_radio_group);
        int randomNumber = new Random().nextInt(4);
        ((RadioButton) answerRadioGroup.getChildAt(randomNumberList.get(randomNumber))).setText(questions.get(numberQuestion).getAnswer());
        randomNumberList.remove(randomNumber);
        randomNumber = new Random().nextInt(3);
        ((RadioButton) answerRadioGroup.getChildAt(randomNumberList.get(randomNumber))).setText(questions.get(numberQuestion).getWrongAnswer1());
        randomNumberList.remove(randomNumber);
        randomNumber = new Random().nextInt(2);
        ((RadioButton) answerRadioGroup.getChildAt(randomNumberList.get(randomNumber))).setText(questions.get(numberQuestion).getWrongAnswer2());
        randomNumberList.remove(randomNumber);
        ((RadioButton) answerRadioGroup.getChildAt(randomNumberList.get(0))).setText(questions.get(numberQuestion).getWrongAnswer3());
    }

    @Override
    public void onBackPressed()
    {
    }
}
