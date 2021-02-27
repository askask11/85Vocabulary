/* 
 * Author: jianqing
 * Date: Oct 17, 2020
 * Description: This document is created for modifing some Pio features.
 */

var activeNotified = false;
//'Hi, ${staff.getUsername()} , ${activeDivCss.contains('block')?'If you want to begin sign - in process, please click on \'Students Sign-in Panel\'. If you wish to end this class, please click \'end session\'. ':' If you wish to start a class, please click \'start a studyhall\' To view/manage your history, click \'show\' under \'Your History Attendances\''}'

(function addOnPio()
{
    if(typeof document.getElementById("pio") !== "undefined")
    {
        var x = setInterval(function () {
            listenMouseTitle();
            clearInterval(x);
        },4000);
    }
})();



function helpcount(seconds, message)
{
    var i = 0;
    var x = setInterval(function () {
        i++;
        console.log(i);
        if (i === seconds)
        {
            notify(message);
            clearInterval(x);
        }
    }, 1000);
}

function randomMember(arr) {
    return arr[Math.floor(Math.random() * arr.length + 1) - 1];
}

/**
 * This makes the Pio girl to speak. It will find the parent pio to speak. You may assign array here.
 * You can also write html for message.
 * @param {string} msg
 * @param {number} exp
 * @returns {undefined}
 */
function notifyC(msg,exp)
{
    //var activeNotified = window.localStorage.getItem('activeNotified');
   
    if (msg.constructor === Array)
    {
        notify(randomMember(msg));
    } else if (typeof msg === "string")
    {
        var es = document.getElementsByClassName("pio-dialog");
        if (es.length === 0)
        {
            es = window.parent.document.getElementsByClassName("pio-dialog");
        }
        for (var i = 0, max = es.length; i < max; i++) {
            var e = es[i];
            e.innerHTML = msg;
            if (!activeNotified)
            {
               // window.localStorage.setItem('activeNotified','true');
               activeNotified = true;
                e.classList.toggle('active');
                
            }
        }
        //console.log(window.localStorage.getItem('activeNotified'));
        var x = setInterval(function () {
            e.classList.toggle('active');
            //window.localStorage.setItem('activeNotified','false');
            activeNotified = false;
            clearInterval(x);
        }, exp);
    } else
    {
        alert('there is an error in the girl');
        console.log(typeof msg);
    }


}
function notify(msg)
{
    notifyC(msg,3000);
}

/**
 * This function Pio girl to speak up each time when the mouse of the user hovers over an element contains title.
 * @returns {undefined}
 */
function listenMouseTitle()
{
    window.addEventListener('mouseover', function (event) {
        var title = event.target.title;
        if (title === "" || title === null || typeof title === "undefined")
        {
//do nothing
        } else
        {
            notify(title);
        }
    });
}




