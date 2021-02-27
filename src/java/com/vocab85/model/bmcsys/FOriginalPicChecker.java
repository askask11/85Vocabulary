/*
 * Author: jianqing
 * Date: Aug 9, 2020
 * Description: This document is created for
 */
package com.vocab85.model.bmcsys;

import java.util.ArrayList;

/**
 *
 * @author jianqing
 */
public class FOriginalPicChecker
{

    private final ArrayList<ArrayList<String>> storage;

    public FOriginalPicChecker()
    {
        storage = new ArrayList<>();
    }

    public FOriginalPicChecker(ArrayList<ArrayList<String>> storage)
    {
        this.storage = storage;
    }

    public boolean check(ArrayList<String> list)
    {
        if (storage.contains(list))
        {
            return false;
        } else
        {
            storage.add(list);
            return true;
        }
    }

    public static void main(String[] args)
    {
        ArrayList<String> l1 = new ArrayList<>(5);
        ArrayList<String> l2 = new ArrayList<>(5);
        ArrayList<String> l3 = new ArrayList<>(5);
        ArrayList<String> l4 = new ArrayList<>(5);
        FOriginalPicChecker ck = new FOriginalPicChecker();
        //l1.add();
        //l1.add("b");
        //l1.add("c");
        l2.add("1");
        //l2.add("1");
        //l2.add("1");

        l3.add("a");
        //l3.add("b");
        //l3.add("c");

        l4.add("1");
        //l4.add("1");
        //l4.add("1");
        //l4.add("1");
        System.out.println(ck.check(l1));
        System.out.println(ck.check(l2));
        System.out.println(ck.check(l3));
        System.out.println(ck.check(l4));
    }
}
