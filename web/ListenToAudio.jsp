<%-- 
    Document   : ListenToAudio
    Created on : Aug 18, 2020, 9:05:52 PM
    Author     : jianqing
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>听录音</title>
        <%@include file="/WEB-INF/jspf/headtags.jspf" %>
    </head>
    <body>
       <%@include file="/WEB-INF/jspf/navbar.jspf" %>
       <div class="jumbotron">
           <h1 class="text-center">
               确认录音已听
           </h1>
           <h4 class="text-center">
               ${listenAudioResult}
           </h4>
       </div>
    </body>
</html>
