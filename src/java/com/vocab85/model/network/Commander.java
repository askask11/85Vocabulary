/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vocab85.model.network;

//import static CmdExec.s;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jianqing
 */
public class Commander
{

    public static String executeCommand(String command) throws InterruptedException, IOException
    {
        //s = new Scanner(System.in);
        //System.out.print("$ ");
        //String cmd = s.nextLine();
        final Process p = Runtime.getRuntime().exec(command);
        String wholeOutput = "";

        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = null;

        while ((line = input.readLine()) != null)
        {
            wholeOutput += line;
        }

        p.waitFor();
        return wholeOutput;
    }

    public static String executeCommand(String... command) throws InterruptedException, IOException
    {
        //s = new Scanner(System.in);
        //System.out.print("$ ");
        //String cmd = s.nextLine();
        final Process p = Runtime.getRuntime().exec(command);
        String wholeOutput = "";

        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = null;

        while ((line = input.readLine()) != null)
        {
            wholeOutput += line;
        }

        p.waitFor();
        return wholeOutput;
    }

    public static void main(String[] args)
    {
        try
        {
            System.out.println(executeCommand("curl -Is https://baidu.com | head -n 1"));
            
        } catch (InterruptedException ex)
        {
            Logger.getLogger(Commander.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex)
        {
            Logger.getLogger(Commander.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
