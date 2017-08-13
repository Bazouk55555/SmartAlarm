package com.example.adrien.smartalarm.SQliteService;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SportsDAO extends AbstractBaseDAO {

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
        value.put(databaseHandler.QUESTION, question.getQuestion());
        value.put(databaseHandler.ANSWER, question.getAnswer());
        value.put(databaseHandler.WRONG_ANSWER_1, question.getWrongAnswer1());
        value.put(databaseHandler.WRONG_ANSWER_2, question.getWrongAnswer2());
        value.put(databaseHandler.WRONG_ANSWER_3, question.getWrongAnswer3());
        mDb.insert(databaseHandler.TABLE_NAME, null, value);
    }

    public Question select() {
        Cursor c = mDb.rawQuery("select * from " + databaseHandler.TABLE_NAME + " LIMIT ?", new String[]{"1"});
        int idKey=0;
        String question="";
        String answer="";
        String wrongAnswer1="";
        String wrongAnswer2="";
        String wrongAnswer3="";
        while(c.moveToNext())
        {
            idKey = c.getInt(0);
            question = c.getString(1);
            answer = c.getString(2);
            wrongAnswer1 = c.getString(3);
            wrongAnswer2 = c.getString(4);
            wrongAnswer3 = c.getString(5);
        }
        c.close();
        return new Question(idKey,question,answer,wrongAnswer1,wrongAnswer2,wrongAnswer3);
    }
}


