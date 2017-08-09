package com.example.adrien.smartalarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SportsDAO extends SportsBaseDAO{

    public static final String TABLE_NAME = "Sports";
    public static final String ID_KEY = "id";
    public static final String QUESTION = "Question";
    public static final String ANSWER = "Answer";

    public SportsDAO(Context pContext) {
        super(pContext);
    }

    public void close() {
        mDb.close();
    }

    public SQLiteDatabase getDb() {
        return mDb;
    }

    public void add(Sports sport) {
        ContentValues value = new ContentValues();
        value.put(QUESTION, sport.getQuestion());
        value.put(ANSWER, sport.getAnswer());
        System.out.println("ALLO ECOUTE MOI!!!");
        mDb.insert(TABLE_NAME, null, value);
    }

    public Sports select(long id) {
        Cursor c = mDb.rawQuery("select * from " + TABLE_NAME + " LIMIT ?", new String[]{"1"});
        int idKey=0;
        String question="";
        String answer="";
        while(c.moveToNext())
        {
            idKey = c.getInt(0);
            System.out.println("ID= "+idKey);
            question = c.getString(1);
            answer = c.getString(2);
        }
        c.close();
        return new Sports(idKey,question,answer);
    }
}


