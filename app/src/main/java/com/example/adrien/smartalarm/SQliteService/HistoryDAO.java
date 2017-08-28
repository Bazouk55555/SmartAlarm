package com.example.adrien.smartalarm.SQliteService;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HistoryDAO extends AbstractQuestionBaseDAO {

	public HistoryDAO(Context pContext) {
		super(pContext);
	}

	@Override
	public int getNumberOfQuestionInFile() {
		return mDb.rawQuery("select * from " + DatabaseQuestionHandler.TABLE_HISTORY_NAME, null).getCount();
	}

	@Override
	public List<Question> select(int numberOfQuestion) {
		String query = "select * from " + DatabaseQuestionHandler.TABLE_HISTORY_NAME;
		List<Integer> numbersChosen = new ArrayList<>();
		System.out.println("numberOfQuestions: "+numberOfQuestion);
		int randomNumber = RANDOM_NUMBER.nextInt(numberOfQuestion) + 1;
		query += " WHERE " + DatabaseQuestionHandler.ID_KEY + "=" + randomNumber;
		numbersChosen.add(randomNumber);
		StringBuilder buffer = new StringBuilder();
		for (int i = 1; i < numberOfQuestion; i++) {
			randomNumber = RANDOM_NUMBER.nextInt(numberOfQuestion) + 1;
			while (numbersChosen.contains(randomNumber)) {
				randomNumber = RANDOM_NUMBER.nextInt(numberOfQuestion) + 1;
			}
			numbersChosen.add(randomNumber);
			buffer.append(" OR ");
			buffer.append(DatabaseQuestionHandler.ID_KEY);
			buffer.append("=");
			buffer.append(randomNumber);
		}
		query+=buffer.toString();
		List<Question> questionList = new ArrayList<>();
		Cursor c = mDb.rawQuery(query, null);
		String question, answer, wrongAnswer1, wrongAnswer2, wrongAnswer3;
		while (c.moveToNext()) {
			question = c.getString(1);
			answer = c.getString(2);
			wrongAnswer1 = c.getString(3);
			wrongAnswer2 = c.getString(4);
			wrongAnswer3 = c.getString(5);
			questionList.add(new Question(question, answer, wrongAnswer1, wrongAnswer2, wrongAnswer3));
		}
		c.close();
		return questionList;
	}
}
