package com.example.adrien.smartalarm.SQliteService;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class AlarmBaseDAO {
    private final static int VERSION = 1;
    private final static String DATABASE = "database_alarm";
    public static int incrementation = 1;

    private SQLiteDatabase mDb = null;
    private DatabaseSaveAlarmHandler databaseHandler = null;

    public AlarmBaseDAO(Context pContext) {
        this.databaseHandler = new DatabaseSaveAlarmHandler(pContext, DATABASE, null, VERSION);
    }

    public SQLiteDatabase open() {
        mDb = databaseHandler.getWritableDatabase();
        return mDb;
    }

    public void close() {
        mDb.close();
    }

    public void add(Alarm alarm)
    {
        ContentValues value = new ContentValues();
        value.put(databaseHandler.ID_KEY, incrementation);
        value.put(databaseHandler.HOUR, alarm.getHour());
        value.put(databaseHandler.MINUTE, alarm.getMinute());
        value.put(databaseHandler.TIME, alarm.getTime());
        value.put(databaseHandler.TITLE, alarm.getTitle());
        value.put(databaseHandler.SOUND, alarm.getSound());
        value.put(databaseHandler.ACTIVATED, (alarm.getActivated())?1:0);
        incrementation++;
        mDb.insert(databaseHandler.TABLE_ALARMS_NAME, null, value);
    };

    public void remove(long id, int size)
    {
        System.out.println("Before remove in method with ID="+id);
        mDb.delete(databaseHandler.TABLE_ALARMS_NAME, databaseHandler.ID_KEY + " = ?", new String[] {String.valueOf(id)});
        incrementation--;
        for(int i=0;i<size;i++)
        {
            updateId(id+1+i,size);
        }
    };

    public void updateId(long id,int size)
    {
        ContentValues value = new ContentValues();
        value.put(databaseHandler.ID_KEY,id-1);
        mDb.update(databaseHandler.TABLE_ALARMS_NAME, value, databaseHandler.ID_KEY   + " = ?", new String[] {String.valueOf(id)});
    }

    public void update(Alarm alarm)
    {

        ContentValues value = new ContentValues();
        value.put(databaseHandler.HOUR, alarm.getHour());
        value.put(databaseHandler.MINUTE, alarm.getMinute());
        value.put(databaseHandler.TIME, alarm.getTime());
        value.put(databaseHandler.TITLE, alarm.getTitle());
        value.put(databaseHandler.SOUND, alarm.getSound());
        mDb.update(databaseHandler.TABLE_ALARMS_NAME, value, databaseHandler.ID_KEY   + " = ?", new String[] {String.valueOf(alarm.getId())});
    }

    public List<Alarm> select()
    {
        List<Alarm> alarmsList = new ArrayList<>();
        Cursor c = mDb.rawQuery("select * from " + databaseHandler.TABLE_ALARMS_NAME, null);
        int id=0;
        int hour=0;
        int minute=0;
        String time="";
        String title="";
        int sound=0;
        boolean isActivated=true;
        while(c.moveToNext())
        {
            id = c.getInt(0);
            System.out.println("ID="+id);
            hour = c.getInt(1);
            System.out.println("ID="+hour);
            minute = c.getInt(2);
            System.out.println("ID="+minute);
            time = c.getString(3);
            System.out.println("ID="+time);
            title = c.getString(4);
            System.out.println("ID="+title);
            sound = c.getInt(5);
            System.out.println("ID="+sound);
            isActivated = (c.getInt(6)==1)?true:false;
            System.out.println("ID="+isActivated);
            alarmsList.add(new Alarm(id,hour,minute,time,title,sound,isActivated));
        }
        c.close();
        return alarmsList;
    }
}


