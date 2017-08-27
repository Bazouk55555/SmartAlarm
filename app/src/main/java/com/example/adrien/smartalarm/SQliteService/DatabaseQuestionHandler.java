package com.example.adrien.smartalarm.SQliteService;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.adrien.smartalarm.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DatabaseQuestionHandler extends SQLiteOpenHelper {
    public static final String ID_KEY = "id";
    public static final String QUESTION = "Question";
    public static final String ANSWER = "Answer";
    public static final String WRONG_ANSWER_1 = "First";
    public static final String WRONG_ANSWER_2 = "Second";
    public static final String WRONG_ANSWER_3 = "Third";

    public static final String TABLE_CINEMA_NAME = "CINEMA";
    public static final String TABLE_CINEMA_CREATE =
            "CREATE TABLE " + TABLE_CINEMA_NAME + " (" +
                    ID_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    QUESTION + " TEXT, " +
                    ANSWER + " TEXT, " +
                    WRONG_ANSWER_1 + " TEXT, " +
                    WRONG_ANSWER_2 + " TEXT, " +
                    WRONG_ANSWER_3 + " TEXT);";
    public static final String TABLE_CINEMA_DROP = "DROP TABLE IF EXISTS " + TABLE_CINEMA_NAME + ";";

    public static final String TABLE_GEOGRAPHY_NAME = "GEOGRAPHY";
    public static final String TABLE_GEOGRAPHY_CREATE =
            "CREATE TABLE " + TABLE_GEOGRAPHY_NAME + " (" +
                    ID_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    QUESTION + " TEXT, " +
                    ANSWER + " TEXT, " +
                    WRONG_ANSWER_1 + " TEXT, " +
                    WRONG_ANSWER_2 + " TEXT, " +
                    WRONG_ANSWER_3 + " TEXT);";
    public static final String TABLE_GEOGRAPHY_DROP = "DROP TABLE IF EXISTS " + TABLE_GEOGRAPHY_NAME + ";";

    public static final String TABLE_HISTORY_NAME = "HISTORY";
    public static final String TABLE_HISTORY_CREATE =
            "CREATE TABLE " + TABLE_HISTORY_NAME + " (" +
                    ID_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    QUESTION + " TEXT, " +
                    ANSWER + " TEXT, " +
                    WRONG_ANSWER_1 + " TEXT, " +
                    WRONG_ANSWER_2 + " TEXT, " +
                    WRONG_ANSWER_3 + " TEXT);";
    public static final String TABLE_HISTORY_DROP = "DROP TABLE IF EXISTS " + TABLE_HISTORY_NAME + ";";

    public static final String TABLE_MUSIC_NAME = "MUSIC";
    public static final String TABLE_MUSIC_CREATE =
            "CREATE TABLE " + TABLE_MUSIC_NAME + " (" +
                    ID_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    QUESTION + " TEXT, " +
                    ANSWER + " TEXT, " +
                    WRONG_ANSWER_1 + " TEXT, " +
                    WRONG_ANSWER_2 + " TEXT, " +
                    WRONG_ANSWER_3 + " TEXT);";
    public static final String TABLE_MUSIC_DROP = "DROP TABLE IF EXISTS " + TABLE_MUSIC_NAME + ";";

    public static final String TABLE_SPORTS_NAME = "SPORTS";
    public static final String TABLE_SPORTS_CREATE =
            "CREATE TABLE " + TABLE_SPORTS_NAME + " (" +
                    ID_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    QUESTION + " TEXT, " +
                    ANSWER + " TEXT, " +
                    WRONG_ANSWER_1 + " TEXT, " +
                    WRONG_ANSWER_2 + " TEXT, " +
                    WRONG_ANSWER_3 + " TEXT);";
    public static final String TABLE_SPORTS_DROP = "DROP TABLE IF EXISTS " + TABLE_SPORTS_NAME + ";";

    private Context dbContext;

    public DatabaseQuestionHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.dbContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CINEMA_CREATE);
        db.execSQL(TABLE_GEOGRAPHY_CREATE);
        db.execSQL(TABLE_HISTORY_CREATE);
        db.execSQL(TABLE_MUSIC_CREATE);
        db.execSQL(TABLE_SPORTS_CREATE);
        loadQuestions(TABLE_CINEMA_NAME,R.raw.cinema_questions,db);
        loadQuestions(TABLE_GEOGRAPHY_NAME,R.raw.geography_questions,db);
        loadQuestions(TABLE_HISTORY_NAME,R.raw.history_questions,db);
        loadQuestions(TABLE_MUSIC_NAME,R.raw.music_questions,db);
        loadQuestions(TABLE_SPORTS_NAME,R.raw.sport_questions,db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TABLE_CINEMA_DROP);
        db.execSQL(TABLE_GEOGRAPHY_DROP);
        db.execSQL(TABLE_HISTORY_DROP);
        db.execSQL(TABLE_MUSIC_DROP);
        db.execSQL(TABLE_SPORTS_DROP);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TABLE_CINEMA_DROP);
        db.execSQL(TABLE_GEOGRAPHY_DROP);
        db.execSQL(TABLE_HISTORY_DROP);
        db.execSQL(TABLE_MUSIC_DROP);
        db.execSQL(TABLE_SPORTS_DROP);
        onCreate(db);
    }

    private void loadQuestions(String table, int questionsFile, SQLiteDatabase db)
    {
        List<String> arrayToFillTheQuestion = new ArrayList<>();
        for(int i = 0 ;i<5;i++) {
            arrayToFillTheQuestion.add("");
        }
        try{
            InputStream ips=null;
                ips=dbContext.getResources().openRawResource(questionsFile);
            InputStreamReader ipsr=new InputStreamReader(ips);
            BufferedReader br=new BufferedReader(ipsr);
            String line;
            while ((line=br.readLine())!=null){
                int wordInArray=0;
                for(int i=0;i<line.length();i++)
                {
                    if(line.charAt(i)!=',')
                    {
                        arrayToFillTheQuestion.set(wordInArray,arrayToFillTheQuestion.get(wordInArray)+line.charAt(i));
                    }
                    else
                    {
                        wordInArray++;
                    }
                }
                Question question = new Question(arrayToFillTheQuestion.get(0),arrayToFillTheQuestion.get(1),arrayToFillTheQuestion.get(2),arrayToFillTheQuestion.get(3),arrayToFillTheQuestion.get(4));
                addQuestion(table,question,db);
                for(int i = 0 ;i<5;i++) {
                    arrayToFillTheQuestion.set(i,"");
                }
            }
            br.close();
        }
        catch (Exception e){
            System.out.println(e.toString());
        }
    }

    private void addQuestion(String table, Question question, SQLiteDatabase db)
    {
        ContentValues value = new ContentValues();
        value.put(QUESTION, question.getQuestion());
        value.put(ANSWER, question.getAnswer());
        value.put(WRONG_ANSWER_1, question.getWrongAnswer1());
        value.put(WRONG_ANSWER_2, question.getWrongAnswer2());
        value.put(WRONG_ANSWER_3, question.getWrongAnswer3());
        db.insert(table, null, value);
    }
}
