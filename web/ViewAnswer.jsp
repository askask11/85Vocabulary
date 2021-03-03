<%-- 
    Document   : ViewAnswer
    Created on : Sep 8, 2020, 5:45:07 PM
    Author     : jianqing
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>测试答案</title>
        <script>
            var pioWelcome = ["{timetip}欢迎我可爱的小天使！", "小天使,{timetip}", ""];

        </script>
        <%@include file="/WEB-INF/jspf/headtags.jspf" %>
        <!--<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/dropzone@5.7.2/dist/basic.css" integrity="sha256-NI6EfwSJhhs7gXBPbwLXD00msI29Bku3GDJT8gYW+gc=" crossorigin="anonymous">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/dropzone@5.7.2/dist/dropzone.css" integrity="sha256-3SE+Qz2RvIa5gOHSNS50MUTTzRAOYREA5+DOmMNFPYk=" crossorigin="anonymous">
        <!--<script src="https://cdn.jsdelivr.net/npm/dropzone@5.7.2/dist/dropzone-amd-module.js" integrity="sha256-oxbkaoOqNIsKBLeLZDlzoylH9F4+WdAKBTzMnc0U1TU=" crossorigin="anonymous"></script>-->
        <script src="https://cdn.jsdelivr.net/npm/dropzone@5.7.6/dist/dropzone.min.js"></script>
        <!--<script src="https://cdn.jsdelivr.net/npm/dropzone@5.7.2/dist/min/dropzone-amd-module.min.js" integrity="sha256-UBOJJ6R/blhj7yI1qhmeer5QDx7Mql0IqYUKgMXRXI8=" crossorigin="anonymous"></script>-->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/dropzone@5.7.2/dist/dropzone.css" integrity="sha256-3SE+Qz2RvIa5gOHSNS50MUTTzRAOYREA5+DOmMNFPYk=" crossorigin="anonymous">
        <!--<link rel="stylesheet" href="https://www.dropzonejs.com/css/style.css?v=1595510599">-->
        <style>
            .dropzone
            {
                border: 2px dashed #0087F7;
                border-radius: 5px;
                background: white;
            }
        </style>
        <script>
            window.Dropzone;
            function handletoserver(response)
            {
                var jobj = JSON.parse(response);
                var msg = jobj.msg;
                if (msg === "ok")
                {
                    //change user message
                    document.getElementById('uploadMessageBox').innerHTML = "(2/2)文件已经存入云端硬盘啦~正在记录链接..";
                    var jurl = jobj.picurl;

                    //submit the url to my server
                    document.getElementById("doc-link-input").setAttribute("value", jurl);

                    var xhr = new XMLHttpRequest();
                    var quizid = document.getElementById('quizidinput').value;
                    xhr.open("GET", "QuizAnswerDoc?quizId=" + quizid + "&link=" + jurl, true);

                    xhr.onreadystatechange = (e) => {
                        //get rid of the loader
                        document.getElementById("loader2").classList.add("hidden");
                        //document.getElementById("file-submit-button").disabled = false;
                        //document.getElementById("file-submit-button").style.setProperty("background-color", beforeBtnColor);

                        if (xhr.readyState === 4 && xhr.status === 200)
                        {
                            var response = xhr.responseText;
                            if (response === "Success")
                            {
                                document.getElementById("uploadMessageBox").innerHTML = "<span style='color:green;'>Yay~测试解析已经get到啦！٩( 'ω' )و get！给元气少女Angel笔芯芯 &#128515; &#10084; <br>感谢你为我付出的时间</span> <a target='_blank' href='" + jurl + "'><br><img width='16' src=\"https://xeduocdn.sirv.com/icons/link.svg\" alt=\"\" />打开解析</a>";
                                document.getElementById('answerlink').setAttribute("href", jurl);
                            } else
                            {
                                document.getElementById("uploadMessageBox").innerHTML = "我的服务器出了点问题，可以告诉我嘛？问题原因是:" + response + ". <br>但是解析上传成功，在这里哦！-> <a target='_blank' href='" + jurl + "'>打开</a>.";
                            }
                        } else
                        {
                            document.getElementById("uploadMessageBox").innerHTML = "上传失败了呜呜呜呜┭┮﹏┭┮  <br>但是解析上传成功，在这里哦！-> <a target='_blank'  href='" + jurl + "'>打开</a>.";
                        }

                    };

                    //var link = document.getElementById('doc-link-input').value;

                    xhr.send();
                } else {
                    document.getElementById("uploadMessageBox").innerHTML = "BMC服务器又崩掉了 &#128557; 5555... Error: " + msg;
                }
            }
            Dropzone.options.form1 = {
                paramName: "file", // The name that will be used to transfer the file
                maxFilesize: 100, // MB
                accept: function (file, done) {
                    if (file.name == "web embeds.txt") {
                        done("Naha, you don't.");
                    } else {
                        done();
                    }

                },
                url: "https://server.1000classes.com/bmcserver/UpHomework",
                autoProcessQueue: true,
                dictDefaultMessage:"<img src=\"https://img.85vocab.com/icons/upload.png\"><br>请拖入答案文件或点击这里选择以上传",
                maxFiles:1,
                success:function(file,response){
                    console.log(response);
                    handletoserver(response);
                },
                sending:function(file,xhr,data)
                {
                    document.getElementById("loader2").classList.remove("hidden");
                    document.getElementById("uploadMessageBox").innerHTML = "谢谢小天使⁽⁽ଘ( ˊᵕˋ )ଓ⁾⁾* &#10083; (1/2)正在上传本次测试解析，请稍后~ &#128519;";
                }

            };
            
        </script>
        <!--<script src="js/sirv.js"></script>
        
        <!--<link href="css/congrats.css" rel="stylesheet">-->
        <!--<script src="js/congrats.js"></script>-->
        <style>
            .loader {
                border: 3px solid #f3f3f3;
                border-radius: 50%;
                border-top: 3px solid #3498db;
                width: 12px;
                height: 12px;
                -webkit-animation: spin 0.5s linear infinite; /* Safari */
                animation: spin 0.5s linear infinite;
            }

            /* Safari */
            @-webkit-keyframes spin {
                0% { -webkit-transform: rotate(0deg); }
                100% { -webkit-transform: rotate(360deg); }
            }

            @keyframes spin {
                0% { transform: rotate(0deg); }
                100% { transform: rotate(360deg); }
            }
        </style>
        <script src="js/viewanswer.js"></script>


    </head>
    <body >
        <nav>
            <%@include file="/WEB-INF/jspf/navbar.jspf" %>
        </nav>
        <div class="jumbotron text-center" style="background-color: #ecf9ff;">
            <br>
            <h1>
                查看我的答案 &#10083;
            </h1>
            <br>


            <img alt="Welcome Image(tell me if you see this)" id="mainImage" />

            <br>
            <br>
            <div>
                <c:out value="${quiz.getMessage()}" />
            </div>

            <br>

            <c:if test="${quiz.getDoclink()!=null&&!quiz.getDoclink().isEmpty()}">
                <div title="点击可以下载原始答题卷哦~">
                    <a href="${quiz.getDoclink()}" target="_blank">
                        <img src="https://img.85vocab.com/icons/link.svg" alt="out" />下载我的卷卷
                    </a>
                </div>
            </c:if>
        </div>

        <div class="container">
            <c:choose>
                <c:when test="${empty quiz}">
                    <div class="alert alert-warning">
                        <strong>Quiz Not Found</strong> 测试未找到，请重试！
                    </div>

                </c:when>

                <c:otherwise>
                    <img id="loader" src="https://img.85vocab.com/icons/spinner.svg" width="30" class="hidden">
                    <!--Live message-->
                    <span id="tbMessageBox" class="no-print">
                        欢迎小天使━(*｀∀´*)ノ亻!进入批改考试界面~ 点击 &#9989; 将答案标记为正确，点击 &#10060; 并输入正确答案， 提交&#10145; 可标记此题为错误。<br>

                    </span>
                    <br>
                    <table class="text-center table table-striped">
                        <thead>
                        <th>
                            #
                        </th>
                        <th>
                            答案
                        </th>
                        <th>
                            操作
                        </th>
                        </thead>

                        <tbody>
                            <c:forEach items="${quizAnswers}" var="record">
                                <tr id="question${record.getSerial()}tr" class="${empty record.isCorrect()? "table-default" : (record.isCorrect()?"table-success":"table-danger")}">
                                    <td>
                                        <!--serial number-->
                                        ${record.getSerial()}
                                    </td>
                                    <td>
                                        <!--Answer-->
                                        ${record.getResponse()}
                                    </td>
                                    <td>

                                        <!--Action To Take-->
                                        <button class="transparent" onclick="markCorrect(${record.getSerial()},${record.getQuizid()});">
                                            &#9989;
                                        </button> | 
                                        <button class="transparent" onclick="clickX(${record.getSerial()});">
                                            &#10060;
                                        </button>
                                        <input class="hidden" placeholder="正确答案" type="text" style="width:70px;" id="question${record.getSerial()}input" value="${record.getAnswer()}">
                                        <button  class="hidden transparent" onclick="markWrong(${record.getSerial()},${record.getQuizid()});" id="question${record.getSerial()}submit">
                                            &#10145;
                                        </button>

                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <br><hr>

                    <!--scoring-->
                    <div class="text-center">
                        <h4 class="text-center">
                            批改分数
                        </h4>
                        <br> 
                        <img src="https://img.85vocab.com/icons/spinner.svg" width="24" class="hidden" id="loader3">
                        <span id="message3">
                            请点击立即改分以批改。
                        </span><br><br>

                        <button class="arrowbutton" onclick="checkScore()">
                            <span>立即改分</span>
                        </button>

                        <br><br>





                        <div id="post-check" class="hidden text-center">
                            <h4>
                                本次测试的分数为 <span id="score"></span> 
                            </h4>


                            <br>

                            <br>

                            <img class="" id="scoreImage">

                        </div>
                    </div>

                    <br><hr>

                    <!--上传解析 不打印-->
                    <div class="no-print">
                        <h4 class="text-center">
                            上传小测解析 ${empty quiz.getAnswerLink() ? "" : "&#9989; "}
                        </h4>

                        <br>

                        <div id="" class="text-center">
                            <img src="https://img.85vocab.com/icons/spinner.svg" width="30" class="hidden" id="loader2" />
                            <span id="uploadMessageBox">
                                <c:choose>
                                    <c:when test="${empty quiz.getAnswerLink()}">
                                        &#128221; 点击下面的"上传"按钮，选择解析文件并开始上传哦~
                                    </c:when>
                                    <c:otherwise>
                                        <span style="color:green">
                                            已经收到解析啦！ღ( ´･ᴗ･` )比心 &#10084;
                                        </span>
                                        <div class="text-center">
                                            <a id="answerlink" href="${quiz.getAnswerLink()}" target="_blank">
                                                <img src="https://img.85vocab.com/icons/link.svg" width="16">打开解析链接
                                            </a>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </span>
                        </div><!--<button type="button" onclick="submitFile();" class="arrowbutton" id="file-submit-button" style="margin: auto; background-color: #3498db; color: white; border-width: 4px; border-color: #0099ff;">
                                <span>
                            <!--<img src="https://xeduocdn.sirv.com/icons/paperplane1.png" width="24" alt="提交"/>-->
                            <!--<img src="https://img.icons8.com/clouds/500/000000/paper-plane.png" width="48"/>-->
                            <!--&#9992; 提交解析~-->
                            <!--</span>
                        </button>-->
                            <br>
                        <form class="dropzone" action="" id="form1" method="POST" enctype="multipart/form-data" action="https://server.1000classes.com/bmcserver/UpHomework">
                            <div class="fallback">
                                <input name="file" type="file" id="file-input" multiple />
                                <button type="button" onclick="submitFile();"></button>
                            </div>
                        </form>



                        <form class="hidden" enctype="multipart/form-data" id="form2">
                            <input type="hidden" name="quizId" value="${quiz.getId()}" id="quizidinput">
                            <input type="hidden" name="link" id="doc-link-input" >
                        </form>
                    </div>


                </c:otherwise>
            </c:choose>


        </div>
        <br><br><br><br><br>

        <script>
            // console.log(VIEW_ANSWER_MAINIMAGES[localRandomNumber(0,VIEW_ANSWER_MAINIMAGES.length-1)]);
            document.getElementById("mainImage").setAttribute("src", "https://img.85vocab.com/bq/" + VIEW_ANSWER_MAINIMAGES[localRandomNumber(0, VIEW_ANSWER_MAINIMAGES.length - 1)] + "?x-oss-process=image/resize,w_220");

            //Dropzone.options.form1.autoProcessQueue=false;
        </script>
        <%@include file="/WEB-INF/jspf/footer.jspf" %>
    </body>
</html>
