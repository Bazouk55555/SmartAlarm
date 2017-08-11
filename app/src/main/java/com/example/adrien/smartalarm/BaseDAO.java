package com.example.adrien.smartalarm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public abstract class BaseDAO {
    // Nous sommes à la première version de la base
    // Si je décide de la mettre à jour, il faudra changer cet attribut
    protected final static int VERSION = 1;
    protected final static String DATABASE = "database";

    protected SQLiteDatabase mDb = null;
    protected AbstractDatabaseHandler databaseHandler = null;

    public static final String QUESTION = "Question";
    public static final String ANSWER = "Answer";

    public SQLiteDatabase open() {
        mDb = databaseHandler.getWritableDatabase();
        return mDb;
    }

    public void close() {
        mDb.close();
    }

    public SQLiteDatabase getDb() {
        return mDb;
    }

}


