/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DraftPick;

/**
 *
 * @author Alvin
 */
public class NFLContract
{

    public final double e16, e18, salary;
    private final double length, value, guarentee;

    public NFLContract(double[] stats)
    {
        length = stats[0];
        value = stats[1] * 1000000.0;
        guarentee = stats[2] * 1000000.0;
        e16 = calcExpected(0.03);
        e18 = calcExpected(0.03375);
        salary = value / length;
    }

    private double calcExpected(double chance)
    {
        return (1 - length * chance) * value + (length * chance * guarentee);
    }
}
