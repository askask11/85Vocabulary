/*
 * Author: jianqing
 * Date: Aug 7, 2020
 * Description: This document is created for
 */
package com.vocab85.model.bmcsys;

import com.google.gson.Gson;
import java.util.ArrayList;

/**
 *
 * @author jianqing
 */
public class SecordAnpaiHomeworkTestResult
{

    private ArrayList<SecordAnpaiHomeworkTestResultInfo> infos;

    private ArrayList<FQuestion> fQuestions;

    public static final String FTYPE_HANDIN = "1";

    public static final String FSTATUS_HANDED = "1",
            FSTATUS_NOT_HANDED = "0";

    private String fId, fStatus, fType;
    
   

    public SecordAnpaiHomeworkTestResult()
    {
        infos = null;
        fQuestions = null;
        fId = null;
        fStatus = null;
        fType = null;
    }

    public SecordAnpaiHomeworkTestResult(ArrayList<SecordAnpaiHomeworkTestResultInfo> infos, ArrayList<FQuestion> fQuestions)
    {
        this.infos = infos;
        this.fQuestions = fQuestions;
    }

    public static SecordAnpaiHomeworkTestResult getDefaultInstance()
    {
        return new SecordAnpaiHomeworkTestResult(new ArrayList<>(1), new ArrayList<>());
    }

    public SecordAnpaiHomeworkTestResult(ArrayList<SecordAnpaiHomeworkTestResultInfo> infos, ArrayList<FQuestion> fQuestions, String fId)
    {
        this.infos = infos;
        this.fQuestions = fQuestions;
        this.fId = fId;
    }

    public static SecordAnpaiHomeworkTestResult getDefaultInstance(String fId)
    {
        SecordAnpaiHomeworkTestResult r = getDefaultInstance();
        r.setfId(fId);
        return r;
    }

    public static SecordAnpaiHomeworkTestResult getInstance(String fType, String fStatus, String fId)
    {
        if (fType.equals(FTYPE_HANDIN) && fStatus.equals(FSTATUS_HANDED))
        {
            SecordAnpaiHomeworkTestResult result = getDefaultInstance(fId);
            result.setfType(fType);
            result.setfStatus(fStatus);
            return result;
        } else
        {
            //if the homework is not handed or is not supported type,
            //don't create any info inside to save space.
            SecordAnpaiHomeworkTestResult r = getDefaultInstance();

            r.setfId(fId);
            r.setfType(fType);
            r.setfStatus(fStatus);
            return r;
        }
    }

    public String getfStatus()
    {
        return fStatus;
    }

    public void setfStatus(String fStatus)
    {
        this.fStatus = fStatus;
    }

    public String getfType()
    {
        return fType;
    }

    public void setfType(String fType)
    {
        this.fType = fType;
    }

    public ArrayList<SecordAnpaiHomeworkTestResultInfo> getInfos()
    {
        return infos;
    }

    public void setInfos(ArrayList<SecordAnpaiHomeworkTestResultInfo> infos)
    {
        this.infos = infos;
    }

    public ArrayList<FQuestion> getfQuestions()
    {
        return fQuestions;
    }

    public void setfQuestions(ArrayList<FQuestion> fQuestions)
    {
        this.fQuestions = fQuestions;
    }

    public String getfId()
    {
        return fId;
    }

    public void setfId(String fId)
    {
        this.fId = fId;
    }
    
    /**
     * See if the instance of the homework is eligible for generating a fq book.
     * @return 
     */
    public boolean isValid()
    {
        return fType.equals(FTYPE_HANDIN) && fStatus.equals(FSTATUS_HANDED);
    }

    @Override
    public String toString()
    {
        return "SecordAnpaiHomeworkTestResult{" + "infos=" + infos + ", fQuestions=" + fQuestions + ", fId=" + fId + ", fStatus=" + fStatus + ", fType=" + fType + '}' + '\n';
    }

    public static void main(String[] args)
    {
        SecordAnpaiHomeworkTestResult r = SecordAnpaiHomeworkTestResult.getDefaultInstance();
        r.getfQuestions().add(new FQuestion());
        System.out.println(new Gson().toJson(r));
    }
    

}
