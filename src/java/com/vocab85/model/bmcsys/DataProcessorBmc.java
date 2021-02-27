/*
 * Author: jianqing
 * Date: Aug 8, 2020
 * Description: This document is created for
 */
package com.vocab85.model.bmcsys;

//import com.github.cliftonlabs.json_simple.Jsoner;
import com.vocab85.model.network.Communicator;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author jianqing
 */
public class DataProcessorBmc
{

    private String token;
    
    public static String BMC_COMPANY_ID="A925AF54-BB87-474E-A924-350A7F124C71";

    public DataProcessorBmc()
    {
        this.token  = null;
    }

    public DataProcessorBmc(String token)
    {
        this.token = token;
    }
    
    public String getBmcStuToken(String id, String pass) throws IOException, InterruptedException, ParseException
    {
        //String url = "https://server.1000classes.com/bmcserver/secordCustLogin.do?fcustlogin="+id+"&floginpwd="+pass+"&fcompanyid="+BMC_COMPANY_ID;
        String loginJSON = new Communicator(token).getStudentLoginInfo(id, pass, BMC_COMPANY_ID);
        JSONParser parser = new JSONParser();
        
        JSONObject rootJSONObject = (JSONObject)parser.parse(loginJSON);
        JSONObject loginMapJSONObject = (JSONObject)rootJSONObject.get("map");
        String token = (String)loginMapJSONObject.get("token");
        return token;
    }
    
    public String getStudentName()throws IOException, InterruptedException, ParseException
    {
        String infoJSONString = new Communicator(token).getStudentInfoJSONString();
        JSONParser parser = new JSONParser();
        JSONObject infoJSONObject = (JSONObject)parser.parse(infoJSONString);
        JSONObject infoMapJSONObject = (JSONObject)infoJSONObject.get("map");
        JSONArray infoListArray = (JSONArray)infoMapJSONObject.get("list");
        
        if(infoListArray.size()>=1)
        {
            JSONObject stuInfoListJSONObject = (JSONObject)infoListArray.get(0);
            return (String) stuInfoListJSONObject.get("fcustname");
        }else
        {
            return null;
        }
        
    }
    
    

