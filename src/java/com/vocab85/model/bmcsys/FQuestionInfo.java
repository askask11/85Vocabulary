/*
 * Author: jianqing
 * Date: Aug 7, 2020
 * Description: This document is created for fquestion.
 */
package com.vocab85.model.bmcsys;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 *
 * @author jianqing
 */
public class FQuestionInfo
{
    private String fQuestionChildNo,
            fOriginalPic,
            fQuestionText,
            fCorrectAnswer;

    public FQuestionInfo(String fQuestionChildNo, String fOriginalPic, String fQuestionText, String fCorrectAnswer)
    {
        this.fQuestionChildNo = fQuestionChildNo;
        this.fOriginalPic = fOriginalPic;
        this.fQuestionText = fQuestionText;
        this.fCorrectAnswer = fCorrectAnswer;
    }

    public FQuestionInfo()
    {
        this.fQuestionChildNo = null;
        this.fOriginalPic = null;
        this.fQuestionText = null;
        this.fCorrectAnswer = null;
    }

    public String getfQuestionChildNo()
    {
        return fQuestionChildNo;
    }

    public void setfQuestionChildNo(String fQuestionChildNo)
    {
        this.fQuestionChildNo = fQuestionChildNo;
    }

    public String getfOriginalPic()
    {
        return fOriginalPic;
    }

    public void setfOriginalPic(String fOriginalPic)
    {
        this.fOriginalPic = fOriginalPic;
    }

    public String getfQuestionText()
    {
        return fQuestionText;
    }

    public void setfQuestionText(String fQuestionText)
    {
        this.fQuestionText = fQuestionText;
    }

    public String getfCorrectAnswer()
    {
        return fCorrectAnswer;
    }

    public void setfCorrectAnswer(String fCorrectAnswer)
    {
        this.fCorrectAnswer = fCorrectAnswer;
    }
    
    public String getEncodedQuestionChildNo() throws UnsupportedEncodingException
    {
        return URLEncoder.encode(fQuestionChildNo, "UTF-8");
    }

    @Override
    public String toString()
    {
        return "FQuestionInfo{" + "fQuestionChildNo=" + fQuestionChildNo + ", fOriginalPic=" + fOriginalPic + ", fQuestionText=" + fQuestionText + ", fCorrectAnswer=" + fCorrectAnswer + '}';
    }

    
    public static void main(String[] args) throws UnsupportedEncodingException
    {
        FQuestionInfo i = new FQuestionInfo();
        i.setfQuestionChildNo("北美");
        System.out.println(i.getEncodedQuestionChildNo());
    }
    
    
    
    
}
