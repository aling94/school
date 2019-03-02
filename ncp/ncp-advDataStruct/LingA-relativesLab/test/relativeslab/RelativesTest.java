/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relativeslab;

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Alvin
 */
public class RelativesTest
{

    private List<String> members, names, results;
    private Relatives r;

    public RelativesTest()
    {
        members = new ArrayList<String>();
        names = new ArrayList<String>();
        results = new ArrayList<String>();
        File f1 = new File("members.dat");
        File f2 = new File("people.dat");
        File f3 = new File("results.dat");
        try
        {
            Scanner scan = new Scanner(f1);
            while (scan.hasNextLine())
            {
                members.add(scan.nextLine());
            }
            scan = new Scanner(f2);
            while(scan.hasNextLine())
            {
                names.add(scan.nextLine());
            }
            scan = new Scanner(f3);
            while(scan.hasNextLine())
            {
                results.add(scan.nextLine());
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
     * Test of getRelatives method, of class Relatives.
     */
    @Test
    public void testGetRelatives()
    {
        r = new Relatives();
        for(String people: members)
        {
        r.setPersonRelative(people);
        }
        
        for(int i = 0; i < names.size(); i++)
        {
            String result = r.getRelatives(names.get(i));
            String expected = results.get(i);
            assertEquals(expected, result);
        }
        r = new Relatives(members);
        for(int i = 0; i < names.size(); i++)
        {
            String result = r.getRelatives(names.get(i));
            String expected = results.get(i);
            assertEquals(expected, result);
        }
    }
}
