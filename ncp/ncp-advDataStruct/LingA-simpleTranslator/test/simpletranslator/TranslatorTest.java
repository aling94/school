/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simpletranslator;

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;
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
public class TranslatorTest
{
    ArrayList<String> testStrings, results;
    Translator t;
    public TranslatorTest()
    {
        testStrings = new ArrayList<String>();
        results = new ArrayList<String>();
        File f = new File("conversation.dat");
        File f2 = new File("results.dat");
        try
        {
            Scanner scan = new Scanner(f);
            Scanner scan2 = new Scanner(f2);
            while(scan.hasNextLine())
            {
                testStrings.add(scan.nextLine());
                results.add(scan2.nextLine());
            }
            
        }
        catch (FileNotFoundException e)
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
     * Test of translate method, of class Translator.
     */
    @Test
    public void testTranslate()
    {
        t = new Translator();
        for(int i = 0; i < testStrings.size(); i++)
        {
            String result = t.translate(testStrings.get(i));
            String expected = results.get(i);
            assertEquals(result, expected);
        }
    }
}
