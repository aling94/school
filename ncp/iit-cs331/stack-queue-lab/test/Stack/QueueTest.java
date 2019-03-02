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
public class QueueTest
{

    public Integer i1 = new Integer(1);
    public Integer i2 = new Integer(2);
    public Integer i5 = new Integer(5);
    public Integer i10 = new Integer(10);
    public Integer i15 = new Integer(15);
    public Integer i20 = new Integer(20);
    public Integer i25 = new Integer(25);
    public Integer[] vals =
    {
        i2, i5, i10, i1, i20, i15, i25
    };

    public QueueTest()
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
     * Test of enqueue method, of class Queue.
     */
    @Test
    public void TestEnqueue()
    {
        Queue<Integer> q = new Queue<Integer>();
        int size = 0;
        assertEquals(size, q.size());
        assertEquals(null, q.front());
        for (Integer num : vals)
        {
            q.enqueue(num);
            size++;
            Integer result = q.front();
            assertEquals(i2, result);
            assertEquals(size, q.size());
        }

    }

    /**
     * Test of dequeue method, of class Queue.
     */
    @Test
    public void testDequeue()
    {
        Queue<Integer> q = new Queue<Integer>();
        assertEquals(null, q.front());
        for (Integer num : vals)
        {
            q.enqueue(num);
        }
        for (int i = 0; i < 7; i++)
        {
            assertEquals(7 - i, q.size());
            assertEquals(vals[i], q.dequeue());
            assertEquals(6 - i, q.size());
        }
    }

    /**
     * Test of front method, of class Queue.
     */
    @Test
    public void testFront()
    {
        Queue<Integer> q = new Queue<Integer>();
        assertEquals(null, q.front());
        for (Integer num : vals)
        {
            q.enqueue(num);
        }
        for (int i = 0; i < 7; i++)
        {
            assertEquals(vals[i], q.front());
            assertEquals(vals[i], q.dequeue());
            if (i != 6)
            {
                assertEquals(vals[i + 1], q.front());
            } else
            {
                assertEquals(null, q.front());
            }
        }

    }

    /**
     * Test of size method, of class Queue.
     */
    @Test
    public void testSize()
    {
        Queue<Integer> q = new Queue<Integer>();
        for (int i = 0; i <= 20; i++)
        {
            q.enqueue(i5);
            int result = q.size();
            if (i == 5)
            {
                assertEquals(6, result);
                q.dequeue();
                q.dequeue();
                q.dequeue();
                result = q.size();
                assertEquals(3, result);
            } else if (i == 20)
            {
                assertEquals(18, result);
                q.dequeue();
                q.dequeue();
                q.dequeue();
                q.dequeue();
                result = q.size();
                assertEquals(14, result);
            }
        }
    }
    
}
