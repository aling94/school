/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DraftPick;

import java.io.File;

/**
 * @author Alvin
 */
public class ACSLTest
{

    private final static byte a = 16, b = 18;

    public static void main(String[] args)
    {
        File f = new File("SampleInput.dat");
        DraftPick d = new DraftPick(f);
        System.out.println("Sample Test:");
        System.out.println("\t#1: " + d.getRange(a));
        System.out.println("\t#2: " + d.getMidRange(b));
        System.out.println("\t#3: " + d.getHighestExpected(a));
        System.out.println("\t#4: " + d.getAverageExpected(b));
        System.out.println("\t#5: " + d.getMedian());
        f = new File("TestInput.dat");
        d = new DraftPick(f);
        System.out.println("Real Test:");
        System.out.println("\t#1: " + d.getRange(a));
        System.out.println("\t#2: " + d.getMidRange(b));
        System.out.println("\t#3: " + d.getHighestExpected(a));
        System.out.println("\t#4: " + d.getAverageExpected(b));
        System.out.println("\t#5: " + d.getMedian());
        System.exit(0);
    }
}
