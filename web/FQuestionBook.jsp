<%-- 
    Document   : FQuestionBook
    Created on : Aug 9, 2020, 2:13:38 AM
    Author     : jianqing
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="encoder" scope="page" class="com.vocab85.model.Encoder"></jsp:useBean>

    <!DOCTYPE html>
    <html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title>${date} ${name}同学的专属错题本</title>
        <script src="https://cdn.jsdelivr.net/npm/clipboard@2.0.6/dist/clipboard.min.js"></script>
        <%@include file="/WEB-INF/jspf/headtags.jspf" %>
        <style>
            .bordered
            {
                border-color: black;
                border-width: 1px;
                border-style: ridge;
            }

            .full-width
            {
                width: 100%;
            }

            .half-width
            {
                width: 50%;
            }

            .transparent
            {
                background-color: rgba(255,255,255,0);
            }

            .break-page
            {
                page-break-before: always !important;
            }

            .centralized
            {
                display: block;
                margin:auto;
            }

            hr.new1 {
                border-top: 3px solid black;
            }
            hr.new2 {
                border-top: 1px dashed gainsboro;
            }

            .underline:after{
                content: "";
                position: absolute;
                height: 15px;
                width: 70px;
                left: 5px;
                top: 37px;
                border-top: 2px solid #66ccff;
                border-radius: 50%;
            }
        </style>

        <!--<script>var pfHeaderImgUrl = '';var pfHeaderTagline = '';var pfdisableClickToDel = 1;var pfHideImages = 0;var pfImageDisplayStyle = 'block';var pfDisablePDF = 0;var pfDisableEmail = 0;var pfDisablePrint = 0;var pfCustomCSS = '';var pfBtVersion = '2';(function () {
                var js, pf;
                pf = document.createElement('script');
                pf.type = 'text/javascript';
                pf.src = '//cdn.printfriendly.com/printfriendly.js';
                document.getElementsByTagName('head')[0].appendChild(pf)
            })();</script>-->
        <%--<c:set var="link" value="http://${pageContext.request.serverName}${path.replace('.jsp','')}?token=${token}%26date=${date}"></c:set>--%>


        <script src="js/fquestionbook.js"></script>

    </head>
    <body>

        <%--<c:set var="wxmessage" value="【85vocab.com错题系统】${name}同学 在 ${date}的 专属错题本概要\n
             ✅完整错题本请访问: ${share} \n
             " scope="page"></c:set>--%>
        <%@include file="/WEB-INF/jspf/navbar.jspf" %>
        <div class="jumbotron text-center">

            <h2>[实时更新]${name}同学专属错题本 &#128221;</h2>
            <br>
            <h2>
                日期：${date}
            </h2>
            <br>
            <h3>本文档由85vocab.com生成 </h3>


            <br>
            <br>
            <div class="no-print">
                <button type="button" class="arrowbutton" style="background-color: #0099ff" onclick="window.print();">
                    <span style="color:white;">
                        &#128424; 打印/保存
                    </span>
                </button><br>
                <!--<a href="https://www.sejda.com/html-to-pdf?save-link&usePrintMedia=true" target="_blank"><img src="img/link.svg">浏览器不支持保存为pdf?</a>-->
                <br>
                <button 
                    type="button"
                    class="arrowbutton"
                    style="background-color: #00cc00" 
                    data-toggle="modal"
                    data-target="#exampleModal"
                    >
                    <span style=""><img src="https://img.85vocab.com/icons/wechat.svg" width="23"> 复制微信消息</span>
                </button>
                <br>
                <button  id="shareBtn" type="button" class="arrowbutton" style="background-color: #ffffff" data-clipboard-text="${share}">
                    <span style="color:black;"> <img src="https://img.icons8.com/ultraviolet/16/000000/copy.png"/> 分享链接</span>
                </button>
                <br>

            </div>

            <center>
                本错题集在线二维码
            </center>
            <br>
            <center>
                <img src="https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=${share}" alt="qrcode">
            </center>

        </div>

        <div class="container">
            <br>
            <br>

            <h3 class="text-center">
                错题本概览 &#128200;
            </h3>
            <br>
            <table class="table table-striped">
                <th>
                    篇章标题 &#128196;
                </th>
                <th>
                    正确 &#9989;
                </th>
                <th>
                    错误 &#10060;
                </th>
                <th>
                    空白 &#128064;
                </th>

                <!--打出可以查看的hw-->
                <c:forEach var="homework" items="${homeworkList}">
                    <!--检测作业种类-->
                    <c:choose>
                        <c:when test="${homework.getfStatus()==0}">

                            <tr>
                                <td>
                                    <span class="txt-notsubmitted">该作业尚未提交</span>
                                </td>
                                <td>
                                    -
                                </td>
                                <td>
                                    -
                                </td>
                                <td>
                                    -
                                </td>
                            </tr>

                        </c:when>
                        <c:when test="${homework.getfType()==3}">
                            <tr>
                                <td>
                                    <span class="txt-videotest">视频面测暂无错题集，请联系你的督导获取错题。</span>
                                </td>
                                <td>
                                    -
                                </td>
                                <td>
                                    -
                                </td>
                                <td>
                                    -
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:set var="hwinfo" value="${homework.getInfos()}"></c:set>
                            <c:if test="${hwinfo.size()>0}">
                                <tr>
                                    <td>
                                        <a href="#${hwinfo.get(0).getfHomework()}" 
                                           class="fhomework" 
                                           >${hwinfo.get(0).getfHomework()}</a>
                                    </td>
                                    <td >
                                        ${hwinfo.get(0).getCorrectNum()}
                                    </td>
                                    <td>
                                        ${hwinfo.get(0).getErrorNum()}
                                    </td>
                                    <td>
                                        ${hwinfo.get(0).getEmptyNum()}
                                    </td>
                                </tr>
                                <!--加错题总结-->
                                <script>
                                    fhomeworks[fhomeworks.length] = new FHomework("${hwinfo.get(0).getfHomework()}",${hwinfo.get(0).getCorrectNum()},${hwinfo.get(0).getErrorNum()},${hwinfo.get(0).getEmptyNum()});
                                </script>

                            </c:if>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>

            </table>
            <!--总起-->




            <br>
            <br>
            <!--<p style="page-break-before:always;" />-->
            <%--beginning of generating section--%>
            <c:forEach var="homework" items="${homeworkList}">
                <br><br>
                <p style="page-break-before:always;"></p>
                <hr class="new1">
                <c:if test="${homework.getfStatus()==0}">
                    <h4 class="text-center">该作业尚未提交</h4>
                </c:if>
                <c:if test="${homework.getfType()==3}">
                    <h5 class="text-center">视频面测暂无错题本，请联系你的督导获取错题。</h5>
                </c:if>

                <br><br>


                <!--判断作业是否可以被生成错题本-->
                <c:if test="${homework.getfStatus()!=0 && homework.getfType()!=3}">


                    <%--开头：作业信息--%>
                    <c:forEach var="homeworkInfo" items="${homework.getInfos()}">

                        <h3 class="text-center">
                            ${homeworkInfo.getfHomework()} &#128196;
                        </h3>
                        <span id="${homeworkInfo.getfHomework()}"></span>
                        <br>


                        <br><br>
                        <!-- <div class="row">
     
     
                             <div class="col-md-4 text-center">
                                 
                             </div>
                             <div class="col-md-4 text-center">
                                 
                             </div>
                             <div class="col-md-4 text-center">
                                 
                             </div>
     
     
                         </div>-->
                        <table class="table table-hover text-center" style="display: table;">
                            <th>
                                正确 &#9989; &nbsp; 
                            </th>
                            <th>
                                错误 &#10060; &nbsp; 
                            </th>
                            <th>
                                空白 &#128064; &nbsp; 
                            </th>
                            <tr>
                                <td>
                                    <h2>${homeworkInfo.getCorrectNum()}</h2>
                                </td>
                                <td>
                                    <h2>${homeworkInfo.getErrorNum()}</h2>
                                </td>
                                <td>
                                    <h2>${homeworkInfo.getEmptyNum()}</h2>
                                </td>
                            </tr>
                        </table>
                        <br><br>





                    </c:forEach>




                    <c:if test="${question.getfType()==3}">
                        <h4 class="text-center">视频面测暂无错题本，请咨询你的督导获取错题。</h4>
                    </c:if>





                    <%--生成题目概览--%>

                    <table class="table table-striped text-center">
                        <th>
                            序号 <div>&#128506;</div>
                        </th>
                        <th>
                            题目名称 <div>&#128204;</div>
                        </th>
                        <th>
                            题目分类 <div>&#127991;</div>
                        </th>

                        <th>
                            正确答案 <div>&#9989;</div>
                        </th>

                        <th>
                            你的答案 <div>&#9999;</div>
                        </th>

                        <th>
                            难度 <div>&#128681;</div>
                        </th>

                        <c:forEach items="${homework.getfQuestions()}" var="question">


                            <tr>
                                <td>
                                    ${question.getSerial()}
                                </td>



                                <td>
                                    <!--info-->
                                    
                                    <c:set var="i" value="0"></c:set>
                                    <c:forEach items="${question.getfQuestionInfos()}" var="info">
                                        ${i>0?"; ":""}  <!--if there are multiple nums, use br to spread them-->
                                        ${info.getfQuestionChildNo()} 
                                        <c:set var="i" value="${i+1}"></c:set>
                                    </c:forEach>
                                </td>
                                <td>
                                    <!--tag-->

                                    <c:forEach items="${question.getfTagNames()}" var="tag">
                                        ${tag};  <!--if there are multiple tags, use space to spread them-->

                                    </c:forEach>
                                </td>
                                <!--right answer-->
                                <td>
                                    ${question.getRightText()}
                                </td>

                                <!--user answer-->

                                <td>
                                    ${question.getStudentAnswer()}
                                </td>


                                <!--难度-->
                                <%--determine the class of next row--%>
                                <c:choose>
                                    <c:when test="${question.getErrorRatio()<=10}">
                                        <c:set var="trclass" value="&#128154;"/>
                                    </c:when>
                                    <c:when test="${question.getErrorRatio()>=10&&question.getErrorRatio()<=20}">
                                        <c:set var="trclass" value="&#128155;"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="trclass" value="&#10084;"/>
                                    </c:otherwise>
                                </c:choose>
                                <td>
                                    ${trclass}
                                </td>

                            </tr>


                        </c:forEach>


                    </table>





                    <%--another loop through hw--%>
                    <c:forEach items="${homework.getfQuestions()}" var="question">







                        <!--题干，图片数量大于1的时候用6，为1的时候用12-->





                        <p style="page-break-before:always;"></p>
                        <hr>
                        <!--检查题干重复-->
                        <c:if test="${fpchecker.check(question.getfOriginalPics())}">

                            <c:choose>
                                <c:when test="${question.getfOriginalPics().size()==0}">
                                    <!--本题没有题干-->

                                </c:when>



                                <c:when test="${question.getfOriginalPics().size()==1}">
                                    <!--本题有一个大大的题干-->
                                    &#128204; <strong>#${question.getSerial()}</strong> ${qinfo.getfQuestionChildNo()}
                                    <br>

                                    <h4 class="text-center">
                                        本题题干 &#128220;
                                    </h4>

                                    <div class="row ">
                                        <div class="col-md-12">
                                            <img class="full-width" src="${question.getfOriginalPics().get(0)}">
                                        </div>
                                    </div>

                                    <p style="page-break-before:always;"></p>
                                </c:when>

                                <c:otherwise>
                                    &#128204; <strong>#${question.getSerial()}</strong> ${qinfo.getfQuestionChildNo()}
                                    <br>


                                    <h4 class="text-center">
                                        本题题干
                                    </h4>

                                    <c:set var="i" value="0"/>
                                    <c:set var="max" value="${question.getfOriginalPics().size()-1}"/>
                                    <c:forEach items="${question.getfOriginalPics()}" var="pic">
                                        <c:if test="${i%2==0}">
                                            <div class="row ">   
                                            </c:if>
                                            <!--${i}-->
                                            <div class="col-md-6">
                                                <img class="full-width max-450" src="${pic}">
                                            </div>

                                            <c:if test="${i%2!=0||i==max}">
                                            </div>
                                        </c:if>
                                        <c:set var="i" value="${i+1}" />
                                    </c:forEach>

                                    <p style="page-break-before:always;"></p>
                                </c:otherwise>


                            </c:choose>

                        </c:if>





                        <!--题目-->
                        <div class="">
                            <br><br>

                            <c:forEach items="${question.getfQuestionInfos()}" var="qinfo">
                                <%--qinfo--%>

                                <c:set var="qname" value="${qinfo.getEncodedQuestionChildNo()}"></c:set>
                                    <figure>
                                        <figcaption>
                                            <!--题目名称-->
                                            &#128204; <strong>#${question.getSerial()}</strong> ${qinfo.getfQuestionChildNo()}
                                    </figcaption>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <!--题目图片-->
                                            <img class="full-width" src="${qinfo.getfOriginalPic()}" alt="image of question ${qinfo.getfQuestionChildNo()}">
                                        </div>
                                        <div class="col-md-6">
                                            <!--Question Text-->


                                            <table class="table table-hover text-center">
                                                <tr>
                                                    <td >
                                                        正确答案 &#9989;
                                                    </td>
                                                    <td >
                                                        ${question.getRightText()}
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        你的答案 ✏
                                                    </td>
                                                    <td>
                                                        ${question.getStudentAnswer()}
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        知识点 &#127991;
                                                    </td>
                                                    <td>
                                                        <!--tag-->
                                                        <c:set var="i" value="0"></c:set>
                                                        <c:forEach items="${question.getfTagNames()}" var="tag">
                                                            ${tag} ; 
                                                            ${i>0?"<br>":""}  <!--if there are multiple tags, use space to spread them-->

                                                            <c:set var="i" value="${i+1}"></c:set>
                                                        </c:forEach>
                                                    </td>
                                                </tr>

                                                <%--determine the class of next row--%>
                                                <c:choose>
                                                    <c:when test="${question.getErrorRatio()<=10}">
                                                        <c:set var="trclass" value="&#128154;"/>
                                                    </c:when>
                                                    <c:when test="${question.getErrorRatio()>=10&&question.getErrorRatio()<=20}">
                                                        <c:set var="trclass" value="&#128155;"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:set var="trclass" value="&#10084;"/>
                                                    </c:otherwise>
                                                </c:choose>
                                                <tr>
                                                    <td>错误率 &#128681;</td>
                                                    <td>
                                                        ${trclass} ${question.getErrorRatio()}% ${trclass}
                                                    </td>
                                                </tr>
                                            </table>

                                            ${qinfo.getfQuestionText()}
                                        </div>
                                    </div>
                                </figure>

                            </c:forEach>
                        </div>


                        <!--订正和正解-->

                        <!--打印订正和我的正解-->

                        <hr class="new2">
                        <c:set var="counter" value="0" scope="page"></c:set>
                        <c:set var="images" value="${question.getImageToShow()}" scope="page"></c:set>
                        <c:set var="max" value="${images.size()}" scope="page"/>
                        <c:forEach var="correction" items="${images}">

                            <c:set var="counter" value="${counter+1}"></c:set>
                            <c:if test="${counter%2!=0}">
                                <div class="row">
                                </c:if>
                                <div class="col-md-6 ">
                                    <h4 class="text-center">
                                        <!--title+emoji-->

                                        ${correction.getTitle()} ${correction.getType()==0?"&#128175;":"&nbsp;&#128273;"}

                                    </h4>
                                    <!--content-->
                                    ${correction.getContent()}
                                </div>
                                <c:if test="${counter%2==0||counter==max}">
                                </div>
                            </c:if>

                        </c:forEach>

                        <!--录音，判断是否为null-->
                        <br>


                        <c:if test="${question.getfMedias().size()>0}">
                            <c:forEach items="${question.getfMedias()}" var="media">
                                <h4 class="text-center">
                                    <!--学习 & 反思 & 总结-->
                                </h4>
                                <hr class="new2">
                                <br>



                                <table class="table table-bordered text-center">
                                    <tr>
                                        <th>
                                            <a href="${media.getfMediaUrl()}" target="_blank">
                                                听解析</a> &#128241; 
                                        </th>
                                        <th>
                                            我的吐血总结 &#128221; 
                                        </th>
                                        <th class="no-print">
                                            <a class="no-print" target="_blank" href="https://${pageContext.request.serverName}/ListenToAudio?token=${token}&fquestionid=<c:out value="${encoder.encode(qname)}"></c:out>">
                                                    系统登记
                                                </a> &#128421; 
                                            </th>
                                        </tr>


                                        <tr>
                                            <!-- 解析 -->
                                            <td>
                                                <img class="centralized" src="https://api.qrserver.com/v1/create-qr-code/?size=100x100&data=${media.getfMediaUrl()}">
                                        </td>
                                        <td>
                                            <!-- 吐血总结 -->
                                            <textarea class="transparent full-width" style="height: 90px; border: none; resize:none;">${question.getVomitingBloodSummary()}</textarea>
                                        </td>
                                        <td class="no-print">
                                            <!--系统登记-->
                                            <c:if test="${exposeToken}">
                                                <img class="centralized no-print"  src="https://api.qrserver.com/v1/create-qr-code/?size=100x100&data=https://${pageContext.request.serverName}/ListenToAudio?token=${token}%26fquestionid=<c:out value="${encoder.encode(qname)}"></c:out>">
                                            </c:if>

                                        </td>
                                    </tr>
                                </table>

                            </c:forEach>

                        </c:if> 


                        <c:if test="${question.getfMedias().size()==0}">
                            <!--没有解析-->



                            <table class="table text-center table-bordered">
                                <th>
                                    我的吐血总结 &#128221; 
                                </th>
                                <tr>
                                    <td>
                                        <textarea class="transparent full-width" style="height: 130px; border: none; resize: none;">${question.getVomitingBloodSummary()}</textarea>   
                                    </td>
                                </tr>
                            </table>
                            <%--结束判断是否有media--%>
                        </c:if>

                        <%--Ending of hw question--%>
                    </c:forEach>

                    <%--结束判断该作业是否可以生成错题本--%>
                </c:if>

                <%--Ending of hw--%>
            </c:forEach>


        </div>


        <br><br>
        <!-- Modal -->
        <div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="exampleModalLabel">微信消息</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        微信消息预览：<span id="copySuccess" style="display:none; color:green;">&#9989;复制成功！</span>
                        <textarea id="wechatMessage" style="height: 500px; width: 100%;margin: auto;"></textarea>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">关闭</button>
                        <button type="button" class="btn btn-primary" id="wechatBtn" data-clipboard-action="copy" data-clipboard-target="#wechatMessage">复制</button>
                    </div>
                </div>
            </div>
        </div>


        <footer class="text-center">
            <hr>
            85vocab.com<br>
            Made By Jianqing Gao<br>
            ICP备案号：<a href="http://beian.miit.gov.cn">粤ICP备2020113241号-1</a><br>
            Contact: <a href="mailto:support@85vocab.com">support@85vocab.com</a><br>
            <span class="no-print">友情链接: <a href="https://icons8.com/icon/43011/复制">图标<img src="https://img.icons8.com/ultraviolet/16/000000/copy.png"/>by icon8</a></span>
            <br><br>
        </footer>

        <c:if test="${exposeToken}">
            <script>
                localStorage.setItem("token", "${token}");
            </script>
        </c:if>

        <script>
            loadWechatMessage('${name}', '${date}', '${share}', '${pageContext.request.serverName}');
            new ClipboardJS('#shareBtn');
            var wechat = new ClipboardJS("#wechatBtn");
            $(document).ready(function () {
                $('[data-toggle="popover"]').popover();
            });
            wechat.on('success', function (e) {
                $("#wechatBtn").removeClass(".btn-primary");
                $("#wechatBtn").addClass(".btn-success");
                $("#copySuccess").fadeIn("slow");
                e.clearSelection();
            });
        </script>

        <input type="text" style="visibility: hidden;" id="tocopy"/>
    </body>
</html>
