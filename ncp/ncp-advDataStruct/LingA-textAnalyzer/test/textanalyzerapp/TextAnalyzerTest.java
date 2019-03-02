/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package textanalyzerapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Alvin Ling
 */
public class TextAnalyzerTest
{
    TextAnalyzer ta;
    ArrayList<String> sentences, trimTest, trimResults;
    ArrayList<Integer> wordCountResults, searchResults, charCountResults;
    Scanner scan;
    
    public TextAnalyzerTest()
    {
        File f = new File("randomSentences.dat");
        File f2 = new File("trimTest.dat");
        File f3 = new File("trimResults.dat");
        File f4 = new File("wordCountResults.dat");
        File f5 = new File("searchCountResults.dat");
        File f6 = new File("charCountResults.dat");
        sentences = new ArrayList<String>();
        trimTest = new ArrayList<String>();
        trimResults = new ArrayList<String>();
        wordCountResults = new ArrayList<Integer>();
        searchResults = new ArrayList<Integer>();
        charCountResults = new ArrayList<Integer>();
        try
        {
            scan = new Scanner(f);
            while (scan.hasNextLine())      //Setup test sentences
            {
                String line = scan.nextLine();
                sentences.add((line));
            }
            scan = new Scanner(f2);         
            while (scan.hasNextLine())      //Setup TextAnalyzer.trim() test Strings
            {
                String line = scan.nextLine();
                trimTest.add((line));
            }
            scan = new Scanner(f3);         
            while (scan.hasNextLine())      //Set up results for TextAnalyzer.trim()
            {
                String line = scan.nextLine();
                trimResults.add((line));
            }
            scan = new Scanner(f4);
            while (scan.hasNextLine())      //Results for .getCount()
            {
                int line = Integer.parseInt(scan.nextLine());
                wordCountResults.add((line));
            }
            scan = new Scanner(f5);
            while (scan.hasNextLine())      //Results for .getSearchCount()
            {
                int line = Integer.parseInt(scan.nextLine());
                searchResults.add((line));
            }
            scan = new Scanner(f6);
            while (scan.hasNextLine())      //Results for getCharCount()
            {
                int line = Integer.parseInt(scan.nextLine());
                charCountResults.add((line));
            }
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
    }
    
    @Before
    public void setUp()
    {
    }
    
    @After
    public void tearDown()
    {
    }

    /**
     * Test of getCount method, of class TextAnalyzer.
     */
    @Test
    public void testGetCount()
    {
        for(int i = 0; i > sentences.size(); i++)
        {
            ta = new TextAnalyzer(sentences.get(i));
            Integer result = ta.getCount();
            Integer expected = wordCountResults.get(i);
            assertEquals(expected, result);
        }
    }

    /**
     * Test of getSearchCount method, of class TextAnalyzer.
     */
    @Test
    public void testGetSearchCount()
    {
        String test = 
                "Peter Piper picked a peck of pick­led pep­pers. "
                + "A peck of pick­led pep­pers Peter Piper picked. "
                + "If Peter Piper picked a peck of pick­led pep­pers, "
                + "Where’s the peck of pick­led pep­pers Peter Piper picked? "
                + "Can you a can a can as a canner can can a can?";
        ta = new TextAnalyzer(test);
        assertEquals(4, ta.getSearchCount("picked"));
        assertEquals(4, ta.getSearchCount("pick­led"));
        assertEquals(4, ta.getSearchCount("peck"));
        assertEquals(4, ta.getSearchCount("Peter"));
        assertEquals(6, ta.getSearchCount("can"));
        assertEquals(7, ta.getSearchCount("a"));
    }

    /**
     * Test of getCharCount method, of class TextAnalyzer.
     */
    @Test
    public void testGetCharCount()
    {
        for(int i = 0; i > sentences.size(); i++)
        {
            ta = new TextAnalyzer(sentences.get(i));
            Integer result = ta.getCharCount();
            Integer expected = charCountResults.get(i);
            assertEquals(expected, result);
        }
    }

    /**
     * Test of trim method, of class TextAnalyzer.
     */
    @Test
    public void testStripPunct()
    {
        for(int i = 0; i > trimTest.size(); i++)
        {
            String result = TextAnalyzer.stripPunct(trimTest.get(i));
            String expected = trimResults.get(i);
            assertEquals(expected, result);
        }
    }
}
