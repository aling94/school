/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Stack;

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
public class StackTest
{
    public Integer i1 = new Integer(1);
    public Integer i2 = new Integer(2);
    public Integer i5 = new Integer(5);
    public Integer i10 = new Integer(10);
    public Integer i15 = new Integer(15);
    public Integer i20 = new Integer(20);
    public Integer i25 = new Integer(25);
    public Integer[] vals = {i2, i5, i10, i1, i20, i15, i25};

    
    public StackTest()
    {
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
     * Test of main method, of class Stack.
     */

    @Test
    public void testPush()
    {
        Stack<Integer> s = new Stack<Integer>();
        for(Integer num : vals)
        {
            s.push(num);
            Integer expected = num;
            Integer result = s.top();
            assertEquals(expected, result);
        }
    }

    /**
     * Test of pop method, of class Stack.
     */
    @Test
    public void testPop()
    {
        Stack<Integer> s = new Stack<Integer>();
        for(Integer num: vals)
        {
            s.push(num);
        }
        for(int i = 6; i >= 0; i--)
        {
            Integer expected = vals[i];
            Integer result = s.pop();
            assertEquals(expected, result);
            if(i != 0)
                assertEquals(vals[i-1], s.top());
            else
                assertEquals(null, s.top());
        }
    }

    /**
     * Test of top method, of class Stack.
     */
    @Test
    public void testTop()
    {
        Stack<Integer> s = new Stack<Integer>();
        assertEquals(null, s.top());
        for(int i = 0; i < 7; i++)
        {
            s.push(vals[i]);
            assertEquals(vals[i], s.top());
        }
        for(int i = 6; i >= 0; i--)
        {
            assertEquals(s.top(), s.pop());
            assertEquals(i, s.size());
            if(i != 0)
                assertEquals(vals[i-1], s.top());
            else
                assertEquals(null, s.top());
        }
    }

    /**
     * Test of size method, of class Stack.
     */
    @Test
    public void testSize()
    {
        Stack<Integer> s = new Stack<Integer>();
        for(int i = 0; i < 7; i++)
        {
            assertEquals(i, s.size());
            s.push(vals[i]);
            assertEquals(i+1, s.size());
        }
        for(int i = 6; i >= 0; i--)
        {
            s.pop();
            assertEquals(i, s.size());
        }
    }
    
    @Test
    public void testSize2()
    {
        Stack<Integer> s = new Stack<Integer>();
        for(int i = 0; i <= 20; i++)
        {
            s.push(i5);
            int result = s.size();
            if(i == 5)
            {
                assertEquals(6, result);
                s.pop(); s.pop(); s.pop(); s.pop();
                result = s.size();
                assertEquals(2, result);
            } else
            if(i == 20)
            {
                assertEquals(17, result);
                s.pop(); s.pop(); s.pop(); s.pop();
                result = s.size();
                assertEquals(13, result);
            }
        }

    }
}
