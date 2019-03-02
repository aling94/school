/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package avl.tree.lab;

import avl.tree.lab.AVL;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Alvin
 */
public class AVLTest
{

   private AVL<Integer, Integer> a;
   private Iterator<Integer> it;
   // 0: size/find keys, 1: right, 2: left, 3: doubleRight, 4: doubleLeft
   private static final Integer[] keys = {50, 17, 72, 12, 23, 54, 76, 9, 14, 19, 67};
   private static final Integer[][] tests = { {6, 4, 7, 3, 5, 2}, {6, 4, 9, 7, 10, 11}, {6, 3, 7, 2, 4, 5}, {6, 4, 10, 9, 11, 7},
                                              {4, 3, 6, 2, 5, 7}, {9, 6, 10, 4, 7, 11}, {4, 3, 6, 2, 5, 7}, {9, 6, 10, 4, 7, 11},
                                              {4, 3, 2, 6, 5, 7}, {9, 6, 4, 7, 10, 11}, {4, 3, 2, 6, 5, 7}, {9, 6, 4, 7, 10, 11}};


   public AVLTest()
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
   public void testAdd()
   {
      a = new AVL<Integer, Integer>();
      it = a.mkBFiterator();
      assertEquals(null, it.get());
      for(int i = 0; i < 4; i ++)
      {
         a = new AVL<Integer, Integer>();
         for(Integer val: tests[i]) a.add(val, val);
         it = a.mkBFiterator();
         for(Integer val: tests[i+4])
         {
            assertEquals(val, it.get());
            it.next();
         }
         it = a.mkDFiterator();
         for(Integer val: tests[i+8])
         {
            assertEquals(val, it.get());
            it.next();
         }
      }
   }

   @Test
   public void testFind()
   {
      AVL<Integer, String> ar = new AVL<Integer, String>();
      String v = "a";
      for(int i = 0; i < keys.length; i++)
      {
         ar.add(keys[i], v);
         v += "b";
      }
      v = "a";
      for(Integer temp: keys)
      {
         assertEquals(v, ar.find(temp));
         v += "b";
      }
   }

   @Test
   public void testDelete()
   {
      AVL<Integer, String> ar = new AVL<Integer, String>();
      for(Integer temp: keys)
         ar.add(temp, "a");
      ar.delete(17);
      ar.delete(34);
      ar.delete(19);
      assertEquals(null, ar.find(17));
      assertEquals(null, ar.find(34));
      assertEquals(null, ar.find(19));
      assertEquals("a", ar.find(50));
   }

   @Test
   public void testDelete2()
   {
      a = new AVL<Integer, Integer>();
      for(Integer vals : tests[0]) a.add(vals, vals);
      a.delete(3); a.delete(2);
      Integer[][] solutions = { {6, 4, 7, 5}, {6, 4, 5, 7} };
      it = a.mkBFiterator();
      for(Integer vals : solutions[0])
      {
         assertEquals(vals, it.get());
         it.next();
      }
      it = a.mkDFiterator();
      for(Integer vals : solutions[1])
      {
         assertEquals(vals, it.get());
         it.next();
      }
   }

   @Test
   public void testSize()
   {
      AVL<Integer, String> ar = new AVL<Integer, String>();
      assertEquals(0, ar.size());
      ar.delete(10);
      assertEquals(0, ar.size());
      for(int i = 0; i < keys.length; i++)
      {
         ar.add(keys[i], "a");
         assertEquals(i+1, ar.size());
      }
      ar.add(50, "b");
      assertEquals(11, ar.size());
      for(int i = keys.length; i > 0; i--)
      {
         ar.delete(keys[i-1]);
         assertEquals(i-1, ar.size());
      }
   }
}
