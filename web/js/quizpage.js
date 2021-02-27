/* 
 * Author: jianqing
 * Date: Sep 1, 2020
 * Description: This document is created for quiz page
 */

function request(url) {
    const Http = new XMLHttpRequest();
    //const url =;
    Http.open("GET", url);

    Http.send(new FormData(document.getElementById("form1")));
    Http.onreadystatechange = (e) => {
        console.log(Http.responseText);
        var jobj = JSON.parse(Http.responseText);
        var msg = jobj.msg;
        if (msg === "ok")
        {
            var jurl = jobj.picurl;
            document.getElementById("result").innerHTML = jurl;
            document.getElementById("result").href = jurl;
            document.getElementById("error").innerHTML = "";
            currenturl = jurl;
        } else
        {
            document.getElementById("error").innerHTML = "TMD BMC服务器又崩掉了 Error: " + msg;
            document.getElementById("result").innerHTML = "";
            currenturl = "";
        }

    };
}