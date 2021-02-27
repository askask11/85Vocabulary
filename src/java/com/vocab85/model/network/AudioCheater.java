package com.vocab85.model.network;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import static com.vocab85.model.network.Communicator.DEFAULT_WEBKITSN;

/**
 * @author jianqing
 */
public class AudioCheater
{

    private static final Communicator com = new Communicator("3586f7a79e1421d09aa5bdd6e334e2f1", DEFAULT_WEBKITSN);

    private static void printHomeworkList(String date/*2020-11-05*/) throws IOException, ParseException, InterruptedException
    {

        String studentScheduleTaskList = com.getStudentScheduleTaskList(date);
        JSONParser parser = new JSONParser();
        JSONObject objjson = (JSONObject) parser.parse(studentScheduleTaskList);
        JSONObject map = (JSONObject) objjson.get("map");
        JSONArray list1 = (JSONArray) map.get("list1");

        for (int i = 0; i < list1.size(); i++)
        {
            JSONObject homework = (JSONObject) list1.get(i);
            System.out.println("-------Homework No." + (i + 1) + "--------");
            System.out.println("ID: " + homework.get("fid"));
            System.out.println("Title: " + homework.get("ftasktitle"));
            System.out.println("Name:" + homework.get("ftaskname"));
            System.out.println("Project" + homework.get("fprojectname"));
        }

    }

    public static void main(String[] args) throws IOException, ParseException, InterruptedException
    {
        Scanner keyboard = new Scanner(System.in);
        int quit = 0, choice = 0, counter = 0;

        String response = null, date;
        System.out.println("Welcome to BMC Cheating system");
        while (quit == 0)
        {
            System.out.println("**MENU**");
            System.out.println("Enter 1 to look for BMC homework.");
            System.out.println("Enter 2 to cheating audio listening on a specific homework");
            System.out.println("Enter 0 to Quit");
            choice = keyboard.nextInt();
            keyboard.nextLine();
            switch (choice)
            {
                case 0:
                    quit = 1;
                    break;
                case 1:
                    System.out.println("Please enter the date(yyyy-mm-dd) to be printed, enter 'today' for today.");
                    response = keyboard.nextLine();
                    if (response.equals("today"))
                    {
                        date = DateTimeFormatter.ISO_DATE.format(LocalDate.now());
                    } else
                    {
                        date = response;
                    }
                    System.out.println("OK, we are looking into your homework book now...");
                    printHomeworkList(date);
                    System.out.println("End of your homework list on " + date);
                    System.out.println();
                    //end of the homework list
                    break;
                case 2:
                    counter = 0;
                    do
                    {
                        if (counter > 0)
                        {
                            System.out.println("Sorry, the information entered is illegal, please try again!");
                        }
                        System.out.println("Please enter the ID of the homework");
                        response = keyboard.nextLine();
                        System.out.println("How many times you want it to be listened?");
                        choice = keyboard.nextInt();
                        keyboard.nextLine();
                        counter++;
                    } while (response.isEmpty() || choice < 0);

                    System.out.println("Thanks... We are on it! Here's the log, you may track your progress.");
                    System.err.println("*Automatic Audio Listening Begin*");
                    com.testListenAudio(choice, 20, com.getWrongQuestionIdList(response));
                    System.err.println("*Automatic Audio Listening END*");
                    break;
                default:
                    break;
            }

        }
    }
}
