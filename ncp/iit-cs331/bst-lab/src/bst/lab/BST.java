/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bst.lab;

/**
 * author Alvin*
 */
public class BST<K extends Comparable, V extends Comparable>
{
   private class Node
   {
      public K key;
      public V value;
      public Node left, right;

      public Node(K key, V value)
      {
         this.left = null;
         this.key = key;
         this.value = value;
         this.right = null;
      }
   }
   private Node root;
   private int size;

   public BST()
   {
      root = null;
      size = 0;
   }

   public void add(K nk, V nv)
   {
      if(nk != null)
         if (size == 0)
         {
            size++;
            root = new Node(nk, nv);
         } else add(nk, nv, root);
   }

   public V find(K target)
   {
      if(target == null) return null;
      else return find(target, root);
   }

   public K revFind(V target)
   {
      if(size == 0) return null;
      else return revFind(target, root);
   }

   public void delete(K target)
   {
      if (root != null && target != null)
         root = delete(target, root);
   }

   public int size()
   {
      return size;
   }

   public Iterator mkBFSIterator()
   {
      return new BFSIterator();
   }

   /***** Recursive Auxiliary Methods *****/
   private void add(K nk, V nv, Node cn)
   {
      if (nk.compareTo(cn.key) < 0)
         if (cn.left != null)
            add(nk, nv, cn.left);
         else
         {
            size++;
            cn.left = new Node(nk, nv);
         }
      else if (nk.compareTo(cn.key) > 0)
         if (cn.right != null)
            add(nk, nv, cn.right);
         else
         {
            size++;
            cn.right = new Node(nk, nv);
         }
      else cn.value = nv;
   }

   private V find(K target, Node cn)
   {
      if (cn == null) return null;
      if (target.compareTo(cn.key) < 0)
         return find(target, cn.left);
      else if(target.compareTo(cn.key) > 0)
         return find(target, cn.right);
      else return cn.value;
   }

   private K revFind(V target, Node cn)
   {
      if (target == cn.value || target.compareTo(cn.value) == 0)
         return cn.key;
      if(cn.left != null && revFind(target, cn.left) != null)
         return revFind(target, cn.left);
      if(cn.right != null && revFind(target, cn.right) != null)
         return revFind(target, cn.right);
      return null;
   }

   private Node delete (K target, Node cn)
   {
      if (cn == null) {}
      else if (target.compareTo(cn.key) < 0)
         cn.left = delete(target, cn.left);
      else if (target.compareTo(cn.key) > 0)
         cn.right = delete(target, cn.right);
      else
      {
         if (cn.left == null || cn.right == null) size--;
         if (cn.left == null && cn.right == null) cn = null;
         else if(cn.right == null) cn = cn.left;
         else if(cn.left == null) cn = cn.right;
         else
         {
            Node replacement = cn.left;
            while(replacement.right != null)
               replacement = replacement.right;
            cn.key = replacement.key;
            cn.value = replacement.value;
            cn.left = delete(replacement.key, cn.left);
         }
      }
      return cn;
   }

   private class BFSIterator implements Iterator
   {
      private Queue<Node> q;
      private boolean valid;

      public BFSIterator()
      {
         q = new Queue<Node>();
         valid = size != 0;
         if (valid) q.enqueue(root);
      }

      public K get()
      {
         if (valid) return q.front().key;
         else return null;
      }

      public void next()
      {
         if(valid)
         {
            Node cn = q.dequeue();
            if(cn.left != null) q.enqueue(cn.left);
            if(cn.right != null) q.enqueue(cn.right);
            valid = q.size() != 0;
         }
      }

      public void delete()
      {
         if (valid)
         {
            BST.this.delete(q.front().key);
            q = new Queue<Node>();
            valid = size != 0;
            if (valid) q.enqueue(root);
         }
      }

      public boolean isValid()
      {
         return valid;
      }
   }
}

