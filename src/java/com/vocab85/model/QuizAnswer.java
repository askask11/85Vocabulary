/*
 * Author: jianqing
 * Date: Sep 8, 2020
 * Description: This document is created for
 */
package com.vocab85.model;

/**
 *
 * @author jianqing
 */
public class QuizAnswer
{

    private int quizid;
    private int serial;
    private String response;
    private Boolean correct;
    private String answer;

    public QuizAnswer(int quizid, int serial, String response, Boolean correct, String correction)
    {
        this.quizid = quizid;
        this.serial = serial;
        this.response = response;
        this.correct = correct;
        this.answer = correction;
    }

    public QuizAnswer()
    {
        serial = 0;
        response = null;

    }

    public QuizAnswer(int quizid, int serial, String answer)
    {
        this.quizid = quizid;
        this.serial = serial;
        this.response = answer;
    }

    public int getSerial()
    {
        return serial;
    }

    public void setSerial(int serial)
    {
        this.serial = serial;
    }

    public String getResponse()
    {
        return response;
    }

    public void setResponse(String response)
    {
        this.response = response;
    }

    public int getQuizid()
    {
        return quizid;
    }

    public void setQuizid(int quizid)
    {
        this.quizid = quizid;
    }

    public Boolean isCorrect()
    {
        return correct;
    }

    public void setCorrect(Boolean correct)
    {
        this.correct = correct;
    }

    public String getAnswer()
    {
        return answer;
    }

    public void setAnswer(String answer)
    {
        this.answer = answer;
    }

    @Override
    public String toString()
    {
        return "QuizAnswer{" + "quizid=" + quizid + ", serial=" + serial + ", response=" + response + ", correct=" + correct + ", correction=" + answer + '}';
    }

    public static void main(String[] args)
    {

    }

}
