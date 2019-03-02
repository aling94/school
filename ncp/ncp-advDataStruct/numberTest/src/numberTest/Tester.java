/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package numberTest;

/**
 *
 * @author Alvin
 */
public class Tester
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
{
   final static double[] eskimo = {0.29, 0.0, 0.03, 0.67},
                  bantu = {0.1, 0.08, 0.12, 0.69},
                  english = {0.2, 0.06, 0.06, 0.66},
                  korean = {0.22, 0.0, 0.2, 0.57},
                  eskbantu = {0.195, 0.04, 0.075, 0.68},
                  eskor = {0.255, 0, 0.115, 0.62},
                  bankor = {0.16, 0.04, 0.16, 0.63},
                  eskbankor = {(0.61/3.0), (0.08/3.0), (0.35/3.0), (1.93/3.0)};


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
       double[] other = eskbankor;
       System.out.println(mag(english));
       System.out.println(dp(english,other));
       System.out.println(mag(english)*mag(other));
       System.out.println(angle(english, other));
       System.out.println(angle(english, other)*(180.0/Math.PI));
    }

    public static double dp (double[] a, double[] b)
    {
       double dotProduct = 0.0;
       for(int i = 0; i < a.length; i++)
          dotProduct += a[i] * b[i];
       return dotProduct;
    }

    public static double mag (double[] a)
    {
       double sumOfSquares = 0;
       for(double value: a)
          sumOfSquares += value*value;
       return Math.sqrt(sumOfSquares);
    }

    public static double ratio(double[] a, double[] b)
    {
       double dotProduct = dp(a, b);
       double magnProduct = mag(a) * mag(b);
       return (dotProduct / magnProduct);
    }

    public static double angle(double[] a, double[] b)
    {
       return Math.acos(ratio(a,b));
    }
}