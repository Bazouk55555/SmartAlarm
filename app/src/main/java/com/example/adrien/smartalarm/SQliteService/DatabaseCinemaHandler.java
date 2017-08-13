package com.example.adrien.smartalarm.SQliteService;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


public class DatabaseCinemaHandler extends AbstractDatabaseHandler {

    public DatabaseCinemaHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        TABLE_NAME = "Cinema";
    }
}