    /**
     * 获取学生当天错题数据
     *
     * @param fDate
     * @throws java.io.IOException
     * @throws org.json.simple.parser.ParseException
     * @throws com.vocab85.model.bmcsys.BMCSystemException
     * @throws java.lang.InterruptedException
     */
     public ArrayList<SecordAnpaiHomeworkTestResult> getStudentFQuestions(String fDate) throws IOException, ParseException, BMCSystemException, InterruptedException
    {

        //The list of howework of a student in a certain day
        ArrayList<SecordAnpaiHomeworkTestResult> hwList;//= new ArrayList<>(3);

        //create a new communicator instance.
        Communicator communicator = new Communicator(token);

        JSONParser parser = new JSONParser();

        //get the homework list detail of a day to process.当天作业，层级1
        String hwListJsonString = communicator.getStudentScheduleTaskList(fDate);

        //read the JSONObject of the test result
        hwList = readHomeworkTestResults(parser, hwListJsonString);

        System.out.println("Homework Task Received and parsed into arraylist");
        /**
         * Start to get the homework detail for each of them*
         *
         * this will be repeated i times if that day has i numbers of homework
         */
        //go through the homeworks
        //System.out.println(hwList);
        for (int i = 0; i < hwList.size(); i++)
        {

            System.out.println("+作业" + (i + 1) + "/" + hwList.size());
            //get a single homework
            SecordAnpaiHomeworkTestResult result1 = hwList.get(i);

            //see if the single homework is eligible for going further
            //if is valid
                //System.out.println("合格作业，继续");
                //get the question list of a homework

                //parse the whole thing into a json obj
                String homeworkListJSONString = communicator.getStudentWrongQuestionList(result1.getfId());
                JSONObject homeworkListJSONObject = (JSONObject) parser.parse(homeworkListJSONString);
                JSONObject homeworkListMapJSONObject = (JSONObject) homeworkListJSONObject.get("map");
                //System.out.println("Wrong question list got and parsed");
                //then get the map 

                //System.out.println(homeworkListJSONObject);
                //check the status
                if ((Long) homeworkListMapJSONObject.get("result") != 1)
                {
                    handleSystemException(homeworkListJSONObject);
                }

                //get the default instance of the homework
                //ArrayList<SecordAnpaiHomeworkTestResultInfo> infoList = result1.getInfos();
                //ArrayList<FQuestion> questionList = result1.getfQuestions();
                ///////////////////////////////////////////////////////////////////
                //LIST0
                ///////////////////////////////////////////////////////////////////
                JSONArray hwInfoArray = (JSONArray) homeworkListMapJSONObject.get("list");

                //////////get the infos of the howework
                //get the infos of the homework
                for (int j = 0; j < hwInfoArray.size(); j++)
                {
                    System.out.println("--作业信息" + (j + 1) + "/" + hwInfoArray.size());
                    //get the json object of single info
                    JSONObject singleInfoObject = (JSONObject) hwInfoArray.get(j);

                    //create an instance of the info
                    SecordAnpaiHomeworkTestResultInfo hwInfo = new SecordAnpaiHomeworkTestResultInfo();
                    //write into the instance
                    hwInfo.setCorrectNum((String) singleInfoObject.get("rightnum"));
                    hwInfo.setEmptyNum((String) singleInfoObject.get("emptynum"));
                    hwInfo.setErrorNum((String) singleInfoObject.get("errornum"));
                    hwInfo.setfHomework((String) singleInfoObject.get("fhomework"));//homework name 这一题集合的名称
                    result1.getInfos().add(hwInfo);//add back to the list
                    //System.out.println("write one object into the info list");
                    //System.out.println(infoList.size());
                }

                ///////////////////////////////////////////////////////////////////
                //LIST1
                ///////////////////////////////////////////////////////////////////
                ///////////get the questions of the homework
                JSONArray fquestionArray = (JSONArray) homeworkListMapJSONObject.get("list1");

                System.out.println("--获取题目列表");
                for (int j = 0; j < fquestionArray.size(); j++)
                {
                    System.out.println("--题目 " + (j + 1) + "/" + fquestionArray.size());
                    //System.out.println(j);
                    //as a whole
                    JSONObject fquestionJSONObject = (JSONObject) fquestionArray.get(j);
                    FQuestion question = FQuestion.getDefaultInstance();

                    /**
                     * **Processing of single question***
                     */
                    //get the ID of  each question
                    //写入question
                    String fquestionchildno = (String) fquestionJSONObject.get("fquestionchildno");
                    question.setIsTruck((String) fquestionJSONObject.get("fistruck"));
                    question.setRightText((String) fquestionJSONObject.get("frighttxt"));
                    question.setStudentAnswer((String) fquestionJSONObject.get("fanswer"));
                    question.setSerial(Integer.parseInt((String) fquestionJSONObject.get("fserial")));
                    // System.out.println(fquestionchildno);

                    //use the question id to locate the question,
                    //download the json object from server.
                    String fquestionJSONString = communicator.getSingleQuestionInfo(Communicator.urlEncodeString(fquestionchildno), question.isTruck());

                    System.out.println(fquestionJSONString);
                    System.out.println("--" + fquestionchildno);
                    //System.out.println(fquestionJSONString);
                    //题干 images
                    //parse the single question into a json array

                    JSONObject fQuestionJSONObject = (JSONObject) parser.parse(fquestionJSONString);
                    JSONObject singleQuestionMapJSONObject = (JSONObject) fQuestionJSONObject.get("map");
                    JSONArray singleQuestionListJSONArray = (JSONArray) singleQuestionMapJSONObject.get("list");

                    //loop through the 题干
                    if (question.isTruck())
                    {
                        for (int k = 0; k < singleQuestionListJSONArray.size(); k++)
                        {
                            System.out.println("----题干" + (k + 1) + "/" + singleQuestionListJSONArray.size());
                            JSONObject singleQuestionTiGanJSONObject = (JSONObject) singleQuestionListJSONArray.get(k);

                            //System.out.println(singleQuestionTiGanJSONObject);
                            //add it into the original pic list
                            //将其加入题干arraylist
                            question.getfOriginalPics().add((String) singleQuestionTiGanJSONObject.get("foginalpic"));
                            question.setErrorRatio(Double.parseDouble((String) singleQuestionTiGanJSONObject.get("ferrorratio")));
                            //System.out.println(k);
                        }
                    }

                    //loop through 题目信息
                    JSONArray singleQuestionList1JSONArray = (JSONArray) singleQuestionMapJSONObject.get("list1");

                    singleQuestionList1JSONArray.forEach((questionInfoJSONObjObj) ->
                    {
                        //题目信息jsonobj在此
                        System.out.println("----题目信息");
                        JSONObject questionInfoJSONObj = (JSONObject) questionInfoJSONObjObj;
                        FQuestionInfo questionInfo = new FQuestionInfo();

                        questionInfo.setfQuestionText((String) questionInfoJSONObj.get("fquestiontext"));
                        questionInfo.setfOriginalPic((String) questionInfoJSONObj.get("forignalpic"));

                        questionInfo.setfCorrectAnswer((String) questionInfoJSONObj.get("frightanswer"));

                        questionInfo.setfQuestionChildNo((String) questionInfoJSONObj.get("fquestionchildno"));

                        //add the 题目信息 into the question array
                        question.getfQuestionInfos().add(questionInfo);
                        if (!question.isTruck())
                        {
                            question.setErrorRatio(Double.parseDouble((String) questionInfoJSONObj.get("ferrorratio")));
                        }

                        //System.out.println("questioninfo array size=" + question.getfQuestionInfos().size());
                    });

                    //loop through 标准解析文字、图片
                    //list3
                    JSONArray singleQuestionList3JSONArray = (JSONArray) singleQuestionMapJSONObject.get("list3");
                    for (int k = 0; k < singleQuestionList3JSONArray.size(); k++)
                    {
                        System.out.println("----解析文字 " + (k + 1) + "/" + singleQuestionList3JSONArray.size());

                        JSONObject analysisJSONObject = (JSONObject) singleQuestionList3JSONArray.get(k);

                        //put the fanalysis read obj into the array.
                        question.getfAnalysises().add(new FAnalysis((String) analysisJSONObject.get("fanalysistext"), (String) analysisJSONObject.get("fsummary")));

                    }

                    //loop through 标签
                    JSONArray singleQuestionList4JSONArray = (JSONArray) singleQuestionMapJSONObject.get("list4");
                    for (int k = 0; k < singleQuestionList4JSONArray.size(); k++)
                    {
                        //写入标签
                        System.out.println("----标签 " + (k + 1) + "/" + singleQuestionList4JSONArray.size());
                        JSONObject tagJSONObject = (JSONObject) singleQuestionList4JSONArray.get(k);
                        question.getfTagNames().add((String) tagJSONObject.get("ftagname"));
                    }

                    //loop through 讲解视频
                    JSONArray singleQuestionList5JSONArray = (JSONArray) singleQuestionMapJSONObject.get("list5");

                    for (int k = 0; k < singleQuestionList5JSONArray.size(); k++)
                    {
                        System.out.println("----讲解视频" + (k + 1) + "/" + singleQuestionList5JSONArray.size());
                        JSONObject fmediaJSONObject = (JSONObject) singleQuestionList5JSONArray.get(k);
                        question.getfMedias().add(new FMedia((String) fmediaJSONObject.get("fmedianame"), (String) fmediaJSONObject.get("fmediaurl"), (String) fmediaJSONObject.get("ftype")));
                    }

                    //loop through 订正
                    JSONArray correctionJSONArray = (JSONArray) singleQuestionMapJSONObject.get("list8");

                    for (int k = 0; k < correctionJSONArray.size(); k++)
                    {
                        System.out.println("----订正" + (k + 1) + "/" + correctionJSONArray.size());
                        JSONObject correctionJSONObject = (JSONObject) correctionJSONArray.get(k);
                        question.getfPicUrls().add((String) correctionJSONObject.get("fpicurl"));
                    }

                    //add the fquestion into the arraylist
                    result1.getfQuestions().add(question);
                }

            
        }

        return hwList;
    }

