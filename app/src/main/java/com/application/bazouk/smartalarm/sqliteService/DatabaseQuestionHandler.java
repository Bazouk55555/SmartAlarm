package com.application.bazouk.smartalarm.sqliteService;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.application.bazouk.smartalarm.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class DatabaseQuestionHandler extends SQLiteOpenHelper {
	public static final String ID_KEY = "Id";
	public static final String QUESTION = "Question";
	public static final String ANSWER = "Answer";
	public static final String WRONG_ANSWER_1 = "First";
	public static final String WRONG_ANSWER_2 = "Second";
	public static final String WRONG_ANSWER_3 = "Third";
	public static final String LEVEL = "Level";

	public static final String TABLE_CINEMA_NAME = "CINEMA";
	public static final String TABLE_CINEMA_CREATE = "CREATE TABLE " + TABLE_CINEMA_NAME + " (" + ID_KEY
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + QUESTION + " TEXT, " + ANSWER + " TEXT, " + WRONG_ANSWER_1
			+ " TEXT, " + WRONG_ANSWER_2 + " TEXT, " + WRONG_ANSWER_3 + " TEXT," + LEVEL + " TEXT);";
	public static final String TABLE_CINEMA_DROP = "DROP TABLE IF EXISTS " + TABLE_CINEMA_NAME + ";";

	public static final String TABLE_GEOGRAPHY_NAME = "GEOGRAPHY";
	public static final String TABLE_GEOGRAPHY_CREATE = "CREATE TABLE " + TABLE_GEOGRAPHY_NAME + " (" + ID_KEY
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + QUESTION + " TEXT, " + ANSWER + " TEXT, " + WRONG_ANSWER_1
			+ " TEXT, " + WRONG_ANSWER_2 + " TEXT, " + WRONG_ANSWER_3 + " TEXT," + LEVEL + " TEXT);";
	public static final String TABLE_GEOGRAPHY_DROP = "DROP TABLE IF EXISTS " + TABLE_GEOGRAPHY_NAME + ";";

	public static final String TABLE_HISTORY_NAME = "HISTORY";
	public static final String TABLE_HISTORY_CREATE = "CREATE TABLE " + TABLE_HISTORY_NAME + " (" + ID_KEY
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + QUESTION + " TEXT, " + ANSWER + " TEXT, " + WRONG_ANSWER_1
			+ " TEXT, " + WRONG_ANSWER_2 + " TEXT, " + WRONG_ANSWER_3 + " TEXT," + LEVEL + " TEXT);";
	public static final String TABLE_HISTORY_DROP = "DROP TABLE IF EXISTS " + TABLE_HISTORY_NAME + ";";

	public static final String TABLE_MUSIC_NAME = "MUSIC";
	public static final String TABLE_MUSIC_CREATE = "CREATE TABLE " + TABLE_MUSIC_NAME + " (" + ID_KEY
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + QUESTION + " TEXT, " + ANSWER + " TEXT, " + WRONG_ANSWER_1
			+ " TEXT, " + WRONG_ANSWER_2 + " TEXT, " + WRONG_ANSWER_3 + " TEXT," + LEVEL + " TEXT);";
	public static final String TABLE_MUSIC_DROP = "DROP TABLE IF EXISTS " + TABLE_MUSIC_NAME + ";";

	public static final String TABLE_SPORTS_NAME = "SPORTS";
	public static final String TABLE_SPORTS_CREATE = "CREATE TABLE " + TABLE_SPORTS_NAME + " (" + ID_KEY
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + QUESTION + " TEXT, " + ANSWER + " TEXT, " + WRONG_ANSWER_1
			+ " TEXT, " + WRONG_ANSWER_2 + " TEXT, " + WRONG_ANSWER_3 + " TEXT," + LEVEL + " TEXT);";
	public static final String TABLE_SPORTS_DROP = "DROP TABLE IF EXISTS " + TABLE_SPORTS_NAME + ";";

	private final static String DATABASE = "database_question";
	private final static int VERSION = 3;

	private Context dbContext;

	public DatabaseQuestionHandler(Context context) {
		super(context, DATABASE, null, VERSION);
		this.dbContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_CINEMA_CREATE);
		db.execSQL(TABLE_GEOGRAPHY_CREATE);
		db.execSQL(TABLE_HISTORY_CREATE);
		db.execSQL(TABLE_MUSIC_CREATE);
		db.execSQL(TABLE_SPORTS_CREATE);
		loadQuestions(TABLE_CINEMA_NAME, R.raw.cinema_questions, db);
		loadQuestions(TABLE_GEOGRAPHY_NAME, R.raw.geography_questions, db);
		loadQuestions(TABLE_HISTORY_NAME, R.raw.history_questions, db);
		loadQuestions(TABLE_MUSIC_NAME, R.raw.music_questions, db);
		loadQuestions(TABLE_SPORTS_NAME, R.raw.sport_questions, db);
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

	private void loadQuestions(String table, int questionsFile, SQLiteDatabase db) {
		List<String> arrayToFillTheQuestion = Arrays.asList("","","","","");
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(dbContext.getResources().openRawResource(questionsFile)));
			String line;
			while (!(line = br.readLine()).equals(dbContext.getResources().getString(R.string.medium))) {
				int wordInArray = 0;
				for (int i = 0; i < line.length(); i++) {
					if (line.charAt(i) != ';') {
						arrayToFillTheQuestion.set(wordInArray,
								arrayToFillTheQuestion.get(wordInArray) + line.charAt(i));
					} else {
						wordInArray++;
					}
				}
				Question question = new Question(arrayToFillTheQuestion.get(0), arrayToFillTheQuestion.get(1),
						arrayToFillTheQuestion.get(2), arrayToFillTheQuestion.get(3), arrayToFillTheQuestion.get(4), dbContext.getResources().getString(R.string.easy));
				addQuestion(table, question, db);
				for (int i = 0; i < 5; i++) {
					arrayToFillTheQuestion.set(i, "");
				}
			}
			while (!(line = br.readLine()).equals(dbContext.getResources().getString(R.string.hard))) {
				int wordInArray = 0;
				for (int i = 0; i < line.length(); i++) {
					if (line.charAt(i) != ';') {
						arrayToFillTheQuestion.set(wordInArray,
								arrayToFillTheQuestion.get(wordInArray) + line.charAt(i));
					} else {
						wordInArray++;
					}
				}
				Question question = new Question(arrayToFillTheQuestion.get(0), arrayToFillTheQuestion.get(1),
						arrayToFillTheQuestion.get(2), arrayToFillTheQuestion.get(3), arrayToFillTheQuestion.get(4),dbContext.getResources().getString(R.string.medium));
				addQuestion(table, question, db);
				for (int i = 0; i < 5; i++) {
					arrayToFillTheQuestion.set(i, "");
				}
			}
			while ((line = br.readLine()) != null) {
				int wordInArray = 0;
				for (int i = 0; i < line.length(); i++) {
					if (line.charAt(i) != ';') {
						arrayToFillTheQuestion.set(wordInArray,
								arrayToFillTheQuestion.get(wordInArray) + line.charAt(i));
					} else {
						wordInArray++;
					}
				}
				Question question = new Question(arrayToFillTheQuestion.get(0), arrayToFillTheQuestion.get(1),
						arrayToFillTheQuestion.get(2), arrayToFillTheQuestion.get(3), arrayToFillTheQuestion.get(4),dbContext.getResources().getString(R.string.hard));
				addQuestion(table, question, db);
				for (int i = 0; i < 5; i++) {
					arrayToFillTheQuestion.set(i, "");
				}
			}
			br.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	private void addQuestion(String table, Question question, SQLiteDatabase db) {
		ContentValues value = new ContentValues();
		value.put(QUESTION, question.getQuestion());
		value.put(ANSWER, question.getAnswer());
		value.put(WRONG_ANSWER_1, question.getWrongAnswer1());
		value.put(WRONG_ANSWER_2, question.getWrongAnswer2());
		value.put(WRONG_ANSWER_3, question.getWrongAnswer3());
		value.put(LEVEL, question.getLevel());
		db.insert(table, null, value);
	}
}
