package com.example.adrien.smartalarm.SQliteService;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CinemaDAO extends AbstractQuestionBaseDAO {

	public CinemaDAO(Context pContext) {
		super(pContext);
	}

	@Override
	public int getNumberOfQuestionInFile() {
		return mDb.rawQuery("select * from " + DatabaseQuestionHandler.TABLE_CINEMA_NAME, null).getCount();
	}

	@Override
	public List<Question> select(int numberOfQuestion) {
		String query = "select * from " + DatabaseQuestionHandler.TABLE_CINEMA_NAME;
		List<Integer> numbersChosen = new ArrayList<>();
		int randomNumber = new Random().nextInt(numberOfQuestion) + 1;
		query += " WHERE " + DatabaseQuestionHandler.ID_KEY + "=" + randomNumber;
		numbersChosen.add(randomNumber);
		for (int i = 1; i < numberOfQuestion; i++) {
			randomNumber = new Random().nextInt(numberOfQuestion) + 1;
			while (numbersChosen.contains(randomNumber)) {
				randomNumber = new Random().nextInt(numberOfQuestion) + 1;
			}
			numbersChosen.add(randomNumber);
			query += " OR " + DatabaseQuestionHandler.ID_KEY + "=" + randomNumber;
		}
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
