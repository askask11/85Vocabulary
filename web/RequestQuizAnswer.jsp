<%-- 
    Document   : QuizAnswer
    Created on : Aug 31, 2020, 7:03:41 PM
    Author     : jianqing
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title id="title">测试答案</title>
        <script>
            var pioWelcome = ["欢迎亲爱的小天使~.{timetip}", "欢迎小仙女来改我的试卷~\n {timetip}", "很高兴见到你,安淇! \n {timetip}"];
        </script>
        <%@include file="/WEB-INF/jspf/headtags.jspf" %>
       <!-- <script src="js/sirv.js"></script>-->
        <link rel="stylesheet" href="css/lds-loader.css">
        <style>
            .hidden
            {
                display: none;
            }

        </style>
        <link rel="stylesheet" href="css/arrowButtonCss.css">
        <script>
            const VIEW_ANSWER_MAINIMAGES = [
                "fafa1.jpg",
                "fafa2.jpg",
                "hxn1.jpg",
                "hxn2.jpg",
                "hxn3.jpg",
                "kaixin1.jpg",
                "liao1.gif",
                "qidai1.jpg",
                "qidai2.jpg",
                "tushe1.jpg",
                "zini1.jpg"
            ];
        </script>
    </head>
    <body>
        <%@include file="/WEB-INF/jspf/navbar.jspf" %>
        <div class="jumbotron text-center" style="background-color: #ecf9ff;">
            <br>

            <h1 id="ts1" title="对就是可爱的你~">
                &#128519; 可爱的Angel终于来啦！ 
            </h1>

            <span id="ts2" title="就...就在下面哦!"> 输入测试ID看看我做得怎么样吧</span>
            <br>
            <br>
            <img 
                title="喜欢这张图片吗？"
                 src="https://img.85vocab.com/bq/aini1.jpg"
                 id="mainImage" alt="random image">

        </div>
        <!--<button id="audiobutton" 
                style="background-color: rgba(255,255,255,0); border:0px;"
                onclick="setMusic();"
                title="点击这里，可以设置静音哦~">
            &#128263;
        </button>-->
        <div id="pre-submit" class="text-center">



            <form action="ViewAnswer"  accept-charset="utf-8" onsubmit="onsubmitform();">
                <div id="idForm">
                    <c:if test="${param.message!=null}">
                        <div title="${param.message}" class="alert alert-warning alert-dismissible fade show" role="alert">
                            <strong>Notice</strong> ${param.message}
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                    </c:if>
                    <!--form to submit-->
                    <!--User input area-->
                    <div class="animated-text-input-container text-center" style="margin:auto;" id="password-input">
                        <input type="text" value="${param.id}" required title="在这里输入我们的暗号才能继续哦~" name="id"  autocomplete="off" id="idInput" />
                        <label class="label-name"><span class="content-name" id="t4">输入秘密暗号以继续</span></label>
                    </div><br>
                    <div>
                        <!--<label for="licenseCheckbox" id="licenseCheckboxParent">
                            <input title="点击这里表示你已经同意了<a href='SOFTWARE LICENSE AGREEMENT.pdf' target='_blank'>软件使用许可</a>了哦~"
                                   id="licenseCheckbox"
                                   name="lll"
                                   type="checkbox"
                                   
                                   onclick="removeLicenseWarning();">
                            我已阅读并同意 <a href="Terms2.html" target="_blank">软件使用许可</a>
                        </label>-->
                    </div>
                    <input type="hidden" name="id3" value="${empty param.id}" id="quizId">

                    <!--Fake submit button-->
                    <button class="arrowbutton"
                            type="submit"

                            style="background-color: #0099ff; display: block; margin: auto;"
                            title="点击即可提交哦~"
                            id="submitbtn">
                        <span id="ts5">
                            提交
                        </span>
                    </button>
                </div>
                <button 
                    class="hidden"
                    type="submit"
                    id="realsubmit"
                    title="如果你看到了这个，请告诉我哦~">
                    submit
                </button>
            </form>

            <!--Captcha-->
            <div class="hidden text-center" id="angelForm">


                <h4 title="其实..就是一些事实而已 &#128518; ">客官且慢！为了安全，需进行仙气检测！</h4><br>

                请输入世界上<strong style="color:#ff99ff;" title="你觉得还能是谁呢?"><u>最最最可爱的小仙女</u></strong>的名字<br><br>
                <!--These two are in a line.-->
                <a href="javascript:changeAngelCaptcha()" title="点击这里可以换一张哦~">看不清?换一张</a>
                <img src="" alt="点击换一张~嘻嘻嘻" onclick="changeAngelCaptcha();" id="angelCaptcha" style="cursor: pointer;" title="点击更换验证码">
                <input width="15" title="请输入小可爱的姓名" placeholder="请输入小可爱的姓名" name="xkaiName" id="xkaiName" autocomplete="off" autofocus onkeydown="angelInputKeyPress(event);">
                <br>
                <div id="confirmMessage" class="hidden" title="快点打Call啊!"><br>对！请为她<span id="dktext"><strong>打call三次</strong></span>以继续！</div><br>
                <button class="arrowbutton" onclick="checkAngelCaptcha();" type="button" style="background-color: #ff99ff; color:#00ffff; " id="checkCaptchaBtn">
                    <span id="checkAngelText">&#10083; 确认是她</span>
                </button>
                <br><br><br>

            </div>


        </div><br>


        <div id="post-submit" class="text-center hidden" title="请稍等哦！ 爱你~">
            <span id="ts7">正在查询，请稍等哦~</span><br><br>
            <%@include file="/WEB-INF/jspf/lds-loader.jspf" %>
        </div>
        <!--<audio id="clip">
            <source id="musicbox" src="https://s.jianqinggao.com/Music Box.wav"  type="audio/wav" >
        </audio>
        <audio id="clip2">
            <source id="computermagic" src="https://s.jianqinggao.com/Computer_Magic.wav" type="audio/wav">
        </audio>
        <audio id="clip3">
            <source id="clickwav" src="https://s.jianqinggao.com/click.wav"  type="audio/wav">
        </audio>

        <audio id="clip4">
            <source id="erroralert" src="https://s.jianqinggao.com/Error Alert.wav"  type="audio/wav">
        </audio>
        <audio id="clip5">
            <source id="btnpush" src="https://s.jianqinggao.com/Tiny Button Push.wav"  type="audio/wav">
        </audio>-->
        <%@include file="/WEB-INF/jspf/footer.jspf" %>
    </body>
    <script>
        var callCount = 0;
        var musicdoc; //= document.createElement("audio");
        var sss;
        var blinkInv;
        var blinkCounter = 0;
        var playMusic=false;
        const COMPUTER_MAGIC_SRC = "https://s.jianqinggao.com/Computer_Magic.wav";
        const MUSIC_BOX_SRC = "https://s.jianqinggao.com/Music Box.wav";
        const CLICK_SRC = "https://s.jianqinggao.com/click.wav";
        const COMPUTER_ERROR_SRC = "https://s.jianqinggao.com/Error Alert.wav";
        const TINY_BUTTON_PUSH = "https://s.jianqinggao.com/Tiny Button Push.wav";



        function setRandomAngelCaptcha()
        {
            document.getElementById("angelCaptcha").setAttribute("src", "https://img.85vocab.com/AngelCaptcha/ac" + localRandomNumber(0, 15) + ".jpeg");
        }

        (function initImage() {
            document.getElementById("mainImage").setAttribute("src", "https://img.85vocab.com/bq/" + VIEW_ANSWER_MAINIMAGES[localRandomNumber(0, VIEW_ANSWER_MAINIMAGES.length - 1)] + "?x-oss-process=image/resize,w_220");
            setRandomAngelCaptcha();
        })();

       /* (function initMusic()
        {
            playMusic = true;
            musicdoc = document.getElementById("clip");
            document.getElementById("clip").load();
            document.getElementById("clip2").load();
            document.getElementById("clip3").load();
            document.getElementById("clip4").load();
            document.getElementById("clip5").load();


            //alert();
            //musicdoc.src=CLICK_SRC;
            //Cache.add(CLICK_SRC);
        })();*/



        function mainInputKeyPress(event)
        {
            if (event.key === 'Enter')
            {
                showAngelForm();
            }
        }

        function angelInputKeyPress(event)
        {
            if (event.key === 'Enter')
            {
                checkAngelCaptcha();
            }
        }


        function setMusic()
        {
            if (playMusic)
            {
                document.getElementById("audiobutton").innerHTML = "&#128263";
                musicdoc.pause();
            } else
            {
                document.getElementById("audiobutton").innerHTML = "&#128264";
            }
            playMusic = !playMusic;
        }
        /**
         * Invoke this method when the form is submitting.
         * @returns {undefined}
         */
        function onsubmitform()
        {
            playClip(COMPUTER_MAGIC_SRC);
            /*clearInterval(blinkInv);*/
             document.getElementById('pre-submit').classList.add('hidden');
             document.getElementById('post-submit').classList.remove('hidden');
        }

        function showAngelForm()
        {
            playClip(CLICK_SRC);
            if (document.getElementById("licenseCheckbox").checked)
            {
                notify("呀嘿~请输入一个小可爱的名字！")
                document.getElementById("idForm").classList.add("hidden");
                document.getElementById("angelForm").classList.remove("hidden");
                document.getElementById("xkaiName").focus();
            } else
            {
                addLicenseWarning();
            }
        }

        function addLicenseWarning()
        {
            document.getElementById("licenseCheckboxParent").style.setProperty("background-color", "rgba(255,0,0,0.5)");
            notify("需要阅读并同意<a href='SOFTWARE LICENSE AGREEMENT.pdf' target='_blank'>软件使用许可</a>，勾选已同意，才能继续哦~");
        }

        function removeLicenseWarning()
        {
            document.getElementById("licenseCheckboxParent").style.setProperty("background-color", "rgba(4,255,0,0.37)");
            notify("谢谢你使用本软件，您已同意该声明，可继续使用85vocab");
        }

        function startBlinkDACALL()
        {
            blinkInv = setInterval(function () {
                if (blinkCounter === 0)
                {
                    document.getElementById("dktext").innerHTML = "<strong style='color:red;'><u>打CALL三次</u></strong>";
                    blinkCounter = 1;
                } else
                {
                    document.getElementById("dktext").innerHTML = "打CALL三次";
                    blinkCounter = 0;
                }

            }, 500);
        }



        function changeAngelCaptcha()
        {
            playClip(COMPUTER_ERROR_SRC);
            alert("别换了！再怎么换还是她！");
            notify(["换了没成能变成其它人", "还有人能比上她的吗？"]);
            //document.getElementById("angelCaptcha").src = document.getElementById("angelCaptcha").src;
            setRandomAngelCaptcha();
        }

        //const NOT_RIGHT_MESSAGE = ["不对不对，再看看，再想想？"];
        function checkAngelCaptcha()
        {
            var value = document.getElementById("xkaiName").value;

            if (value === "邓安淇" || value === "daq")
            {
                if (callCount === 0)
                {
                    playClip(TINY_BUTTON_PUSH);
                    document.getElementById("checkAngelText").innerHTML = "为她打Call! &#10083;";
                    document.getElementById("confirmMessage").classList.remove("hidden");
                    callCount++;
                    startBlinkDACALL();
                } else if (callCount === 4)
                {
                    alert("没错就是安淇! 你可以继续啦! 爱你~");
                    submitForm();
                } else
                {
                    playClip(MUSIC_BOX_SRC);
                    var st = "";
                    if (callCount > 0)
                    {
                        st += "&#127881;";
                    }
                    if (callCount > 1)
                    {
                        st += "&#10084;";
                    }
                    if (callCount > 2)
                    {
                        st += "&#129392;";
                    }
                    document.getElementById("checkAngelText").innerHTML = "为她打Call! " + st + " &#10083;";
                    callCount++;
                }
            } else
            {
                playClip(COMPUTER_ERROR_SRC);
                alert("不对不对, 再看看, 再想想? 难道还有人能替代她吗? ");
            }
        }

        function submitForm()
        {
            var k = document.getElementById("idInput").value;
            if (k === "")
            {
                playClip(COMPUTER_ERROR_SRC);
                alert("请输入测试ID");
                document.getElementById("idInput").focus();
            } else
            {
                playClip(COMPUTER_MAGIC_SRC);
                document.getElementById("quizId").value = k;
                document.getElementById("realsubmit").click();
            }
        }

        function playClip(src)
        {
            if (playMusic) {
                musicdoc.src = src;
                musicdoc.play();
            }
        }
        makeTranslator("RequestQuizAnswer");
    </script>
</html>
