/*
 * Author: Jianqing Gao
 * Date: Aug 25, 2020
 * Description: This document is created for url encoder to use in JSP Pages.
 */
package com.vocab85.model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * URL Encoder to use within JSP pages.
 * @author Jianqing Gao
 */
public class Encoder

{

    public Encoder()
    {
    }

    public static String encode(String str) throws UnsupportedEncodingException
    {
        return URLEncoder.encode(str, "UTF-8");
    }

    public static boolean isValidStrings(String... checkStrings)
    {
        for (String checkString : checkStrings)
        {
            if (checkString == null || checkString.isEmpty())
            {
                return false;
            }
        }
        return true;
    }
}
