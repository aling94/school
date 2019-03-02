package numberTest;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Phil
 */
public class Function
{
    public static int f(int x, int y)
    {
        if(x > y)
        {
            return max(f(x-4, y+3), f(y,x));
        } else return x * y;
    }
    
    public static int max(int x, int y)
    {
        if(x > y)
        {
            return x;
        } else return y;
    }
}
