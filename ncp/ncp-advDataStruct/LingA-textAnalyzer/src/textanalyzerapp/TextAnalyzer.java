/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package textanalyzerapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

/**
 * @author Alvin Ling
 * Text Analyzer parses a String of text by spaces using the Scanner class.
 * Each element is then stored in an ArrayList
 */
public class TextAnalyzer
{

    private ArrayList<String> wordList; // Creates a new instance of TextAnalyzer
    private final ArticleConjun artConjun = new ArticleConjun();
    private HashMap<String, String> histMap;
    private HashMap<String, Integer> countMap;
    private Iterator histIterator;
    private Scanner scan;

    public TextAnalyzer()
    {
        wordList = new ArrayList<String>();
        histMap = new HashMap<String, String>();
        countMap = new HashMap<String, Integer>();
    }

    /**
     * Text Analyzer constructor with string parameter.
     * @param  s -user input string
     */
    public TextAnalyzer(String s)
    {
        wordList = new ArrayList<String>();
        histMap = new HashMap<String, String>();
        countMap = new HashMap<String, Integer>();
        scan = new Scanner(s);
        while (scan.hasNext())
        {
            String word = scan.next();
            if (!word.matches("[_\\W]+"))
            {
                word = stripPunct(word).toLowerCase();
                wordList.add(word);
                buildCountMap(word);
            }
        }
        buildHistMap();
        histIterator = histMap.entrySet().iterator();
    }

    /**
     * Returns the number of words within the given text
     */
    public int getCount()
    {
        return wordList.size();
    }

    /**
     * Returns the number of occurrences of the given String within the text
     * @param str String to be searched
     */
    public int getSearchCount(String search)
    {
        if (countMap.containsKey(search.toLowerCase()))
        {
            return countMap.get(search.toLowerCase());
        } else
        {
            return 0;
        }
    }

    /**
     * Returns the number of characters within the String, excluding spaces
     * and punctuation (returns a count of all letters and digits)
     */
    public int getCharCount()
    {
        return wordList.toString().replaceAll("[_\\W]", "").length();
    }

    /**
     * Returns true if the Histogram Map has more entries, else false 
     */
    public boolean hasNextHistEntry()
    {
        return histIterator.hasNext();
    }

    /**
     * Returns the next entry of the Histogram Map
     */
    public String nextHistEntry()
    {
        Map.Entry me = (Map.Entry) histIterator.next();
        return "" + me.getKey() + me.getValue();
    }

    /**
     * Removes leading and trailing non-word characters from a String
     * @param str String to be trimmed
     */
    public static String stripPunct(String str)
    {
        if (str.equals("") || str.matches("[_\\W]+"))
        {
            return "";
        }
        int start = 0;
        for (int i = 0; Character.toString(str.charAt(i)).matches("[_\\W]"); i++)
        {
            start++;
        }
        int end = str.length() - 1;
        for (int i = end; Character.toString(str.charAt(i)).matches("[_\\W]"); i--)
        {
            end--;
        }
        return str.substring(start, end + 1);
    }

    /**
     * Builds a HashMap with the words and corresponding occurences
     */
    private void buildCountMap(String word)
    {
        if (!countMap.containsKey(word))
        {
            countMap.put(word, 1);
        } else
        {
            countMap.put(word, countMap.get(word) + 1);
        }
    }

    /**
     * Builds a HashMap with the word and its Histogram entry
     */
    private void buildHistMap()
    {
        Iterator countIterator = countMap.entrySet().iterator();
        while (countIterator.hasNext())
        {
            Map.Entry me = (Map.Entry) countIterator.next();
            String word = me.getKey() + "";
            if (!histMap.containsKey(word) && !artConjun.contains(word)
                    && !word.matches("[0-9]+"))
            {
                int sCount = Integer.parseInt(me.getValue() + "");
                double amount = sCount / 10.0;
                String plots = "";
                for (double d = 0.0; d <= amount - 1; d++)
                {
                    plots += "\u25A0";
                }
                String histEntry = "\t\t(" + sCount + ")\t" + plots;
                histMap.put(word, histEntry);
            }
        }
    }
} //Class ends here
