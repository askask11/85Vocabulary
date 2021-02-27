package com.vocab85.model;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.setting.Setting;
import com.vocab85.model.network.Mailer;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/*
 * 
 * Sample class for OCRWebService.com (REST API) 
 * 
 */
public class OCRRestAPI
{

    final static Setting SETTINGS = new Setting("ocr.setting");

    /**
     * This will call an cloud API to regonize text inside of an image.
     *
     * @param license_code
     * @param user_name
     * @param inputPath
     * @param formatOut
     * @param language
     * @return JSON of the API response
     * @throws IOException
     */
    public static String regonizeImage(
            final String license_code,
            final String user_name,
            final Path inputPath,
            final String formatOut,
            final String language) throws IOException

    {
        /*
        
        	Sample project for OCRWebService.com (REST API).
        	Extract text from scanned images and convert into editable formats.
        	Please create new account with ocrwebservice.com via http://www.ocrwebservice.com/account/signup and get license code

         */

 /*
	
	       You should specify OCR settings. See full description http://www.ocrwebservice.com/service/restguide
	      
	       Input parameters:
	      
		   [language]      - Specifies the recognition language. 
		   		    		 This parameter can contain several language names separated with commas. 
	                         For example "language=english,german,spanish".
				    		 Optional parameter. By default:english
	     
		   [pagerange]     - Enter page numbers and/or page ranges separated by commas. 
				    		 For example "pagerange=1,3,5-12" or "pagerange=allpages".
	                         Optional parameter. By default:allpages
	      
	       [tobw]	  	   - Convert image to black and white (recommend for color image and photo). 
				    		 For example "tobw=false"
	                         Optional parameter. By default:false
	      
	       [zone]          - Specifies the region on the image for zonal OCR. 
				    		 The coordinates in pixels relative to the left top corner in the following format: top:left:height:width. 
				    		 This parameter can contain several zones separated with commas. 
			            	 For example "zone=0:0:100:100,50:50:50:50"
	                         Optional parameter.
	       
	       [outputformat]  - Specifies the output file format.
	                         Can be specified up to two output formats, separated with commas.
				    		 For example "outputformat=pdf,txt"
	                         Optional parameter. By default:doc
	
	       [gettext]	   - Specifies that extracted text will be returned.
				    		 For example "tobw=true"
	                         Optional parameter. By default:false
	     
	        [description]  - Specifies your task description. Will be returned in response.
	                         Optional parameter. 
	
	
		   !!!!  For getting result you must specify "gettext" or "outputformat" !!!!  
	
         */
        // Build your OCR:
        // Extraction text with English language
        String ocrURL = "http://www.ocrwebservice.com/restservices/processDocument?language=" + language + "&outputformat=" + formatOut;

        // Extraction text with English and German language using zonal OCR
        // ocrURL = "http://www.ocrwebservice.com/restservices/processDocument?language=english,german&zone=0:0:600:400,500:1000:150:400";
        // Convert first 5 pages of multipage document into doc and txt
        // ocrURL = "http://www.ocrwebservice.com/restservices/processDocument?language=english&pagerange=1-5&outputformat=doc,txt";
        // Full path to uploaded document
        //String filePath = "C:\\sample_image.jpg";
        byte[] fileContent = Files.readAllBytes(inputPath);

        URL url = new URL(ocrURL);
        System.out.println("Opening connection...");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod("POST");

        connection.setRequestProperty("Authorization", "Basic " + Base64.getEncoder().encodeToString((user_name + ":" + license_code).getBytes()));

        // Specify Response format to JSON or XML (application/json or application/xml)
        connection.setRequestProperty("Content-Type", "application/json");

        connection.setRequestProperty("Content-Length", Integer.toString(fileContent.length));

        // Send POST request
        try (OutputStream stream = connection.getOutputStream())
        {
            // Send POST request
            stream.write(fileContent);
        }

        int httpCode = connection.getResponseCode();

        System.out.println("HTTP Response code: " + httpCode);

        String response;
        // Success request
        switch (httpCode)
        {

            case HttpURLConnection.HTTP_UNAUTHORIZED:
                System.out.println("OCR Error Message: Unauthorizied request");
                response = HttpURLConnection.HTTP_UNAUTHORIZED + "";
                break;
            case HttpURLConnection.HTTP_OK:

                // Get response stream
                // Parse and print response from OCR server
                System.out.println("Success!");
                response = GetResponseToString(connection.getInputStream());
                break;
            default:
                System.out.println("Failed!");
                response = GetResponseToString(connection.getErrorStream());
                //JSONParser parser = new JSONParser();
                //JSONObject jsonObj = (JSONObject) parser.parse(jsonResponse);
                // Error message
                break;

        }

        connection.disconnect();
        return response;
    }

