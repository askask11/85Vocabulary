/*
 * Author: jianqing
 * Date: Jun 23, 2020
 * Description: This document is created for connecting database
 */
package com.vocab85.model.network;

import cn.hutool.core.lang.Console;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.hutool.setting.Setting;
import com.vocab85.model.MyQuiz;
import com.vocab85.model.OCRRestAPI;
import com.vocab85.model.QuizAnswer;
import com.vocab85.model.Randomizer;
import com.vocab85.model.Vocabulary;
import com.vocab85.model.WrongVocabulary;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
//import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.json.simple.parser.ParseException;

/**
 *
 * @author jianqing
 */
public class DatabaseConnector implements AutoCloseable
{

    private Connection dbConn;
    private String dbName;

    public DatabaseConnector() throws SQLException, ClassNotFoundException
    {
        establishConnection();
    }

    public DatabaseConnector(String dbName, String host, String dbUsername, String dbPassword, boolean useSSL) throws SQLException, ClassNotFoundException
    {
        this.dbName = dbName;
        this.establishConnection(dbName, host, dbUsername, dbPassword, useSSL);
        //stablishConnection(dbName, dbName, dbUsername, dbPassword, true);); 
    }

    ///////////////
    ////////////////////VOCAB TABLE
    ///////////////////
    /**
     * Get the translate of a word. An empty string will be returned if there is
     * no such word exists.
     *
     * @param term
     * @return
     * @throws SQLException
     */
    public String selectTranslateFromVocabTableByTerm(String term) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("SELECT translate FROM vocabularyTable WHERE term=?");
        ResultSet rs = null;
        ps.setString(1, term);

        rs = ps.executeQuery();

