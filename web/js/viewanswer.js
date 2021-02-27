
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
    "zini1.jpg",
    "aini2.png",
    "aini3.jpg",
    "aini4.jpg",
    "aini5.jpg",
    "aini6.jpg",
    "aini7.jpg",
    "aini8.jpg",
    "aini9.jpg",
    "aini10.jpg",
    "aini11.jpg",
    "aini12.jpg"
];

const SUCCESS_IMAGES = ["aini1.jpg",
    "fafa1.jpg",
    "hxn1.jpg",
    "kaixin3.jpg",
    "kaixin4.jpg",
    "kaixin5.jpg",
    "aini2.png",
    "aini3.jpg",
    "aini4.jpg",
    "aini5.jpg",
    "aini6.jpg",
    "aini7.jpg",
    "aini8.jpg",
    "aini9.jpg",
    "aini10.jpg",
    "aini11.jpg",
    "aini12.jpg"
];

const FAILED_IMAGE = [];


function checkScore()
{
    if (document.getElementsByClassName('table-default').length !== 0)
    {//unfinished
        document.getElementById('message3').innerHTML = "仍然有未改的题目，请再检查一下下~";
    } else
    {
        document.getElementById("message3").innerHTML = "正在改分...";
        document.getElementById("loader3").classList.remove("hidden");
        var corrects = document.getElementsByClassName("table-success").length;
        var errors = document.getElementsByClassName("table-danger").length;
        var total = corrects + errors;

        var score = Math.round((corrects / total) * 100);

        document.getElementById("score").innerHTML = score;


        document.getElementById("score").style.setProperty("color", score >= 90 ? "green" : (score >= 70 ? "orange" : "red"));

        var myImage;
        if (score >= 90)
        {
            myImage = "https://xeduocdn.sirv.com/bq/" + SUCCESS_IMAGES[localRandomNumber(0, SUCCESS_IMAGES.length - 1)];
        } else
        {
            myImage = "https://xeduocdn.sirv.com/bq/nanshou/ns" + localRandomNumber(1, 20) + ".jpg";
        }
        document.getElementById("scoreImage").setAttribute("src", myImage + "?profile=Page%20Main");

        document.getElementById("post-check").classList.remove("hidden");
        //upload the score to the server
        document.getElementById("message3").innerHTML = "正在同步分数至服务器";
        var xhr = new XMLHttpRequest();
        var id = document.getElementById("quizidinput").value;


        xhr.open("GET", "QuizScore?quizId=" + id + "&score=" + score, true);

        xhr.onreadystatechange = (e) => {
            if (xhr.readyState === 4)
            {
                document.getElementById("loader3").classList.add("hidden");
            }
            if (xhr.status === 200 && xhr.readyState === 4)
            {
                var response = xhr.responseText;
                if (response === "Success")
                {
                    document.getElementById("message3").innerHTML = "<span style='color:green'>我知道我考" + score + "啦~服务器同步成功</span>";
                } else
                {
                    document.getElementById("message3").innerHTML = "<span style='color:red'>分数上传失败，因为" + response + "</span>";
                }
            } else
            {
                document.getElementById("message3").innerHTML = "<span style='color:red'>分数上传失败，因为500网络错误</span>";
            }

        };

        xhr.send();
    }
}

function markCorrect(serial, quizId)
{
    var xhr = new XMLHttpRequest();
    var params = "serial=" + serial + "&quizId=" + quizId;
    var beforeClass = document.getElementById('question' + serial + 'tr').getAttribute("class");

    xhr.open("GET", "QuizRecordCorrect?" + params, true);
    xhr.onreadystatechange = function () {//Call a function when the state changes.
        if (xhr.readyState === 4 && xhr.status === 200) {
            //alert(xhr.responseText);
            var response = xhr.responseText;
            if (response === "Success")
            {
                document.getElementById('tbMessageBox').innerHTML = "<span style='color:green;'>耶！第" + serial + "题我对了耶！</span>";
                document.getElementById('question' + serial + 'tr').setAttribute("class", "table-success");
            } else
            {
                document.getElementById('tbMessageBox').innerHTML = "Failed to update because of " + response;
                document.getElementById('question' + serial + 'tr').setAttribute("class", beforeClass);
            }
        } else
        {
            //request failed.
            document.getElementById('tbMessageBox').innerHTML = "Update Failed.";
            document.getElementById('question' + serial + 'tr').setAttribute("class", beforeClass);
        }
        //toggleLoader(serial);
        closeLoader();
    };
    document.getElementById('tbMessageBox').innerHTML = "稍等一下噢！正在帮你记录这一刻... &#9999;";
    document.getElementById('question' + serial + 'tr').setAttribute("class", "table-primary");
    //toggleLoader(serial);
    openLoader();
    xhr.send(params);

}

