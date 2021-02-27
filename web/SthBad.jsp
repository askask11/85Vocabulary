<%-- 
    Document   : SthBad
    Created on : Sep 24, 2020, 2:59:29 AM
    Author     : jianqing
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>A Gloomy day</title>
        <%@include file="/WEB-INF/jspf/headtags.jspf" %>
        
    </head>
    <body class="text-center">
        <br>
        <h4>
            What happened exactly?
        </h4>
        <div id="msg">${sthbadmessage}</div>
        <p style="color:grey;">-Password Restricted Area-</p>
        <form action="SthBad" method="POST" id="form1">
            <div class="animated-text-input-container text-center" style="margin:auto;" id="password-input">
                <input type="text" required title="测试ID" name="password" id="password"  autocomplete="off"/>
                <label class="label-name"><span class="content-name">Enter password</span></label>
            </div>
            <div>
                Enter captcha: <input type="text" name="captcha" id="captcha" size="5">
                <img src="Captcha" alt="click to change captcha" title="click to change" onclick="this.src='Captcha'" style="cursor: pointer;">
            </div>
            <button type="button" class="arrowbutton" onclick="ago()">
                <span>
                    Submit
                </span>
            </button>
        </form>
        You may email Jianqing Gao for password.
        <script>
            function ago()
            {
                var input = document.getElementById("password").value;
                var captcha = document.getElementById("captcha").value;
                var xhr = new XMLHttpRequest();
                xhr.open("GET", "SthBadPw?password="+input+"&captcha="+captcha, true)

                xhr.onreadystatechange = function (e) {
                    if (xhr.status === 200 && xhr.readyState === 4)
                    {
                        var response = xhr.responseText;
                        if (response === "Success")
                        {
                            document.getElementById("form1").submit();
                        } else
                        {
                            document.getElementById("msg").innerHTML = response;
                        }
                    }
                };
                xhr.send();
            }
            
        </script>
    </body>
</html>
