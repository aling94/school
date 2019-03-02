/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package avl.tree.lab;

/**
 *
 * @author Alvin
 */
public class AVLRunner
{
   public static void main(String[] args)
   {
      AVL<Integer, Integer> a = new AVL<>();
      Integer[] i = {5, 3, 9, 2, 4, 7, 10, 11, 12};
      for(int t = 0; t < i.length; t++)
      {
         a.add(i[t], i[t]);
      }

      Iterator<Integer> it = a.mkBFiterator();
      for(; it.isValid(); it.next()) System.out.println(a.find(it.get()));
      System.out.println("\n");
      it = a.mkBFiterator();
      Integer[] d = {5, 3, 9, 2, 4, 7, 10, 11, 12};
      for(Integer val : i)
      {
         it = a.mkBFiterator();
         for(; it.isValid(); it.next()) System.out.print(a.find(it.get()) + " ");
         a.delete(val);
         System.out.print("\n");
      }
   }
}
