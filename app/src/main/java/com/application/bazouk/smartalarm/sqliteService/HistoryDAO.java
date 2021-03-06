package com.application.bazouk.smartalarm.sqliteService;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class HistoryDAO extends AbstractQuestionBaseDAO {

	public HistoryDAO(Context pContext) {
		super(pContext);
	}

	@Override
	public int getNumberOfQuestions(String level) {
		return mDb.rawQuery("select * from " + DatabaseQuestionHandler.TABLE_HISTORY_NAME + " WHERE "+DatabaseQuestionHandler.LEVEL + "=?",new String[]{level}).getCount();
	}

	@Override
	public List<Question> select(int numberOfQuestion, String level) {
		String query = "select * from " + DatabaseQuestionHandler.TABLE_HISTORY_NAME+ " WHERE "
				+ DatabaseQuestionHandler.LEVEL + "=?" + " ORDER BY RANDOM() LIMIT " + numberOfQuestion;
		List<Question> questionList = new ArrayList<>();
		Cursor c = mDb.rawQuery(query, new String[]{level});
		while (c.moveToNext()) {
			questionList.add(new Question(c.getString(1), c.getString(2), c.getString(3), c.getString(4),
					c.getString(5), level));
		}
		c.close();
		return questionList;
	}
}
