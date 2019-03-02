/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package numberTest;

/**
 *
 * @author Alvin
 */
public class Cat
{
    //Data fields (instance fields)
    private String color;
    private String gender;
    private double weight;
    
    //Constructors
    //Default - allow us to create a generic object
    public Cat()
    {
        color = "rainbow";
        gender = "potok";
        weight = 3.14;
    }
    
    public Cat(String c, String g, double w)
    {
        color = c;
        gender = g;
        weight = w;
    }
    
    public String toString()
    {
        String output = color + " " + gender + " " + weight;
        return output;
    }
    
    public String getColor()
    {
        return color;
    }
    
    public String getGender()
    {
        return gender;
    }
    
    public double getWeight()
    {
        return weight;
    }
    
    public void setColor(String c)
    {
        color = c;
    }
    
    public void setGender(String g)
    {
        gender = g;
    }
    
    public void setWeight(double w)
    {
        weight = w;
    }
}