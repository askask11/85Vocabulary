<%-- 
    Document   : index
    Created on : Jun 23, 2020, 1:59:47 AM
    Author     : jianqing
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html style="height: 100%;">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="languange" content="cn">
        <meta name="description" content="一个大家可以一起帮你背单词的网站">
        <title> 85背单词 </title>
        <%@include file="/WEB-INF/jspf/headtags.jspf" %>
        <style>
            /*   .op
               {
                   background-color: #33ccff;
                   
               }
               
               .op:hover
               {
                   background-color: #ccffff;
               }*/

        </style>
    </head>
    <body style="height: 100%;">

        <noscript>
        This application need javascript. Please enable javascript.
        </noscript>
        <%@include file="/WEB-INF/jspf/navbar.jspf" %>
        <div class="jumbotron text-center">
            <h1>85背单词&#128221;</h1>
            <p>&#128541;大家一起背单词，告别枯燥无聊~</p> 

            <p></p>
        </div>

        <div id="di1" class="container text-center">
            <br> <h4> 菜单</h4>
            <br>
            <form action="Intro.html">
            <button 
                title="本站介绍" 
                type="submit"
                class="btn btn-lg btn-outline-primary btn-block"
                style="width:50%; margin: auto;"
                ><img src="https://img.85vocab.com/icons/angel.png" width="25">介绍</button>
            </form>
            <br><br>
            <div class="row">
                <div class="col-md-6 op" id="dahd">
                    <button title="单词测试答案核对"
                            type="button"
                            class="btn btn-lg btn-outline-primary btn-default btn-block"
                            onclick="document.getElementById('di1').classList.add('hidden');document.getElementById('di2').classList.remove('hidden');"
                            >&#128221; 词测答案核对
                    </button>
                    <c:if test="${vocabAnswerList!=null}">
                        <form action="TestAnswerFragment" method="GET">
                            &nbsp;<button
                                title="刚才的考试#${testId}的答案核对界面"
                                class="btn btn-success btn-block"
                                type="submit"
                                >考试ID#${testId}答案核对</button>
                        </form>
                    </c:if>
                </div>

                <div class="col-md-6 op" id="zyxz">
                    <form action="FindHomework" method="GET">
                        <button 
                            title="下载个人当天的BMC作业" 
                            type="submit"
                            class="btn btn-lg btn-outline-primary btn-block"
                            >&#128421; 作业下载</button>
                    </form>
                </div>
            </div>
            <br><br>
            <div class="row">
                <div class="col-md-6 op" id="ctb">
                    <form action="RequestFQuestion" method="GET">
                        <button
                            type="submit"
                            class="btn btn-lg btn-outline-primary btn-block"
                            title="制作专属于你个人的错题统计订正报告"
                            >&#10060; 统计错题报告</button>
                    </form>
                </div>
                <div class="col-md-6 op" id="angel">
                    <form action="RequestQuizAnswer" method="GET">
                        <button title="这个是安淇小姐姐才能看的秘密哦！"
                                class="btn btn-lg btn-outline-primary btn-block">&#128519; Angel御用</button>
                    </form>
                </div>
            </div>
        </div>

        <div id="di2" class="container text-center hidden">

            <br>
            <div>
                查看测试答案 &#128221;
                <h4>
                    请输入测试ID
                </h4>
            </div>

            <form action='TestAnswerFragment' method='POST'>
                ${inputMsg}
                <div class="animated-text-input-container text-center" style="margin:auto;">
                    <input type="number" required title="测试ID" name="id" />
                    <label class="label-name"><span class="content-name">测试ID:</span></label>
                </div>

                <input type="checkbox" class="form-check-input" name="hideAnswer" id="hideAnswer">
                <label for="hideAnswer">Hide Answer</label>
                <br>
                <button class="arrowbutton" style="background-color: #0099ff"><span><!--&#9989;--> 确认</span></button> 
            </form>
        </div>
        <br><br>
        <%@include file="/WEB-INF/jspf/footer.jspf" %>
    </body>
</html>
