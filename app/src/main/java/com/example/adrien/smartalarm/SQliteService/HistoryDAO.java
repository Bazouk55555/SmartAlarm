package com.example.adrien.smartalarm.SQliteService;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HistoryDAO extends AbstractBaseDAO {

    public HistoryDAO(Context pContext) {
        this.databaseHandler = new DatabaseHistoryHandler(pContext, DATABASE, null, VERSION);
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
        mDb.insert(((DatabaseHistoryHandler)databaseHandler).TABLE_NAME, null, value);
    }

    public List<Question> select(int numberOfQuestion) {
        String query = "select * from " + ((DatabaseSportsHandler)databaseHandler).TABLE_NAME ;
        List<Integer>numbersChosen = new ArrayList<>();
        int randomNumber = new Random().nextInt(NUMBER_OF_QUESTION)+1;
        System.out.println("FIRST RANDOM NUMBER="+ randomNumber);
        query+= " WHERE "+ databaseHandler.ID_KEY+"="+randomNumber;
        numbersChosen.add(randomNumber);
        for(int i = 1;i<numberOfQuestion;i++)
        {
            randomNumber=new Random().nextInt(NUMBER_OF_QUESTION)+1;
            while(numbersChosen.contains(randomNumber))
            {
                randomNumber=new Random().nextInt(NUMBER_OF_QUESTION)+1;
            }
            System.out.println("FIRST RANDOM NUMBER="+ randomNumber);
            numbersChosen.add(randomNumber);
            query+=" OR "+databaseHandler.ID_KEY+"="+randomNumber;
        }
        List<Question> questionList = new ArrayList<>();
        Cursor c = mDb.rawQuery(query, null);
        String question="";
        String answer="";
        String wrongAnswer1="";
        String wrongAnswer2="";
        String wrongAnswer3="";
        while(c.moveToNext())
        {
            question = c.getString(1);
            answer = c.getString(2);
            wrongAnswer1 = c.getString(3);
            wrongAnswer2 = c.getString(4);
            wrongAnswer3 = c.getString(5);
            questionList.add(new Question(question,answer,wrongAnswer1,wrongAnswer2,wrongAnswer3));
        }
        System.out.println("THE SIZE IS: "+questionList.size());
        c.close();
        return questionList;
    }
}