        if (rs.next())
        {
            return rs.getString(1);
        } else
        {
            return "";
        }

    }

    ///////////////
    ////////////////////SETTING TABLE
    ///////////////////
    public String selectValueFromSettingsByKey(String name) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("SELECT * FROM settings WHERE name=?");
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();
        if (rs.next())
        {
            return rs.getString("value");
        } else
        {
            return null;
        }

    }

    public String getWebAnnoucement() throws SQLException
    {
        return selectValueFromSettingsByKey("webAnnoucement");

    }

    ///////////////
    ////////////////////TEST TABLE
    ///////////////////
    public ArrayList<Vocabulary> selectFromTestTableByTestId(int testId) throws SQLException
    {
        //prepare sql statement, select by testId
        String sql = "SELECT * FROM testTable WHERE testId=" + testId;

        Statement s = dbConn.createStatement();

        ResultSet rs = s.executeQuery(sql);

        //new instance of connection vocab
        ArrayList<Vocabulary> vocabularys = new ArrayList<>();

        //write into rs
        while (rs.next())
        {
            vocabularys.add(new Vocabulary(rs.getInt(1), rs.getString(2), rs.getString(3)));
        }

        return vocabularys;

    }

    ///////////////////
    ///////////////////WRONG WORDS
    //////////////////
    public void smartInsertIntoWrongWords(String[] vocabs) throws SQLException
    {

        for (int i = 0, wrongCount; i < vocabs.length; i++)
        {
            String vocab = vocabs[i];
            wrongCount = getWrongWordsTimesCountByTerm(vocab);
            if (wrongCount > 0)
            {
                updateWrongWordCountByTerm(vocab, wrongCount + 1);//plus the wrong count by 1
            } else
            {
                //create a new record for the word
                insertIntoWrongWords(vocab, selectTranslateFromVocabTableByTerm(vocab));
            }
        }
    }

    public boolean wrongWordsContains(String term) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("SELECT count(*) FROM wrongWords WHERE term=?");
        ResultSet rs;
        ps.setString(1, term);
        rs = ps.executeQuery();
        if (rs.next())
        {
            return (rs.getInt(1) > 0);//if there's more than one row means that the record exists
        }
        return Boolean.FALSE;//no result in the result set means no row
    }

    public int getWrongWordsTimesCountByTerm(String term) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("SELECT count FROM wrongWords WHERE term=?");
        ps.setString(1, term);
        ResultSet rs = ps.executeQuery();
        if (rs.next())
        {
            return rs.getInt(1);//return count
        } else
        {
            return 0;//if there is no such word, means the user never got it wrong before
        }
    }

    /**
     * Update the "count" attribute of table "wrongWords" by "term".
     *
     * @param term
     * @param newCount
     * @return
     * @throws SQLException
     */
    public int updateWrongWordCountByTerm(String term, int newCount) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("UPDATE wrongWords SET count=? WHERE term=?");
        ps.setInt(1, newCount);
        ps.setString(2, term);
        return ps.executeUpdate();
    }

    /**
     * Insert a record into wrong words.
     *
     * @param term
     * @param translate
     * @return
     * @throws SQLException
     */
    public int insertIntoWrongWords(String term, String translate, int count) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("INSERT INTO wrongWords VALUES(?,?,?)");
        ps.setString(1, term);
        ps.setString(2, translate);
        ps.setInt(3, count);//default count is 1, because when the record is created user already get it wrong.
        return ps.executeUpdate();
    }

    /**
     * Insert a record into wrong words, default count=1;
     *
     * @param term
     * @param translate
     * @return
     * @throws SQLException
     */
    public int insertIntoWrongWords(String term, String translate) throws SQLException
    {
        return insertIntoWrongWords(term, translate, 1);
    }

    public ArrayList<WrongVocabulary> selectFromWrongVocabularys() throws SQLException
    {
        Statement s = dbConn.createStatement();//= dbConn.prepareStatement("");
        ArrayList<WrongVocabulary> wrongWords = new ArrayList<>();
        ResultSet rs = s.executeQuery("SELECT * FROM wrongWords");

        while (rs.next())
        {
            wrongWords.add(new WrongVocabulary(rs.getInt("count"), 0, rs.getString("term"), rs.getString("translate")));
        }

        return wrongWords;
    }

    //////////////////
    ///////////////////TEST TABLE
    /////////////////
    public int updateTestTableGradeCompleteByTestId(int testId, int grade, boolean completed) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("UPDATE TestInfo SET grade=?, completed=? WHERE id=?");
        ps.setInt(1, grade);
        ps.setBoolean(2, completed);
        ps.setInt(3, testId);
        return ps.executeUpdate();
    }

    //////////////////
    ///////////////////USERS TABLE
    /////////////////
    public String getBmcToken(String secureToken) throws SQLException
    {
        //String token;
        PreparedStatement ps = dbConn.prepareStatement("SELECT bmctoken FROM users WHERE securetoken=?");
        ps.setString(1, secureToken);
        ResultSet rs = ps.executeQuery();
        if (rs.next())
        {
            return rs.getString(1);
        } else
        {
            return null;
        }
    }

    public String smartCheckUser(String id, String password, String token) throws SQLException
    {
        String secureToken = selectUserTokenByIdPassword(id, password, "securetoken");
        if (secureToken == null)
        {
            String generatedToken = Randomizer.generateBash();
            insertIntoUsers(id, password, token, generatedToken);
            return generatedToken;
        } else
        {
            return secureToken;
        }
    }

    public String selectUserTokenByIdPassword(String id, String pass, String tokenCol) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("SELECT " + tokenCol + " FROM users WHERE id=? and password=?");
        ResultSet rs;
        ps.setString(1, id);
        ps.setString(2, pass);
        rs = ps.executeQuery();
        if (rs.next())
        {
            return rs.getString(1);
        } else
        {
            return null;
        }
    }

    public String getUserXiaomarkToken(String secureToken) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("SELECT sourlapi FROM users WHERE securetoken=?");
        ResultSet rs;
        ps.setString(1, secureToken);
        rs = ps.executeQuery();
        if (rs.next())
        {
            return rs.getString(1);
        } else
        {
            return null;
        }
    }

    public int insertIntoUsers(String id, String pass, String token, String secureToken) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("INSERT INTO users VALUES(?,?,?,?)");
        ps.setString(1, id);
        ps.setString(2, pass);
        ps.setString(3, token);
        ps.setString(4, secureToken);
        return ps.executeUpdate();
    }

    public String getXiaomarkApi(String secureToken) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("SELECT sourlapi FROM users WHERE securetoken=?");
        ResultSet rs;
        ps.setString(1, secureToken);

        rs = ps.executeQuery();

        if (rs.next())
        {
            return rs.getString("sourlapi");
        } else
        {
            return null;
        }
    }

    public int updateXiaomarkApi(String secureToken, String api) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("UPDATE users SET sourlapi=? WHERE securetoken=?");

        if (api == null || api.isEmpty())
        {
            //if the user decides to revoke token, OK.
            ps.setNull(1, java.sql.Types.VARCHAR);
        } else
        {
            //user set new token
            ps.setString(1, api);
        }

        ps.setString(2, secureToken);

        return ps.executeUpdate();
    }

    /////////////////////
    //////QUIZIDS//////
    /////////////////////
    public int getQuizIdByTextId(String textId) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("SELECT id FROM quizid WHERE textid=?");
        ResultSet rs;
        ps.setString(1, textId);
        rs = ps.executeQuery();
        if (rs.next())
        {
            return rs.getInt(1);
        } else
        {
            return 0;
        }
    }

    /////////////////////
    //////QUIZZES//////
    /////////////////////
    public MyQuiz selectFromQuizsById(int id) throws SQLException
    {
        Statement s = dbConn.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM quizzes WHERE id=" + id);

        if (rs.next())
        {
            //return new MyQuiz(rs.getInt(1), dbName, dbName, dbName, id, dbName)
            MyQuiz quiz = new MyQuiz();
            quiz.setId(rs.getInt(1));
            quiz.setDoclink(rs.getString(2));
            quiz.setDate(rs.getString(3));
            quiz.setMessage(rs.getString("message"));
            quiz.setScore(rs.getInt("score"));
            quiz.setAnswerLink(rs.getString("answerlink"));
            return quiz;
        } else
        {
            return null;
        }

    }

    public ArrayList<QuizAnswer> selectFromQuizAnswerByQuizId(int quizid) throws SQLException
    {
        Statement s = dbConn.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM quizanswers WHERE quizid=" + quizid + " ORDER BY serial ASC");
        ArrayList<QuizAnswer> answers = new ArrayList<>();
        while (rs.next())
        {
            QuizAnswer a = new QuizAnswer();
            a.setQuizid(rs.getInt("quizid"));
            a.setSerial(rs.getInt("serial"));
            a.setResponse(rs.getString("response"));
            //null means the question has not been reviewed
            a.setCorrect(rs.getString("correct") == null ? null : rs.getBoolean("correct"));
            a.setAnswer(rs.getString("answer"));
            answers.add(a);
        }
        return answers;
    }

    public int updateQuizAnswerCorrectByQuizIdSerial(int quizId, int serial, boolean correct) throws SQLException
    {
        Statement s = dbConn.createStatement();
        return s.executeUpdate("UPDATE quizanswers SET correct=" + correct + " WHERE quizid=" + quizId + " AND serial=" + serial);
    }

    public int updateQuizAnswerCorrectAnswerByQuizIdSerial(int quizId, int serial, String answer) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareCall("UPDATE quizanswers SET answer=? WHERE quizid=? AND serial=?");
        ps.setString(1, answer);
        ps.setInt(2, quizId);
        ps.setInt(3, serial);
        return ps.executeUpdate();
    }

    /**
     * Update the answer link by quiz id.
     *
     * @param quizId
     * @param docLink
     * @return
     * @throws SQLException
     */
    public int updateQuizzesAnswerDocByQuizId(int quizId, String docLink) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("UPDATE quizzes SET answerlink=? WHERE id=?");
        ps.setString(1, docLink);
        ps.setInt(2, quizId);
        return ps.executeUpdate();
    }

    public int updateQuizScore(int quizId, int score) throws SQLException
    {
        Statement s = dbConn.createStatement();
        return s.executeUpdate("UPDATE quizzes SET score=" + score + " WHERE id=" + quizId);
    }

    ////////
    //////////////////////////
    /////FBOOKSHARE
    ///////////////////////
    ////////
    public Map<String, String> selectFromFBookShareById(int id) throws SQLException
    {
        HashMap<String, String> m = new HashMap<>(2);
        Statement s = dbConn.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM fbookshare WHERE id=" + id);
        if (rs.next())
        {
            m.put("securetoken", rs.getString("securetoken"));
            m.put("t", rs.getString("t"));
            return m;
        } else
        {
            return null;
        }
    }

    public int fbookId(String date, String token) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("SELECT * FROM fbookshare WHERE securetoken=? AND t=?");
        ps.setString(1, token);
        ps.setString(2, date);
        ResultSet rs = ps.executeQuery();
        if (rs.next())
        {
            return rs.getInt("id");
        } else
        {
            return 0;
        }
    }

    public int insertIntoFBookShare(int id, String date, String token) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("INSERT INTO fbookshare VALUES(?,?,?,?)");
        ps.setInt(1, id);
        ps.setString(2, token);
        ps.setString(3, date);
        ps.setNull(4, java.sql.Types.VARCHAR);
        return ps.executeUpdate();
    }

    public String getXiaomarkURL(int id) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("SELECT xiaomarkurl FROM fbookshare WHERE id=?");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next())
        {
            return rs.getString(1);
        }
        return null;
    }

    public int insertIntoFBookXiaomark(int id, String url) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("UPDATE fbookshare SET xiaomarkurl=? WHERE id=?");
        ps.setString(1, url);
        ps.setInt(2, id);
        return ps.executeUpdate();
    }

    public int deleteXiaomarkLinkFromFBookShare(int id) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("UPDATE fbookshare SET xiaomarkurl=? WHERE id=?");
        ps.setNull(1, java.sql.Types.VARCHAR);
        ps.setInt(2, id);
        return ps.executeUpdate();
    }

    public String selectSecureTokenFromFBookShare(int id) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("SELECT securetoken FROM fbookshare WHERE id=?");
        ResultSet rs;
        ps.setInt(1, id);
        rs = ps.executeQuery();
        if (rs.next())
        {
            return rs.getString(1);
        }
        return null;
    }

    //////////////
    /////API USER
    //////////////
    public String[] selectFirstFromOCRAPI() throws SQLException
    {
        Statement s = dbConn.createStatement();
        String[] res = new String[2];
        ResultSet rs = s.executeQuery("SELECT * FROM ocrapi ORDER BY expire ASC LIMIT 1");
        if (rs.next())
        {
            res[0] = rs.getString("username");
            res[1] = rs.getString("token");
            return res;
        } else
        {
            return null;
        }
    }

    public void autoUpdateAPIAccountQuota() throws SQLException, IOException, ParseException
    {
        Statement s = dbConn.createStatement();
        //PreparedStatement ps = dbConn.prepareStatement("UPDATE ocrapi WHERE username=? SET quota=?");

        ResultSet rs = s.executeQuery("SELECT * FROM ocrapi");
        String username, token;
        long quota, quotaOnFile;
        LocalDate date;
        while (rs.next())
        {
            username = rs.getString("username");
            token = rs.getString("token");
            quotaOnFile = rs.getLong("quota");
            date = LocalDate.parse(rs.getString("expire"));
            if (LocalDate.now().isAfter(date))
            {
                deleteFromOCRAPIByUsername(username);
            } else
            {
                quota = OCRRestAPI.getAccountQuota(username, token);
                if (quota == 0)
                {
                    deleteFromOCRAPIByUsername(username);
                } else if (quota != quotaOnFile)
                {
                    updateOCRAPIByUsername(username, quota);
                }
            }
        }
    }

    public int deleteFromOCRAPIByUsername(String username) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("DELETE FROM ocrapi WHERE username=?");
        ps.setString(1, username);
        return ps.executeUpdate();
    }

    public int updateOCRAPIByUsername(String username, long quota) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("UPDATE ocrapi SET quota=? WHERE username=?");
        ps.setLong(1, quota);
        ps.setString(2, username);
        return ps.executeUpdate();
    }

    public static void main(String[] args) throws SQLException
    {
        //////Please DO NOT use "PAC mode" for VPN when connecting to the server.
        //////either switch off the VPN or use "Global Mode".
//        try (DatabaseConnector dbconn = DatabaseConnector.getDefaultInstance())
//        {
//            //System.out.println(dbconn.getQuizIdByTextId("是心动啊"));
//            dbconn.autoUpdateAPIAccountQuota();
//        } catch (Exception ex)
//        {
//            ex.printStackTrace();
//        }
        Entity get = Db.use().get(Entity.create("users"));
        Console.log(get);
    }

    public static DatabaseConnector getDefaultInstance() throws SQLException, ClassNotFoundException
    {
        Setting setting = new Setting("db.setting");
        return /*new DatabaseConnector();*/ new DatabaseConnector(setting.getStr("dbname"), setting.getStr("host"), setting.getStr("user"), setting.getStr("pass"), false);
    }

    public void establishConnection(String dbName, String host, String dbUsername, String dbPassword, boolean useSSL) throws SQLException, ClassNotFoundException
    {
        //NO this.dbConn = dbConn;
        String connectionURL = "jdbc:mysql://" + host + "/" + dbName;
        this.dbConn = null;
        //Find the driver and make connection;

        Class.forName("com.mysql.cj.jdbc.Driver"); //URL for new version jdbc connector.
        Properties properties = new Properties(); //connection system property
        properties.setProperty("user", dbUsername);
        properties.setProperty("password", dbPassword);
        properties.setProperty("useSSL", Boolean.toString(useSSL));//set this true if domain suppotes SSL
        //"-u root -p mysql1 -useSSL false"
        this.dbConn = DriverManager.getConnection(connectionURL, properties);
    }

    public void establishConnection() throws SQLException, ClassNotFoundException
    {
        //Class.forName("com.mysql.cj.jdbc.Driver");
        this.dbConn = Db.use().getConnection();
    }

    ////////////////
    /////VOCAB LIST
    ///////////////
    public ArrayList<Vocabulary> selectFromVocabulary() throws SQLException
    {
        ArrayList<Vocabulary> vocabList = new ArrayList<>();
        Statement s = dbConn.createStatement();
        String sql = "SELECT * FROM vocabularyTable";
        //System.out.println(sql);
        ResultSet rs = s.executeQuery(sql);

        while (rs.next())
        {
            vocabList.add(getVocabularyObjFromRs(rs));
        }
        return vocabList;
    }

    private Vocabulary getVocabularyObjFromRs(ResultSet rs) throws SQLException
    {
        return new Vocabulary(rs.getInt(1), rs.getString(2), rs.getString(3));

    }

    @Override
    public void close() throws SQLException
    {
        this.dbConn.close();
    }

}
