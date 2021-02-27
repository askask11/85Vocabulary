<%-- 
    Document   : CheatVocabQuiz
    Created on : Nov 16, 2020, 3:04:24 PM
    Author     : jianqing
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file="/WEB-INF/jspf/headtags.jspf" %>
        <title>JSP Page</title>
    </head>
    <body>
        <%@include file="/WEB-INF/jspf/navbar.jspf" %>
        <div class="jumbotron text-center">
            <br>
            <h1>
                Cheating Interface for Vocab Quiz
            </h1>
            <!-- <br><strong>注意：</strong>
             使用本功能，您需先将图片通过WPS 图片转文字（表格）/OCR功能（推荐）或<a href="https://document.online-convert.com/convert-to-xlsx" target="_blank">在线转换</a> 将图片转换成xlsx文件后继续。
             <br>若您使用<a href="https://document.online-convert.com/convert-to-xlsx" target="_blank">在线转换</a>，请勾选"Use OCR"功能。
             <br>-->

        </div>
        <br>
        <div class="container text-center">
            ${CheatVocabQuizMessage}
            <h4>
                ~上传文件~
            </h4>
            <form id="form1" action="CheatQuizUpload" method="POST" target="miao" enctype="multipart/form-data" onsubmit="onsubmitform();">
                支持格式
                <c:forEach items="${formats}" var="f">
                    .<c:out value="${f}" />,&nbsp;
                </c:forEach>
                    ,推荐上传.xlsx--><br><br>
                    <c:if test="${formats!=null}">
                        <input required type="file" name="file" accept="<c:forEach items="${formats}" var="f">
                    .<c:out value="${f}" />, 
                </c:forEach>">
                    </c:if>
                        <c:if test="${empty formats}">
                            请登录后使用！
                        </c:if>
                
               <br>分数控制：<input type="number" name="skip" id="skipInput1">
                <br><br>
                <button class="btn btn-success">提交</button>
                <br>
               <!-- <button class="btn btn-primary" type="button" id="toImageBtn">
                    改上传图片文件
                </button>-->
            </form>
            
        </div>



        <div id="od" style="display: none;" class="text-center">
            <span id="msg">Please wait, your download should begin soon. If not, please refresh page and try again! If there is an error, message will appear below.</span><img id="loader" src="https://img.85vocab.com/icons/spinner.svg" width="30" >
        </div>

        <iframe name="miao" id="miao" style="display: block; width: 100%; height: 40px; border: none; margin:auto;" frameborder="0">

        </iframe>

        <input id="apitoken" type="hidden" value="0922c3ec39ab5d684a66f40f53fe4e20">
        <script>
            const SCHEMA = "https://api2.online-convert.com/schema";
            function onsubmitform()
            {
                var skipSize =document.getElementById("skipInput1").value;
                if(skipSize!==0 && skipSize>0)
                {
                    console.log("jj")
                    document.getElementById("form1").action += "?skip=" + skipSize;
                }
                $("#od").fadeIn("slow");
            }

            $("#toImageBtn").click(function () {
                $("#form1").fadeOut("fast").promise().done(function () {
                    $("#form2").fadeIn("slow");
                });
            });


            function toXlsxInput()
            {
                $("#form2").fadeOut("fast").promise().done(function () {
                    $("#form1").fadeIn("slow");
                });
            }

            /**
             * Invoke this method when user submits a local image.
             * @returns {undefined} nothing
             */
            function uploadImage()
            {
                $("#od").fadeIn("slow");
                var token = document.getElementById("apitoken").value;
                if (token === "")
                {
                    $("#od").html("错误：第三方API密钥缺失!");
                    return;
                }

                $("#msg").html("正在上传您的文件");
                const Http = new XMLHttpRequest();
                //const url =;
                Http.open("POST", "https://server.1000classes.com/bmcserver/UpHomework");
                Http.send(new FormData(document.getElementById("form2")));
                Http.onreadystatechange = (e) => {
                    if (Http.readyState === 4 && Http.status === 200)
                    {
                        console.log(Http.responseText);
                        var jobj = JSON.parse(Http.responseText);
                        var msg = jobj.msg;
                        if (msg === "ok")
                        {
                            //The generated URL of remote file.
                            var jurl = jobj.picurl;
                            convertImage(jurl);
                        } else
                        {
                            document.getElementById("msg").innerHTML = "文件服务器错误，" + msg;
                        }
                    } else if (Http.readyState === 4)
                    {
                        $("#msg").html("文件服务器错误");
                    }
                };
            }

            /**
             * This function is called when the image has been uploaded to BMC server.
             * @param {string} src pass the remote file URL here
             * @returns {undefined} nothing
             */
            function convertImage(src)
            {
                var token = document.getElementById("apitoken").value;
                var xhr = new XMLHttpRequest();

                $("#msg").html("图片上传成功，正在提交任务");

                if (token === "")
                {
                    $("#msg").html("API授权失败，密钥缺失");
                    return;
                }
                //else
                var data = JSON.stringify({
                    "input": [
                        {
                            "type": "remote",
                            "source": src
                        }
                    ],
                    "conversion": [
                        {
                            "options": {
                                "ocr": true,
                                "allow_multiple_outputs": true,
                                "language": "eng"
                            },
                            "category": "document",
                            "target": "xlsx"
                        }
                    ],
                    "fail_on_input_error": false,
                    "fail_on_conversion_error": false
                });
                xhr.withCredentials = true;
                xhr.addEventListener("readystatechange", function () {
                    if (this.readyState === 4) {
                        $("#msg").html("任务提交成功！");
                        console.log(this.responseText);
                        var json = JSON.parse(this.responseText);
                        console.log("id is" + json.id);
                        console.log("status is " + json.status.code);
                        console.log("more info status " + json.status.info);
                        console.log("conversion minute spent: " + json.spent);
                        trackJob(json.id);
                    }
                });

                xhr.open("POST", "https://api2.online-convert.com/jobs");
                xhr.setRequestHeader("x-oc-api-key", token);
                xhr.setRequestHeader("content-type", "application/json");
                xhr.setRequestHeader("cache-control", "no-cache");
                xhr.send(data);

                $("#msg").html("任务已提交远程服务器");
            }
            var x;
            /**
             * Track the remote job.
             * @@param {string} id The job id
             * @returns {undefined}
             */
            function trackJob(id)
            {
                x = setInterval(function () {
                    var token = document.getElementById("apitoken").value;
                    var xhr = new XMLHttpRequest();
                    xhr.open("GET", "https://api2.online-convert.com/jobs/" + id);
                    xhr.setRequestHeader("x-oc-api-key", token);
                    xhr.setRequestHeader("cache-control", "no-cache");
                    xhr.send();
                    xhr.onreadystatechange = function () {
                        var json = JSON.parse(this.responseText);
                        if (json.status.code === "failed")
                        {
                            $("msg").html("文件转换失败！任务失败！");
                            clearInterval(x);
                            return;
                        }
                        console.log("id is" + json.id);
                        console.log("status is " + json.status.code);
                        console.log("more info status " + json.status.info);
                        console.log("conversion minute spent: " + json.spent);
                        trackJob(json.id);
                        if (json.status.code === "completed")
                        {
                            $("msg").html("convertion done");
                            console.log("download src= " + json.output[0].uri);
                            clearInterval(x);
                        } else if (json.status.code === "failed")
                        {
                            $("msg").html("文件转换失败！任务失败！");
                            clearInterval(x);
                        }
                    };
                }, 6000);
            }




        </script>
        <%@include file="/WEB-INF/jspf/footer.jspf" %>
    </body>
</html>
