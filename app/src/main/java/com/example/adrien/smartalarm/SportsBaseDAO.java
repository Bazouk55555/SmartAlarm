package com.example.adrien.smartalarm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public abstract class SportsBaseDAO {
    // Nous sommes à la première version de la base
    // Si je décide de la mettre à jour, il faudra changer cet attribut
    protected final static int VERSION = 2;
    protected final static String DATABASE = "database";

    protected SQLiteDatabase mDb = null;
    protected DatabaseSportsHandler sportsHandler = null;

    public SportsBaseDAO(Context pContext) {
        this.sportsHandler = new DatabaseSportsHandler(pContext, DATABASE, null, VERSION);
    }

    public SQLiteDatabase open() {
        mDb = sportsHandler.getWritableDatabase();
        return mDb;
    }

    public void close() {
        mDb.close();
    }

    public SQLiteDatabase getDb() {
        return mDb;
    }

}


