/*
 * Author: jianqing
 * Date: Aug 12, 2020
 * Description: This document is created for
 */
package com.vocab85.model.bmcsys;

/**
 *
 * @author jianqing
 */
public class FQuestionCorrection
{

    private String title;
    private String content;
    private int type;
    final static String TITLE_STUCORRECT = "我的订正";
    final static String TITLE_STDCORRECT = "标准解析流程";
    //int type;
    
    static final int TYPE_STUDENT_CORRECTION = 0;
    
    final static int TYPE_STANDARD_SOLUTION = 1;

    public FQuestionCorrection()
    {
    }

    public FQuestionCorrection(String title, String content)
    {
        this.title = title;
        this.content = content;
    }

    
    public String getContent()
    {
        if (title.equals(TITLE_STUCORRECT))
        {
            return "<img class=\"full-width\" src=\"" + content + "\" alt=\"student's correction\">";
        } else
        {
            return content;
        }
    }

    public String getTitle()
    {
        return title;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public int getType()
    {
        return type;
    }

    
    
    
    
}
