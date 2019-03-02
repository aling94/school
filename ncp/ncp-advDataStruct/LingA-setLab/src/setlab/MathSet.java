/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package setlab;

import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Alvin Ling
 */
public class MathSet
{

    private TreeSet<Integer> one;
    private TreeSet<Integer> two;

    public MathSet()
    {
        one = new TreeSet<Integer>();
        two = new TreeSet<Integer>();
    }

    /**
     * MathSet constructor with parameters
     * @param o Numbers to be loaded into set One 
     *              should be the format # # # # #
     * @param t Numbers to be loaded into set Two
     *              Same format
     */
    public MathSet(String o, String t)
    {
        one = new TreeSet<Integer>();
        two = new TreeSet<Integer>();
        Matcher m1 = Pattern.compile("[^0-9^\\s]").matcher(o),
                m2 = Pattern.compile("[^0-9^\\s]").matcher(t);
        if (m1.find() || o.equals(""))
        {
        } else
        {
            for (String num : o.split("\\s"))
            {
                one.add(Integer.parseInt(num));
            }
        }
        if (m2.find() || t.equals(""))
        {
        } else
        {
            for (String num : t.split("\\s"))
            {
                two.add(Integer.parseInt(num));
            }
        }
    }

    public Set<Integer> union()
    {
        Set<Integer> unionSet = (TreeSet<Integer>)one.clone();
        unionSet.addAll(two);
        return unionSet;
    }

    public Set<Integer> intersection()
    {
        Set<Integer> interSet = (TreeSet<Integer>)one.clone();
        interSet.retainAll(two);
        return interSet;
    }

    public Set<Integer> differenceAMinusB()
    {
        Set<Integer> aMinusBSet = (TreeSet<Integer>)one.clone();
        aMinusBSet.removeAll(two);
        return aMinusBSet;
    }

    public Set<Integer> differenceBMinusA()
    {
        Set<Integer> bMinusASet = (TreeSet<Integer>)two.clone();
        bMinusASet.removeAll(one);
        return bMinusASet;
    }

    public Set<Integer> symmetricDifference()
    {
        Set<Integer> symDiffSet = differenceAMinusB();
        Set<Integer> diffB = differenceBMinusA();
        symDiffSet.addAll(diffB);
        return symDiffSet;
    }

    public String toString()
    {
        return "Set one " + one.toString() + "\n" + "Set two " + two.toString() + "\n";
    }
}
