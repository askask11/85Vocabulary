/*
 * Author: jianqing
 * Date: Jun 23, 2020
 * Description: This document is created for main web servlet.
 */
package com.vocab85.controller;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.vocab85.model.BSAlerts;
import com.vocab85.model.Captcha;
import com.vocab85.model.MyQuiz;
import com.vocab85.model.QuizAnswer;
import com.vocab85.model.Vocabulary;
import com.vocab85.model.WrongVocabulary;
import com.vocab85.model.bmcsys.BMCSystemException;
import com.vocab85.model.bmcsys.DataProcessorBmc;
import com.vocab85.model.bmcsys.FOriginalPicChecker;
import com.vocab85.model.network.DatabaseConnector;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.simple.parser.ParseException;

import static com.vocab85.model.Encoder.isValidStrings;
import static com.vocab85.model.AliOSS.logError;
import com.vocab85.model.Randomizer;
import com.vocab85.model.network.Commander;
import com.vocab85.model.network.XiaomarkAPI;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 *
 * @author jianqing
 */
@WebServlet(name = "Servlet", urlPatterns =
{
    "/index",
    "/TestAnswerFragment",
    "/SubmitTest",
    "/TestSubmitTest",
    "/DownloadHomework",
    "/FindHomework",
    "/FQuestionBook",
    "/RequestFQuestion",
    "/StudentNameLookup",
    "/iwait",
    "/ListenToAudio",
    "/RequestQuizAnswer",
    "/ViewAnswer",
    "/QuizRecordCorrect",
    "/QuizRecordWrong",
    "/QuizAnswerDoc",
    "/QuizScore",
    "/SthBad",
    "/SthBadPw",
    "/Captcha",
    "/AngelCaptcha",
    "/StudentName",
    "/BMCUserDetail",
    "/EditXiaomarkApi",
    "/RequestCheat",
    "/CheatVocabQuiz",
    "/startDB",
    "/HomeworkExists"
//TODO: write method to receive submitted test. / To write db method to update test table.
})
public class Servlet extends HttpServlet
{

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter())
        {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Servlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Servlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    protected void sendMessage(HttpServletRequest request, HttpServletResponse response, String title, String body)
            throws ServletException, IOException
    {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter())
        {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Message</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>" + title + "</h1>");
            out.println("" + body + "");
            out.println("</body>");
            out.println("</html>");
        }
    }

    protected void timeOutContent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        sendMessage(request, response, "Time Out", "You session has timed out. Please go back to main page again. <a href=\"index\">Go</a>");

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        //processRequest(request, response);
        String path = request.getServletPath();
        HttpSession session = request.getSession();
        switch (path)
        {
            case "/index":
                processIndexGET(request, response);
                break;
            case "/TestAnswerFragment":
                //request.getRequestDispatcher("/TestAnswerFragment.jsp").forward(request, response);
                processTestAnswerGET(request, response);
                break;
            case "/TestSubmitTest":
                processTestSubmitTest(request, response);
                break;
            case "/FindHomework":
                processFindHomeworkGET(request, response);
                break;
            case "/FQuestionBook":
                //request.getRequestDispatcher("/FQuestionBook.jsp").forward(request, response);
                processFQuestionBookGET(request, response);
                break;
            case "/RequestFQuestion":
            case "/RequestCheat":
                request.getRequestDispatcher(request.getServletPath() + ".jsp").forward(request, response);
                break;
            case "/StudentNameLookup":
                processStudentNameLookupGET(request, response);
                break;
            case "/ListenToAudio":
                processListenToAudioGET(request, response);
                break;
            case "/iwait":
                request.getRequestDispatcher("/WEB-INF" + path + ".jsp").forward(request, response);
                break;
            case "/RequestQuizAnswer":
                processRequestQuizAnswerGET(request, response);
                break;
            case "/ViewAnswer":
                processViewAnswerGET(request, response);
                break;
            case "/QuizRecordCorrect":
                processQuizRecordCorrectGET(request, response);
                break;
            case "/QuizAnswerDoc":
                processQuizAnswerDocGET_POST(request, response);
                break;
            case "/QuizScore":
                processQuizScoreGET(request, response);
                break;
            case "/SthBad":
                request.getRequestDispatcher(request.getServletPath() + ".jsp").forward(request, response);
                break;
            case "/SthBadPw":
                processSthBadPwGET(request, response);
                break;
            case "/Captcha":
                processCaptchaGET(request, response);
                break;
            case "/AngelCaptcha":
                processAngelCaptchaGET(request, response);
                break;
            case "/StudentName":
                processStudentNameGET(request, response);
                break;
            case "/BMCUserDetail":
                processBMCUserDetailGET(request, response);
                break;
            case "/EditXiaomarkApi":
                processEditXiaomarkApiGET(request, response);
                break;
            case "/startDB":
            {
                try
                {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                } catch (ClassNotFoundException ex)
                {
                    Logger.getLogger(Servlet.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            case "/HomeworkExists":
                processHomeworkExistsGET(request, response);
            default:
                processRequest(request, response);
                break;
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        //processRequest(request, response);
        String path = request.getServletPath();
        switch (path)
        {
            case "/TestAnswerFragment":
                processTestAnswerPOST(request, response);
                break;
            case "/SubmitTest":
                processSubmitTestPOST(request, response);
                break;
            case "/DownloadHomework":
                processDownloadHomeworkPOST(request, response);
                break;
            case "/QuizRecordWrong":
                processQuizRecordWrongPOST(request, response);
                break;
            case "/QuizAnswerDoc":
                processQuizAnswerDocGET_POST(request, response);
                break;
            case "/SthBad":
                processSthBadPOST(request, response);
                break;
            case "/CheatVocabQuiz":
                processCheatVocabQuizPOST(request, response);
                break;
            default:
                processRequest(request, response);
                break;
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo()
    {
        return "Short description";
    }// </editor-fold>

    protected void processHomeworkExistsGET(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        JSONObject responseJSON = JSONUtil.createObj();

        //properties: process: true/false
        //msg: 
        try
        {
            int day = Integer.parseInt(request.getParameter("day"));
            int mouth = Integer.parseInt(request.getParameter("mouth"));
            String name = request.getParameter("name");
            // not required on cloud
            //name= new String(name.getBytes("ISO8859_1"), "UTF-8");
            String encodedURL = "https://server.1000classes.com/bmcserver/download/" + URLEncoder.encode("" + name + "同学的" + mouth + "月" + day + "日的作业题.pdf", "utf-8");
            System.out.println("encoded url: " + encodedURL);
            String resp = Commander.executeCommand("curl -Is " + encodedURL + " | head -n 1");
            if (resp.contains("200"))
            {
                //success
                responseJSON.putOpt("process", true);
                responseJSON.putOpt("msg", "已找到" + name + "同学在当天的作业，将跳转至目标页。");
            } else if (resp.contains("404"))
            {
                //not found
                responseJSON.putOpt("process", false);
                responseJSON.putOpt("msg", "未找到" + name + "同学在" + mouth + "月" + day + "日的作业。");
            } else
            {
                //unknown error
                responseJSON.putOpt("process", true);
                responseJSON.putOpt("msg", "未知错误，将为您跳转至下一页。");
                logError(new Exception(resp));
                System.out.println("Unknown response: " + resp);
            }
        } catch (NumberFormatException e)
        {
            responseJSON.putOpt("process", false);
            responseJSON.putOpt("msg", "请输入正确的日期格式：yyyy-mm-dd");
        } catch (InterruptedException ex)
        {
            responseJSON.putOpt("process", true);
            responseJSON.putOpt("msg", "IE未知错误，将为您跳转至下一页。");
            logError(ex);
            Logger.getLogger(Servlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        //send response
        try (PrintWriter pw = response.getWriter())
        {
            pw.write(responseJSON.toStringPretty());
            pw.flush();
        }
    }

    protected void processCheatVocabQuizPOST(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession();
        String pw = request.getParameter("password");
        String verify = request.getParameter("captcha");
        Captcha cap = (Captcha) session.getAttribute("captcha");

        if (isValidStrings(pw))
        {
            //verify password
            if (cap == null)
            {
                session.setAttribute("RequestCheatMessage", BSAlerts.warningMessage("No", "Internal error"));
                response.sendRedirect(request.getContextPath() + "/RequestCheat");
            } else
            {
                if (cap.getBody().equals(verify))
                {
                    if (pw.equals("3266933"))
                    {
                        session.setAttribute("cheat", true);
                        session.setAttribute("formats", FileServlet.ALLOWED_EXTENSIONS_OCR);
                        request.getRequestDispatcher("/WEB-INF" + request.getServletPath() + ".jsp").forward(request, response);

                    } else
                    {
                        session.setAttribute("RequestCheatMessage", BSAlerts.warningMessage("Password Incorrect密码错误", "Please check your password"));
                        response.sendRedirect(request.getContextPath() + "/RequestCheat");
                    }
                } else
                {
                    session.setAttribute("RequestCheatMessage", BSAlerts.warningMessage("验证码错误", "请重新输入验证码"));
                    response.sendRedirect(request.getContextPath() + "/RequestCheat");
                }
            }
        } else
        {
            session.setAttribute("RequestCheatMessage", BSAlerts.warningMessage("密码为空", "请输入密码"));
            response.sendRedirect(request.getContextPath() + "/RequestCheat");
        }
    }

    protected void processEditXiaomarkApiGET(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/plain");
        String fakeToken = request.getParameter("token");
        String apiString = request.getParameter("api");
        try (PrintWriter out = response.getWriter())
        {
            if (isValidStrings(fakeToken))
            {
                try (DatabaseConnector dbConn = DatabaseConnector.getDefaultInstance())
                {
                    int rows = dbConn.updateXiaomarkApi(fakeToken, apiString);
                    if (rows == 1)
                    {
                        out.print("Success");
                    } else
                    {
                        out.print("MaybeError");
                    }
                } catch (SQLException | ClassNotFoundException e)
                {
                    out.println("SQLError");
                    logError(e);
                }
            } else
            {
                out.println("Error");
            }
        }
    }

    protected void processBMCUserDetailGET(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("application/json");//REST
        response.setCharacterEncoding("utf-8");
        String fakeToken = request.getParameter("token");
        try (PrintWriter out = response.getWriter())
        {
            if (isValidStrings(fakeToken))
            {
                try (DatabaseConnector dbConn = DatabaseConnector.getDefaultInstance())
                {
                    //get the useXiaomark api and name
                    String token = dbConn.getBmcToken(fakeToken);
                    String xiaomarkApi = dbConn.getXiaomarkApi(fakeToken);
                    if (isValidStrings(token))
                    {
                        DataProcessorBmc d = new DataProcessorBmc(token);
                        String n = d.getStudentName();

                        out.write("{\"name\":\"" + n + "\",\n\"sourlapi\":\"" + (xiaomarkApi == null ? "" : xiaomarkApi) + "\"}");
                    } else
                    {
                        out.write("Error");
                    }
                } catch (Exception e)
                {
                    out.write("Error");
                    logError(e);
                }
            } else
            {
                out.write("Error");
            }
        }
    }

    protected void processStudentNameGET(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/plain");
        response.setCharacterEncoding("utf-8");
        String fakeToken = request.getParameter("token");
        try (PrintWriter out = response.getWriter())
        {
            if (isValidStrings(fakeToken))
            {
                try (DatabaseConnector dbConn = DatabaseConnector.getDefaultInstance())
                {
                    String token = dbConn.getBmcToken(fakeToken);
                    if (isValidStrings(token))
                    {
                        DataProcessorBmc d = new DataProcessorBmc(token);
                        out.write(d.getStudentName());
                    }
                } catch (Exception e)
                {
                    out.write("Error");
                    logError(e);
                }
            } else
            {
                out.write("Error");
            }
        }
    }

    protected void processAngelCaptchaGET(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession();
        //decide the returning datatype and avoid potential security issues.
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");

        Captcha captcha = Captcha.generateCaptcha("邓安淇", 17, 22, 100/*width*/, 35 /*height*/);
        try (OutputStream os = response.getOutputStream())
        {
            //captcha.setExpireTime(LocalDateTime.now(ZoneId.of("UTC")).plusMinutes(5));//set expire 5 mins later.
            //session.setAttribute("captcha", captcha);//user may only hold one captcha at a time. The captcha will be used app wise.
            //no forward required.
            //write the image onto the page
            ImageIO.write(captcha.getImage(), "jpg", os);

        }
    }

    public void processCaptchaGET(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession();
        //String userPath = request.getServletPath();
        //decide the returning datatype and avoid potential security issues.
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");

        Captcha captcha = Captcha.generateCaptcha(100/*width*/, 35 /*height*/);
        try (OutputStream os = response.getOutputStream())
        {
            captcha.setExpireTime(LocalDateTime.now(ZoneId.of("UTC")).plusMinutes(5));//set expire 5 mins later.
            session.setAttribute("captcha", captcha);//user may only hold one captcha at a time. The captcha will be used app wise.
            //no forward required.
            //write the image onto the page
            ImageIO.write(captcha.getImage(), "jpg", os);

        }
    }

    public void processSthBadPwGET(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String pw = request.getParameter("password");
        String c = request.getParameter("captcha");
        HttpSession session = request.getSession();
        Captcha captcha = (Captcha) session.getAttribute("captcha");
        response.setContentType("text/plain");
        if (isValidStrings(pw, c))
        {
            if (c.equals(captcha.getBody()))
            {
                if (pw.equals("neveragain0922"))
                {
                    response.getWriter().write("Success");
                } else
                {
                    response.getWriter().write(BSAlerts.dangerMessage("Password Incorrect", "You have entered a wrong password."));

                }
            } else
            {
                response.getWriter().write(BSAlerts.dangerMessage("Captcha Incorrect", "Check and try again."));
            }
        } else
        {
            response.getWriter().write(BSAlerts.dangerMessage("Enter password", "Please enter a password to continue"));
        }
    }

    protected void processSthBadPOST(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession();
        String password = request.getParameter("password");
        String cap = request.getParameter("captcha");
        Captcha captcha = (Captcha) session.getAttribute("captcha");
        if (isValidStrings(password, cap))
        {
            if (password.equals("neveragain0922") && cap.equals(captcha.getBody()))
            {
                request.getRequestDispatcher("/WEB-INF" + request.getServletPath() + ".jsp").forward(request, response);
            } else
            {
                if (!cap.equals(captcha.getBody()))
                {
                    session.setAttribute("sthbadmessage", BSAlerts.dangerMessage("Wrong Captcha", "You have entered a wrong captcha."));
                    response.sendRedirect(request.getContextPath() + "/SthBad");
                } else
                {
                    session.setAttribute("sthbadmessage", BSAlerts.dangerMessage("Wrong Password", "You have entered a wrong password."));
                    response.sendRedirect(request.getContextPath() + "/SthBad");
                }
            }
        } else
        {
            session.setAttribute("sthbadmessage", BSAlerts.dangerMessage("Password Restricted", "This is password restricted area. Enter the password to continue."));
        }
    }

    private void processQuizScoreGET(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String quizIdString = request.getParameter("quizId");
        String scoreStr = request.getParameter("score");
        response.setContentType("text/plain");

        if (isValidStrings(quizIdString, scoreStr))
        {
            try (DatabaseConnector db = DatabaseConnector.getDefaultInstance())
            {
                int id = Integer.parseInt(quizIdString);
                int score = Integer.parseInt(scoreStr);

                int result = db.updateQuizScore(id, score);

                if (result == 1)
                {
                    response.getWriter().write("Success");

                } else
                {
                    response.getWriter().write("QuizNotFound");
                }

            } catch (NumberFormatException nfe)
            {
                response.getWriter().write("NumberFormatException");
                logError(nfe);
            } catch (SQLException | ClassNotFoundException sqle)
            {
                response.getWriter().write("SQLException");
                response.setStatus(500);
                logError(sqle);
            }
        } else
        {
            response.getWriter().write("NoParameter");
        }

    }

    private void processQuizAnswerDocGET_POST(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        //HttpSession session = request.getSession();
        String quizIdStr = request.getParameter("quizId");
        String docLink = request.getParameter("link");

        response.setContentType("text/plain");

        if (isValidStrings(quizIdStr, docLink))
        {
            try (DatabaseConnector dbConn = DatabaseConnector.getDefaultInstance())
            {
                int quizId = Integer.parseInt(quizIdStr);

                int result = dbConn.updateQuizzesAnswerDocByQuizId(quizId, docLink);

                if (result == 1)
                {
                    response.getWriter().write("Success");
                } else
                {
                    response.getWriter().write("WrongParam");
                }

            } catch (NumberFormatException e)
            {
                response.getWriter().write("NumberFormatException");
                logError(e);
            } catch (SQLException | ClassNotFoundException sqle)
            {
                response.getWriter().write("SQLException");
                response.setStatus(500);
                logError(sqle);
            }
        } else
        {
            response.getWriter().write("NoParameters");
        }
    }

    private void processRequestQuizAnswerGET(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        request.getRequestDispatcher(request.getServletPath() + ".jsp").forward(request, response);
    }

    private void processQuizRecordWrongPOST(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession();
        String answerString, serialString, quizIdString;
        answerString = request.getParameter("answer");
        quizIdString = request.getParameter("quizId");
        serialString = request.getParameter("serial");
        if (isValidStrings(serialString, quizIdString))
        {
            try (DatabaseConnector db = DatabaseConnector.getDefaultInstance())
            {
                int serial = Integer.parseInt(serialString);
                int quizId = Integer.parseInt(quizIdString);
                int result = db.updateQuizAnswerCorrectByQuizIdSerial(quizId, serial, false);
                if (result == 1)
                {
                    db.updateQuizAnswerCorrectAnswerByQuizIdSerial(quizId, serial, answerString);
                    response.getWriter().write("Success");
                }
            } catch (SQLException | ClassNotFoundException sqle)
            {
                response.getWriter().write("SQLException");
                logError(sqle);
            } catch (NumberFormatException nfe)
            {
                response.getWriter().write("NumberFormatException");
                logError(nfe);
            }
        } else
        {
            //invalid params. empty param
            response.getWriter().write("NoParameter");
        }
    }

////method for recording a quiz that an answer is correct
    private void processQuizRecordCorrectGET(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        //get parameters and session
        //HttpSession session = request.getSession();
        String serialString, quizIdString;
        serialString = request.getParameter("serial");
        quizIdString = request.getParameter("quizId");
        //set response type
        response.setContentType("text/plain");

        //check parameters
        if (isValidStrings(serialString, quizIdString))
        {
            try (DatabaseConnector db = DatabaseConnector.getDefaultInstance())
            {
                int serial = Integer.parseInt(serialString);
                int quizId = Integer.parseInt(quizIdString);
                int result = db.updateQuizAnswerCorrectByQuizIdSerial(quizId, serial, true);
                if (result == 1)
                {
                    response.getWriter().write("Success");
                }
            } catch (SQLException | ClassNotFoundException sqle)
            {
                response.getWriter().write("SQLException");
                logError(sqle);

            } catch (NumberFormatException nfe)
            {
                response.getWriter().write("NumberFormatException");
                logError(nfe);

            }
        } else
        {
            //invalid params. empty param
            response.getWriter().write("NoParameter");
        }
    }

    private void processViewAnswerGET(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession();
        String quizid = request.getParameter("id");
        if (quizid == null || quizid.isEmpty())
        {
            response.sendRedirect(request.getContextPath() + "/RequestQuizAnswer");
        } else
        {
            try (DatabaseConnector dbConn = DatabaseConnector.getDefaultInstance())
            {
                int id;
                try
                {
                    //a numeric id 
                    id = Integer.parseInt(quizid);
                } catch (NumberFormatException e)
                {
                    //a text id
                    //convert any non-chinese characters to chinese characters

                    //quizid = new String(quizid.getBytes("ISO8859_1"), "UTF-8");
                    session.setAttribute("quizid", quizid);
                    id = dbConn.getQuizIdByTextId(quizid);

                }
                //System.out.println("remote host: "+request.getRemoteHost());
                //System.out.println(quizid);
                //retrieve answer records from db
                MyQuiz myQuiz = dbConn.selectFromQuizsById(id);

                if (myQuiz != null)
                {
                    ArrayList<QuizAnswer> quizAnswers = dbConn.selectFromQuizAnswerByQuizId(id);
                    session.setAttribute("quiz", myQuiz);
                    session.setAttribute("quizAnswers", quizAnswers);
                } else
                {
                    session.setAttribute("quiz", null);
                }

                //forward the request
                request.getRequestDispatcher(request.getServletPath() + ".jsp").forward(request, response);
            } catch (NumberFormatException e)
            {
                response.sendRedirect(request.getContextPath() + "/RequestQuizAnswer?message=Please+enter+a+valid+quiz+id");
            } catch (SQLException | ClassNotFoundException ex)
            {
                Logger.getLogger(Servlet.class
                        .getName()).log(Level.SEVERE, null, ex);
                response.sendError(500, ex.getLocalizedMessage());
                logError(ex);

            }
        }
    }

    private void processListenToAudioGET(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession();
        String fquestionid = request.getParameter("fquestionid");
        String secureToken = request.getParameter("token");//get the secure token
        String listenAudioResult;
        String id = request.getParameter("id");

        //long result = new DataProcessorBmc()
        if (fquestionid != null && !fquestionid.isEmpty())
        {
            if (StrUtil.isNotBlank(secureToken) || StrUtil.isNotEmpty(id))
            {

                try (DatabaseConnector db = DatabaseConnector.getDefaultInstance())
                {
                    if (StrUtil.isNotBlank(id))
                    {
                        secureToken = db.selectSecureTokenFromFBookShare(Integer.parseInt(id));
                    }
                    //获得用户安全token所对应的真token
                    String token = db.getBmcToken(secureToken);

                    if (token == null)
                    {
                        listenAudioResult = "此链接密钥已失效";
                    } else
                    {
                        DataProcessorBmc dp = new DataProcessorBmc(token);
                        long result = dp.listenAudio(fquestionid);
                        if (result == 1)
                        {
                            listenAudioResult = "<span style=\"color:green;\">记录成功</span>";
                        } else
                        {
                            listenAudioResult = "录音记录失败，返回" + result;
                        }

                    }

                } catch (SQLException | ClassNotFoundException sqle)
                {
                    Logger.getLogger(Servlet.class
                            .getName()).log(Level.SEVERE, null, sqle);
                    listenAudioResult = "数据库错误，请稍后再试";
                    logError(sqle);
                } catch (InterruptedException | ParseException ex)
                {
                    Logger.getLogger(Servlet.class
                            .getName()).log(Level.SEVERE, null, ex);
                    listenAudioResult = "BMC服务器返回无效";
                    logError(ex);
                }
            } else
            {
                //token
                listenAudioResult = "<strong>缺少密钥，此链接无效。</strong>";
            }
        } else
        {
            //fquestion id
            listenAudioResult = "缺少问题id，此链接无效";
        }

        //forward page
        session.setAttribute("listenAudioResult", listenAudioResult);
        request.getRequestDispatcher(request.getServletPath() + ".jsp").forward(request, response);

    }

    private void processStudentNameLookupGET(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String secureToken = request.getParameter("token");
        String prefix = request.getParameter("prefix");
        String postfix = request.getParameter("postfix");

        String name, xiaomarkURL;

        prefix = URLDecoder.decode(prefix, "UTF-8");
        postfix = URLDecoder.decode(postfix, "UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        if (secureToken == null || secureToken.isEmpty())
        {
            response.getWriter().println("token为空");
        } else
        {
            try (DatabaseConnector dbConn = DatabaseConnector.getDefaultInstance())
            {
                String token = dbConn.getBmcToken(secureToken);
                name = new DataProcessorBmc(token).getStudentName();

                PrintWriter out = response.getWriter();
                //out.println("<head><link rel=\"stylesheet\" href=\"css/pacetrackbar.css\">");
                //out.println("<script src=\"js/pace.js\"></script></head>");
                out.println("<center>" + prefix + name + postfix + "</center>");

            } catch (Exception e)
            {
                response.getWriter().println("读取姓名出错。");
                logError(e);
            }
        }
    }

    /**
     * Servlet method to process FBook request page.
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void processFQuestionBookGET(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession();
        String secureToken;
        String testDate;
        String token = null;
        secureToken = request.getParameter("token");
        testDate = request.getParameter("date");
        String shareId = request.getParameter("share");
        String useXiaomark = request.getParameter("shortlink");
        String xiaomarkURL = "";
        int id = 0;
        String userid = request.getParameter("userid");
        String userpassword = request.getParameter("password");
        boolean exposeToken = false;

        //determine weather the user is trying to login using BMC cridentials or token.
        if (shareId == null || shareId.isEmpty())
        {

            if (testDate == null || testDate.isEmpty())
            {
                session.setAttribute("message", BSAlerts.infoMessage("缺少日期", "请选择你希望生成哪天的错题本。"));
                response.sendRedirect(request.getContextPath() + "/RequestFQuestion");
            }

            //no share id, check if use username/password to login
            if (userid == null || userid.isEmpty() || userpassword == null || userpassword.isEmpty())
            {
                //no username,password, probably using token
                if (secureToken == null || secureToken.isEmpty())
                {
                    //no login cridential
                    session.setAttribute("message", BSAlerts.infoMessage("无法验证身份", "请输入用户名和密码"));
                    response.sendRedirect(request.getContextPath() + "/RequestFQuestion");
                } else
                {
                    //login using secure token
                    try (DatabaseConnector dbConn = DatabaseConnector.getDefaultInstance())
                    {
                        token = dbConn.getBmcToken(secureToken);//get user real token
                        if (token == null)
                        {
                            session.setAttribute("message", BSAlerts.infoMessage("登录密钥失效", "请退出登录后再试"));
                            response.sendRedirect(request.getContextPath() + "/RequestFQuestion");
                        }
                        exposeToken = true;
                    } catch (Exception e)
                    {
                        logError(e);
                        session.setAttribute("message", BSAlerts.infoMessage("数据库错误", "请稍后再试"));
                        response.sendRedirect(request.getContextPath() + "/RequestFQuestion");
                        //logError(e);
                    }
                }
            } else
            {
                //username+password login, get their token
                try (DatabaseConnector dbConn = DatabaseConnector.getDefaultInstance())
                {

                    //get user real token
                    token = new DataProcessorBmc().getBmcStuToken(userid, userpassword);
                    if (token == null)
                    {
                        session.setAttribute("message", BSAlerts.warningMessage("无法验证身份", "用户名/密码错误。请重试"));

                        response.sendRedirect(request.getContextPath() + "/RequestFQuestion");
                    } else
                    {
                        //get secure token
                        secureToken = dbConn.smartCheckUser(userid, userpassword, token);
                        exposeToken = true;
                    }

                    //testToken = secureToken;
                } catch (InterruptedException | ParseException | ClassNotFoundException | SQLException ex)
                {
                    Logger.getLogger(Servlet.class
                            .getName()).log(Level.SEVERE, null, ex);
                    ex.printStackTrace(response.getWriter());
                    token = null;
                    logError(ex);
                }

            }

        } else
        {
            //use share id to see fbook 

            try (DatabaseConnector conn = DatabaseConnector.getDefaultInstance())
            {
                id = Integer.parseInt(shareId);
                Map<String, String> map = conn.selectFromFBookShareById(id);
                if (map != null)
                {
                    testDate = map.get("t");
                    secureToken = map.get("securetoken");
                    token = conn.getBmcToken(secureToken);
                } else
                {
                    session.setAttribute("message", BSAlerts.infoMessage("id无效", "该错题本不存在或已被停止分享。"));
                    response.sendRedirect(request.getContextPath() + "/RequestFQuestion");
                }

            } catch (NumberFormatException e)
            {
                session.setAttribute("message", BSAlerts.infoMessage("id无效", "请输入一个有效地数字分享id"));
                response.sendRedirect(request.getContextPath() + "/RequestFQuestion");
            } catch (SQLException | ClassNotFoundException sqle)
            {
                session.setAttribute("message", BSAlerts.infoMessage("系统错误", "数据库坏了，请右下角客服咨询。"));
                response.sendRedirect(request.getContextPath() + "/RequestFQuestion");
                logError(sqle);
            }

        }
        session.setAttribute("date", testDate);

        /**
         * ****** SECTION 2 ********
         */
        //Communicator comm = new Communicator(secureToken);
        DataProcessorBmc processorBmc = new DataProcessorBmc(token);
        session.setAttribute("token", secureToken);
        try (DatabaseConnector db = DatabaseConnector.getDefaultInstance())
        {
            session.setAttribute("homeworkList", processorBmc.getStudentFQuestions(testDate));/*core*/
            session.setAttribute("fpchecker", new FOriginalPicChecker());
            session.setAttribute("name", processorBmc.getStudentName());

            //generate share link
            //fook id
            id = db.fbookId(testDate, secureToken);
            if (id == 0)
            {
                int fbookId = Randomizer.randomInt(0, 9999);
                db.insertIntoFBookShare(fbookId, testDate, secureToken);
                id = fbookId;
            }

            String defaultShare = request.getScheme() + "://" + request.getServerName() + (request.getServerPort() == 80 ? "" : ":" + request.getServerPort()) + (request.getContextPath().equals("/") ? "" : request.getContextPath()) + request.getServletPath() + "?share=" + id;
            //if generate useXiaomark link
            if (StrUtil.equals(useXiaomark, "on"))
            {
                //if useXiaomark exists in db
                xiaomarkURL = db.getXiaomarkURL(id);
                String xiaomarkToken = db.getXiaomarkApi(secureToken);
                if (StrUtil.isBlank(xiaomarkURL))
                {
                    //fook does not have a xiaomark url.
                    if (StrUtil.isBlank(xiaomarkToken))
                    {
                        //user doesn't have a token, go away!
                        session.setAttribute("share", defaultShare);
                    } else
                    {
                        //and user has a valid token, therefore generate one xiaomark link
                        //TODO IMPLEMENT XIAOMARK URL
                        String dest = defaultShare.replace("127.0.0.1:8081/85Vocabulary", "85vocab.com").replace("localhost:8080/85Vocabulary", "85vocab.com").replace("localhost:8081/85Vocabulary", "85vocab.com");
                        System.out.println(dest);
                        XiaomarkAPI xiaomarkAPI = XiaomarkAPI.generateXiaomarkUrl(xiaomarkToken, dest);
                        if (xiaomarkAPI.getCode() == 0)
                        {
                            xiaomarkURL = xiaomarkAPI.getUrl();
                            db.insertIntoFBookXiaomark(id, xiaomarkURL);
                            session.setAttribute("share", xiaomarkURL);
                        } else
                        {
                            //failed to generate
                            System.out.println(xiaomarkAPI);
                            session.setAttribute("share", defaultShare);
                        }
                        //store this token
                    }
                } else
                {
                    //there is already a xiaomark url generated before, use that!
                    session.setAttribute("share", xiaomarkURL);
                }
            } else
            {
                //no useXiaomark, normal share.
                session.setAttribute("share", defaultShare);
            }

            session.setAttribute("exposeToken", exposeToken);
            request.getRequestDispatcher(request.getServletPath() + ".jsp").forward(request, response);
            //for now, get token from
        } catch (ParseException | BMCSystemException | InterruptedException | SQLException | ClassNotFoundException ex)
        {
            //print stk
            Logger.getLogger(Servlet.class
                    .getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace(response.getWriter());
            logError(ex);
        }
    }

    private void processMyWrongWordsPOST(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession();
        String pageString = request.getParameter("page");
        ArrayList<WrongVocabulary> vocabList;
        //wrongWords

        int page = -1;

        if (pageString != null && !pageString.isEmpty())
        {
            page = new Integer(pageString);
        }

        try (DatabaseConnector dbConn = DatabaseConnector.getDefaultInstance())
        {
            vocabList = dbConn.selectFromWrongVocabularys();

            if (page < 0)
            {
                session.setAttribute("wrongWords", vocabList);
            } else
            {

            }

        } catch (Exception e)
        {
            logError(e);

        }

    }

    private void processFindHomeworkGET(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {

        request.getRequestDispatcher(request.getServletPath() + ".jsp").forward(request, response);

    }

    private void processDownloadHomeworkPOST(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        String dateString = request.getParameter("date");
        String nameString = request.getParameter("name");

        String[] dateArray = dateString.split("-");
        String mouth = dateArray[1];
        String day = dateArray[2];
        String url = "https://server.1000classes.com/bmcserver/download/" + nameString + "同学的" + mouth + "月" + day + "日的作业题.pdf";
        sendMessage(request, response, "", url);
        //response.sendRedirect("https://server.1000classes.com/bmcserver/download/" + nameString + "同学的" + mouth + "月"  + day + "日的作业题.pdf");
    }

    private void processSubmitTestPOST(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession();
        //params
        String wrongWordsString = request.getParameter("wrongWords");
        String gradeString = request.getParameter("grade");

        String[] wrongWords = wrongWordsString.split(",");
        Object testIdObject = session.getAttribute("testId");
        String testIdString = request.getParameter("id");
        if (testIdObject == null && (testIdString == null || testIdString.isEmpty()))
        {
            //session.setAttribute("test", testIdObject);
            session.setAttribute("wrongWordsString", wrongWordsString);
            session.setAttribute("gradeString", gradeString);

            request.getRequestDispatcher("/WEB-INF" + request.getServletPath() + ".jsp").forward(request, response);
        } else
        {
            if (testIdObject == null && testIdString != null)
            {
                testIdObject = testIdString;
            }
            session.setAttribute("testId", testIdObject);
            try (DatabaseConnector dbConn = DatabaseConnector.getDefaultInstance())
            {
                //insert if user has wrong words
                if (wrongWords.length > 0)
                {
                    dbConn.smartInsertIntoWrongWords(wrongWords);
                }

                //PARSE grade into an int
                int grade = Integer.parseInt(gradeString);
                int testId = Integer.parseInt(testIdObject.toString());

                //test id
                dbConn.updateTestTableGradeCompleteByTestId(grade, testId, true);

                //done
                request.getRequestDispatcher("/WEB-INF" + request.getServletPath() + ".jsp").forward(request, response);

            } catch (SQLException | ClassNotFoundException sqle)
            {
                //SQL ERROR
                // PrintWriter o = response.getWriter();
                sendMessage(request, response, "Temporary Error", BSAlerts.dangerMessage("Temporary Error", "There is temporary error. Please try again later."));
                logError(sqle);
            } catch (NumberFormatException nfe)
            {
                //GRADE PARSING ERROR.
                sendMessage(request, response, "Number Error", "Please click \"check grade\" again.");
            }
        }
    }

    private void processTestAnswerPOST(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession();

        try (DatabaseConnector dbConn = DatabaseConnector.getDefaultInstance())
        {
            String idString = request.getParameter("id");
            if (idString == null || idString.isEmpty())
            {
                session.setAttribute("inputMsg", BSAlerts.warningMessage("Please input", "Please enter test id to continue."));
                response.sendRedirect(request.getContextPath() + "/index");
            } else
            {
                //get the id of the test
                int id = Integer.parseInt(idString);
                ArrayList<Vocabulary> list = dbConn.selectFromTestTableByTestId(id);
                String wrongWordsInit = "";
                for (int i = 0; i < list.size(); i++)
                {
                    Vocabulary get = list.get(i);
                    wrongWordsInit += i == 0 ? "" : "," + get.getTerm();
                }
                session.setAttribute("vocabAnswerList", list);
                session.setAttribute("testId", id);
                session.setAttribute("wrongWordsInit", wrongWordsInit);
                request.getRequestDispatcher(request.getServletPath() + ".jsp").forward(request, response);
            }

        } catch (ClassNotFoundException | SQLException e)
        {
            sendMessage(request, response, "ERROR", "There is an unknown error happening...");
            e.printStackTrace();
            logError(e);
        } catch (NumberFormatException nfe)
        {
            session.setAttribute("inputMsg", BSAlerts.warningMessage("Please input ID", "Please enter test id to continue."));
            response.sendRedirect(request.getContextPath() + "/index");

        }
    }

    private void processTestAnswerGET(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession();
        Object listObject = session.getAttribute("vocabAnswerList");
        if (listObject == null)
        {
            timeOutContent(request, response);
        } else
        {
            request.getRequestDispatcher(request.getServletPath() + ".jsp").forward(request, response);
        }
    }

    private void processIndexGET(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        //get session
//        HttpSession session = request.getSession();
//
//        //connect to database
//        try (DatabaseConnector dbConn = DatabaseConnector.getDefaultInstance())
//        {
//            String webMessage = dbConn.getWebAnnoucement();
//            if (webMessage == null || webMessage.isEmpty())
//            {
//                webMessage = "";
//            } else
//            {
//                webMessage = BSAlerts.infoMessage("Message", webMessage);
//            }
//
//            //determine if the website is open for public or not
//            String openWeb = dbConn.selectValueFromSettingsByKey("openweb");
//
//            //In case the website is shut down.
//            if (openWeb.equals("false"))
//            {
//                sendMessage(request, response, "503 SERVICE UNAVAILABLE", "Sorry, the website has been shut down by an admin. Please try again later.");
//            } else
//            {
//                session.setAttribute("webAnnoucement", webMessage);
//                request.getRequestDispatcher(request.getServletPath() + ".jsp").forward(request, response);
//            }
//
//        } catch (ClassNotFoundException | SQLException e)
//        {
//            e.printStackTrace();
//            sendMessage(request, response, "500 Error", "This website is temporarily unavailable. Please try again later.");
//        }

        request.getRequestDispatcher(request.getServletPath() + ".jsp").forward(request, response);
    }

    protected void editFBookShortLink(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String action = request.getParameter("action");
        String token = request.getParameter("token");
        String idStr = request.getParameter("id");
        //JSONObject responseJSON = JSONUtil.createObj();
        try (DatabaseConnector db = DatabaseConnector.getDefaultInstance())
        {
            if (action.equals("delete"))
            {
                //db.delete
            }
        } catch (SQLException | ClassNotFoundException sqle)
        {
            Console.error(sqle);
            logError(sqle);
        }

    }

    private void processTestSubmitTest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        request.getSession().setAttribute("testId", null);
        sendMessage(request, response, "Clear", "TEST ID CLEARED");
    }

}
