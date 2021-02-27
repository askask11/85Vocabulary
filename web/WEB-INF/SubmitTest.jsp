<%-- 
    Document   : SubmitTest
    Created on : Jun 30, 2020, 7:52:06 PM
    Author     : jianqing
A successful message that user has submitted their test.
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Test Submit Result</title>
        <%@include file="/WEB-INF/jspf/headtags.jspf" %>
    </head>
    <body>
        <c:choose>
            <c:when test="${empty testId}">
                <!--<h3>抱歉，出了一些错误，但是该错误是可逆的</h3>-->
                <h4>
                    <!--我们清楚这次测试对你的重要性，所以我们在尽力保证您成绩能被递交服务器。<br>-->
                    为了保证您成绩的准确性， 请在下面输入您此次测试的ID以继续
                </h4>
                <form action="SubmitTest" method="POST">
                    <div class="animated-text-input-container text-center" style="margin:auto;">
                        <input type="hidden" name="wrongWords" value="${wrongWordsString}">
                        <input type="hidden" name="grade" value="${gradeString}">
                        <input type="number" required title="测试ID" name="id" />
                        <label class="label-name"><span class="content-name">测试ID:</span></label>
                    </div>
                    <button class="arrowbutton" style="background-color: #0099ff; display: block; margin: auto;"><span><!--&#9989;--> 确认</span></button> 

                </form>
               
                      
            </c:when>
            <c:otherwise>
                <h3 class="text-center" style="color: #00cc00">&#9989;提交成功</h3>
                <p>如果你看见此页面，你的测试已经提交成功！</p>
            </c:otherwise>
        </c:choose>

    </body>
</html>
