package com.example.adrien.smartalarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class GeneralDAONOTUSED extends GeneralBaseDAONOTUSED {

    public static final String TABLE_GENERAL_NAME = "General questions";
    public static final String CATEGORY= "Category";

    public GeneralDAONOTUSED(Context pContext) {
        super(pContext);
    }

    public void close() {
        mDb.close();
    }

    public SQLiteDatabase getDb() {
        return mDb;
    }

    public void add(GeneralQuestionNOTUSED generalQuestion) {
        ContentValues value = new ContentValues();
        value.put(CATEGORY, generalQuestion.getCategory());
        mDb.insert(TABLE_GENERAL_NAME, null, value);
    }

    /*public GeneralQuestion select(long id) {
        Cursor c = mDb.rawQuery("select * from " + TABLE_NAME + " LIMIT ?", new String[]{"1"});
        int idKey=0;
        String category="";
        while(c.moveToNext())
        {
            idKey = c.getInt(0);
            category = c.getString(1);
        }
        c.close();
        return new GeneralQuestion(idKey,category);
    }*/
}


