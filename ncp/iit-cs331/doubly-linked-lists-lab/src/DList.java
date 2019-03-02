/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Alvin
 */
public class DList<E extends Comparable<E>>
{
   private class Node
    {
        public E data;
        public Node next, prev;

        public Node(Node prev, E data, Node next)
        {
            this.prev = prev;
            this.data = data;
            this.next = next;
        }
    }

    private int size = 0;
    private Node sentinel;

    public DList()
    {
        size = 0;
        sentinel = new Node(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
    }

    public void insertFront(E data)
    {
        sentinel.next = new Node(sentinel, data, sentinel.next);
        sentinel.next.next.prev = sentinel.next;
        size++;
    }

    public void insertEnd(E data)
    {
        sentinel.prev = new Node(sentinel.prev,data,sentinel);
        sentinel.prev.prev.next = sentinel.prev;
        size++;
    }

    public void deleteFront()
    {
        if(size != 0)
        {
            sentinel.next = sentinel.next.next;
            sentinel.next.prev = sentinel;
            size--;
        }
    }

    public void deleteEnd()
    {
        if(size != 0)
        {
            sentinel.prev = sentinel.prev.prev;
            sentinel.prev.next = sentinel;
            size --;
        }
    }

    public int size()   {return size;}

    /**** Iterator Initializers ****/
    public Iterator makeFwdIterator()               {return new FwdIterator();}
    public Iterator makeRevIterator()               {return new RevIterator();}
    public Iterator makeFwdFindIterator(E target)   {return new FwdFindIterator(target);}
    public Iterator makeRevFindIterator(E target)   {return new RevFindIterator(target);}

    private abstract class AllIterator implements Iterator<E>
    {
        protected Node cursor;
        protected boolean valid;

        public E get()
        {
            if (valid)
                return cursor.data;
            else return null;
        }

        public void delete()
        {
            if(valid)
            {
                size--;
                cursor.next.prev = cursor.prev;
                cursor.prev.next = cursor.next;
                this.next();
            }
        }

        public boolean isValid()        {return valid;}
        public abstract void next();
    }

    private class FwdIterator extends AllIterator
    {
        public FwdIterator()
        {
            cursor = sentinel.next;
            valid = cursor != sentinel;
        }

        public void next()
        {
            if (valid)
            {
                cursor = cursor.next;
                valid = cursor != sentinel;
            }
        }
    }

    private class RevIterator extends AllIterator
    {
        public RevIterator()
        {
            cursor = sentinel.prev;
            valid = cursor != sentinel;
        }

        public void next()
        {
            if (valid)
            {
                cursor = cursor.prev;
                valid = cursor != sentinel;
            }
        }
    }

    private class FwdFindIterator extends FwdIterator
    {
        private E target;

        public FwdFindIterator(E target)
        {
            cursor = sentinel;
            valid = size != 0;
            this.target = target;
            this.next();
        }

        @Override
        public void next()
        {
            if(target == null)
                find_Null: while(valid)
                {
                    super.next();
                    if (cursor.data == target)
                        break find_Null;
                }
            else
                find_Value: while(valid)
                {
                    super.next();
                    if (cursor.data != null && target.compareTo(cursor.data) == 0)
                        break find_Value;
                }
        }
    }

    private class RevFindIterator extends RevIterator
    {
        private E target;

        public RevFindIterator(E target)
        {
            cursor = sentinel;
            valid = size != 0;
            this.target = target;
            this.next();
        }

        @Override
        public void next()
        {
            if(target == null)
                find_Null: while(valid)
                {
                    super.next();
                    if (cursor.data == target)
                        break find_Null;
                }
            else
                find_Value: while(valid)
                {
                    super.next();
                    if (cursor.data != null && target.compareTo(cursor.data) == 0)
                        break find_Value;
                }
        }
    }
}   /**  End of DList  **/