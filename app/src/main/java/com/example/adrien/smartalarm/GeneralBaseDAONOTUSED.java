package com.example.adrien.smartalarm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public abstract class GeneralBaseDAONOTUSED {
    // Nous sommes à la première version de la base
    // Si je décide de la mettre à jour, il faudra changer cet attribut
    protected final static int VERSION = 1;
    protected final static String DATABASE = "database_general";

    protected SQLiteDatabase mDb = null;
    protected DatabaseGeneralHandlerNOTUSED generalHandler = null;

    public GeneralBaseDAONOTUSED(Context pContext) {
        this.generalHandler = new DatabaseGeneralHandlerNOTUSED(pContext, DATABASE, null, VERSION);
    }

    public SQLiteDatabase open() {
        mDb = generalHandler.getWritableDatabase();
        return mDb;
    }

    public void close() {
        mDb.close();
    }

    public SQLiteDatabase getDb() {
        return mDb;
    }

}


