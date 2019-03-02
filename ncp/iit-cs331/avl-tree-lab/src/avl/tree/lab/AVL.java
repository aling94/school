
package avl.tree.lab;

public class AVL<K extends Comparable, V extends Comparable>
{
   private class Node
   {
      public K key;
      public V value;
      public Node parent, left, right;
      public int height, balance;

      public Node(K key, V value, Node parent)
      {
         this.parent = parent;   this.left = null;    this.right = null;
         this.key = key;         this.value = value;
         height = 1;             balance = 0;
      }

      public void updateHeightBalance()
      {
         int leftHeight = (this.left != null) ? this.left.height : 0;
         int rightHeight = (this.right != null) ? this.right.height : 0;
         height = Math.max(leftHeight, rightHeight) + 1;
         balance = leftHeight - rightHeight;
      }
   }
   private Node root;
   private int size;

   public AVL()
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
            root = new Node(nk, nv, null);
         } else
         {
            add(nk, nv, root);
            rotate(root);
         }
   }

   public void delete(K target)
   {
      if (root != null && target != null)
      {
         root = delete(target, root);
         rotate(root);
      }
   }

   public V find(K target)
   { return (target == null) ? null : find(target, root); }

   public int size()
   { return size; }

   public Iterator<V> mkBFiterator()
   { return new BFiterator(); }

   public Iterator<V> mkDFiterator()
   { return new DFiterator(); }

   /******** Auxiliary Methods ********/
   private void add(K nk, V nv, Node cn)
   {
      if (nk.compareTo(cn.key) < 0)
      {
         if (cn.left != null) add(nk, nv, cn.left);
         else { cn.left = new Node(nk, nv, cn); size++; }
      }
      else if (nk.compareTo(cn.key) > 0)
      {
         if (cn.right != null) add(nk, nv, cn.right);
         else { cn.right = new Node(nk, nv, cn); size++; }
      }
      else cn.value = nv;
      cn.updateHeightBalance();
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
            Node predecessor = cn.left;
            while(predecessor.right != null)
               predecessor = predecessor.right;
            cn.key = predecessor.key;
            cn.value = predecessor.value;
            cn.left = delete(predecessor.key, cn.left);
         }
      }
      if(cn != null) cn.updateHeightBalance();
      return cn;
   }

   /****** Rotational Methods ******/
   private void rotate(Node cn)
   {
      if(cn == null) return;
      if (cn.balance > 1)
         if (cn.left.balance >= 0) rightRotation(cn);
         else doubleRight(cn);
      else if (cn.balance < -1)
         if (cn.right.balance <= 0) leftRotation(cn);
         else doubleLeft(cn);
   }

   private void leftRotation(Node target)
   {
      Node promoted = target.right;
      Node traitor = promoted.left;
      target.right = traitor;
      if(traitor != null) traitor.parent = target;
      promoted.left = target;
      promoted.parent = target.parent;
      if(promoted.parent != null) promoted.parent.left = promoted;
      else root = promoted;
      target.parent = promoted;
      target.updateHeightBalance();
      promoted.updateHeightBalance();
   }

   private void rightRotation(Node target)
   {
      Node promoted = target.left;
      Node traitor = promoted.right;
      target.left = traitor;
      if(traitor != null) traitor.parent = target;
      promoted.right = target;
      promoted.parent = target.parent;
      if(promoted.parent != null) promoted.parent.right = promoted;
      else root = promoted;
      target.parent = promoted;
      target.updateHeightBalance();
      promoted.updateHeightBalance();
   }

   private void doubleLeft(Node target)
   {
      rightRotation(target.right);
      leftRotation(target);
   }

   private void doubleRight(Node target)
   {
      leftRotation(target.left);
      rightRotation(target);
   }

   private abstract class AVLiterator implements Iterator<V>
   {
      protected Queue<Node> q;
      protected boolean valid;

      public V get()
      { return (valid) ? q.front().value : null; }

      abstract public void next();

      public boolean isValid()
      { return valid; }
   }

   private class BFiterator extends AVLiterator
   {
      public BFiterator()
      {
         valid = root != null;
         if (valid)
         {
            q = new Queue<Node>();
            q.enqueue(root);
         }
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
   }

   private class DFiterator extends AVLiterator
   {
      public DFiterator()
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

      private void fillQueue(Node cn)
      {
         q.enqueue(cn);
         if(cn.left != null) fillQueue(cn.left);
         if(cn.right != null) fillQueue(cn.right);
      }
   }
}
