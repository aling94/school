/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package traversal.lab;

/**
 *
 * @author Alvin
 */
public class Tree
{
   private class Node
   {
      public int data;
      public Node left, right;

      public Node(int data)
      {
         this.data = data;
      }
   }
   private Node root;
   private int size;

   public Tree()
   {
      root = null;
      size = 0;
   }

   public void add(int newData)
   {
      if (size == 0)
      {
         size++;
         root = new Node(newData);
      } else auxAdd(newData, root);
   }

   private void auxAdd(int newData, Node cn)
   {
      if(newData == cn.data) return;
      if (newData < cn.data)
         if (cn.left != null) auxAdd(newData, cn.left);
         else
         {
            size++;
            cn.left = new Node(newData);
         }
      else if (newData > cn.data)
         if (cn.right != null) auxAdd(newData, cn.right);
         else
         {
            size++;
            cn.right = new Node(newData);
         }
   }

   /************ Tree Iterators ************/
   public Iterator mkBFSIterator()        {return new BFSIterator();}
   public Iterator mkDFSIterator()        {return new PreorderIterator();}
   public Iterator mkPreorderIterator()   {return new PreorderIterator();}
   public Iterator mkInorderIterator()    {return new InorderIterator();}
   public Iterator mkPostorderIterator()  {return new PostorderIterator();}
   public Iterator mkFrontierIterator()   {return new FrontierIterator();}

   private abstract class TreeIterator implements Iterator
   {
      protected Queue<Node> q;
      protected boolean valid;

      public int get()
      {
         if (valid) return q.front().data;
         else return 0;
      }

      public abstract void next();

      public boolean isValid() {return valid;}
   }

   private class BFSIterator extends TreeIterator
   {
      public BFSIterator()
      {
         valid = size != 0;
         if (valid)
         {
            q = new Queue<Node>();
            q.enqueue(root);
         }
      }

      public void next()
      {
         if (valid)
         {
            Node cn = q.dequeue();
            if (cn.left != null) q.enqueue(cn.left);
            if (cn.right != null) q.enqueue(cn.right);
            valid = q.size() != 0;
         }
      }
   }

   private abstract class DFS extends TreeIterator
   {
      public DFS()
      {
         valid = root != null;
         if (valid)
         {
            q = new Queue<Node>();
            this.fillQueue(root);
         }
      }

      public void next()
      {
         if (valid)
         {
            q.dequeue();
            valid = q.size() != 0;
         }
      }

      protected abstract void fillQueue(Node cn);
   }

   private class PreorderIterator extends DFS
   {
      protected void fillQueue(Node cn)
      {
         q.enqueue(cn);
         if(cn.left != null) fillQueue(cn.left);
         if(cn.right != null) fillQueue(cn.right);
      }
   }

   private class InorderIterator extends DFS
   {
      protected void fillQueue(Node cn)
      {
         if(cn.left != null) fillQueue(cn.left);
         q.enqueue(cn);
         if(cn.right != null) fillQueue(cn.right);
      }
   }

   private class PostorderIterator extends DFS
   {
      protected void fillQueue(Node cn)
      {
         if(cn.left != null) fillQueue(cn.left);
         if(cn.right != null) fillQueue(cn.right);
         q.enqueue(cn);
      }
   }

   private class FrontierIterator extends DFS
   {
      protected void fillQueue(Node cn)
      {
         if(cn.left == null && cn.right == null) q.enqueue(cn);
         if(cn.left != null) fillQueue(cn.left);
         if(cn.right != null) fillQueue(cn.right);
      }
   }
}


