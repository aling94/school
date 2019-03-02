/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bst.lab;

import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Alvin
 */
public class BSTTest
{
   private BST<Integer, String> b;
   private Iterator i;
   private static final Integer[] keys = {50, 17, 72, 12, 23, 54, 76, 9, 14, 19, 67};

   public BSTTest()
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
    * Test of add method, of class BST.
    */
   @Test
   public void testAdd()
   {
      b = new BST<Integer, String>();
      i = b.mkBFSIterator();
      assertEquals(null, i.get());
      for(int i = 0; i < keys.length; i++)
         b.add(keys[i], "a");
      i = b.mkBFSIterator();
      for(Integer temp: keys)
      {
         assertEquals(temp, i.get());
         i.next();
      }
   }

   /**
    * Test of find method, of class BST.
    */
   @Test
   public void testFind()
   {
      b = new BST<Integer, String>();
      String v = "a";
      for(int i = 0; i < keys.length; i++)
      {
         b.add(keys[i], v);
         v += "b";
      }
      v = "a";
      for(Integer temp: keys)
      {
         assertEquals(v, b.find(temp));
         v += "b";
      }
   }

   /**
    * Test of revFind method, of class BST.
    */
   @Test
   public void testRevFind()
   {
      b = new BST<Integer, String>();
      String v = "a";
      for(int i = 0; i < keys.length; i++)
      {
         b.add(keys[i], v);
         v += "b";
      }
      v = "a";
      for(Integer temp: keys)
      {
         assertEquals(temp, b.revFind(v));
         v += "b";
      }
   }

   /**
    * Test of delete method, of class BST.
    */
   @Test
   public void testDelete()
   {
      b = new BST<Integer, String>();
      for(Integer temp: keys)
         b.add(temp, "a");
      b.delete(17);
      b.delete(34);
      b.delete(19);
      assertEquals(null, b.find(17));
      assertEquals(null, b.find(34));
      assertEquals(null, b.find(19));
      assertEquals("a", b.find(50));
   }

   /**
    * Test of size method, of class BST.
    */
   @Test
   public void testSize()
   {
      b = new BST<Integer, String>();
      assertEquals(0, b.size());
      b.delete(10);
      assertEquals(0, b.size());
      for(int i = 0; i < keys.length; i++)
      {
         b.add(keys[i], "a");
         assertEquals(i+1, b.size());
      }
      b.add(50, "b");
      assertEquals(11, b.size());
      for(int i = keys.length; i > 0; i--)
      {
         b.delete(keys[i-1]);
         assertEquals(i-1, b.size());
      }
   }
}
