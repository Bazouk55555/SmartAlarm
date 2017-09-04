package com.example.adrien.smartalarm.SQliteService;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class QuestionTest {

    private Question question = new Question("Question", "Answer", "Wrong Answer 1","Wrong Answer 2","Wrong Answer 3");

    @Test
    public void getQuestionTest() throws Exception {
        String questionExpected = "Question";
        assertEquals(questionExpected, question.getQuestion());
    }

    @Test
    public void getAnswerTest() throws Exception {
        String answerExpected = "Answer";
        assertEquals(answerExpected, question.getAnswer());
    }

    @Test
    public void getWrongAnswer1Test() throws Exception {
        String wrongAnswer1Expected = "Wrong Answer 1";
        assertEquals(wrongAnswer1Expected, question.getWrongAnswer1());
    }

    @Test
    public void getWrongAnswer2Test() throws Exception {
        String wrongAnswer2Expected = "Wrong Answer 2";
        assertEquals(wrongAnswer2Expected, question.getWrongAnswer2());
    }

    @Test
    public void getWrongAnswer3Test() throws Exception {
        String wrongAnswer3Expected = "Wrong Answer 3";
        assertEquals(wrongAnswer3Expected, question.getWrongAnswer3());
    }

}