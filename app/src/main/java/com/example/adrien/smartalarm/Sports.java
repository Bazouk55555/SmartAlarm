package com.example.adrien.smartalarm;

public class Sports {

    private long id;
    private String question;
    private String answer;

    public Sports(long id, String question, String answer) {
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

    public void setQuestion(String intitule) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

}
