/*
 * Author: jianqing
 * Date: Aug 7, 2020
 * Description: This document is created for fquestion.
 */
package com.vocab85.model.bmcsys;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author jianqing gao
 */
public class FQuestion
{

    /**
     * The 题干 of every questions.
     */
    private ArrayList<String> fOriginalPics;

    /**
     * The tag for user's wrong question. 错题标签
     */
    private ArrayList<String> fTagNames;

    /**
     * 错题订正图片url.
     */
    private ArrayList<String> fPicUrls;

    /**
     * 本题的错题信息集合（一般默认为1）.
     */
    private ArrayList<FQuestionInfo> fQuestionInfos;

    /**
     * 老师的错题解析 文字 (default=1)
     */
    private ArrayList<FAnalysis> fAnalysises;

    /**
     * 这个问题的唯一ID.
     */
    private String fQuestionChildNo;

    /**
     * 错题解析的录音视频（默认1）.
     *
     */
    private ArrayList<FMedia> fMedias;

    private String isTruck;

    private int serial = 0;

    private String rightText = "";

    private String studentAnswer = "";

    private double errorRatio = 0;

    public FQuestion()
    {
        fOriginalPics = null;
        fTagNames = null;
        fPicUrls = null;
        fQuestionInfos = null;
        fAnalysises = null;
        fMedias = null;
    }

    public static FQuestion getDefaultInstance()
    {
        return new FQuestion(new ArrayList<>(5),
                new ArrayList<>(5),
                new ArrayList<>(5),
                new ArrayList<>(1),
                new ArrayList<>(1),
                "",
                new ArrayList<>(1),
                "");
    }

    public FQuestion(ArrayList<String> fOriginalPics,
            ArrayList<String> fTagNames,
            ArrayList<String> fPicUrls,
            ArrayList<FQuestionInfo> fQuestionInfos,
            ArrayList<FAnalysis> fAnalysises,
            String fQuestionChildNo,
            ArrayList<FMedia> fMedias,
            String fIsTruck)
    {
        this.fOriginalPics = fOriginalPics;
        this.fTagNames = fTagNames;
        this.fPicUrls = fPicUrls;
        this.fQuestionInfos = fQuestionInfos;
        this.fAnalysises = fAnalysises;
        this.fQuestionChildNo = fQuestionChildNo;
        this.fMedias = fMedias;
        this.isTruck = fIsTruck;
    }

    public ArrayList<String> getfOriginalPics()
    {
        return fOriginalPics;
    }

    public void setfOriginalPics(ArrayList<String> fOriginalPics)
    {
        this.fOriginalPics = fOriginalPics;
    }

    public ArrayList<String> getfTagNames()
    {
        return fTagNames;
    }

    public void setfTagNames(ArrayList<String> fTagNames)
    {
        this.fTagNames = fTagNames;
    }

    public ArrayList<String> getfPicUrls()
    {
        return fPicUrls;
    }

    public void setfPicUrls(ArrayList<String> fPicUrls)
    {
        this.fPicUrls = fPicUrls;
    }

    public ArrayList<FQuestionInfo> getfQuestionInfos()
    {
        return fQuestionInfos;
    }

    public void setfQuestionInfos(ArrayList<FQuestionInfo> fQuestionInfos)
    {
        this.fQuestionInfos = fQuestionInfos;
    }

    public ArrayList<FAnalysis> getfAnalysises()
    {
        return fAnalysises;
    }

    public void setfAnalysises(ArrayList<FAnalysis> fAnalysises)
    {
        this.fAnalysises = fAnalysises;
    }

    public String getfQuestionChildNo()
    {
        return fQuestionChildNo;
    }

    public void setfQuestionChildNo(String fQuestionChildNo)
    {
        this.fQuestionChildNo = fQuestionChildNo;
    }

    public ArrayList<FMedia> getfMedias()
    {
        return fMedias;
    }

    public void setfMedias(ArrayList<FMedia> fMedias)
    {
        this.fMedias = fMedias;
    }

    public void setIsTruck(String isTruck)
    {
        this.isTruck = isTruck;
    }