    /**
     * This will regonize the text within an image with cloud API.
     *
     * @param license_code
     * @param user_name
     * @param inputPath
     * @param formatOut
     * @param language
     * @return URL of the output file.
     * @throws IOException
     * @throws ParseException
     */
    public static String regonizeImage_url(
            final String license_code,
            final String user_name,
            final Path inputPath,
            final String formatOut,
            final String language) throws IOException, ParseException
    {
        String response = regonizeImage(license_code, user_name, inputPath, formatOut, language);
        JSONParser parser = new JSONParser();
        if (response.equals("401"))
        {
            return null;
        }

        JSONObject jsonObj = (JSONObject) parser.parse(response);
        System.out.println("AvailablePages: " + jsonObj.get("AvailablePages"));
        System.out.println("ProcessedPages: " + jsonObj.get("ProcessedPages"));
        System.out.println("OutputFileUrl: " + jsonObj.get("OutputFileUrl"));
        if (jsonObj.get("AvailablePages").equals("1"))
        {

            try
            {
                Mailer.sendDefaultMail("xeduoover18@gmail.com", "85vocab.com cheat api quota up", "please update a new token for 85vocab.com");
            } catch (MessagingException ex)
            {
                Logger.getLogger(OCRRestAPI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return (String) jsonObj.get("OutputFileUrl");
    }

    public static String regonizeImage_url(
            final Path inputPath,
            final String formatOut,
            final String language) throws IOException, ParseException
    {

        return regonizeImage_url(SETTINGS.get("code"), SETTINGS.get("user"), inputPath, formatOut, language);
    }

    public static Long getAccountQuota(final String username, final String token) throws IOException, ParseException
    {
        String ocrURL = "http://www.ocrwebservice.com/restservices/getAccountInformation";

        //byte[] fileContent = Files.readAllBytes(inputPath);
        URL url = new URL(ocrURL);
        String response;
        System.out.println("Opening connection...");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod("GET");

        connection.setRequestProperty("Authorization", "Basic " + Base64.getEncoder().encodeToString((username + ":" + token).getBytes()));
        // Specify Response format to JSON or XML (application/json or application/xml)
        connection.setRequestProperty("Content-Type", "application/json");

        connection.connect();

        response = GetResponseToString(connection.getInputStream());

        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(response);
        return (Long) json.get("AvailablePages");
    }

    public static String aliyunRegonizeImage_url(final String appcode, final File inputFile) throws IOException, ParseException
    {
        String bast64Str = cn.hutool.core.codec.Base64.encode(inputFile);
        cn.hutool.json.JSONObject obj = JSONUtil.createObj();
        obj.putOpt("image", bast64Str);
        obj.putOpt("configure", "{\"format\":\"xlsx\",\"dir_assure\":true,\"line_less\":false}");
        HttpRequest request = HttpUtil.createPost("http://form.market.alicloudapi.com/api/predict/ocr_table_parse");
        request.header("Authorization", "APPCODE " + appcode);
        request.body(obj.toString());
        String b = request.execute().body();
        System.out.println(b);
        cn.hutool.json.JSONObject response = JSONUtil.parseObj(b);
        File outfile = File.createTempFile("regonizedVocab", ".xlsx");
        cn.hutool.core.codec.Base64.decodeToFile(response.getStr("tables"), outfile);

        return postToBmc(outfile);
    }

    public static String postToBmc(File file) throws ParseException
    {
        HttpRequest request = HttpUtil.createPost("http://server.1000classes.com/bmcserver/UpHomework");
        request.form("file", file);
        JSONParser parser = new JSONParser();
        JSONObject jSONObject = (JSONObject) parser.parse(request.execute().body());
        return (String) jSONObject.get("picurl");
    }

    public static void main(String[] args) throws Exception
    {
//        JFileChooser chooser = new JFileChooser();
//        int showOpenDialog = chooser.showOpenDialog(null);
//        if (showOpenDialog == JFileChooser.APPROVE_OPTION)
//        {
//        File f = File.createTempFile("originalVocab", ".png");
//        f.deleteOnExit();
//        FileManager.readFileUrl(f, new URL("https://server.1000classes.com/bmcserver/attachment/2020-12/24eec24e452f48b08b3b8da120895507.png").openStream());
//        System.out.println("File path:" + f.getAbsolutePath());
//        regonizeImage_url(LICENSE_CODE, USERNAME, f.toPath(), "xlsx", "english");
        System.out.println(getAccountQuota("XEDUO", "4E79431F-3351-4FD8-8D0E-2B2D95495237"));
    }

    private static String GetResponseToString(InputStream inputStream) throws IOException
    {
        InputStreamReader responseStream = new InputStreamReader(inputStream);

        BufferedReader br = new BufferedReader(responseStream);
        StringBuilder strBuff = new StringBuilder();
        String s;
        while ((s = br.readLine()) != null)
        {
            strBuff.append(s);
        }

        return strBuff.toString();
    }
    /*
    private static void PrintOCRResponse(String jsonResponse) throws ParseException, IOException
    {
        // Parse JSON data
        JSONParser parser = new JSONParser();
        JSONObject jsonObj = (JSONObject) parser.parse(jsonResponse);

        // Get available pages
        System.out.println("Available pages: " + jsonObj.get("AvailablePages"));

        // get an array from the JSON object
        JSONArray text = (JSONArray) jsonObj.get("OCRText");

        // For zonal OCR: OCRText[z][p]    z - zone, p - pages
        for (int i = 0; i < text.size(); i++)
        {
            System.out.println(" " + text.get(i));
        }

        // Output file URL
        String outputFileUrl = (String) jsonObj.get("OutputFileUrl");

        // If output file URL is specified
        if (outputFileUrl != null && !outputFileUrl.equals(""))
        {
            // Download output file
            DownloadConvertedFile(outputFileUrl);
        }
    }

    // Download converted output file from OCRWebService
    private static void DownloadConvertedFile(String outputFileUrl) throws IOException
    {
        URL downloadUrl = new URL(outputFileUrl);
        HttpURLConnection downloadConnection = (HttpURLConnection) downloadUrl.openConnection();

        if (downloadConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
        {

            // opens an output stream to save into file
            try (InputStream inputStream = downloadConnection.getInputStream())
            {
                // opens an output stream to save into file
                FileOutputStream outputStream = new FileOutputStream("C:\\converted_file.doc");

                int bytesRead = -1;
                byte[] buffer = new byte[4096];
                while ((bytesRead = inputStream.read(buffer)) != -1)
                {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.close();
            }
        }

        downloadConnection.disconnect();
    }
     */

}
