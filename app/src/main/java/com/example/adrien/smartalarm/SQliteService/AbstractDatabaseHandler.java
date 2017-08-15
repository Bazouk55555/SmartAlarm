package com.example.adrien.smartalarm.SQliteService;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class AbstractDatabaseHandler extends SQLiteOpenHelper {
    public static final String ID_KEY = "id";
    public static final String QUESTION = "Question";
    public static final String ANSWER = "Answer";
    public static final String WRONG_ANSWER_1 = "First";
    public static final String WRONG_ANSWER_2 = "Second";
    public static final String WRONG_ANSWER_3 = "Third";

    public AbstractDatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
}
