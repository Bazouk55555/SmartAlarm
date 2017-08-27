package com.example.adrien.smartalarm.SQliteService;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

public abstract class AbstractQuestionBaseDAO {
    protected final static int VERSION = 1;
    protected final static String DATABASE = "database";

    protected SQLiteDatabase mDb = null;
    protected DatabaseQuestionHandler databaseHandler = null;
    protected final int NUMBER_OF_QUESTION = 6;

    public AbstractQuestionBaseDAO(Context pContext) {
        this.databaseHandler = new DatabaseQuestionHandler(pContext, DATABASE, null, VERSION);
    }

    public SQLiteDatabase open() {
        mDb = databaseHandler.getWritableDatabase();
        return mDb;
    }

    public void close() {
        mDb.close();
    }

    public abstract int getNumberOfQuestion();

    public abstract List<Question> select(int number);

}


