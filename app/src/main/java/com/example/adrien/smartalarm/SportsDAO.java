package com.example.adrien.smartalarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SportsDAO extends BaseDAO{

    public static final String TABLE_SPORT_NAME = "Sports";

    public SportsDAO(Context pContext) {
        this.databaseHandler = new DatabaseSportsHandler(pContext, DATABASE, null, VERSION);
    }

    public void close() {
        mDb.close();
    }

    public SQLiteDatabase getDb() {
        return mDb;
    }

    public void add(Question question) {
        ContentValues value = new ContentValues();
        value.put(QUESTION, question.getQuestion());
        value.put(ANSWER, question.getAnswer());
        mDb.insert(TABLE_SPORT_NAME, null, value);
    }

    public Question select() {
        Cursor c = mDb.rawQuery("select * from " + TABLE_SPORT_NAME + " LIMIT ?", new String[]{"1"});
        int idKey=0;
        String question="";
        String answer="";
        while(c.moveToNext())
        {
            idKey = c.getInt(0);
            question = c.getString(1);
            answer = c.getString(2);
        }
        c.close();
        return new Question(idKey,question,answer);
    }
}


