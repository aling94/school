/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DraftPick;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * @author Alvin
 */
public class DraftPick
{
    private TreeMap<Double, Integer> e16, e18;
    public double[] salary;
    
    public DraftPick(File f)
    {
        loadStats(f);
    }
    
    public int getRange(byte seasonLength)
    {
        double range = salary[9] - salary[0];
        switch (seasonLength)
        {
            default: return 0;
            case 16: return (int) StrictMath.round(range / 16);
            case 18: return (int) StrictMath.round(range / 18);
        }
    }
    
    public int getMidRange(byte seasonLength)
    {
        double sumRange = (salary[0] + salary[9]) / 2;
        switch (seasonLength)
        {
            default: return 0;
            case 16: return (int) StrictMath.round(sumRange / 16);
            case 18: return (int) StrictMath.round(sumRange / 18);
        }
    }
    
    public String getHighestExpected(byte seasonLength)
    {
        switch (seasonLength)
        {
            default: return 0 + "";
            case 16: return (int) StrictMath.round(e16.lastKey()) + " by #" + e16.get(e16.lastKey());
            case 18: return (int) StrictMath.round(e18.lastKey()) + " by #" + e18.get(e18.lastKey());
        }
    }
    
    public int getAverageExpected(byte seasonLength)
    {
        double output = 0.0;
        switch (seasonLength)
        {
            case 16: for(double value : e16.keySet())
                output += value; break;
            case 18: for(double value : e18.keySet())
                output += value; break;
        }
        return (int) StrictMath.round(output / 10);
    }
    
    public int getMedian()
    {
        double output = (salary[4] + salary[5]) / 2;
        return (int) StrictMath.round(output);
    }
    
    public static double[] toDoubArray(String[] strArray)
    {
        double[] output = new double[strArray.length];
        for (int i = 0; i < strArray.length; i++)
            output[i] = Double.parseDouble(strArray[i]);
        return output;
    }
    
    private void loadStats(File f)
    {
        NFLContract na;
        salary = new double[10];
        e16 = new TreeMap<Double, Integer>();
        e18 = new TreeMap<Double, Integer>();   
        try
        {
            Scanner s = new Scanner(f);
            for(int i = 0; i < 10; i++)
            {
                String[] line = s.nextLine().split(",");
                na = new NFLContract(toDoubArray(line));
                e16.put(na.e16, i+1);
                e18.put(na.e18, i+1);
                salary[i] = na.salary;
            }
            Arrays.sort(salary);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }
}
