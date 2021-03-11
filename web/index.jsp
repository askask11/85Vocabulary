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
        <title id="title"> 85背单词 </title>
        <%@include file="/WEB-INF/jspf/headtags.jspf" %>

    </head>
    <body style="height: 100%;">

        <noscript>
        This application need javascript. Please enable javascript.
        </noscript>
        <%@include file="/WEB-INF/jspf/navbar.jspf" %>
        <div class="jumbotron text-center">
            <h1><span id="t1">85背单词</span>&#128221;</h1>
            <p>&#128541;<span id="t2">大家一起背单词，告别枯燥无聊~</span></p> 
            <p></p>
        </div>
        
        <div id="di1" class="container text-center">
            <br> <h4 id="t-3"> 菜单</h4>
            <br>
            <form action="Intro.html">
                <button 
                    id="intro"
                    title="本站介绍" 
                    type="submit"
                    class="btn btn-lg btn-outline-primary btn-block"
                    style="width:50%; margin: auto;"
                    ><img src="https://img.85vocab.com/icons/angel.png" width="25"><span id="t-4">介绍</span></button>
            </form>
            <br><br>
            <div class="row">
                <div class="col-md-6 op" id="dahd">
                    <button 
                        id="answercheck"
                        title="单词测试答案核对"
                            type="button"
                            class="btn btn-lg btn-outline-primary btn-default btn-block"
                            onclick="document.getElementById('di1').classList.add('hidden');document.getElementById('di2').classList.remove('hidden');"
                            >&#128221; <span id="t5">词测答案核对</span>
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
                            id="hwbtn"
                            title="下载个人当天的BMC作业" 
                            type="submit"
                            class="btn btn-lg btn-outline-primary btn-block"
                            >&#128421; <span id="t6">作业下载</span></button>
                    </form>
                </div>
            </div>
            <br><br>
            <div class="row">
                <div class="col-md-6 op" id="ctb">
                    <form action="RequestFQuestion" method="GET">
                        <button
                            id="repbtn"
                            type="submit"
                            class="btn btn-lg btn-outline-primary btn-block"
                            title="制作专属于你个人的错题统计订正报告"
                            >&#10060; <span id="t7">统计错题报告</span></button>
                    </form>
                </div>
                <div class="col-md-6 op" id="angel">
                    <form action="RequestQuizAnswer" method="GET">
                        <button id="wkbtn" title="这个是安淇小姐姐才能看的秘密哦！"
                                class="btn btn-lg btn-outline-primary btn-block">&#128519; <span id="t8">Angel御用</span></button>
                    </form>
                </div>
            </div>
        </div>

        <div id="di2" class="container text-center hidden">

            <br>
            <div>
                <span id="t9">查看测试答案</span> &#128221;
                <h4 id="t10">
                    请输入测试ID
                </h4>
            </div>

            <form action='TestAnswerFragment' method='POST'>
                ${inputMsg}
                <div class="animated-text-input-container text-center" style="margin:auto;">
                    <input type="number" required title="测试ID" name="id" />
                    <label class="label-name"><span class="content-name" id="t11">测试ID:</span></label>
                </div>

                <input type="checkbox" class="form-check-input" name="hideAnswer" id="hideAnswer">
                <label for="hideAnswer">Hide Answer</label>
                <br>
                <button class="arrowbutton" style="background-color: #0099ff"><span id="t12"><!--&#9989;--> 确认</span></button> 
            </form>
        </div>
        <br><br>
        <%@include file="/WEB-INF/jspf/footer.jspf" %>
        <script>
            makeTranslator("index")
        </script>
    </body>
</html>
