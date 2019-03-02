/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Stack;

/**
 *
 * @author Alvin
 */
public class Queue<E>
{
    private List<E> l;
    
    public Queue()
    {
        l = new List<E>();
    }

    public void enqueue(E data)
    {
        l.insertAtEnd(data);
    }

    public E dequeue()
    {
        if(l.size() == 0)
            return null;
        else
            return l.removeFromFront();
    }

    public E front()
    {
        return l.getFirst();
    }

    public int size()
    {
        return l.size();
    }
}
