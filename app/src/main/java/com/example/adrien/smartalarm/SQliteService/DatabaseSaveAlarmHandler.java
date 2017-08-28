package com.example.adrien.smartalarm.SQliteService;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.adrien.smartalarm.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DatabaseSaveAlarmHandler extends SQLiteOpenHelper {
	public static final String ID_KEY = "Id";
	public static final String HOUR = "Hour";
	public static final String MINUTE = "Minute";
	public static final String TIME = "Time";
	public static final String TITLE = "Title";
	public static final String SOUND = "Sound";
	public static final String ACTIVATED = "Activated";

	public static final String TABLE_ALARMS_NAME = "ALARMS";
	public static final String TABLE_ALARMS_CREATE = "CREATE TABLE " + TABLE_ALARMS_NAME + " (" + ID_KEY
			+ " INTEGER PRIMARY KEY, " + HOUR + " INTEGER, " + MINUTE + " INTEGER, " + TIME + " TEXT, " + TITLE
			+ " TEXT, " + SOUND + " INTEGER, " + ACTIVATED + " INTEGER);";
	public static final String TABLE_ALARMS_DROP = "DROP TABLE IF EXISTS " + TABLE_ALARMS_NAME + ";";

	public DatabaseSaveAlarmHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_ALARMS_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(TABLE_ALARMS_DROP);
		onCreate(db);
	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(TABLE_ALARMS_DROP);
		onCreate(db);
	}
}
