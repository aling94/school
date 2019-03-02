/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relativeslab;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alvin
 */
public class Tester
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        // TODO code application logic here
        List<String> relations = new ArrayList<String>();
        relations.add("Fred Jim");
        relations.add("Sally Alice");
        relations.add("Jim Tom");
        relations.add("Jim Tammy");
        relations.add("Bob John");
        relations.add("Dot Fred");
        relations.add("Dot Tom");
        relations.add("Dot Chuck");
        relations.add("Bob Tom");
        relations.add("Fred James");
        relations.add("Timmy Amanda");
        relations.add("Almas Brian");
        relations.add("Elton Linh");
        relations.add("Dot Jason");
        Relatives r = new Relatives(relations);
        System.out.println(r);

    }
}
