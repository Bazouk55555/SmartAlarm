package com.example.adrien.smartalarm.SQliteService;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class AlarmBaseDAO {

	private SQLiteDatabase mDb = null;
	private DatabaseSaveAlarmHandler databaseHandler = null;

	public AlarmBaseDAO(Context pContext) {
		this.databaseHandler = new DatabaseSaveAlarmHandler(pContext);
	}

	public SQLiteDatabase open() {
		mDb = databaseHandler.getWritableDatabase();
		return mDb;
	}

	public void close() {
		mDb.close();
	}

	public void add(Alarm alarm) {
		ContentValues value = new ContentValues();
		value.put(DatabaseSaveAlarmHandler.ID_KEY, alarm.getId());
		value.put(DatabaseSaveAlarmHandler.HOUR, alarm.getHour());
		value.put(DatabaseSaveAlarmHandler.MINUTE, alarm.getMinute());
		value.put(DatabaseSaveAlarmHandler.TIME, alarm.getTime());
		value.put(DatabaseSaveAlarmHandler.TITLE, alarm.getTitle());
		value.put(DatabaseSaveAlarmHandler.SOUND, alarm.getSound());
		value.put(DatabaseSaveAlarmHandler.ACTIVATED, (alarm.getActivated()) ? 1 : 0);
		mDb.insert(DatabaseSaveAlarmHandler.TABLE_ALARMS_NAME, null, value);
	}

	public void remove(long id, int size) {
		mDb.delete(DatabaseSaveAlarmHandler.TABLE_ALARMS_NAME, DatabaseSaveAlarmHandler.ID_KEY + " = ?",
				new String[]{String.valueOf(id)});
		for (int i = 0; i < size; i++) {
			updateId(id + 1 + i);
		}
	}

	private void updateId(long id) {
		ContentValues value = new ContentValues();
		value.put(DatabaseSaveAlarmHandler.ID_KEY, id - 1);
		mDb.update(DatabaseSaveAlarmHandler.TABLE_ALARMS_NAME, value, DatabaseSaveAlarmHandler.ID_KEY + " = ?",
				new String[]{String.valueOf(id)});
	}

	public void update(Alarm alarm) {

		ContentValues value = new ContentValues();
		value.put(DatabaseSaveAlarmHandler.HOUR, alarm.getHour());
		value.put(DatabaseSaveAlarmHandler.MINUTE, alarm.getMinute());
		value.put(DatabaseSaveAlarmHandler.TIME, alarm.getTime());
		value.put(DatabaseSaveAlarmHandler.TITLE, alarm.getTitle());
		value.put(DatabaseSaveAlarmHandler.SOUND, alarm.getSound());
		mDb.update(DatabaseSaveAlarmHandler.TABLE_ALARMS_NAME, value, DatabaseSaveAlarmHandler.ID_KEY + " = ?",
				new String[]{String.valueOf(alarm.getId())});
	}

	public void updateActivation(int position, boolean isActivated)
	{
		ContentValues value = new ContentValues();
		value.put(DatabaseSaveAlarmHandler.ACTIVATED, isActivated);
		mDb.update(DatabaseSaveAlarmHandler.TABLE_ALARMS_NAME, value, DatabaseSaveAlarmHandler.ID_KEY + " = ?",
				new String[]{String.valueOf(position+1)});
	}

	public List<Alarm> select() {
		List<Alarm> alarmsList = new ArrayList<>();
		Cursor c = mDb.rawQuery("select * from " + DatabaseSaveAlarmHandler.TABLE_ALARMS_NAME, null);
		int id, hour, minute, sound;
		String time, title;
		boolean isActivated;
		while (c.moveToNext()) {
			id = c.getInt(0);
			hour = c.getInt(1);
			minute = c.getInt(2);
			time = c.getString(3);
			title = c.getString(4);
			sound = c.getInt(5);
			isActivated = c.getInt(6) == 1;
			alarmsList.add(new Alarm(id, hour, minute, time, title, sound, isActivated));
		}
		c.close();
		return alarmsList;
	}
}
