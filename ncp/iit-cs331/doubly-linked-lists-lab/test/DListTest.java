/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Alvin
 */
public class DListTest
{
    private static final Integer[] vals = {23, 100, 24, 45, 67, 65, 100, 4, 56, 77, 99, 45};
    private static final Integer[] findVals = {23, 100, 24, 666, 67, 65, 666, 4, 56, 666, 99, 666};
    private static final Integer magicNumber = 666;
    private DList<Integer> d;
    private Iterator itf, itr, itff, itfr;
    
    public DListTest()
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
    
    @Test
    public void testConstructors()
    {
        d = new DList<>();
        assertEquals(0, d.size());
        itf = d.makeFwdIterator();
        itr = d.makeRevIterator();
        itff = d.makeFwdFindIterator(vals[0]);
        itfr = d.makeRevFindIterator(vals[0]);
        assertEquals(itf.get(), null);
        assertEquals(itr.get(), null);
        assertEquals(itff.get(), null);
        assertEquals(itfr.get(), null);
    }

    /**
     * Test of insertFront method, of class DList.
     */
    @Test
    public void testInsertFront()
    {
        d = new DList<>();
        testInsert: for(int i = 0; i < 12; i++)
        {
            d.insertFront(vals[i]);
            itf = d.makeFwdIterator();
            itr = d.makeRevIterator();
            assertEquals(true, itf.isValid());
            assertEquals(true, itr.isValid());
            assertEquals(vals[i], itf.get());
            assertEquals(vals[0], itr.get());
        }
        itf = d.makeFwdIterator();
        itr = d.makeRevIterator();
        testNextValid: for(int i = 0; i < 12; i++)
        {
            assertEquals(true, itf.isValid());
            assertEquals(vals[11-i], itf.get());
            itf.next();
            assertEquals(true, itr.isValid());
            assertEquals(vals[i], itr.get());
            itr.next();
        }
        itf.next();
        itr.next();
        assertEquals(false, itf.isValid());
        assertEquals(false, itr.isValid());
        assertEquals(null, itf.get());
        assertEquals(null, itr.get());
    }

    /**
     * Test of insertEnd method, of class DList.
     */
    @Test
    public void testInsertEnd()
    {
        d = new DList<>();
        itf = d.makeFwdIterator();
        itr = d.makeRevIterator();
        testInsert: for(int i = 0; i < 12; i++)
        {
            d.insertEnd(vals[i]);
            itf = d.makeFwdIterator();
            itr = d.makeRevIterator();
            assertEquals(true, itf.isValid());
            assertEquals(true, itr.isValid());
            assertEquals(vals[i], itr.get());
            assertEquals(vals[0], itf.get());
        }
        itf = d.makeFwdIterator();
        itr = d.makeRevIterator();
        testNextValid: for(int i = 0; i < 12; i++)
        {
            assertEquals(true, itf.isValid());
            assertEquals(true, itr.isValid());
            assertEquals(vals[i], itf.get());
            itf.next();
            assertEquals(vals[11-i], itr.get());
            itr.next();
        }
        itf.next();
        itr.next();
        assertEquals(false, itf.isValid());
        assertEquals(false, itr.isValid());
        assertEquals(null, itf.get());
        assertEquals(null, itr.get());
    }

    /**
     * Test of deleteFront method, of class DList.
     */
    @Test
    public void testDeleteFront()
    {
        d = new DList<>();
        for(Integer val: vals)
            d.insertFront(val);
        testDelete: for(int i = 1; i < 12; i++)
        {
            d.deleteFront();
            itf = d.makeFwdIterator();
            itr = d.makeRevIterator();
            assertEquals(vals[11-i], itf.get());
            assertEquals(vals[0], itr.get());
        }
        d.deleteFront();
        itf = d.makeFwdIterator();
        itr = d.makeRevIterator();
        assertEquals(null, itf.get());
        assertEquals(null, itr.get());
    }

