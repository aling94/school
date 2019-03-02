/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Stack;

/**
 * by Alvin Ling
 */
public class Stack<E>
{
    private List<E> l;

    public Stack()
    {
        l = new List<E>();
    }

    public void push(E data)
    {
        l.insertAtFront(data);
    }

    public E pop()
    {
        if (l.size() == 0)
            return null;
        else
            return l.removeFromFront();
    }

    public E top()
    {
        return l.getFirst();
    }

    public int size()
    {
        return l.size();
    }
}
