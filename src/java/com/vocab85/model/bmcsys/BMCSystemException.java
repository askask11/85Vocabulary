/*
 * Author: jianqing
 * Date: Aug 8, 2020
 * Description: This document is created for
 */
package com.vocab85.model.bmcsys;

/**
 *
 * @author jianqing
 */
public class BMCSystemException extends Exception
{

    private Long result;
    /**
     * Creates a new instance of <code>BMCSystemException</code> without detail
     * message.
     */
    public BMCSystemException()
    {
    }

    /**
     * Constructs an instance of <code>BMCSystemException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public BMCSystemException(String msg)
    {
        super(msg);
    }
    
    public BMCSystemException(String msg,  Long result)
    {
        super(msg + ", system result result = " + result);
        this.result = result;
    }

    public void setResult(Long result)
    {
        this.result = result;
    }

    public Long getResult()
    {
        return result;
    }

    
    
}
