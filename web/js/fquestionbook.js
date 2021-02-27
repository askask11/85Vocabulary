/* 
 * Author: jianqing
 * Date: Sep 3, 2020
 * Description: This document is created for fquestionbook
 */

class FHomework
{
    constructor(name, correctnum, incorrectnum, emptynum)
    {
        this.name = name;
        this.correctnum = correctnum;
        this.incorrectnum = incorrectnum;
        this.emptynum = emptynum;
    }

    getWechatLine()
    {
        return "⭐️" + this.name + "\n   -✅正确题目数量: " + this.correctnum + "\n   -❌错误题目数量: " + this.incorrectnum + "\n   -👀空白题目数量: " + this.emptynum + "\n\n";
    }
}
var fhomeworks = new Array();
function loadWechatMessage(name,date,link,host)
{
    //construct the message
    var msg = "【"+name+"同学"+date+"的专属错题本】\n 完整错题本请访问: "+link+"\n";
    msg += "\n******特殊作业列表********\n\n";
    msg += "📌未交作业数量: " + document.getElementsByClassName("txt-notsubmitted").length + "\n";
    msg += "📱视频面测数量: " + document.getElementsByClassName("txt-videotest").length + "\n";
    msg += "\n******已提交作业列表********\n\n";
    for (var i = 0, max = fhomeworks.length; i < max; i++) {
        msg += fhomeworks[i].getWechatLine();
    }
    msg += "\n**********" + host + "***********";
    //put the msg to the input and copy

    //document.getElementById("tocopy").value = msg;
    document.getElementById("wechatMessage").innerHTML = msg;
}




