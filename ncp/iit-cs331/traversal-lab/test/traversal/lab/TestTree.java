/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package traversal.lab;

import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Alvin
 */
public class TestTree
{

   public TestTree()
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

   private Iterator i;
   private static final Tree t;
   static
   {
      t = new Tree();
      t.add(50);  t.add(17);  t.add(72);  t.add(12);
      t.add(23);  t.add(54);  t.add(76);  t.add(9);
      t.add(14);  t.add(19);  t.add(67);
   }

   @Test
   public void testBFS()
   {
      int[] order = {50, 17, 72, 12, 23, 54, 76, 9, 14, 19, 67};
      i = t.mkBFSIterator();
      for(int temp: order)
      {
         assertEquals(temp, i.get());
         i.next();
      }
      i.next();
   }

   @Test
   public void testDFS()
   {
      int[] order = {50, 17, 12, 9, 14, 23, 19, 72, 54, 67, 76};
      i = t.mkDFSIterator();
      for(int temp: order)
      {
         assertEquals(temp, i.get());
         i.next();
      }
   }

   @Test
   public void testPre()
   {
      int[] order = {50, 17, 12, 9, 14, 23, 19, 72, 54, 67, 76};
      i = t.mkPreorderIterator();
      for(int temp: order)
      {
         assertEquals(temp, i.get());
         i.next();
      }
   }

   @Test
   public void testIn()
   {
      int[] order = {9, 12, 14, 17, 19, 23, 50, 54, 67, 72, 76};
      i = t.mkInorderIterator();
      for(int temp: order)
      {
         assertEquals(temp, i.get());
         i.next();
      }
   }

   @Test
   public void testPost()
   {
      int[] order = {9, 14, 12, 19, 23, 17, 67, 54, 76, 72, 50};
      i = t.mkPostorderIterator();
      for(int temp: order)
      {
         assertEquals(temp, i.get());
         i.next();
      }
   }

   @Test
   public void testFront()
   {
      int[] order = {9, 14, 19, 67};
      i = t.mkFrontierIterator();
      for(int temp: order)
      {
         assertEquals(temp, i.get());
         i.next();
      }
   }
}
