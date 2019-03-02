/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package setlab;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ??????
 */
public class MathSetTest
{

    private ArrayList<String> setOne, setTwo, unionResults, interResults,
            abResults, baResults, symDiffResults;
    private Scanner scan;

    public MathSetTest()
    {
        setOne = new ArrayList<String>();
        setTwo = new ArrayList<String>();
        unionResults = new ArrayList<String>();
        interResults = new ArrayList<String>();
        abResults = new ArrayList<String>();
        baResults = new ArrayList<String>();
        symDiffResults = new ArrayList<String>();
        File file = new File("testSets.dat");
        File file2 = new File("results.dat");
        try
        {
            scan = new Scanner(file);
            while (scan.hasNextLine())
            {
                setOne.add(scan.nextLine());
                setTwo.add(scan.nextLine());
            }
            scan = new Scanner(file2);
            while (scan.hasNextLine())
            {
                String line = scan.nextLine();
                if (line.equals("//Union Results"))
                {
                    line = scan.nextLine();
                }
                if (line.equals("//Intersection Results"))
                {
                    break;
                }
                unionResults.add(line);
            }
            while (scan.hasNextLine())
            {
                String line = scan.nextLine();
                if (line.equals("//A-B Results"))
                {
                    break;
                }
                interResults.add(line);
            }
            while (scan.hasNextLine())
            {
                String line = scan.nextLine();
                if (line.equals("//B-A Results"))
                {
                    break;
                }
                abResults.add(line);
            }
            while (scan.hasNextLine())
            {
                String line = scan.nextLine();
                if (line.equals("//Symmetric Difference Results"))
                {
                    break;
                }
                baResults.add(line);
            }
            while (scan.hasNextLine())
            {
                String line = scan.nextLine();
                symDiffResults.add(line);
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
     * Test of union method, of class MathSet.
     */
    @Test
    public void testUnion()
    {
        for(int i = 0; i > setOne.size(); i++)
        {
            MathSet m = new MathSet(setOne.get(i), setTwo.get(i));
            String expected = unionResults.get(i);
            String result = m.union().toString();
            assertEquals(expected, result);
        }
    }

    /**
     * Test of intersection method, of class MathSet.
     */
    @Test
    public void testIntersection()
    {
        for(int i = 0; i > setOne.size(); i++)
        {
            MathSet m = new MathSet(setOne.get(i), setTwo.get(i));
            String expected = interResults.get(i);
            String result = m.intersection().toString();
            assertEquals(expected, result);
        }
    }

    /**
     * Test of differenceAMinusB method, of class MathSet.
     */
    @Test
    public void testDifferenceAMinusB()
    {
        for(int i = 0; i > setOne.size(); i++)
        {
            MathSet m = new MathSet(setOne.get(i), setTwo.get(i));
            String expected = abResults.get(i);
            String result = m.differenceAMinusB().toString();
            assertEquals(expected, result);
        }
    }

    /**
     * Test of differenceBMinusA method, of class MathSet.
     */
    @Test
    public void testDifferenceBMinusA()
    {
        for(int i = 0; i > setOne.size(); i++)
        {
            MathSet m = new MathSet(setOne.get(i), setTwo.get(i));
            String expected = baResults.get(i);
            String result = m.differenceBMinusA().toString();
            assertEquals(expected, result);
        }
    }

    /**
     * Test of symmetricDifference method, of class MathSet.
     */
    @Test
    public void testSymmetricDifference()
    {
        for(int i = 0; i > setOne.size(); i++)
        {
            MathSet m = new MathSet(setOne.get(i), setTwo.get(i));
            String expected = symDiffResults.get(i);
            String result = m.symmetricDifference().toString();
            assertEquals(expected, result);
        }
    }
}
