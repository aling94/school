/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package setlab;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *
 * @author ??????
 */
public class Tester {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try
        {
            Scanner file = new Scanner(new File("numSets.dat"));
            while(file.hasNext())
            {
                    String one = file.nextLine();
                    String two = file.nextLine();
                    MathSet mSet = new MathSet(one,two);
                    System.out.println(mSet);
                    System.out.println("union - " + mSet.union());
                    System.out.println("intersection - " + mSet.intersection());
                    System.out.println("difference A-B - " + mSet.differenceAMinusB());	
                    System.out.println("difference B-A - " + mSet.differenceBMinusA());				
                    System.out.println("symmetric difference " + mSet.symmetricDifference()+"\n\n");
            }
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        
        
        String s = "1 2 3 4 5";
        String [] str = s.split(" ");
        for(String entry: str)
        {
           
        }
    }
}
