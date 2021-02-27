/*
 * Author: jianqing
 * Date: Sep 8, 2020
 * Description: This document is created for a bean class for a quiz.
 */
package com.vocab85.model;

/**
 * A bean class for a quiz.
 *
 * @author jianqing
 */
public class MyQuiz
{

    private int id;
    private String doclink;
    private String date;
    private String message;
    private int score;
    private String answerLink;

    public MyQuiz()
    {
        id = 0;
        doclink = null;
        date = null;
        message = null;
        score = 0;
        answerLink = null;

    }

    public MyQuiz(int id, String doclink, String date, String message, int score, String answerLink)
    {
        this.id = id;
        this.doclink = doclink;
        this.date = date;
        this.message = message;
        this.score = score;
        this.answerLink = answerLink;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getDoclink()
    {
        return doclink;
    }

    public void setDoclink(String doclink)
    {
        this.doclink = doclink;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public int getScore()
    {
        return score;
    }

    public void setScore(int score)
    {
        this.score = score;
    }

    public String getAnswerLink()
    {
        return answerLink;
    }

    public void setAnswerLink(String answerLink)
    {
        this.answerLink = answerLink;
    }

    @Override
    public String toString()
    {
        return "MyQuiz{" + "id=" + id + ", doclink=" + doclink + ", date=" + date + ", message=" + message + ", score=" + score + ", answerLink=" + answerLink + '}';
    }

}
