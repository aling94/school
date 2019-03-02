/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package emailvalidator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Alvin Ling
 */
public class Tester
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        String s = ",";
        System.out.println(s.matches("^(,|\\s|[0-9|])"));

    }
}
