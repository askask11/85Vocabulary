/*
 * Author: jianqing
 * Date: Apr 26, 2020
 * Description: This document is created for generating captcha.
The code in this class was optimized from https://www.jianshu.com/p/009914797af2.
Original author: SamGroves.
Code base and parameters were changed to fit this application. Code body was completely understood by Jianqing Gao.
 */
package com.vocab85.model;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import static com.vocab85.model.Randomizer.getRandomColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Bean class for captcha. This will generate a Captchas as well. Store this
 * class in session to verify users.
 *
 * @author jianqing
 */
public class Captcha
{

    public static final char[] MAP_TABLE =
    {
        '0', '1', '2', '3', '4', '5',
        '6', '7', '8', '9', '0', '1',
        '2', '3', '4', '5', '6', '7',
        '8', '9'
    };
    private String body;//the body of the code
    private LocalDateTime expireTime;//use UTC time zone.
    private BufferedImage image;

    public static final Font FONT = new Font(/*"宋体"*/"Times New Roman", Font.PLAIN, 18);

    public Captcha(String body, LocalDateTime expireTime)
    {
        this.body = body;
        this.expireTime = expireTime;
    }

    public Captcha()
    {
    }

    public String getBody()
    {
        return body;
    }

    public void setBody(String body)
    {
        this.body = body;
    }

    public LocalDateTime getExpireTime()
    {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime)
    {
        this.expireTime = expireTime;
    }

    public BufferedImage getImage()
    {
        return image;
    }

    public void setImage(BufferedImage image)
    {
        this.image = image;
    }

    public static Captcha generateCaptcha(int width, int height) throws UnsupportedEncodingException
    {
        return generateCaptcha(Randomizer.randomLetters(MAP_TABLE, 4), 20, 13, width, height);
    }

    public static Captcha generateCaptcha(String msg, int left, int space, int width, int height) throws UnsupportedEncodingException
    {
        //msg = new String(msg.getBytes("UTF-8"),"UTF-8");
//        byte[] utf8 = msg.getBytes("UTF-8");
//        msg = new String(utf8);
        if (width <= 0)
        {
            width = 60;
        }
        if (height <= 0)
        {
            height = 20;
        }
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // get the image graphics
        Graphics g = image.getGraphics();
        //creates a random object
        Random random = new Random();
        //creates an instance of captcha class.
        Captcha captcha = new Captcha();
        // set background color
        g.setColor(getRandomColor(200, 250));
        g.fillRect(0, 0, width, height);
        //set the font
        g.setFont(FONT);
        // generate 169 random lines, so it is less possible to be read by bots.
        g.setColor(getRandomColor(160, 200));
        for (int i = 0; i < 168; i++)
        {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }
        //get the randomly generated code
        //String strEnsure = "";//"邓安淇";
        String str;
        for (int i = 0; i < msg.length(); ++i)
        {
            //strEnsure += MAP_TABLE[(int) (MAP_TABLE.length * Math.random())];
            // show the code on the image
            g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
            // append the character to the string
            str = msg.substring(i, i + 1);
            // draw the code on the image
            //g.
            g.drawString(str, space * i + left, 25);
        }
        // output image
        g.dispose();//stop drawing
        captcha.setImage(image);
        captcha.setBody(msg);
        return captcha;
    }

    public static void main(String[] args) throws IOException
    {
        //cons a frame
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        //see a label
        Captcha captcha = Captcha.generateCaptcha(97, 37);
        JLabel imageLabel = new JLabel();
        imageLabel.setIcon(new ImageIcon(captcha.getImage()));
        imageLabel.setText(captcha.getBody());

        frame.add(imageLabel);

        /*Batch captcha generate*/
        imageLabel.addMouseListener(new MouseAdapter()
        {
            //int counter=0;
            @Override
            public void mouseClicked(MouseEvent e)
            {
                try
                {
                    int w, h;
                    w = 100;
                    h = 35;
                    Captcha captcha = Captcha.generateCaptcha(new String("邓安淇".getBytes("ISO8859_1"), "UTF-8"), 17, 22, w, h);
                    imageLabel.setIcon(new ImageIcon(captcha.getImage()));
                    imageLabel.setBounds(imageLabel.getX(), imageLabel.getY(), w, h);
                    imageLabel.setText(captcha.getBody());
//                try
//                {
//                    File parent = new File("/Users/jianqing/Desktop/ac");
//                    File children = new File(parent, "ac"+counter+".jpeg");
//                    System.out.println(ImageIO.write(captcha.getImage(), "jpeg", children));
//                    counter++;
//                } catch (IOException ex)
//                {
//                    Logger.getLogger(Captcha.class.getName()).log(Level.SEVERE, null, ex);
//                }
                } catch (UnsupportedEncodingException ex)
                {
                    Logger.getLogger(Captcha.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });

        frame.pack();

        frame.setVisible(true);

    }

}
