package com.example.adrien.smartalarm.smartalarm;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.adrien.smartalarm.R;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDialogAddOrRemove extends Dialog {
	protected SmartAlarm smartAlarm;
	protected Spinner listTone = null;
	protected Button save = null;
	protected EditText hours = null;
	protected EditText minutes = null;
	protected EditText editTitle = null;
	private List<String> alarms = new ArrayList<>();
	private Handler redArrowForShortTime;
	private boolean isLongPressed;
	private boolean isShortPressed;

	public AbstractDialogAddOrRemove(@NonNull Context context, SmartAlarm smartAlarm) {
		super(context);
		this.smartAlarm = smartAlarm;
		alarms.add(smartAlarm.getResources().getString(R.string.alarm1));
		alarms.add(smartAlarm.getResources().getString(R.string.alarm2));
		alarms.add(smartAlarm.getResources().getString(R.string.alarm3));
		alarms.add(smartAlarm.getResources().getString(R.string.alarm4));
		alarms.add(smartAlarm.getResources().getString(R.string.alarm5));
	}

	@Override
	protected void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		setCanceledOnTouchOutside(false);

		listTone = (Spinner) findViewById(R.id.list_tone);
		ArrayAdapter<String> adapter = new ArrayAdapter<>(smartAlarm, android.R.layout.simple_spinner_dropdown_item,
				alarms);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		listTone.setAdapter(adapter);

		Button cancel = (Button) findViewById(R.id.cancel);
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AbstractDialogAddOrRemove.this.dismiss();
			}
		});

		editTitle = (EditText) findViewById(R.id.editTitle);

		hours = (EditText) findViewById(R.id.hours);
		hours.addTextChangedListener(new TextWatcherTime(2, 3, hours));
		hours.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					String hourModified = ((EditText) v).getText().toString();
					if (hourModified.isEmpty()) {
						((EditText) v).setText(smartAlarm.getResources().getString(R.string.double_zero));
					} else if (hourModified.length() != 2) {
						((EditText) v).setText(smartAlarm.getResources().getString(R.string.zero) + ((EditText) v).getText().toString());
					}
				}
			}
		});

		minutes = (EditText) findViewById(R.id.minutes);
		minutes.addTextChangedListener(new TextWatcherTime(6, -1, minutes));
		minutes.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					String minuteModified = ((EditText) v).getText().toString();
					if (minuteModified.isEmpty()) {
						((EditText) v).setText(smartAlarm.getResources().getString(R.string.double_zero));
					} else if (minuteModified.length() != 2) {
						((EditText) v).setText(smartAlarm.getResources().getString(R.string.zero) + ((EditText) v).getText().toString());
					}
				}
			}
		});

		redArrowForShortTime = new Handler(Looper.getMainLooper());

		prepareArrows((ImageView) findViewById(R.id.arrow_up1), hours, R.drawable.ic_arrow_up,
				R.drawable.ic_arrow_up_red, true);
		prepareArrows((ImageView) findViewById(R.id.arrow_up2), minutes, R.drawable.ic_arrow_up,
				R.drawable.ic_arrow_up_red, true);
		prepareArrows((ImageView) findViewById(R.id.arrow_down1), hours, R.drawable.ic_arrow_down,
				R.drawable.ic_arrow_down_red, false);
		prepareArrows((ImageView) findViewById(R.id.arrow_down2), minutes, R.drawable.ic_arrow_down,
				R.drawable.ic_arrow_down_red, false);
	}

	private void prepareArrows(final ImageView arrow, final EditText editTextTime, final int arrowDrawable,
			final int arrowRedDrawable, final boolean isAdded) {
		arrow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isShortPressed) {
					arrow.setImageResource(arrowRedDrawable);
					int number;
					if (isAdded && editTextTime == hours) {
						number = (Integer.parseInt(editTextTime.getText().toString()) + 1) % 24;
					} else if (!isAdded && editTextTime == hours) {
						number = ((Integer.parseInt(editTextTime.getText().toString()) - 1) + 24) % 24;
					} else if (isAdded && editTextTime == minutes) {
						number = (Integer.parseInt(editTextTime.getText().toString()) + 1) % 60;
					} else {
						number = ((Integer.parseInt(editTextTime.getText().toString()) - 1) + 60) % 60;
					}
					String textNumber = String.valueOf(number);
					if (number > -1 && number < 10) {
						textNumber = "0" + textNumber;
					}
					editTextTime.setText(textNumber);
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
									if (isAdded && editTextTime == hours) {
										number = (Integer.parseInt(editTextTime.getText().toString()) + 1) % 24;
									} else if (!isAdded && editTextTime == hours) {
										number = ((Integer.parseInt(editTextTime.getText().toString()) - 1) + 24) % 24;
									} else if (isAdded && editTextTime == minutes) {
										number = (Integer.parseInt(editTextTime.getText().toString()) + 1) % 60;
									} else {
										number = ((Integer.parseInt(editTextTime.getText().toString()) - 1) + 60) % 60;
									}
									String textNumber = String.valueOf(number);
									if (number > -1 && number < 10) {
										textNumber = "0" + textNumber;
									}
									editTextTime.setText(textNumber);
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
					default :
						break;
				}
				return false;
			}
		});
	}

	List<String> getAlarms() {
		return alarms;
	}

	String getTimeBeforeSave() {
		checkNonEmptyBeforeSave();
		String hour = hours.getText().toString();
		String minute = minutes.getText().toString();
		if (hour.length() == 1) {
			hour = "0" + hour;
		}
		if (minute.length() == 1) {
			minute = "0" + minute;
		}
		return hour + ":" + minute;
	}

	private void checkNonEmptyBeforeSave()
	{
		if (hours.getText().toString().isEmpty()) {
			hours.setText(smartAlarm.getResources().getString(R.string.double_zero));
		}
		if (minutes.getText().toString().isEmpty()) {
			minutes.setText(smartAlarm.getResources().getString(R.string.double_zero));
		}
	}

	protected void alertAlarmInDouble()
	{
		AlertDialog.Builder builder;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			builder = new AlertDialog.Builder(smartAlarm, android.R.style.Theme_Material_Dialog_Alert);
		} else {
			builder = new AlertDialog.Builder(smartAlarm);
		}
		builder.setTitle(smartAlarm.getResources().getString(R.string.alarm_already_set_title)).setMessage(smartAlarm.getResources().getString(R.string.alarm_already_set_message))
				.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				}).setIcon(android.R.drawable.ic_dialog_alert).show();
	}

	private class TextWatcherTime implements TextWatcher {
		private int firstLimitTime;
		private int secondLimitTime;
		private EditText editText;
		private boolean canModifyText = true;
		private int start;

		private TextWatcherTime(int firstLimitTime, int secondLimitTime, EditText editText) {
			this.firstLimitTime = firstLimitTime;
			this.secondLimitTime = secondLimitTime;
			this.editText = editText;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			this.start = start;
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}

		@Override
		public void afterTextChanged(Editable s) {
			if (canModifyText) {
				if (s.toString().length() == 3) {
					if (start == 2) {
						canModifyText = false;
						editText.setText(s.toString().substring(0, 2));
						canModifyText = true;
					} else if (start == 1) {
						if (Character.getNumericValue(s.toString().charAt(0)) > firstLimitTime
								|| (Character.getNumericValue(s.toString().charAt(0)) == firstLimitTime
										&& Character.getNumericValue(s.toString().charAt(start)) > secondLimitTime)) {
							editText.setText(s.toString().substring(0, 1) + s.toString().substring(2, 3));
						} else {
							editText.setText(s.toString().substring(0, 2));
						}
					} else if (start == 0) {
						if (Character.getNumericValue(s.toString().charAt(start)) > firstLimitTime
								|| (Character.getNumericValue(s.toString().charAt(start)) == firstLimitTime
										&& Character.getNumericValue(s.toString().charAt(1)) > secondLimitTime)) {
							editText.setText(s.toString().substring(1, 3));
						} else {
							editText.setText(s.toString().substring(0, 2));
						}
					}
				}
				if (s.toString().length() == 2) {
					canModifyText = false;
					if (start == 0) {
						if (Character.getNumericValue(s.toString().charAt(start)) > firstLimitTime
								|| (Character.getNumericValue(s.toString().charAt(start)) == firstLimitTime
										&& Character.getNumericValue(s.toString().charAt(1)) > secondLimitTime)) {
							editText.setText(s.toString().substring(1, 2));
						}
					}
					if (start == 1) {
						boolean goToNextStep = true;
						if (Character.getNumericValue(s.toString().charAt(0)) > firstLimitTime
								|| (Character.getNumericValue(s.toString().charAt(0)) == firstLimitTime
										&& Character.getNumericValue(s.toString().charAt(start)) > secondLimitTime)) {
							editText.setText(s.toString().substring(0, 1));
							goToNextStep = false;
						}
						if (goToNextStep) {
							if (editText == hours) {
								minutes.requestFocus();
							}
							else if(editText == minutes)
							{
								minutes.clearFocus();
								InputMethodManager inputManager = (InputMethodManager) smartAlarm.getSystemService(Context.INPUT_METHOD_SERVICE);
								inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
										InputMethodManager.HIDE_NOT_ALWAYS);
							}
						}
					}
					canModifyText = true;
				}
			}
		}
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
}
