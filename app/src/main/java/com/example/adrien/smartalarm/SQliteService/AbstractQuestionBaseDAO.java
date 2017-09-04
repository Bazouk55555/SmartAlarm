package com.example.adrien.smartalarm.SQliteService;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;
import java.util.Random;

public abstract class AbstractQuestionBaseDAO {

	protected SQLiteDatabase mDb = null;
	protected DatabaseQuestionHandler databaseHandler = null;
	protected final static Random RANDOM_NUMBER= new Random();

	AbstractQuestionBaseDAO(Context pContext) {
		this.databaseHandler = new DatabaseQuestionHandler(pContext);
	}

	public SQLiteDatabase open() {
		mDb = databaseHandler.getWritableDatabase();
		return mDb;
	}

	public void close() {
		mDb.close();
	}

	public abstract int getNumberOfQuestions(String level);

	public abstract List<Question> select(int number, String level);

}
