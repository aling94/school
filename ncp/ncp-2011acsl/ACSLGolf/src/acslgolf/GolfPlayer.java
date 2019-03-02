/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package acslgolf;

/**
 *
 * @author Alvin Ling
 */
public class GolfPlayer
{
    private int[] parScore;             //Scores for each hole from in order from 1-9
    private int cumulativeScore = 0;    //Sum of the scores from all 9 holes
    private String coursePar = "";      //String representation of score relative to normal par
    
    public GolfPlayer(int[] scoreArray, int normalPar)
    {
        parScore = scoreArray;
        for(int score: scoreArray)
        {
            cumulativeScore += score;
        }
        int par = cumulativeScore - normalPar;
        if(par < 0)
        {
            coursePar = Math.abs(par) + " under par";
        } else if (par > 0)
        {
            coursePar = Math.abs(par) + " above par";
        } else 
        {
            coursePar = "par";
        }
    }
    
    /**
     * Returns an int[] of the scores in order from holes 1-9
     */
    public int[] getParScore()
    {
        return parScore;
    }
    
    /**
     * Returns sum of scores from all 9 holes
     */
    public int getCumulative()
    {
        return cumulativeScore;
    }
    
    public String getCoursePar()
    {
        return coursePar;
    }
    
}
