package com.application.bazouk.smartalarm.sqliteService;

public class Question {

	private String question;
	private String answer;
	private String wrongAnswer1;
	private String wrongAnswer2;
	private String wrongAnswer3;
	private String level;

	public Question(String question, String answer, String wrongAnswer1, String wrongAnswer2, String wrongAnswer3, String level) {
		this.question = question;
		this.answer = answer;
		this.wrongAnswer1 = wrongAnswer1;
		this.wrongAnswer2 = wrongAnswer2;
		this.wrongAnswer3 = wrongAnswer3;
		this.level = level;
	}

	public String getQuestion() {
		return question;
	}

	public String getAnswer() {
		return answer;
	}

	public String getWrongAnswer1() {
		return wrongAnswer1;
	}

	public String getWrongAnswer2() {
		return wrongAnswer2;
	}

	public String getWrongAnswer3() {
		return wrongAnswer3;
	}

	public String getLevel() {
		return level;
	}

}
