/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bst.lab;

/**
 *
 * @author Alvin
 */
public class Queue<E>
{
    private class Node
    {
        public E data;
        public Node next;

        public Node(E data)
        {
            this.data = data;
        }

        public Node(E data, Node next)
        {
            this.data = data;
            this.next = next;
        }
    }
    private int size;
    private Node first, last;

    public Queue()
    {
        size = 0;
        first = null;
        last = null;
    }

    public E front()
    {
        if (first == null)
            return null;
        else return first.data;
    }

    public void enqueue(E data)
    {
        Node tmp = new Node(data);
        size++;
        if (last == null)
        {
            first = tmp;
            last = tmp;
        } else
        {
            last.next = tmp;
            last = tmp;
        }
    }

    public E dequeue()
    {
        E tmp;
        if (first == null)
            return null;
        else
        {
            tmp = first.data;
            first = first.next;
            size--;
            if (first == null)
                last = null;
            return tmp;
        }
    }

    public int size()
    {
        return size;
    }
}
