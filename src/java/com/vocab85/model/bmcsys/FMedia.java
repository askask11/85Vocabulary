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
public class FMedia
{
    private String fMediaName,
            fMediaUrl,
            fType;

    public FMedia()
    {
        fMediaName = null;
        fMediaUrl = null;
        fType = null;
    }

    public FMedia(String fMediaName, String fMediaUrl, String fType)
    {
        this.fMediaName = fMediaName;
        this.fMediaUrl = fMediaUrl;
        this.fType = fType;
    }

    public String getfMediaName()
    {
        return fMediaName;
    }

    public void setfMediaName(String fMediaName)
    {
        this.fMediaName = fMediaName;
    }

    public String getfMediaUrl()
    {
        return fMediaUrl;
    }

    public void setfMediaUrl(String fMediaUrl)
    {
        this.fMediaUrl = fMediaUrl;
    }

    public String getfType()
    {
        return fType;
    }

    public void setfType(String fType)
    {
        this.fType = fType;
    }

    @Override
    public String toString()
    {
        return "FMedia{" + "fMediaName=" + fMediaName + ", fMediaUrl=" + fMediaUrl + ", fType=" + fType + '}';
    }
    
    
    
}
