/*
 * Author: jianqing
 * Date: Aug 7, 2020
 * Description: This document is created for communicating with BMC system.
 */
package com.vocab85.model.network;

import cn.hutool.http.HttpUtil;
import cn.hutool.setting.Setting;
import com.vocab85.model.Randomizer;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Communication with BMC system, sending requests. REST model.
 *
 * @author Jianqing Gao
 */
public class Communicator
{

    private int audioListenCounter = 0;
    private String token, fwebkitsn;

    private boolean testMode = true;

    public final static String DEFAULT_WEBKITSN = "bmc_stu_protal";

    public final static String UTF_ENCODING = "UTF-8";

    public Communicator(String token, String fwebkitsn)
    {
        this.token = token;
        this.fwebkitsn = fwebkitsn;
    }

    public Communicator(String token)
    {
        this.token = token;
        this.fwebkitsn = DEFAULT_WEBKITSN;
    }

    public Communicator()
    {
        token = null;
        fwebkitsn = null;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public String getFwebkitsn()
    {
        return fwebkitsn;
    }

    public void setFwebkitsn(String fwebkitsn)
    {
        this.fwebkitsn = fwebkitsn;
    }

    public boolean isTestMode()
    {
        return testMode;
    }

    public void setTestMode(boolean testMode)
    {
        this.testMode = testMode;
    }

    public String getStudentLoginInfo(String id, String pass, String companyId) throws IOException, InterruptedException, InterruptedException
    {
        String url = "https://server.1000classes.com/bmcserver/secordCustLogin.do?fcustlogin=" + id + "&floginpwd=" + pass + "&fcompanyid=" + companyId;
        return openUrlConnection(url);
    }

    public String getStudentInfoJSONString() throws IOException, InterruptedException
    {
        String url = "https://server.1000classes.com/bmcserver/studentDetail.do";
        return openUrlConnection(url);
    }

    /**
     * 获得某一份作业的下载链接
     *
     * @param fDate
     * @return
     * @throws IOException
     * @throws java.lang.InterruptedException
     */
    public String getStudentHomeworkDownloadLink(String fDate) throws IOException, InterruptedException
    {
        String link = "https://server.1000classes.com/bmcserver/thirdCreateCustHomeworkPager.do?fdate=" + fDate;
        return openUrlConnection(link);
    }

    /**
     * 获取学生错题表.针对某一份作业，通过fid来定位。
     *
     * @param fhomeworkid
     * @return
     * @throws IOException
     * @throws java.lang.InterruptedException
     */
    public String getStudentWrongQuestionList(String fhomeworkid) throws IOException, InterruptedException
    {
        String link = "https://server.1000classes.com/bmcserver/secordAnpaiHomeworkTestResult.do?fhomeworkid=" + fhomeworkid;
        return openUrlConnection(link);

    }

    /**
     * 获取某一道题的错题细节。
     *
     * @param fQuestionChildNo
     * @return
     * @throws IOException
     * @throws java.lang.InterruptedException
     */
    public String getSingleQuestionInfo(String fQuestionChildNo, boolean istruck) throws IOException, InterruptedException
    {
        String link = "https://server.1000classes.com/bmcserver/student" + (istruck ? "" : "Notrunck") + "ErrorquestionDetail.do?fquestionchildno=" + fQuestionChildNo;
        return openUrlConnection(link);

    }

    /**
     * 获得学生某天作业列表。
     *
     * @param fDate
     * @return
     * @throws MalformedURLException
     * @throws IOException
     * @throws java.lang.InterruptedException
     */
    public String getStudentScheduleTaskList(String fDate) throws MalformedURLException, IOException, InterruptedException
    {
        String link = "https://server.1000classes.com/bmcserver/studentScheduleTaskList.do?fdate=" + fDate;

        return openUrlConnection(link);

    }

    public String listenAudioCount(String questionNo) throws IOException, InterruptedException
    {
        return openUrlConnection("https://server.1000classes.com/bmcserver/thirdCustListenAudio.do?fquestionno=" + questionNo);
    }

    public void testListenAudio(int times, int sec, String[] question)
    {

        if (question.length > 0)
        {

            Timer timer = new Timer(sec, (ActionEvent e) ->
            {
                try
                {

                    String gonnaString = question[Randomizer.randomInt(0, question.length - 1)];
                    System.out.print(gonnaString + ", result ");
                    System.out.println(audioListenCounter + listenAudioCount(urlEncodeString(gonnaString)));
                    audioListenCounter++;
                } catch (IOException | InterruptedException ex)
                {
                    Logger.getLogger(Communicator.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ArrayIndexOutOfBoundsException ioobe)
                {
                    System.out.println("Bah, there is no question here. Please choose another one.");
                }

                if (audioListenCounter == times)
                {
                    Timer t = (Timer) e.getSource();
                    t.stop();
                    System.exit(0);
                }
            });
            timer.setRepeats(true);
            timer.setDelay(sec);
            timer.setInitialDelay(5);
            timer.start();

        } else
        {
            System.out.println("Bah, there is no question here. Please choose another one.");
        }
    }

    //public String getStudentHomework(String )
    private String openUrlConnection(String normalResource) throws IOException, InterruptedException
    {
        URL url;
        URLConnection conn;
        if (!testMode)
        {
            // url = getClass().getResource(testingResource);
            url = new URL(normalResource);
            conn = url.openConnection();

            conn.addRequestProperty("token", token);

            conn.addRequestProperty("fwebkitsn", fwebkitsn);

            conn.connect();
            return IOUtils.toString(conn.getInputStream(), UTF_ENCODING);
        } else
        {
            String str = Commander.executeCommand("curl -H token:" + token + " -H fwebkitsn:" + fwebkitsn + " -X GET " + normalResource);
            return str;
        }
        //return conn;
    }

    public static void main(String[] args) throws IOException, InterruptedException, ParseException
    {
        Setting creds = new Setting("test.setting");
        Communicator com = new Communicator(creds.get("token"), DEFAULT_WEBKITSN);
        //String openUrlConnection = com.openUrlConnection("http://server.1000classes.com/bmcserver/studentCustPhotoUpdate.do?fpicurl=http://server.1000classes.com/bmcserver/attachment/2020-08/344266bb-98d6-4c29-96a5-96916c050e0e.jpeg");
        //System.out.println(com.getStudentScheduleTaskList("2020-07-28"));
        // System.out.println(com.openUrlConnection("https://server.1000classes.com/bmcserver/studentScheduleTaskList.do?fdate=2020-08-01"));
        //System.out.println(com.openUrlConnection("https://server.1000classes.com/bmcserver/studentScheduleTaskList.do?fdate=2020-08-01"));
        //System.out.println(Arrays.toString());
        String studentScheduleTaskList = com.getStudentScheduleTaskList("2020-11-05");
        JSONParser parser = new JSONParser();
        JSONObject objjson = (JSONObject) parser.parse(studentScheduleTaskList);
        JSONObject map = (JSONObject) objjson.get("map");
        JSONArray list1 = (JSONArray) map.get("list1");
        for (int i = 0; i < list1.size(); i++)
        {
            JSONObject homework = (JSONObject) list1.get(i);
            System.out.println("ID: " + homework.get("fid"));
            System.out.println("Title: " + homework.get("ftasktitle"));
            System.out.println("Name:" + homework.get("ftaskname"));
            System.out.println("Project" + homework.get("fprojectname"));
        }
        System.out.println(list1);
        //com.testListenAudio(520, 10, com.getWrongQuestionIdList("5DCCEFB6-10ED-4F67-83AE-6D27E8EB1FE0"));
        //System.out.println(openUrlConnection);
    }

    public String[] getWrongQuestionIdList(String fhwid) throws ParseException, IOException, InterruptedException
    {
        String questionsJSONString = getStudentWrongQuestionList(fhwid);
        JSONParser parser = new JSONParser();

        JSONObject questionsJSON, questionJSONMap, indivisualJSON;
        JSONArray listJSONArray;

        System.out.println("*Get Wrong Questions List*");
        questionsJSON = (JSONObject) parser.parse(questionsJSONString);

        questionJSONMap = (JSONObject) questionsJSON.get("map");

        listJSONArray = (JSONArray) questionJSONMap.get("list1");

        String[] data = new String[listJSONArray.size()];

        for (int i = 0; i < listJSONArray.size(); i++)
        {
            indivisualJSON = (JSONObject) listJSONArray.get(i);
            data[i] = (String) indivisualJSON.get("fquestionchildno");
        }
        return data;
    }

    public void generateTrueRandomInteger(int min, int max) throws IOException
    {
        String address = "https://www.random.org/integers/?num=50&min=" + min + "&max=" + max + "&col=1&base=10&format=plain&rnd=new";

        String res = HttpUtil.get(address);

        System.out.println(res);
    }

    public static String urlEncodeString(String input) throws UnsupportedEncodingException
    {
        return URLEncoder.encode(input, StandardCharsets.UTF_8.toString());
    }

}
