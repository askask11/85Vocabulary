<%-- 
    Document   : RequestFQuestion
    Created on : Aug 10, 2020, 6:40:25 PM
    Author     : jianqing
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>生成专属错题本</title>
        <%@include file="/WEB-INF/jspf/headtags.jspf" %>
        <script src="js/bmclogin.js"></script>
        <style>
            .hidden
            {
                display: none;
            }

            .np
            {
                padding-right: 0px;
                padding-left: 0px;
            }
        </style>
        <link rel="stylesheet" href="css/lds-loader.css">
        <link rel='stylesheet' href="css/popup-nojs.css">

    </head>
    <body onload="checkPage();">

        <%@include file="/WEB-INF/jspf/navbar.jspf" %>
        <div class="jumbotron text-center">
            <br>
            <h1>
                生成你的专属错题本 &#128221;
            </h1>

            <br>


            <br>
           <!-- <p>
                相比BMC错题本，它的优势<br>
                改进bmc的错题本：<br>
                1.题干不再重复生成<br>
                2.更加简洁友好的排版<br>
                3.生成时支持你自己的订正<br>
                4.增加听录音二维码<br>
                5.增加手机网站，随时更新。<br>
            </p>-->
        </div>

        <div class="container text-center">

            ${message}

            <c:set var="message" scope="session" value="">

            </c:set>

            <h4>
                现在生成 &#128221;
            </h4>

            <form action="FQuestionBook" method="GET" id="form1" onsubmit="lockpage();">

                <div class="no-token" id="no-token">

                    <br>
                    仅供个人使用，非商用。
                    <br>
                    
                    <div class="animated-text-input-container text-center" style="margin:auto;" id="id-input">
                        <input type="text" required title="BMC Account" name="userid"  autocomplete="off"/>
                        <label class="label-name"><span class="content-name">BMC Account</span></label>
                    </div>

                    <div class="animated-text-input-container text-center" style="margin:auto;" id="password-input">
                        <input type="password" required title="BMC Password" name="password"  autocomplete="off"/>
                        <label class="label-name"><span class="content-name">BMC Password</span></label>
                    </div>
                 
                </div>

                <div class="yes-token" id="yes-token">

                    <div class="text-center" style="margin: auto;">
                        <!--以。。。的身份继续-->
                        <iframe style="width: 200px; height: 41px; border:none; display: inline;" id="nameIframe" name="nameIframe" src="iwait">

                        </iframe>
                    </div>
                    <br>
                    <a href="#popup1" id="p1trigger" style="visibility: hidden;"> </a>
                    <button type="button" onclick="editProfile();" class="arrowbutton" style="background-color: #66ccff; margin:auto;">
                        <span>编辑</span>
                    </button> &nbsp;
                    <button type="button" onclick="logout();" class="arrowbutton" style="background-color: #ff6666; margin:auto;">
                        <span>退出</span>
                    </button>
                    <br>

                    <!--Edit Account Information-->



                </div>

                <br>
                <h4>
                    选择错题本日期 &#128198;
                </h4>
                <input type="date" name="date" required id="date">
                <br>
               
                <br>
                <!---->

                <input type="hidden" name="token" id="tokenInput">

                <button type="submit" class="arrowbutton centralized text-center" title="立即获取你当日的错题信息">
                    <span>提交</span>
                </button><br>
 <label><input type="checkbox" name="shortlink" id="xiaomark" onclick="updateXiaomarkGenerate()"><!-- comment -->
                    生成小码短链接
                </label><!-- Generate Xiaomark Shortlink -->
            </form>

            <div id="popup1" class="overlay">
                <div class="popup" style="margin: 140px auto; ">
                    <h2>编辑BMC Account</h2>
                    <a class="close" href="#">&times;</a>

                    <div class="content">
                        <div id="editpanel" class="hidden">
                            Name: <input title="这是从BMC系统获取的姓名，如有问题请咨询BMC员工。" class="form-control" disabled id="realName" size="15"><br>
                            <a title="点击查看小码短连接是什么" href="https://xiaomark.com" target="_blank">short link<img src="https://img.85vocab.com/icons/link.svg" width="16"></a><a title="点击查看API授权具体信息" href="https://xiaomark.com/open" target="_blank">API授权<img src="https://img.85vocab.com/icons/link.svg" width="16"></a>:

                            <div class="row"> 
                                <div class="col-md-9 "> <input title="复制你的小码短连接并粘贴到这里，可以实现短连接分享错题本的功能" name="sourlapi" placeholder="小码API" class="form-control" id="xiaomarkApi" size="50"></div>
                                <div class="col-md-3 np">
                                    <button class="btn btn-primary" title="更换你的小码短链接API Key" onclick="editXiaomarkApi();">确认</button>
                                </div><br>
                            </div>
                            <br>

                            <!--<a href="https://xiaomark.com" target="_blank">什么是小码短连接？<img src="https://img.85vocab.com/icons/link.svg" width="16"></a>-->

                            <button title="这里可以永久性删除你的账户" class="btn btn-danger" onclick="document.getElementById('popup2trigger').click();notify('这将会永久注销你的账号，所有数据将会<u>不可逆</u>地被擦除，确认要继续吗？');">
                                删除
                            </button>

                        </div>
                        <br>
                        <div>
                            <strong id="messageUserProfile">
                                <!--message here-->
                            </strong>
                        </div>
                        <button id="loadData" onclick="editProfile();" class="arrowbutton">
                            <span>加载数据</span>
                        </button>
                        <a href="#popup2" id="popup2trigger"> </a>
                    </div>
                </div>
            </div>

            <div id="popup2" class="overlay">
                <div class="popup" style="margin: 140px auto; ">
                    <h2>确定删除账户？</h2>
                    <a class="close" href="#">&times;</a>
                    <div class="content">
                        <br><strong style="color:red;">
                            警告！此操作不可逆转
                        </strong>
                        这将会永久和BMC Account说拜拜，确认要继续吗？<br><br>
                        <button title="取消并返回" class="btn btn-success" onclick="document.getElementById('p1trigger').click();notify('很好~欢迎━(*｀∀´*)ノ亻!继续使用85Vocab.com. 有疑问请右下角反馈哦！');">
                            取消并返回编辑
                        </button>
                        <!--write method for continue delete account-->
                        <button title="不可反悔，确定吗？" class="btn btn-danger" >
                            继续
                        </button>
                    </div>
                </div>
            </div>
            <br>

            <div class="text-center hidden" id="waitingdiv">
                请稍后， 正在为您生成错题本<br><br>
                约需一分钟左右。

                <br><br>
                <!--loader-->
                <%@include file="/WEB-INF/jspf/lds-loader.jspf" %>

            </div>


        </div>

        <script>
            if (localStorage.getItem("token") !== null)
            {
                document.getElementById("id-input").value = "";

                document.getElementById("password-input").value = "";
            }

        </script>
        <%@include file="/WEB-INF/jspf/footer.jspf" %>
    </body>
</html>
