package com.example.adrien.smartalarm.SQliteService;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


public class DatabaseMusicHandler extends AbstractDatabaseHandler {

    public static final String TABLE_NAME = "SPORTS";
    public static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    ID_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    QUESTION + " TEXT, " +
                    ANSWER + " TEXT, " +
                    WRONG_ANSWER_1 + " TEXT, " +
                    WRONG_ANSWER_2 + " TEXT, " +
                    WRONG_ANSWER_3 + " TEXT);";
    public static final String TABLE_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    public DatabaseMusicHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
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

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TABLE_DROP);
        onCreate(db);
    }
}
