<%-- 
    Document   : RequestCheat
    Created on : Nov 16, 2020, 1:41:37 PM
    Author     : jianqing
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file="/WEB-INF/jspf/headtags.jspf" %>
        <title>Request Page</title>
        
    </head>
    <body>
        <script>
            function changeCaptcha()
            {
                
                $("#captcha").fadeOut("slow").promise().done (function (){$("#captcha").attr("src","");$("#captcha").attr("src","Captcha"); $("#captcha").fadeIn("slow");});
            }
        </script>
        <%@include file="/WEB-INF/jspf/navbar.jspf" %>
        <div class="jumbotron text-center">
            <br>
            <h1>
                您即将进入密码限制区域
            </h1>
            <br>
            请输入密码以继续
        </div>

        <br><br>
        <!--password field-->
        ${RequestCheatMessage}
        <form action="CheatVocabQuiz" method="POST" class="text-center" >
            <div class="animated-text-input-container text-center" style="margin:auto;">
                <input type="text" required title="Password" name="password" />
                <label class="label-name"><span class="content-name">密码</span></label>
            </div>
            <br>
            <a href="javascript:changeCaptcha()">换一张</a>
            <iframe src="Captcha"
                 onclick="changeCaptcha();"
                 title="点击换一张"
                 style="cursor: pointer; border:0px; width: 110px; height: 52px; overflow: hidden;"
                 id="captcha"
                 frameborder="0"
                 ></iframe>
            <input class="" size="6" placeholder="验证码.." name="captcha">
            <button class="btn btn-primary">提交</button>
        </form>
        
       
        
        <br><br>
        <%@include file="/WEB-INF/jspf/footer.jspf" %>
    </body>
</html>
