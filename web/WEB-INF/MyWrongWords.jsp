<%-- 
    Document   : MyWrongWords
    Created on : Jul 27, 2020, 11:13:36 PM
    Author     : jianqing
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>我的错题</title>
        <%@include file="/WEB-INF/jspf/headtags.jspf" %>
        <style>
            .serial
            {
                width: 15px;
            }
        </style>
    </head>
    <body>
        <%@include file="/WEB-INF/jspf/navbar.jspf" %>
        <div class="jumbotron">
            <h1 class="text-center">我的错词</h1>
            <h4 class="text-center">在这里，回顾你之前的错误</h4>
        </div>

        <br>

        <table>
            <!--The header-->
            <th class="serial">
                #
            </th>
            <th>
                单词
            </th>
            <th>
                释义
            </th>

            <!--Table body-->
            <c:set var="i" value="0"></c:set>
            <c:forEach items="${wrongWords}" var="row">
                <tr>
                    <td>
                        ${i+1}
                    </td>
                    <td>
                        ${row.getTerm()}
                    </td>
                    <td>
                        ${row.getTranslate()}
                    </td>
                </tr>
                <c:set var="i" value="${i+1}"></c:set>
            </c:forEach>
        </table>
    </body>
</html>
