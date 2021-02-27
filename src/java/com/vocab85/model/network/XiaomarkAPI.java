/*
 * Author: Jianqing Gao
 * Date: Dec 19, 2020
 * Description: This document is created for sending requests to Xiaomark APIs for creating short links.
 */
package com.vocab85.model.network;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import java.io.IOException;

/**
 * This class is responsible for creating short links with Xiaomark API.
 *
 * @author jianqing
 */
public class XiaomarkAPI
{

    private int code, httpCode;

    private String message, url;

    public XiaomarkAPI(int code, String message, String url)
    {
        this.code = code;
        this.message = message;
        this.url = url;
    }

    public XiaomarkAPI(int code, int httpCode, String message, String url)
    {
        this.code = code;
        this.httpCode = httpCode;
        this.message = message;
        this.url = url;
    }

    public static XiaomarkAPI generateXiaomarkUrl(String token, String originalURL) throws InterruptedException, IOException
    {
        //declear variables
        int code;
        String message, url;
        HttpRequest request = HttpUtil.createPost("http://api.xiaomark.com/v1/link/create");
        HttpResponse response = null;
        //prepare data to launch
        JSONObject responseJSON, responseDataJSON, responseLinkJSON;
        JSONObject requestJSON = JSONUtil.createObj();
        requestJSON.putOpt("apikey", token);
        requestJSON.putOpt("origin_url", originalURL);
        request.body(requestJSON.toStringPretty());
        request.contentType("application/json");
        //send request
        response = request.execute();

        responseJSON = JSONUtil.parseObj(response.body());

        responseDataJSON = responseJSON.get("data", JSONObject.class);
        responseLinkJSON = responseDataJSON.get("link", JSONObject.class);
        if (responseLinkJSON != null)
        {
            url = responseLinkJSON.getStr("url");
        } else
        {
            url = null;
        }

        code = responseJSON.getInt("code");
        //httpCode = response.getStatus();
        message = responseJSON.getStr("message");

        return new XiaomarkAPI(code, code == 0 ? 200 : 403, message, url);
    }

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    @Override
    public String toString()
    {
        return "XiaomarkAPI{" + "code=" + code + ", httpCode=" + httpCode + ", message=" + message + ", url=" + url + '}';
    }

    public static void main(String[] args)
    {
        //System.out.println(generateXiaomarkUrl("20f43b4dd9fe047b9c03831b6bcf7561", "http://85vocab.com"));
    }
}