    public void setRightText(String rightText)
    {
        this.rightText = rightText;
    }

    public void setSerial(int serial)
    {
        this.serial = serial;
    }

    public String getRightText()
    {
        return rightText;
    }

    public int getSerial()
    {
        return serial;
    }

    public String getStudentAnswer()
    {
        return studentAnswer;
    }

    public void setStudentAnswer(String studentAnswer)
    {
        this.studentAnswer = studentAnswer;
    }

    public void setErrorRatio(double errorRatio)
    {
        this.errorRatio = errorRatio;
    }

    public double getErrorRatio()
    {
        return errorRatio;
    }

    @Override
    public String toString()
    {
        return "FQuestion{" + "fOriginalPics=" + fOriginalPics + ", fTagNames=" + fTagNames + ", fPicUrls=" + fPicUrls + ", fQuestionInfos=" + fQuestionInfos + ", fAnalysises=" + fAnalysises + ", fQuestionChildNo=" + fQuestionChildNo + ", fMedias=" + fMedias + ", isTruck=" + isTruck + ", serial=" + serial + ", rightText=" + rightText + ", studentAnswer=" + studentAnswer + '}';
    }

    public boolean isTruck()
    {
        return this.isTruck.equals("1");
    }

    /**
     * 生成订正table
     *@deprecated 
     * @return
     */
    public HashMap<String, ArrayList<String>> imgsrcMap()
    {
        HashMap<String, ArrayList<String>> map; //= new HashMap<>();

        int lengthFpic = fPicUrls.size();

        int lengthAnalysis = fAnalysises.size();

        ArrayList<String> analyzeList = new ArrayList<>(lengthAnalysis);

        for (int i = 0; i < lengthAnalysis; i++)
        {
            analyzeList.add(fAnalysises.get(i).getfAnalysisText());
        }

        //int totalLength = lengthFpic + lengthAnalysis;
        map = new HashMap<>(2);

        map.put("我的订正", fPicUrls);

        map.put("标准解析流程", analyzeList);

//        
//        for (int i = 0; i < lengthFpic; i++)
//        {
//            map.put("我的订正", fPicUrls.get(i));
//        }
//
//        for (int i = 0; i < lengthAnalysis; i++)
//        {
//            map.put("标准解析流程", fAnalysises.get(i).getfAnalysisText());
//        }
        return map;
    }

    /**
     *
     * @return
     */
    public ArrayList<FQuestionCorrection> getImageToShow()
    {
        FQuestionCorrection imageToShow;//= new ImageToShow();
        int lengthFpic = fPicUrls.size();
        int lengthAnalysis = fAnalysises.size();
        int totalLength = lengthAnalysis + lengthFpic;
        
        ArrayList<FQuestionCorrection> list = new ArrayList<>(totalLength);
        
         for (int i = 0; i < lengthFpic; i++)
        {
           // map.put("我的订正", fPicUrls.get(i));
            imageToShow = new FQuestionCorrection();
            imageToShow.setTitle(FQuestionCorrection.TITLE_STUCORRECT);
            imageToShow.setContent(fPicUrls.get(i));//la
            imageToShow.setType(FQuestionCorrection.TYPE_STUDENT_CORRECTION);
            list.add(imageToShow);
        }

        for (int i = 0; i < lengthAnalysis; i++)
        {
            //map.put("标准解析流程", fAnalysises.get(i).getfAnalysisText());
            imageToShow = new FQuestionCorrection();
            imageToShow.setTitle(FQuestionCorrection.TITLE_STDCORRECT);
            imageToShow.setContent(fAnalysises.get(i).getfAnalysisText());
            imageToShow.setType(FQuestionCorrection.TYPE_STANDARD_SOLUTION);
            list.add(imageToShow);
        }
        
        
        return list;
    }
    
    public String getVomitingBloodSummary()
    {
        String sm = "";
        for (int i = 0; i < fAnalysises.size(); i++)
        {
            sm+=fAnalysises.get(i).getfSummary() + "";
        }
        return sm;
    }

}


