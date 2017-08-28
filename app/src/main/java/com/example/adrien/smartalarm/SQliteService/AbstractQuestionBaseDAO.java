package com.example.adrien.smartalarm.SQliteService;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;
import java.util.Random;

public abstract class AbstractQuestionBaseDAO {
	private final static int VERSION = 1;
	private final static String DATABASE = "database_question";

	protected SQLiteDatabase mDb = null;
	protected DatabaseQuestionHandler databaseHandler = null;
	protected int numberOfQuestion;
	protected final static Random RANDOM_NUMBER= new Random();

	AbstractQuestionBaseDAO(Context pContext) {
		this.databaseHandler = new DatabaseQuestionHandler(pContext, DATABASE, null, VERSION);
	}

	public SQLiteDatabase open() {
		mDb = databaseHandler.getWritableDatabase();
		numberOfQuestion = getNumberOfQuestionInFile();
		return mDb;
	}

	public void close() {
		mDb.close();
	}

	public int getNumberOfQuestion()
	{
		return numberOfQuestion;
	}

	public abstract int getNumberOfQuestionInFile();

	public abstract List<Question> select(int number);

}
