/* 
 * Author: jianqing
 * Date: Jun 29, 2020
 * Description: This document is created for the javascript of the site
 */


var totalCorrect = 0, totalSkipped = 0, totalWrong = 0;
var TOTAL_VOCABS;
var resubmitTimer = 0;
const DEFAULT_TIME = 480;
var timeLeft = {
    seconds: DEFAULT_TIME,

    //how many minutes in integer has this left
    getMinutesLeft: function () {
        return Math.floor((this.seconds / 60));
    },

    getSecondsLeft: function () {
        return this.seconds % 60;
    },

    getTimeFormatted: function () {
        return this.getMinutesLeft() + ":" + this.getSecondsLeft();
    }


};
//var wrongWords="";
var isTiming = false;

var timerInterval = null;

function defineVocab(vocab)
{
    TOTAL_VOCABS = vocab;
}
//
//function initalizeWrongWords(wrongWordin)
//{
//    wrongWords = wrongWordin;
//    
//}


/**
 * Perform a document check to see if user has answered all words.
 * @returns {Boolean} true if user has answered all words.
 */
function isAllWordsAnswered()
{
    for (var i = 1, max = TOTAL_VOCABS; i <= max; i++) {
        if (document.getElementById('word-check-' + i).classList.length === 0)
        {
            return false;
        }
    }
    return true;
}


function submit()
{
//    if (isAllWordsAnswered())
//    {
        document.getElementById('message').innerHTML = "";



        //document.getElementById('wrongWords').value=wrongWords;
        //do a document scan
        //
        //var vocabularies = document.getElementsByClassName("word-check-row");
        //
        //
        //initalize
        var wordswrong = "";
        totalCorrect = 0;
        totalWrong = 0;
        totalSkipped = 0;
        initVocabTable();
        
        //var wordsSkipped = "";
        for (var i = 1, max = TOTAL_VOCABS; i <= max; i++) {
            var doc = document.getElementById('word-check-' + i);
            if (doc.classList.contains('table-success'))
            {
                ///word correct
                totalCorrect++;
            } else
            {
                //word skipped
                wordswrong += (wordswrong.length === 0 ? "" : ",") + document.getElementById('word-' + i).innerHTML.trim();
                //console.log("!wow!!");
                //start to mark skip
                if (doc.classList.contains('table-danger'))
                {
                    totalWrong++;
                } else
                {
                    totalSkipped++;
                }
                appendRowToWrongWordTable(i);



            }

        }

//wrong words
        document.getElementById('totalWrong').innerHTML = totalWrong;

        //correct words
        document.getElementById('totalCorrect').innerHTML = totalCorrect;

        //skipped Words
        document.getElementById('totalSkipped').innerHTML = totalSkipped;
        //precentage score
        var totalScore = ((totalCorrect / TOTAL_VOCABS) * 100);
        document.getElementById('totalScore').innerHTML = totalScore;

        document.getElementById('wrongWords').value = wordswrong;
        document.getElementById('grade').value = totalCorrect;


        document.getElementById('result').classList.remove("hidden");
        document.getElementById("wrongWordsTable").classList.remove("hidden");
        document.getElementById('wrongWordsTableTitle').classList.remove("hidden");

//    } else
//    {
//        if (force === true)//force quit
//        {
//            //totalSkipped += (TOTAL_VOCABS - totalCorrect - totalSkipped - totalWrong);
//
//            submit(force);
//        } else
//        {
//            document.getElementById('message').innerHTML = "Please check, there are some words unanswered.";
//        }
//
//
//    }
}



function submitTestToServer()
{
    var btn = document.getElementById("submitTestButton");
    if (resubmitTimer === 0)
    {
        btn.innerHTML = "重新提交";
        btn.disabled = true;
        btn.parentNode.style.setProperty("background-color", "grey");
        resubmitTimer = 60;

        document.getElementById("submitTestForm").submit();

        var x = setInterval(function () {
            resubmitTimer--;
            document.getElementById("submitTestButton").innerHTML = "重新提交(" + resubmitTimer + ")";
            if (resubmitTimer === 0)
            {
                btn.disabled = false;
                btn.innerHTML = "重新提交";
                btn.parentNode.style.setProperty("background-color", "");

                clearInterval(x);
            }
        }, 1000);
    }

}

/**
 * Append a row to the "wrong words" table.
 * @param {type} id
 * @returns {undefined}
 */
