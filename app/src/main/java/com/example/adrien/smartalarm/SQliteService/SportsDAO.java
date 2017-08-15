package com.example.adrien.smartalarm.SQliteService;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class SportsDAO extends AbstractBaseDAO {

    public SportsDAO(Context pContext) {
        this.databaseHandler = new DatabaseSportsHandler(pContext, DATABASE, null, VERSION);
    }

    public SQLiteDatabase getDb() {
        return mDb;
    }

    public void add(Question question) {
        ContentValues value = new ContentValues();
        System.out.println("HERE PUREE!!!");
        value.put(databaseHandler.QUESTION, question.getQuestion());
        value.put(databaseHandler.ANSWER, question.getAnswer());
        value.put(databaseHandler.WRONG_ANSWER_1, question.getWrongAnswer1());
        value.put(databaseHandler.WRONG_ANSWER_2, question.getWrongAnswer2());
        value.put(databaseHandler.WRONG_ANSWER_3, question.getWrongAnswer3());
        mDb.insert(((DatabaseSportsHandler)databaseHandler).TABLE_NAME, null, value);
    }

    public List<Question> select() {
        List<Question> questionList = new ArrayList<>();
        Cursor c = mDb.rawQuery("select * from " + ((DatabaseSportsHandler)databaseHandler).TABLE_NAME + " LIMIT ?", new String[]{"5"});
        String question="";
        String answer="";
        String wrongAnswer1="";
        String wrongAnswer2="";
        String wrongAnswer3="";
        System.out.println("ICI!!!: "+c.getCount());
        while(c.moveToNext())
        {
            question = c.getString(1);
            answer = c.getString(2);
            wrongAnswer1 = c.getString(3);
            wrongAnswer2 = c.getString(4);
            wrongAnswer3 = c.getString(5);
            questionList.add(new Question(question,answer,wrongAnswer1,wrongAnswer2,wrongAnswer3));
        }
        c.close();
        return questionList;
    }
}


