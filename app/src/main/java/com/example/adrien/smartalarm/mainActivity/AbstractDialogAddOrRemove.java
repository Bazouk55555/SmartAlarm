package com.example.adrien.smartalarm.mainActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.adrien.smartalarm.R;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDialogAddOrRemove extends Dialog {
	protected SmartAlarm main_activity;
	protected Spinner list_tone = null;
	protected Button save = null;
	protected EditText hours = null;
	protected EditText minutes = null;
	protected EditText editTitle = null;
	protected List<String> alarms = new ArrayList<String>();
	protected Handler redArrowForShortTime;
	private boolean isLongPressed;
	private boolean isShortPressed;

	public AbstractDialogAddOrRemove(@NonNull Context context, SmartAlarm main_activity) {
		super(context);
		this.main_activity = main_activity;
		alarms.add("alarm1");
		alarms.add("alarm2");
		alarms.add("alarm3");
		alarms.add("alarm4");
		alarms.add("alarm5");
	}

	@Override
	protected void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);

		setCanceledOnTouchOutside(false);
		list_tone = (Spinner) findViewById(R.id.list_tone);
		ArrayAdapter<String> adapter = new ArrayAdapter<>(main_activity, android.R.layout.simple_spinner_dropdown_item,
				alarms);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		list_tone.setAdapter(adapter);

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
						((EditText) v).setText("00");
					} else if (hourModified.length() != 2) {
						((EditText) v).setText("0" + ((EditText) v).getText().toString());
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
						((EditText) v).setText("00");
					} else if (minuteModified.length() != 2) {
						((EditText) v).setText("0" + ((EditText) v).getText().toString());
					}
				}
			}
		});

		redArrowForShortTime = new Handler(Looper.getMainLooper());

		prepareArrows((ImageView) findViewById(R.id.arrow_up1), hours, R.drawable.ic_arrow_up, R.drawable.ic_arrow_up_red, true);
		prepareArrows((ImageView) findViewById(R.id.arrow_up2), minutes, R.drawable.ic_arrow_up, R.drawable.ic_arrow_up_red, true);
		prepareArrows((ImageView) findViewById(R.id.arrow_down1), hours, R.drawable.ic_arrow_down, R.drawable.ic_arrow_down_red, false);
		prepareArrows( (ImageView) findViewById(R.id.arrow_down2), minutes, R.drawable.ic_arrow_down, R.drawable.ic_arrow_down_red, false);
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
					String text_number = String.valueOf(number);
					if (number > -1 && number < 10) {
						text_number = "0" + text_number;
					}
					editTextTime.setText(text_number);
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
							main_activity.runOnUiThread(new Runnable() {
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
									String text_number = String.valueOf(number);
									if (number > -1 && number < 10) {
										text_number = "0" + text_number;
									}
									editTextTime.setText(text_number);
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

	List<String> getAlarms() {
		return alarms;
	}

	public void checkBeforeSave() {
		if (hours.getText().toString().isEmpty()) {
			hours.setText("00");
		}
		if (minutes.getText().toString().isEmpty()) {
			minutes.setText("00");
		}
	}

	private class TextWatcherTime implements TextWatcher {
		private int firstLimitTime;
		private int secondLimitTime;
		private EditText editText;
		private boolean canModifyText = true;
		private int start;

		public TextWatcherTime(int firstLimitTime, int secondLimitTime, EditText editText) {
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
							} else if (editText == minutes) {
								findViewById(R.id.editTitle).requestFocus();
							}
						}
					}
					canModifyText = true;
				}
			}
		}
	}

	private class redArrowRunnable implements Runnable {

		ImageView imageView;
		int drawableArrow;

		public redArrowRunnable(ImageView imageView, int drawableArrow) {
			this.imageView = imageView;
			this.drawableArrow = drawableArrow;
		}

		@Override
		public void run() {
			imageView.setImageResource(drawableArrow);
		}
	}
}
