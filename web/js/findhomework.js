/* 
 * Author: jianqing
 * Date: Jul 19, 2020
 * Description: This document is created for
 */


function initDownloadHomework()
{


}


function findHomework()
{
    var name = document.getElementById("nameInput").value;
    var dateString = document.getElementById("dateInput").value;

    var mouth = dateString.split("-")[1];
    var day = dateString.split("-")[2];
    //var redir, msg;

    if (name !== "" || dateString !== "")
    {
        if (mouth < 10)
        {
            mouth = mouth.replace("0", "");
        }

        if (day < 10)
        {
            day = day.replace("0", "");
        }

        function pass(name, mouth, day)
        {
            var loc = "https://server.1000classes.com/bmcserver/download/" + name + "同学的" + mouth + "月" + day + "日的作业题.pdf";
            window.open(loc, "_blank");
        }
        var xhr = new XMLHttpRequest();
        console.log(day)
        xhr.open("GET", (window.location.hostname==="localhost"?"/85Vocabulary":"")+"/HomeworkExists?mouth=" + mouth + "&day=" + day + "&name=" + name);
        xhr.send();
        xhr.onreadystatechange = function (e) {
            if (xhr.readyState === 4 && xhr.status === 200)
            {
                //read json
                var json = JSON.parse(xhr.responseText);
                var process = json.process;
                var message = json.msg;
                if (process)
                {
                    pass(name,mouth,day);
                    message += "  <a target=\"_blank\" href=\""+"https://server.1000classes.com/bmcserver/download/" + name + "同学的" + mouth + "月" + day + "日的作业题.pdf"+"\">点击下载</a>"
                }
                document.getElementById("msg").innerHTML = message;
            } else
            {
                if (xhr.readyState === 4)
                {
                    pass(name,mouth,day);
                }
            }
        };

        //window.localStorage.setItem("student-name-dlhw",name);
    } else
    {
        alert("请输入姓名及日期");
    }

}

