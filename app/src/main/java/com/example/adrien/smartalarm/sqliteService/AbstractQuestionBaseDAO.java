package com.example.adrien.smartalarm.sqliteService;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

public abstract class AbstractQuestionBaseDAO {

	protected SQLiteDatabase mDb = null;
	protected DatabaseQuestionHandler databaseHandler = null;

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
