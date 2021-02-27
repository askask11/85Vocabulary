/*
 * Author: Jianqing Gao
 * Date: Dec 5, 2020
 * Description: This document is created for translating excels.
 */
package com.vocab85.model;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author jianqing
 */
public class FileManager
{

    public static void readFileUrl(File target, InputStream url) throws IOException
    {
        try (BufferedInputStream inputStream = new BufferedInputStream(url);
                FileOutputStream fileOS = new FileOutputStream(target))
        {
            byte data[] = new byte[1024];
            int byteContent;
            while ((byteContent = inputStream.read(data, 0, 1024)) != -1)
            {
                fileOS.write(data, 0, byteContent);
            }
        }
    }
}
