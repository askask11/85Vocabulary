/*
 * Author: Jianqing Gao
 * Date: Nov 16, 2020
 * Description: This document is created for receiving files.
 */
package com.vocab85.controller;

import com.vocab85.model.ExcelTranslator;
import com.vocab85.model.FileManager;
import com.vocab85.model.OCRRestAPI;
import com.vocab85.model.Randomizer;
import com.vocab85.model.Vocabulary;
import com.vocab85.model.network.DatabaseConnector;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

/**
 * Servlet created for receiving files.
 *
 * @author Jianqing Gao
 */
@WebServlet(name = "FileServlet", urlPatterns =
{
    "/CheatQuizUpload", "/MaintainAPI"
})
@MultipartConfig
public class FileServlet extends HttpServlet
{

    public static final String[] ALLOWED_EXTENSIONS_OCR =
    {
        "xlsx", "pdf", "tif", "tiff", "jpeg", "jpg", "bmp", "pcx", "png", "gif", "zip"
    };

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
            out.println("<title>Servlet FileServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet FileServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
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
        String path = request.getServletPath();
        if (path.equals("/MaintainAPI"))
        {
            response.setContentType("text/plain");
            try (DatabaseConnector d = DatabaseConnector.getDefaultInstance())
            {
                d.autoUpdateAPIAccountQuota();
                response.getWriter().write("Success");
            } catch (Exception e)
            {
                e.printStackTrace();
                e.printStackTrace(response.getWriter());
            }
        } else
        {
            processRequest(request, response);
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
        String path = request.getServletPath();
        if (path.equals("/CheatQuizUpload"))
        {
            processCheatQuizUploadPOST(request, response);
        } else
        {
            processRequest(request, response);
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

    protected void processCheatQuizUploadPOST(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        HttpSession session = request.getSession();
        if (session.getAttribute("cheat") != null)
        {
            try (DatabaseConnector dbConn = DatabaseConnector.getDefaultInstance())
            {
                File uploadedFile = getFileFromUpload("file", request);
                File file;
                //System.out.println(file.getName());
                if (uploadedFile == null)
                {
                    response.getWriter().write("We did not receive your file!");
                    //session.setAttribute("CheatVocabQuizMessage", BSAlerts.warningMessage("No File Detected", "Please re-upload your file. The file has not been detected!"));
                } else
                {

                    if (isSupportedFormat(uploadedFile.getName()))
                    {
                        if (uploadedFile.getName().endsWith(".xlsx"))
                        {
                            System.out.println("Uploaded an excel 2007");
                            file = uploadedFile;
                        } else
                        {
                            System.out.println("Uploaded an image!");
                            String apiUser, token;
                            String[] cri = dbConn.selectFirstFromOCRAPI();
                            apiUser = cri[0];
                            token = cri[1];
                            String url = OCRRestAPI.regonizeImage_url(token, apiUser, uploadedFile.toPath(), "xlsx", "english");
                            file = File.createTempFile("regonizedVocab", ".xlsx");
                            FileManager.readFileUrl(file, new java.net.URL(url).openStream());
                        }
                        ArrayList<Vocabulary> list = dbConn.selectFromVocabulary();
                        ExcelTranslator translator = new ExcelTranslator(list, file);
                        String skip = request.getParameter("skip");
                        File outFile;
                        int skipInt;
                        if (skip != null && !skip.isEmpty())
                        {

                            skipInt = Integer.parseInt(skip);
                            skipInt = skipInt < 0 ? 0 : skipInt;//no
                        } else
                        {
                            skipInt = 0;
                        }

                        if (skipInt == 0)
                        {
                            outFile = translator.translateExcel();
                        } else
                        {
                            int[] seq;
                            try
                            {
                                seq = Randomizer.trueRandomSet(1, 100, skipInt);
                                System.out.println("True random set generated");
                            } catch (Exception e)
                            {
                                seq = Randomizer.randomInts(1, 100, skipInt);
                                Logger.getLogger(FileServlet.class.getName()).log(Level.SEVERE, "failed to generate true random int set", e);
                            }
                            outFile = translator.translateExcel(seq);
                        }

                        //ok
                        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                        response.setHeader("Content-disposition", "attachment; filename=\"" + outFile.getName() + "\"");
                        response.setHeader("Cache-Control", "no-cache, must-revalidate");
                        OutputStream out;
                        try (FileInputStream in = new FileInputStream(outFile))
                        {
                            out = response.getOutputStream();
                            byte[] buffer = new byte[4096];
                            int length;
                            while ((length = in.read(buffer)) > 0)
                            {
                                out.write(buffer, 0, length);
                            }
                        }
                        out.flush();
                        outFile.delete();
                        file.deleteOnExit();
                        uploadedFile.deleteOnExit();
                        file.delete();
                        uploadedFile.delete();
                        out.close();
                    } else
                    {
                        String formatSupported = "";
                        for (String string : ALLOWED_EXTENSIONS_OCR)
                        {
                            formatSupported += " ." + string;
                        }
                        response.getWriter().write("Your file format is not supported! We support extensions " + formatSupported);
                        //file = null;
                    }

                }

            } catch (Exception e)
            {
                e.printStackTrace(response.getWriter());
                e.printStackTrace();
            }
        } else
        {
            //user not authorized to cheat
            response.sendError(403, "You are not authorized to use this function! Please use it after verified with password.");
        }
    }

    protected File getFileFromUpload(String param, HttpServletRequest request) throws ServletException, IOException
    {
        File storeFile;
        Part filePart = request.getPart(param);
        if (filePart != null)
        {
            String fileName = filePart.getSubmittedFileName();
            String[] fileNameSplit = fileName.split(Pattern.quote("."));
            storeFile = File.createTempFile(fileNameSplit[0], fileNameSplit.length > 1 ? "." + fileNameSplit[fileNameSplit.length - 1] : "");
            storeFile.deleteOnExit();
            FileManager.readFileUrl(storeFile, filePart.getInputStream());
        } else
        {
            storeFile = null;
        }
        return storeFile;
    }

    protected InputStream getFileFromUpload_Stream(String param, HttpServletRequest request) throws IOException, ServletException
    {
        Part filePart = request.getPart(param);
        return filePart.getInputStream();
    }

//    protected File getFileFromUpload(HttpServletRequest request, HttpServletResponse response)throws IOException
//    {
//       
//    }
    private boolean isSupportedFormat(String name)
    {
        for (String extension : ALLOWED_EXTENSIONS_OCR)
        {
            if (name.endsWith(extension))
            {
                return true;
            }
        }
        return false;
    }
}
