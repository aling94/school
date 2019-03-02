/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package textanalyzerapp;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author dgyanek
 */
public class Tester
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        String yourRegexHere = "You can also type your regex directly into the Pattern argument";
        String targetString = "carcarcarcarcar";
        Pattern p = Pattern.compile("car");
        Matcher m = p.matcher(targetString);
        for(int i = 0; i <= 10; i++)
        {
            System.out.println(m.find());
        }

    }
}
