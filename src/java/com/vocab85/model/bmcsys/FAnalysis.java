/*
 * Author: jianqing
 * Date: Aug 7, 2020
 * Description: This document is created for
 */
package com.vocab85.model.bmcsys;

/**
 * This is the analysis text of an fquestion.
 * @author jianqing gao
 */
public class FAnalysis
{
    private String fAnalysisText,fSummary;

    public FAnalysis()
    {
        fAnalysisText = null;
        fSummary = null;
    }

    public FAnalysis(String fAnalysisText, String fSummary)
    {
        this.fAnalysisText = fAnalysisText;
        this.fSummary = fSummary;
    }

    public String getfAnalysisText()
    {
        return fAnalysisText;
    }

    public void setfAnalysisText(String fAnalysisText)
    {
        this.fAnalysisText = fAnalysisText;
    }

    public String getfSummary()
    {
        return fSummary;
    }

    public void setfSummary(String fSummary)
    {
        this.fSummary = fSummary;
    }

    @Override
    public String toString()
    {
        return "FAnalysis{" + "fAnalysisText=" + (fAnalysisText==null?"null":"some text") + ", fSummary=" + fSummary + '}';
    }
    
    
    
}
