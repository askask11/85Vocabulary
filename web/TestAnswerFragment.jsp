<%-- 
    Document   : TestAnswerFragment
    Created on : Jun 26, 2020, 1:31:05 AM
    Author     : jianqing
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Vocabulary Test</title>
        <%@include file="/WEB-INF/jspf/headtags.jspf" %>
        <script src="js/testpage.js"></script>
        
        <style>
            .hidden
            {
                display: none;
            }

            .invisible
            {
                visibility: hidden;
            }
            .transparent
            {
                background-color: rgba(255,255,255,0);
                border: none;
            }

            .fixed
            {
                border-width:3px;
                background-color:#cae8ca;
                border-style:solid;
                position:fixed;
                padding:7px;
                bottom:0;
                right:0;
                max-width:450px;
                z-index:999
            }

            #pageTimer
            {
                border-color: #4CAF50;
            }


        </style>

    </head>
    <body onload="defineVocab(${vocabAnswerList.size()});">
        <header class="">


            <%@include file="/WEB-INF/jspf/navbar.jspf" %>
            <br>
            <h1 class='text-center'>~测试答案~</h1><br>
            <h5 class='text-center'>&#128221; Test ID: <c:out value="${testId}"></c:out></h5>
            </header>

        <c:if test="${vocabAnswerList.isEmpty()}">
        <center>
            测试ID不存在，请检查并重新输入。
            <br>It looks like the test ID you entered doesn't exist. Please check and enter again.</center>
        <form action="index" method="GET">
            <center><button class="arrowbutton" style="background-color: #0099ff;"><span>返回主页</span></button></center></form>
            </c:if>

    <c:set var="hideAnswer" value="${param.hideAnswer}" scope="page"></c:set>
    <c:set var="wrongWordsInit" value="" ></c:set><%--Wrong word init--%>
        <center>
            <strong class="text-center">${hideAnswer.equals("on")?"Please click on &#128064; to display answer.<br>请点击 &#128064; 以显示答案":""}</strong></center>
    <div class='container' style='${vocabAnswerList.isEmpty()?"display:none":""}'>
        <table class="table table-striped table-responsive text-center">
            <th>
                序号
            </th>
            <th>
                单词
            </th>
            <th>
                释义
            </th>
            <!--Show this only if in hide answer mode-->
            <c:if test="${hideAnswer.equals('on')}">
                <th>
                    操作
                </th>
            </c:if>

            <c:set var="i" value="1" scope="page"></c:set>
            <c:forEach var="row" items="${vocabAnswerList}" >
                <%--Each row has id word-check-i --%>
                <tr class="" id="word-check-${i}">
                    <td onclick="mark(${i})">
                        ${i} 
                    </td> 
                    <td onclick="mark(${i})">
                        <span id="word-${i}"><c:out value="${row.getTerm()}"></c:out></span>
                        </td> 


                        <td onclick="mark(${i})">


                        <%---Answer hidden,
                        each answer 
                        is id
                        answer-vocab-i --%>
                        <span class="${hideAnswer.equals('on')?"invisible":""}" id="answer-vocab-${i}" >
                            <c:out value="${row.getTranslate()}"></c:out>
                            </span>


                        </td>
                    <c:if test="${hideAnswer.equals('on')}">
                        <td>
                            <button class="transparent" onclick="showAnswer(${i})">
                                &#128064;
                            </button>
                        </td>
                    </c:if>
                </tr>


                <c:set var="i" value="${i+1}"></c:set>
                <%--Put all words into wrong words string--%>
                <%--<c:set var="wrongWordsInit" value="${wrongWordsInit+(i==0?'':',')+row.getTerm()}" ></c:set>--%>
            </c:forEach>
        </table>

        <button class="arrowbutton" onclick="submit();" style="background-color: #0099ff; margin: auto; display: block;">
            <span>考试完成</span>
        </button>

        <button class="${testing?"":"hidden"}" onclick="testPage();">
            Test
        </button>
        <br>
        <strong id="message" class="text-center"></strong>
        <br>
        <h4 class="text-center hidden" id="wrongWordsTableTitle">&#128221; 错题本 &#128221;</h4>
        <br>
        <!--Wrong words table-->
        <table class="hidden table table-striped text-center" id="wrongWordsTable" >
           <!--Table header in javascript-->

        </table>
        <br>
        <div class="hidden text-center" id="result" style="border-color:#0099ff;
             border-width: 3px;
             border-style: inset;">
            
            <center style="line-height: 36px;">&#128204; <strong>测试ID: ${testId}</strong> </center>
            Score分数: <strong id="totalScore"></strong>%;<br>
            Skipped Words跳过单词: <strong id="totalSkipped"></strong>;<br>
            Incorrect Words不正确单词: <strong id="totalWrong"></strong>;<br>
            Correct Words正确单词: <strong id="totalCorrect"></strong>;<br>
            <form action="SubmitTest" method="POST" id="submitTestForm" target="submitResultIframe">
                <input type="hidden" name="wrongWords" id="wrongWords">
                <input type="hidden" name="grade" id="grade">
                <button type="button" class="arrowbutton" onclick='submitTestToServer();' ><span id='submitTestButton'>保存并结束</span></button>
            </form>

            <!--Hide this-->
            <form action="TestSubmitTest" method="GET" target="submitResultIframe" class="${testing?"":"hidden"}">
                <button type="submit">T</button>
            </form>
        </div>




        <iframe class="" name="submitResultIframe" id="submitResultIframe" frameborder="0" style="width:100%;">
            Sorry your browser does not support this.
        </iframe>

        <div class='fixed' id='pageTimer'>
            <button class="transparent" onclick="toggleTimer();"><span title='Click to hide/show time'>&#9200;</span> </button> <span id="timerBody"> <a id="timeLeft">10:0</a> | <button class='transparent' id="operateButton" onclick="operateTimer();">&#9654;</button> <button class="transparent ${testing?"":"hidden"}" onclick="testTimer()">T</button> </span>
        </div>



    </div>

    <%@include file="/WEB-INF/jspf/footer.jspf" %>
</body>
</html>
