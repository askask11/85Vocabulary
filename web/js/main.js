//Jianqing Gao

function getBaseUrl(currentPage) {
    var fullURL = window.location.href;
    var nakedURL = fullURL.split("?")[0];
    var realURL = nakedURL.replace(currentPage, "");
    return realURL;
}

function localRandomNumber(min, max)
{
    return Math.round(Math.random() * (max - min) + min);
}

//请保留版权说明
(function printLicense(){
    if (window.console && window.console.log) {
   console.log("85voca.com 软件使用许可: https://85vocab.com/SOFTWARE LICENSE AGREEMENT.pdf");
   console.log("联系支持: support@jianqinggao.com");
}
})();



