package com.example.adrien.smartalarm;

public class Question {

    private long id;
    private String question;
    private String answer;

    public Question(long id, String question, String answer) {
        super();
        this.id = id;
        this.question= question;
        this.answer = answer;
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

}
