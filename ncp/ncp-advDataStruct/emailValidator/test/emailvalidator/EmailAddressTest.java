/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package emailvalidator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Phil
 */
public class EmailAddressTest
{
    ArrayList<EmailAddress> emlList;
    ArrayList<Boolean> resultList, localResults, domainResults; 
    ArrayList<String> localList, domainList;
    
    public EmailAddressTest()
    {
        //Load files and ArrayLists for testing .validate()
        emlList = new ArrayList<EmailAddress>();
        resultList = new ArrayList<Boolean>();
        File testFile = new File("addresses.dat");
        try
        {
            Scanner scanner = new Scanner(testFile);
            while (scanner.hasNextLine())
            {
                String line = scanner.nextLine();
                emlList.add(new EmailAddress(line));
            }
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        File resultFile = new File("results.dat");
        try
        {
            Scanner scanner2 = new Scanner(resultFile);
            while (scanner2.hasNextLine())
            {
                String line = scanner2.nextLine();
                resultList.add(Boolean.parseBoolean(line));
            }
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        
        //Load files and ArrayLists for testing .isName()
        localList = new ArrayList<String>();
        localResults = new ArrayList<Boolean>();
        File localTest = new File("locals.dat");
        try
        {
            Scanner scanner = new Scanner(localTest);
            while (scanner.hasNextLine())
            {
                String line = scanner.nextLine();
                localList.add(line);
            }
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        File localResult = new File("localResults.dat");
        try
        {
            Scanner scanner2 = new Scanner(localResult);
            while (scanner2.hasNextLine())
            {
                String line = scanner2.nextLine();
                localResults.add(Boolean.parseBoolean(line));
            }
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        
        //Load files and ArrayLists for testing .isDomain()
        domainList = new ArrayList<String>();
        domainResults = new ArrayList<Boolean>();
        File domainTest = new File("domains.dat");
        try
        {
            Scanner scanner = new Scanner(domainTest);
            while (scanner.hasNextLine())
            {
                String line = scanner.nextLine();
                domainList.add(line);
            }
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        File domainResult = new File("domainResults.dat");
        try
        {
            Scanner scanner2 = new Scanner(domainResult);
            while (scanner2.hasNextLine())
            {
                String line = scanner2.nextLine();
                domainResults.add(Boolean.parseBoolean(line));
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
     * Test of toString method, of class EmailAddress.
     */
    @Test
    public void testToString()
    {
        for(int i = 0; i < emlList.size(); i++)
        {
            String expected = emlList.get(i).toString();
            String tested = emlList.get(i).toString();
            assertEquals(expected, tested);
        }
    }

    /**
     * Test of isName method, of class EmailAddress.
     */
    @Test
    public void testIsName()
    {
        for(int i = 0; i < localList.size(); i++)
        {
            boolean expected = localResults.get(i);
            boolean tested = EmailAddress.isName(localList.get(i));
            assertEquals(expected, tested);
        }
    }

    /**
     * Test of isDomain method, of class EmailAddress.
     */
    @Test
    public void testIsDomain()
    {
        for(int i = 0; i < domainList.size(); i++)
        {
            boolean expected = domainResults.get(i);
            boolean tested = EmailAddress.isDomain(domainList.get(i));
            assertEquals(expected, tested);
        }
    }

    /**
     * Test of validate method, of class EmailAddress.
     */
    @Test
    public void testValidate()
    {
        for(int i = 0; i < emlList.size(); i++)
        {
            boolean expected = resultList.get(i);
            boolean tested = emlList.get(i).validate();
            assertEquals(expected, tested);
        }
    }
}