    /**
     * Test of deleteEnd method, of class DList.
     */
    @Test
    public void testDeleteEnd()
    {
        d = new DList<>();
        for(Integer val: vals)
            d.insertFront(val);
        testDelete: for(int i = 1; i < 12; i++)
        {
            d.deleteEnd();
            itf = d.makeFwdIterator();
            itr = d.makeRevIterator();
            assertEquals(vals[11], itf.get());
            assertEquals(vals[i], itr.get());
        }
        d.deleteFront();
        itf = d.makeFwdIterator();
        itr = d.makeRevIterator();
        assertEquals(null, itf.get());
        assertEquals(null, itr.get());
    }

    /**
     * Test of size method, of class DList.
     */
    @Test
    public void testSizeFront()
    {
        d = new DList<>();
        testInsertSize: for(int i = 0; i < 12; i ++)
        {
            d.insertFront(vals[i]);
            assertEquals(i+1, d.size());
        }
        assertEquals(12, d.size());
        testDeleteSize: for(int i = 12; i > 0; i--)
        {
            d.deleteFront();
            assertEquals(i - 1, d.size());
        }
        d.deleteFront();
        assertEquals(0, d.size());
    }
    
    @Test
    public void testSizeEnd()
    {
        d = new DList<>();
        testInsertSize: for(int i = 0; i < 12; i ++)
        {
            d.insertEnd(vals[i]);
            assertEquals(i+1, d.size());
        }
        assertEquals(12, d.size());
        testDeleteSize: for(int i = 12; i > 0; i--)
        {
            d.deleteEnd();
            assertEquals(i - 1, d.size());
        }
        d.deleteEnd();
        assertEquals(0, d.size());
    }
    
    @Test
    public void testFwdIterator()
    {
        d = new DList<>();
        itf = d.makeFwdIterator();
        itf.delete();
        assertEquals(0, d.size());
        for(Integer val: vals)
            d.insertFront(val);
        itf = d.makeFwdIterator();
        test: for(int i = 12; i > 0; i--)
        {
            assertEquals(vals[i-1], itf.get());
            assertEquals(true, itf.isValid());
            itf.delete();
            assertEquals(i - 1, d.size());
        }
        itf.delete();
        assertEquals(null, itf.get());
        assertEquals(false, itf.isValid());
        assertEquals(0, d.size());
    }
    
    @Test
    public void testRevIterator()
    {
        d = new DList<>();
        itr = d.makeFwdIterator();
        itr.delete();
        assertEquals(0, d.size());
        for(Integer val: vals)
            d.insertFront(val);
        itr = d.makeFwdIterator();
        test: for(int i = 12; i > 0; i--)
        {
            assertEquals(vals[i-1], itr.get());
            assertEquals(true, itr.isValid());
            itr.delete();
            assertEquals(i - 1, d.size());
        }
        itr.delete();
        assertEquals(null, itr.get());
        assertEquals(false, itr.isValid());
        assertEquals(0, d.size());
    }
    
    @Test
    public void testFwdFindIterator()
    {
        d = new DList<>();
        testNoFind: for(int i = 0; i < 3; i++)
        {
            d.insertFront(findVals[i]);
            itff = d.makeFwdFindIterator(magicNumber);
            assertEquals(false, itff.isValid());
            assertEquals(null, itff.get());
        }
        testFound: for(int i = 3; i < 12; i++)
        {
            d.insertFront(findVals[i]);
            itff = d.makeFwdFindIterator(magicNumber);
            assertEquals(true, itff.isValid());
            assertEquals(magicNumber, itff.get());
        }
        testNext: for(int i = 0; i < 3; i++)
        {
            itff.next();
            assertEquals(true, itff.isValid());
            assertEquals(magicNumber, itff.get());
        }
        itff.next();
        assertEquals(false, itff.isValid());
        assertEquals(null, itff.get());
        itff = d.makeFwdFindIterator(magicNumber);
        testDelete: for(int i = 0; i < 3; i++)
        {
            itff.delete();
            assertEquals(11-i, d.size());
            assertEquals(true, itff.isValid());
            assertEquals(magicNumber, itff.get());
        }
        itff.delete();
        assertEquals(8, d.size());
        assertEquals(false, itff.isValid());
        assertEquals(null, itff.get());
    }
    