function clickX(serial)
{
    document.getElementById('tbMessageBox').innerHTML = "啊这... 别吧 &#128552;";
    toggleCorrectAnswerArea(serial);
}
function toggleCorrectAnswerArea(serial)
{
    document.getElementById("question" + serial + "input").classList.toggle("hidden");
    document.getElementById("question" + serial + "submit").classList.toggle("hidden");
}

function markWrong(serial, quizId)
{
    //get the answer
    var answer = document.getElementById('question' + serial + "input").value;
    var beforeClass = document.getElementById('question' + serial + 'tr').getAttribute("class");
    //process new xhr request
    var xhr = new XMLHttpRequest();
    xhr.open("POST", "QuizRecordWrong", true);
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xhr.onreadystatechange = function () {//Call a function when the state changes.
        if (xhr.readyState === 4 && xhr.status === 200) {
            //alert(xhr.responseText);
            var response = xhr.responseText;
            //document.getElementById('question'+serial+'input').clas
            toggleCorrectAnswerArea(serial);
            if (response === "Success")
            {
                document.getElementById('tbMessageBox').innerHTML = "<span style='color:green;'>&#128557; 我怎么错了一题，还是第 " + serial + " 题</span>";
                document.getElementById('question' + serial + 'tr').setAttribute("class", "table-danger");
            } else
            {
                document.getElementById('tbMessageBox').innerHTML = "Failed to update because of " + response;
                document.getElementById('question' + serial + 'tr').setAttribute("class", beforeClass);
            }
        } else
        {
            //request failed.
            document.getElementById('tbMessageBox').innerHTML = "Update Failed.";
            document.getElementById('question' + serial + 'tr').setAttribute("class", beforeClass);
        }
        closeLoader();
    };
    //toggleLoader(serial);
    openLoader();
    document.getElementById('question' + serial + 'tr').setAttribute("class", "table-primary");
    document.getElementById('tbMessageBox').innerHTML = "正在帮你记录黑历史...";
    xhr.send("serial=" + serial + "&quizId=" + quizId + "&answer=" + answer);
}

function openLoader()
{
    document.getElementById("loader").classList.remove('hidden');
}
function closeLoader()
{
    document.getElementById("loader").classList.add('hidden');
}

function renderPageMainImage()
{
    document.getElementById("mainImg").setAttribute("data-src", renderRandom(VIEW_ANSWER_MAINIMAGES));
}

function onloadPage()
{
    renderPageMainImage();
}



function submitFile() {

    if (document.getElementsByName("file")[0].value !== "")
    {
        //set user message
        var beforeBtnColor = document.getElementById("file-submit-button").style.getPropertyValue("background-color");
        document.getElementById("file-submit-button").disabled = "disabled";
        document.getElementById("file-submit-button").style.setProperty("background-color", "grey");
        document.getElementById("loader2").classList.remove("hidden");
        document.getElementById("uploadMessageBox").innerHTML = "谢谢小天使⁽⁽ଘ( ˊᵕˋ )ଓ⁾⁾* &#10083; (1/2)正在上传本次测试解析，请稍后~ &#128519;";


        //file selected

        const Http = new XMLHttpRequest();
        const url = "https://server.1000classes.com/bmcserver/UpHomework";
        Http.open("POST", url);

        //upload the file to BMC server and get the result link
        Http.send(new FormData(document.getElementById("form1")));
        Http.onreadystatechange = (e) => {
            console.log(Http.responseText);
            if (Http.readyState === 4 && Http.status === 200)
            {
                var jobj = JSON.parse(Http.responseText);
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
                        document.getElementById("file-submit-button").disabled = false;
                        document.getElementById("file-submit-button").style.setProperty("background-color", beforeBtnColor);

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

        };
    } else
    {
        //no file selected
        document.getElementById('uploadMessageBox').innerHTML = "<span style='color:red;'>&#128196; 请先选择一个文件噢~</span>";
    }
    //return false;
}



function generateTrueRandomNumber(min, max)
{

    var params = "num=1&min=" + min + "&max=" + max + "&col=1&base=10&format=plain&rnd=new";
    var url = "https://www.random.org/integers?" + params;
    var xhr = new XMLHttpRequest();
    xhr.open("GET", url, true);

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200)
        {
            alert(xhr.responseText);
            return xhr.responseText;
        } else {
            return localRandomNumber(min, max);
        }
    };
    try {
        xhr.send();
    } catch (e) {
        console.log(e);
        return localRandomNumber(min, max);
    }

}




