package com.example.adrien.smartalarm.SQliteService;

import android.database.sqlite.SQLiteDatabase;

import com.example.adrien.smartalarm.SQliteService.AbstractDatabaseHandler;

import java.util.List;

public abstract class AbstractBaseDAO {
    // Nous sommes à la première version de la base
    // Si je décide de la mettre à jour, il faudra changer cet attribut
    protected final static int VERSION = 5;
    protected final static String DATABASE = "database";

    protected SQLiteDatabase mDb = null;
    protected AbstractDatabaseHandler databaseHandler = null;
    protected final int NUMBER_OF_QUESTION = 6;

    public SQLiteDatabase open() {
        mDb = databaseHandler.getWritableDatabase();
        return mDb;
    }

    public void close() {
        mDb.close();
    }

    public abstract void add(Question question);

    public abstract List<Question> select(int number);

}


