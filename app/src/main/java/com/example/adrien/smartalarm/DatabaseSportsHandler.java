package com.example.adrien.smartalarm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseSportsHandler extends SQLiteOpenHelper {
    public static final String ID_KEY = "id";
    public static final String QUESTION = "Question";
    public static final String ANSWER = "Answer";

    public static final String TABLE_NAME = "Sports";
    public static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    ID_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    QUESTION + " TEXT, " +
                    ANSWER + " TEXT);";
    public static final String TABLE_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    public DatabaseSportsHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TABLE_DROP);
        onCreate(db);
    }
}
