<%-- 
    Document   : FindHomework
    Created on : Jul 18, 2020, 11:27:14 PM
    Author     : jianqing
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title id="title">Download Your BMC Homework</title>
        <%@include file="/WEB-INF/jspf/headtags.jspf" %>
        <script src="js/findhomework.js"></script>
    </head>
    
    
    
    <body>
        <%@include file="/WEB-INF/jspf/navbar.jspf" %>
        
        <div class="jumbotron">
            <h1 class="text-center" id="t1">下载作业</h1>
            <br>
        <h5 class="text-center" id="h2">&#128221; 你可以在这里一键下载你的BMC的作业，<strong>无需登录</strong></h5>
        </div>
        
        <div class="container">
            
            <div style="margin: auto;">
                
                
                
                <form onsubmit="findHomework();" target="tt" action="about:blank" accept-charset="utf-8">
                <div class="text-center" style="" id="h3">
                    请输入姓名: 
                </div>
                <br>
                
                <div class="text-center">
                   <input type="text" placeholder="姓名" name="name" id="nameInput" required >
                </div>
                <div class="text-center" style="">
                   
                </div>
                <br>
                <div class="text-center" id="h4">请输入要下载的作业日期:<br><span id="h5">如无日历选择器，格式： (yyyy-mm-dd)</span></div>
                
                <br>
                <div class="text-center">
                <!--<label for="mouthInput"></label>
                <input name="mouth" id="mouthInput" placeholder="月份" type="number" > 月
                <label for="dayInput"></label>
                <input name="day" id="dayInput" placeholder="日期" type="number"> 日-->
                <input name="date" title="date" id="dateInput" type="date" required>
                </div>
                <br>
                <div class="text-center"><strong id="msg"></strong></div>
                <button type="submit" id="submitButton" class="arrowbutton" style="display: block; margin: auto;" >
                    <span id="h6">提交</span>
                </button>
                <br><!-- third party page -->
                <div class="text-center" id="h7">提示：您将被转到由BMC提供的作业页面</div>
                </form>
            </div>
            <iframe name="tt" id="tt" style="width: 1px; height:1px; visibility: hidden;"></iframe>
        </div>
        <%@include file="/WEB-INF/jspf/footer.jspf" %>
        <!-- Translate site -->
        <script>
            makeTranslator("FindHomework");
        </script>
    </body>
</html>
