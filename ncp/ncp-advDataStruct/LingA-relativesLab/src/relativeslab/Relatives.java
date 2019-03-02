/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relativeslab;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author Alvin
 */
public class Relatives
{

    private Map<String, Set<String>> relations;

    public Relatives()
    {
        relations = new HashMap<String, Set<String>>();
    }

    public Relatives(List<String> list)
    {
        relations = new HashMap<String, Set<String>>();
        for (String entry : list)
        {
            setPersonRelative(entry);
        }
    }

    public final void setPersonRelative(String str)
    {
        String[] people = str.split(" ");
        String person = people[0];
        String relative = people[1];
        Set<String> relatives;
        if (relations.containsKey(person))
        {
            relatives = relations.get(person);
        } else
        {
            relatives = new TreeSet<String>();
        }
        relatives.add(relative);
        relations.put(person, relatives);
    }

    public String getRelatives(String person)
    {
        String relatives = relations.get(person).toString();
        String output = person + " is related to " + relatives;
        return output.replaceAll("[\\[\\]]", "");
    }

    public String toString()
    {
        Set<String> names = relations.keySet();
        String output = "";
        for (String person : names)
        {
            output += getRelatives(person) + "\n";
        }
        return output.replaceAll("[\\[\\]]", "");
    }
}