function appendRowToWrongWordTable(id)
{
    //create elements
    var row = document.createElement("tr");//main row
    var tdId = document.createElement("td");//append to row
    var tdTerm = document.createElement("td");//append to row
    var tdTranslate = document.createElement("td");//append to row
    var aId = document.createElement("a");//append to a
    var aTerm = document.createElement("a");//append to a


    //set innerhtml of elements.


    tdTranslate.innerHTML = document.getElementById("answer-vocab-" + id).innerHTML;

    //set attribute of a element

    aId.setAttribute("href", "#word-check-" + id);
    aId.innerHTML = id;
    aTerm.setAttribute("href", "#word-check-" + id);
    aTerm.innerHTML = document.getElementById("word-" + id).innerHTML;

    //add children element to their parent elements

    tdId.appendChild(aId);
    tdTerm.appendChild(aTerm);

    row.appendChild(tdId);
    row.appendChild(tdTerm);
    row.appendChild(tdTranslate);
    row.classList.add("wrong-word-row");

    document.getElementById("wrongWordsTable").appendChild(row);

}



function testPage()
{
    //var anotherWrongWords = wrongWords;
    //var testWrongWords = anotherWrongWords.split(",");
    for (var i = 1, max = TOTAL_VOCABS; i <= max; i++) {
        mark(i);
        if (i % 25 === 0)
        {
            //mark 25,75 as skipped
            mark(i);
        }
        if (i % 50 === 0)
        {
            //mark 50,100 as wrong.
            mark(i);
        }
    }
}



function mark(id)
{
    var word = document.getElementById("word-" + id).innerHTML;
    var item = document.getElementById("word-check-" + id);
    if (item.classList.length === 0)
    {
        item.classList.add('table-success');
        //totalCorrect++;
        //wrongWords.replace(word,"").replace(","+word,"");//delete both ,sometxt and sometxt
    } else if (item.classList.contains("table-success"))
    {
        item.classList.remove("table-success");
        item.classList.add("table-warning");
//        totalCorrect--;
//        totalSkipped++;

    } else if (item.classList.contains("table-warning"))
    {
//        totalSkipped--;
//        totalWrong++;
        item.classList.remove("table-warning");
        item.classList.add("table-danger");
        //wrongWords+=(wrongWords.length==0?"":",")+word;//add a wrong word.
    } else
    {
//        totalWrong--;
        item.classList.remove("table-danger");

    }
    //console.log(wrongWords);
}

function initVocabTable()
{
    //clear everything
    //var wrongWordsNodes = document.getElementsByClassName("wrong-word-row");
    var table = document.getElementById("wrongWordsTable");

    table.innerHTML = "";
    
    table.appendChild(getWrongWordTableHeader());
}


function getWrongWordTableHeader()
{
    var row = document.createElement("tr");
    var th1 = document.createElement("th");
    var th2 = document.createElement("th");
    var th3 = document.createElement("th");
    
    th1.innerHTML = "序列";
    th2.innerHTML = "单词";
    th3.innerHTML = "释义";
    row.appendChild(th1);
    row.appendChild(th2);
    row.appendChild(th3);
    return row;
}



function showAnswer(id)
{
    document.getElementById("answer-vocab-" + id).classList.toggle('invisible');
    if (document.getElementById("word-check-" + id).classList.length === 0)
    {
        document.getElementById("word-check-" + id).classList.add("table-success");
    }
}


function toggleTimer()
{
    document.getElementById("timerBody").classList.toggle("hidden");
}

/**
 * A method to start/stop timer
 * @returns {undefined}
 */
function operateTimer()
{
    if (isTiming)
    {
        //pause
        stopTimer();
    } else
    {
        //start
        document.getElementById("operateButton").innerHTML = "&#9208;";
        timerInterval = setInterval(function () {
            timeLeft.seconds--;
            document.getElementById("timeLeft").innerHTML = timeLeft.getTimeFormatted();
            //when time's up
            if(timeLeft.seconds===0)
            {
                //submit the test and clear interval
                submit();
                document.getElementById("timeLeft").innerHTML = "Time's Up! &#128515;";
                timeLeft.seconds=DEFAULT_TIME;
                isTiming = false;
                stopTimer();
                
                //console.log(isTiming);
            }
        }, 1000);
    }

    isTiming = !isTiming;
    //console.log(isTiming);
}

function resetTimer()
{
    
}

function stopTimer()
{
    document.getElementById("operateButton").innerHTML = "&#9654;";
    clearInterval(timerInterval);
}

function testTimer()
{
    timeLeft.seconds = 2;
}

