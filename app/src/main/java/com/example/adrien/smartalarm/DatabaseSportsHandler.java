package com.example.adrien.smartalarm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


public class DatabaseSportsHandler extends AbstractDatabaseHandler{

    public DatabaseSportsHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        TABLE_NAME = "Sports";
    }
}
