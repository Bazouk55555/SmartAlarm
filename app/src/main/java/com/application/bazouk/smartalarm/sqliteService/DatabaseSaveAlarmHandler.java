package com.application.bazouk.smartalarm.sqliteService;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

	private final static String DATABASE = "database_alarm";
	private final static int VERSION = 1;

	public DatabaseSaveAlarmHandler(Context context) {
		super(context, DATABASE, null, VERSION);
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
