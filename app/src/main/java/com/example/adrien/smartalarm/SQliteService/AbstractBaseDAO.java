package com.example.adrien.smartalarm.SQliteService;

import android.database.sqlite.SQLiteDatabase;

import com.example.adrien.smartalarm.SQliteService.AbstractDatabaseHandler;

public abstract class AbstractBaseDAO {
    // Nous sommes à la première version de la base
    // Si je décide de la mettre à jour, il faudra changer cet attribut
    protected final static int VERSION = 5;
    protected final static String DATABASE = "database";

    protected SQLiteDatabase mDb = null;
    protected AbstractDatabaseHandler databaseHandler = null;

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