    @Test
    public void testRevFindIterator()
    {
        d = new DList<>();
        testNoFind: for(int i = 0; i < 3; i++)
        {
            d.insertFront(findVals[i]);
            itfr = d.makeRevFindIterator(magicNumber);
            assertEquals(false, itfr.isValid());
            assertEquals(null, itfr.get());
        }
        testFound: for(int i = 3; i < 12; i++)
        {
            d.insertFront(findVals[i]);
            itfr = d.makeRevFindIterator(magicNumber);
            assertEquals(true, itfr.isValid());
            assertEquals(magicNumber, itfr.get());
        }
        testNext: for(int i = 0; i < 3; i++)
        {
            itfr.next();
            assertEquals(true, itfr.isValid());
            assertEquals(magicNumber, itfr.get());
        }
        itfr.next();
        assertEquals(false, itfr.isValid());
        assertEquals(null, itfr.get());
        itfr = d.makeRevFindIterator(magicNumber);
        testDelete: for(int i = 0; i < 3; i++)
        {
            itfr.delete();
            assertEquals(11-i, d.size());
            assertEquals(true, itfr.isValid());
            assertEquals(magicNumber, itfr.get());
        }
        itfr.delete();
        assertEquals(8, d.size());
        assertEquals(false, itfr.isValid());
        assertEquals(null, itfr.get());
    }
    
    @Test
    public void testFwdFindNull()
    {
        d = new DList<>();
        Integer[] nullVals = {23, 100, 24, null, 67, 65, null, 4, 56, null, 99, null};
        testNoFind: for(int i = 0; i < 3; i++)
        {
            d.insertFront(nullVals[i]);
            itff = d.makeFwdFindIterator(null);
            assertEquals(false, itff.isValid());
        }
        testFound: for(int i = 3; i < 12; i++)
        {
            d.insertFront(nullVals[i]);
            itff = d.makeFwdFindIterator(null);
            assertEquals(true, itff.isValid());
        }
        testNext: for(int i = 0; i < 3; i++)
        {
            itff.next();
            assertEquals(true, itff.isValid());
        }
        itff.next();
        assertEquals(false, itff.isValid());
        itff = d.makeFwdFindIterator(null);
        testDelete: for(int i = 0; i < 3; i++)
        {
            itff.delete();
            assertEquals(11-i, d.size());
            assertEquals(true, itff.isValid());
        }
        itff.delete();
        assertEquals(8, d.size());
        assertEquals(false, itff.isValid());
    }
    
    @Test
    public void testRevFindNull()
    {
        d = new DList<Integer>();
        Integer[] nullVals = {23, 100, 24, null, 67, 65, null, 4, 56, null, 99, null};
        testNoFind: for(int i = 0; i < 3; i++)
        {
            d.insertFront(nullVals[i]);
            itfr = d.makeRevFindIterator(null);
            assertEquals(false, itfr.isValid());
        }
        testFound: for(int i = 3; i < 12; i++)
        {
            d.insertFront(nullVals[i]);
            itfr = d.makeRevFindIterator(null);
            assertEquals(true, itfr.isValid());
        }
        testNext: for(int i = 0; i < 3; i++)
        {
            itfr.next();
            assertEquals(true, itfr.isValid());
        }
        itfr.next();
        assertEquals(false, itfr.isValid());
        itfr = d.makeRevFindIterator(null);
        testDelete: for(int i = 0; i < 3; i++)
        {
            itfr.delete();
            assertEquals(11-i, d.size());
            assertEquals(true, itfr.isValid());
        }
        itfr.delete();
        assertEquals(8, d.size());
        assertEquals(false, itfr.isValid());
    }

}