    /**
     * Read the homework list json array of a day.
     *
     * @param hwListJsonString
     * @return
     * @throws BMCSystemException
     * @throws ParseException
     */
    private ArrayList<SecordAnpaiHomeworkTestResult> readHomeworkTestResults(JSONParser parser, String hwListJsonString) throws BMCSystemException, ParseException
    {
        //The list of howework of a student in a certain day
        ArrayList<SecordAnpaiHomeworkTestResult> hwList = new ArrayList<>(3);

        /**
         * Start the processing of JSON : Get Homework List*
         */
        //JSONParser parser = new JSONParser();
        //get the json object(top layer) of the homework list
        JSONObject topLevelJSONObject = (JSONObject) parser.parse(hwListJsonString);

        //Everything is wrapped with "map", so we must get the JSON object of map first
        JSONObject mapJSONObject = (JSONObject) topLevelJSONObject.get("map");

        //System.out.println(mapJSONObject.get("result")); works
        Long result = (Long) mapJSONObject.get("result");

        //see if the result is legal (if the json is complete)
        if (result != 1)
        {
            handleSystemException(mapJSONObject);
        }

        JSONArray homeworkJSONArray = (JSONArray) mapJSONObject.get("list1");

        ///beginning to go through each of the homework by their fid.
        for (int i = 0; i < homeworkJSONArray.size(); i++)
        {
            //get a single homework
            JSONObject singleHomeworkInfo = (JSONObject) homeworkJSONArray.get(i);

            //create an instance of homework, we need smart instance to save space
            SecordAnpaiHomeworkTestResult homeworkTestResult = SecordAnpaiHomeworkTestResult.getInstance((String) singleHomeworkInfo.get("ftype"),
                    (String) singleHomeworkInfo.get("fstatus"),
                    (String) singleHomeworkInfo.get("fid")); //= SecordAnpaiHomeworkTestResult.getDefaultInstance();

            //System.out.println(homeworkTestResult);
            hwList.add(homeworkTestResult);
        }

        /**
         * End Get Homework List*
         */
        return hwList;
    }

    
    public long listenAudio(String fquestionid)throws IOException, InterruptedException, ParseException
    {
        Communicator com = new Communicator(token);
        final String LISTEN_AUDIO_JSON_STRING = com.listenAudioCount(fquestionid);
        JSONParser parser = new JSONParser();
        
        final JSONObject LISTON_AUDIO_JSON = (JSONObject)parser.parse(LISTEN_AUDIO_JSON_STRING);
        
        final JSONObject LISTEN_AUDIO_MAP_JSON = (JSONObject)LISTON_AUDIO_JSON.get("map");
        
        return (long)LISTEN_AUDIO_MAP_JSON.get("result");
    }
    
    
    
    private void handleSystemException(JSONObject json) throws BMCSystemException
    {
        throw new BMCSystemException((String) json.get("msg"), (Long) json.get("result"));
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public static void main(String[] args) throws IOException, ParseException, BMCSystemException, InterruptedException
    {
        DataProcessorBmc dataProcessorBmc = new DataProcessorBmc("3586f7a79e1421d09aa5bdd6e334e2f1");
       // ArrayList<SecordAnpaiHomeworkTestResult> studentFQuestions = dataProcessorBmc.getStudentFQuestions("2020-08-04");
        //System.out.println(studentFQuestions);
      //  System.out.println(dataProcessorBmc.getBmcStuToken("2006234814", "347431"));
      //dataProcessorBmc.
        System.out.println(dataProcessorBmc.listenAudio(URLEncoder.encode("SAT-201803亚洲-S2-013", "UTF-8")));
    }

}
