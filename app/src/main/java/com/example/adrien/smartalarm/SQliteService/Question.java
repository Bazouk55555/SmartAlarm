package com.example.adrien.smartalarm.SQliteService;

public class Question {

    private long id;
    private String question;
    private String answer;
    String wrongAnswer1;
    String wrongAnswer2;
    String wrongAnswer3;

    public Question(long id, String question, String answer, String wrongAnswer1, String wrongAnswer2, String wrongAnswer3) {
        super();
        this.id = id;
        this.question= question;
        this.answer = answer;
        this.wrongAnswer1 = wrongAnswer1;
        this.wrongAnswer2 = wrongAnswer2;
        this.wrongAnswer3 = wrongAnswer3;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

}
