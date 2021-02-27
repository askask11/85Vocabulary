/* 
 * Author: jianqing
 * Date: Aug 11, 2020
 * Description: This document is created for bmc login.
 */

/**
 * Check if user has logged in before.
 * @returns {undefined}
 */
var form;
var idinput;
var passinput;
var loaderImage;
function checkPage()
{
    var token = localStorage.getItem("token");
//    console.log(typeof token);
//    console.log(token);
    var docs;
    form = document.getElementById("form1");
    idinput = document.getElementById("id-input");
    passinput = document.getElementById("password-input");
    //removing doc
    if (token === null)
    {
        //no token, hide all yes token
        docs = document.getElementsByClassName("yes-token");



    } else
    {
        docs = document.getElementsByClassName("no-token");

        loadStudentName();

        document.getElementById("tokenInput").value = token;

        //remove inputs from form

        form.removeChild(document.getElementById("no-token"));
        // form.removeChild(passinput);



        //document.getElementById("password-input").value = "";

    }
    for (var i = 0; i < docs.length; i++) {
        docs[i].classList.toggle("hidden");
    }
    loadXiaomarkGenerate();
}

function loadStudentName()
{
    //token detec
    var form = document.createElement("form");

    //setinput field
    var prefix = document.createElement("input");
    var postfix = document.createElement("input");
    var tokenInput = document.createElement("input");
    prefix.value = "%E4%BB%A5";
    postfix.value = "%E7%9A%84%E8%BA%AB%E4%BB%BD%E7%BB%A7%E7%BB%AD";
    tokenInput.value = localStorage.getItem("token");
    prefix.name = "prefix";
    postfix.name = "postfix";
    tokenInput.name = "token";
    prefix.type = "hidden";
    tokenInput.type = "hidden";
    postfix.type = "hidden";

    form.setAttribute("accept-charset", "UTF-8");
    form.setAttribute("target", "nameIframe");
    form.setAttribute("method", "GET");
    form.setAttribute("action", "StudentNameLookup");

    form.appendChild(prefix);
    form.appendChild(postfix);
    form.appendChild(tokenInput);

    document.getElementsByTagName("body")[0].appendChild(form);
    form.submit();

    console.log("form submitted");
}

function lockpage()
{
    document.getElementById("form1").classList.add("hidden");
    document.getElementById("waitingdiv").classList.remove("hidden");
}

function logout()
{
    localStorage.removeItem("token");
    window.location.reload();
}
/**
 * Update default setting if user wants to generate a xiaomark link.
 * @returns {undefined}
 */
function loadXiaomarkGenerate()
{
    var localXM = localStorage.getItem("xiaomark");
    if(localXM===undefined)
    {
        localStorage.setItem("xiaomark");
    }else if(localXM==="true")
    {
        document.getElementById("xiaomark").checked = true;
    }
}

function updateXiaomarkGenerate()
{
    var check = document.getElementById("xiaomark").checked;
    localStorage.setItem("xiaomark",check);
}

function editProfile()
{

    var token = localStorage.getItem("token");
    var returnedText, name, api;
    document.getElementById("loadData").classList.add("hidden");
    document.getElementById('p1trigger').click();
    //let user wait message
    notify("欢迎来到个人信息编辑界面。");
    loaderImage = document.createElement("img");
    loaderImage.src = "https://xeduocdn.sirv.com/icons/spinner.svg";
    loaderImage.width = 16;
    var loadingMessage = document.createElement("span");
    loadingMessage.innerHTML = "信息加载中，请稍等...0% <a href='javascript:editProfile()'>无响应？</a>";

    document.getElementById("messageUserProfile").innerHTML="";
    document.getElementById("messageUserProfile").appendChild(loaderImage);
    document.getElementById("messageUserProfile").appendChild(loadingMessage);

    var xhr = new XMLHttpRequest();
    xhr.open("GET", "BMCUserDetail?token="+token, true);
    xhr.send();
    xhr.onreadystatechange = function (e) {
        
        if (xhr.readyState === 4)
        {
            loaderImage.classList.add("hidden");
        }else
        {
            loadingMessage.innerHTML="信息加载中，请稍等..."+(xhr.readyState*100/4)+"% <a href='javascript:editProfile()'>无响应？</a>";
        }
        if (xhr.status === 200 && xhr.readyState === 4)
        {
            returnedText = xhr.responseText;
            if(returnedText==="Error")
            {
                //error with the backend
                loadingMessage.innerHTML="错误，请稍后重试。";
                notify("出错了哦~如果你看到这个，请重新登录。如果还不行的话，记得反馈给support@jianqinggao.com~");
            }else
            {
                //success and retrive data from json，and load the into the user edit panel
                
                loadingMessage.innerHTML="欢迎进入编辑界面~";
                var responseJSONObject = JSON.parse(returnedText);
                name = responseJSONObject.name;
                api = responseJSONObject.sourlapi;
                
                document.getElementById("realName").value = name;
                document.getElementById("xiaomarkApi").value = api;
                
                //show the edit panel
                document.getElementById("editpanel").classList.remove("hidden");
                
                notify("信息读取完毕~请选择你需要做的操作.");
            }
        }else
        {
            loadingMessage.innerHTML="错误，请稍后重试。";
                notify("出错了哦~如果你看到这个，请重新登录。如果还不行的话，记得反馈给support@jianqinggao.com~");
        }
    };

}

function editXiaomarkApi()
{
    //show user waiting message
    document.getElementById("messageUserProfile").innerHTML="";
    loaderImage.classList.remove("hidden");
    document.getElementById("messageUserProfile").appendChild(loaderImage);
    var loadingMessage = document.createElement("span");//create txt msg
    loadingMessage.innerHTML = "正在操作，请稍等";
    document.getElementById("messageUserProfile").appendChild(loadingMessage);//append the txt to msg tag;
    var token = localStorage.getItem("token");
    var api = document.getElementById("xiaomarkApi").value;
    //bulid xhr
    
    var xhr = new XMLHttpRequest();
    xhr.open("GET","EditXiaomarkApi?token="+token+"&api="+api);
    xhr.send();
    
    xhr.onreadystatechange = function (e){
        
        if(xhr.readyState===4)
        {
            loaderImage.classList.add("hidden");
            
        }
        if(xhr.readyState===4||xhr.status ===200)
        {
            var t = xhr.responseText;
            if(t==="Success")
            {
                loadingMessage.innerHTML = "修改成功";
                notify("修改成功~");
            }else if(t==="MaybeError")
            {
                loadingMessage.innerHTML="请<a href='javascript:editProfile()'>重载数据</a>确保修改成功。若没有成功，请重试或右下角客服咨询。";
                notify("请<a href='javascript:editProfile()'>重载数据</a>确保修改成功。若没有成功，请重试或右下角客服咨询。");
            }else if(t==="SQLError")
            {
                loadingMessage.innerHTML="SQL ERROR 请右下角客服咨询。";
                notify("请右下角客服咨询。");
            }else
            {
                loadingMessage.innerHTML="请<a href='javascript:editProfile()'>重载数据</a>确保修改成功。若没有成功，请重试或右下角客服咨询。";
                notify("请<a href='javascript:editProfile()'>重载数据</a>确保修改成功。若没有成功，请重试或右下角客服咨询。");
            }
            
        }else
        {
            loadingMessage.innerHTML="错误，请稍后重试。";
            notify("出错了哦~如果你看到这个，请重新登录。如果还不行的话，记得反馈给support@jianqinggao.com~");
        }
    };
}