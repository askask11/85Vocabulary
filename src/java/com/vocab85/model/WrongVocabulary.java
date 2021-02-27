/*
 * Author: Jianqing Gao
 * Date: Jul 5, 2020
 * Description: This document is created for bean class for wrong vocab
 */
package com.vocab85.model;

/**
 * Bean class for wrong vocabulary.
 *
 * @author Jianqing gao
 */
public class WrongVocabulary extends Vocabulary
{

    private int count;

    public WrongVocabulary(int count)
    {
        this.count = count;
    }

    public WrongVocabulary(int count, int listId, String term, String translate)
    {
        super(listId, term, translate);
        this.count = count;
    }

    public int getCount()
    {
        return count;
    }

    public void setCount(int count)
    {
        this.count = count;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("WrongVocabulary{count=").append(count);
        sb.append('}');
        return sb.toString();
    }

}
