package com.example.adrien.smartalarm.SQliteService;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.adrien.smartalarm.SQliteService.AbstractDatabaseHandler;


public class DatabaseSportsHandler extends AbstractDatabaseHandler {

    public DatabaseSportsHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        TABLE_NAME = "Sports";
    }
}
