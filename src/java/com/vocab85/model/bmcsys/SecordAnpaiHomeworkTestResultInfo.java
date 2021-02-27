/*
 * Author: jianqing
 * Date: Aug 7, 2020
 * Description: This document is created for
 */
package com.vocab85.model.bmcsys;

/**
 *
 * @author jianqing
 */
public class SecordAnpaiHomeworkTestResultInfo
{
    private String fHomework,errorNum, emptyNum, correctNum;

    public SecordAnpaiHomeworkTestResultInfo()
    {
    }

    public SecordAnpaiHomeworkTestResultInfo(String fHomework, String errorNum, String emptyNum, String correctNum)
    {
        this.fHomework = fHomework;
        this.errorNum = errorNum;
        this.emptyNum = emptyNum;
        this.correctNum = correctNum;
    }

    public void setCorrectNum(String correctNum)
    {
        this.correctNum = correctNum;
    }

    public void setEmptyNum(String emptyNum)
    {
        this.emptyNum = emptyNum;
    }

    public void setErrorNum(String errorNum)
    {
        this.errorNum = errorNum;
    }

    public void setfHomework(String fHomework)
    {
        this.fHomework = fHomework;
    }

    public String getCorrectNum()
    {
        return correctNum;
    }

    public String getEmptyNum()
    {
        return emptyNum;
    }

    public String getErrorNum()
    {
        return errorNum;
    }

    public String getfHomework()
    {
        return fHomework;
    }

    @Override
    public String toString()
    {
        return "SecordAnpaiHomeworkTestResultInfo{" + "fHomework=" + fHomework + ", errorNum=" + errorNum + ", emptyNum=" + emptyNum + ", correctNum=" + correctNum + '}';
    }
    
    
    
    
}
