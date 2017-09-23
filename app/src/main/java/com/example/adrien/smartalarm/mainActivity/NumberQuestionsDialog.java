package com.example.adrien.smartalarm.mainActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.adrien.smartalarm.R;
import com.example.adrien.smartalarm.sqliteService.AbstractQuestionBaseDAO;
import com.example.adrien.smartalarm.sqliteService.CinemaDAO;
import com.example.adrien.smartalarm.sqliteService.GeographyDAO;
import com.example.adrien.smartalarm.sqliteService.HistoryDAO;
import com.example.adrien.smartalarm.sqliteService.MusicDAO;
import com.example.adrien.smartalarm.sqliteService.SportsDAO;

import java.util.Arrays;
import java.util.List;

public class NumberQuestionsDialog extends Dialog {

	private SmartAlarm smartAlarm;
	private boolean isLongPressed;
	private boolean isShortPressed;
	private EditText numberOfQuestions = null;
	private Handler redArrowForShortTime;
	private int numberMaximumOfQuestions;

	public NumberQuestionsDialog(@NonNull Context context, SmartAlarm smartAlarm) {
		super(context);
		this.smartAlarm = smartAlarm;
	}

	protected void onCreate(Bundle savedInstance) {
		setContentView(R.layout.dialog_number_of_question);
		super.onCreate(savedInstance);
		setCanceledOnTouchOutside(false);
		determinateNumberMaximumOfQuestions();

		numberOfQuestions = (EditText) findViewById(R.id.number_of_questions);
		int currentNumberOfQuestion = PreferenceManager.getDefaultSharedPreferences(smartAlarm).getInt(SmartAlarm.NUMBER_OF_QUESTIONS,1);
		System.out.println("nombre de question: "+currentNumberOfQuestion);
		System.out.println("nombre de question maximum: "+numberMaximumOfQuestions);

		if(currentNumberOfQuestion<numberMaximumOfQuestions) {
			numberOfQuestions.setText(String.valueOf(currentNumberOfQuestion));
		}
		else
		{
			numberOfQuestions.setText(String.valueOf(numberMaximumOfQuestions));
		}
		numberOfQuestions.addTextChangedListener(new TextWatcherTime(numberOfQuestions));

		redArrowForShortTime = new Handler(Looper.getMainLooper());

		prepareArrows((ImageView) findViewById(R.id.arrow_up1), numberOfQuestions, R.drawable.ic_arrow_up,
				R.drawable.ic_arrow_up_red, true);
		prepareArrows((ImageView) findViewById(R.id.arrow_down1), numberOfQuestions, R.drawable.ic_arrow_down,
				R.drawable.ic_arrow_down_red, false);

		findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				checkBeforeSave();
				smartAlarm.setNumberOfQuestions(Integer.parseInt(numberOfQuestions.getText().toString()));
				dismiss();
			}
		});
	}

	private void determinateNumberMaximumOfQuestions() {
		String level = PreferenceManager.getDefaultSharedPreferences(smartAlarm).getString(SmartAlarm.LEVEL,smartAlarm.getResources().getString(R.string.easy));
		String category = PreferenceManager.getDefaultSharedPreferences(smartAlarm).getString(SmartAlarm.CATEGORY,"");
		if(category.equals(smartAlarm.getResources().getString(R.string.category_cinema))){
			CinemaDAO cinemaDAO = new CinemaDAO(smartAlarm);
			cinemaDAO.open();
			numberMaximumOfQuestions = cinemaDAO.getNumberOfQuestions(level);
			cinemaDAO.close();
		}
		else if(category.equals(smartAlarm.getResources().getString(R.string.category_geography))) {
			GeographyDAO geographyDAO = new GeographyDAO(smartAlarm);
			geographyDAO.open();
			numberMaximumOfQuestions = geographyDAO.getNumberOfQuestions(level);
			geographyDAO.close();
		}
		else if(category.equals(smartAlarm.getResources().getString(R.string.category_history))) {
			HistoryDAO historyDAO = new HistoryDAO(smartAlarm);
			historyDAO.open();
			numberMaximumOfQuestions = historyDAO.getNumberOfQuestions(level);
			historyDAO.close();
		}
		else if(category.equals(smartAlarm.getResources().getString(R.string.category_music))) {
			MusicDAO musicDAO = new MusicDAO(smartAlarm);
			musicDAO.open();
			numberMaximumOfQuestions = musicDAO.getNumberOfQuestions(level);
			musicDAO.close();
		}
		else if(category.equals(smartAlarm.getResources().getString(R.string.category_sports))) {
			SportsDAO sportsDAO = new SportsDAO(smartAlarm);
			sportsDAO.open();
			numberMaximumOfQuestions = sportsDAO.getNumberOfQuestions(level);
			System.out.println("NOMBRE MAXIMUM IS:"+numberMaximumOfQuestions);
			sportsDAO.close();
		}
		else {
			List<? extends AbstractQuestionBaseDAO> abstractDAOList = Arrays.asList(new CinemaDAO(smartAlarm),
					new GeographyDAO(smartAlarm), new HistoryDAO(smartAlarm), new MusicDAO(smartAlarm),
					new SportsDAO(smartAlarm));
			abstractDAOList.get(0).open();
			numberMaximumOfQuestions = abstractDAOList.get(0).getNumberOfQuestions(level);
			abstractDAOList.get(0).close();
			for (int i = 1; i < abstractDAOList.size(); i++) {
				abstractDAOList.get(i).open();
				int maximumOfNewAbstractDAO = abstractDAOList.get(i).getNumberOfQuestions(level);
				if (maximumOfNewAbstractDAO < numberMaximumOfQuestions) {
					numberMaximumOfQuestions = maximumOfNewAbstractDAO;
				}
				abstractDAOList.get(i).close();
			}
		}
	}

	private void checkBeforeSave() {
		if (numberOfQuestions.getText().toString().isEmpty()) {
			numberOfQuestions.setText("1");
		}
	}

	private void prepareArrows(final ImageView arrow, final EditText editTextTime, final int arrowDrawable,
			final int arrowRedDrawable, final boolean isAdded) {
		arrow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isShortPressed) {
					arrow.setImageResource(arrowRedDrawable);
					int number;
					if (isAdded) {
						if (Integer.parseInt(editTextTime.getText().toString()) < numberMaximumOfQuestions) {
							number = Integer.parseInt(editTextTime.getText().toString()) + 1;
						} else {
							number = 1;
						}
					} else {
						if (Integer.parseInt(editTextTime.getText().toString()) > 1) {
							number = Integer.parseInt(editTextTime.getText().toString()) - 1;
						} else {
							number = numberMaximumOfQuestions;
						}
					}
					editTextTime.setText(String.valueOf(number));
					redArrowForShortTime.postDelayed(new redArrowRunnable(arrow, arrowDrawable), 150);
				}
			}
		});

		arrow.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				isShortPressed = false;
				arrow.setImageResource(arrowRedDrawable);
				Thread longClickThread = new Thread(new Runnable() {
					@Override
					public void run() {
						while (isLongPressed) {
							smartAlarm.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									int number;
									if (isAdded) {
										if (Integer.parseInt(
												editTextTime.getText().toString()) < numberMaximumOfQuestions) {
											number = Integer.parseInt(editTextTime.getText().toString()) + 1;
										} else {
											number = 1;
										}
									} else {
										if (Integer.parseInt(editTextTime.getText().toString()) > 1) {
											number = Integer.parseInt(editTextTime.getText().toString()) - 1;
										} else {
											number = numberMaximumOfQuestions;
										}
									}
									editTextTime.setText(String.valueOf(number));
								}
							});
							try {
								Thread.sleep(200);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				});
				longClickThread.start();
				return false;
			}
		});
		arrow.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN :
						isLongPressed = true;
						isShortPressed = true;
						break;
					case MotionEvent.ACTION_UP :
						isLongPressed = false;
						redArrowForShortTime.post(new redArrowRunnable(arrow, arrowDrawable));
						break;
					default:
						break;
				}
				return false;
			}
		});
	}

	private static class redArrowRunnable implements Runnable {

		ImageView imageView;
		int drawableArrow;

		private redArrowRunnable(ImageView imageView, int drawableArrow) {
			this.imageView = imageView;
			this.drawableArrow = drawableArrow;
		}

		@Override
		public void run() {
			imageView.setImageResource(drawableArrow);
		}
	}

	private class TextWatcherTime implements TextWatcher {
		;
		private EditText editText;
		private String beforeChange;
		private boolean canModifyText = true;

		private TextWatcherTime(EditText editText) {
			this.editText = editText;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			beforeChange = s.toString();
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}

		@Override
		public void afterTextChanged(Editable s) {
			if (!s.toString().isEmpty() && canModifyText) {
				System.out.println("HERE: " + s.toString() + " END");
				canModifyText = false;
				if (s.toString().substring(0, 1).equals("0")) {
					editText.setText(beforeChange);
				} else if (Integer.parseInt(s.toString()) > numberMaximumOfQuestions) {
					if (!beforeChange.isEmpty()) {
						editText.setText(beforeChange);
					} else {
						editText.setText("1");
					}
				}
				canModifyText = true;
			}
		}
	}

	@Override
	public void onStop()
	{
		System.out.println("FINISHED!!!!!!");
	}
}
