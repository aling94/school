/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package acslgolf;

import java.util.Arrays;
import java.util.TreeMap;
import javax.swing.JOptionPane;

/**
 *
 * @author Alvin Ling
 */
public class ASCLGolf
{
    private GolfPlayer a, b, c, d;
    private String compiledScores = "";
    private int[] allScores;
    private TreeMap<Integer, Integer> cumulativeHoles;
    private static final Object[] players = { 'A', 'B', 'C', 'D' };
    private final int normalPar;
    
    public static void main(String[] args)
    {
        ASCLGolf a = new ASCLGolf(36);
        System.out.println(a.getPar());
        System.out.println(a.getPar());
        System.out.println(a.topCumulativeHoles());
        System.out.println(a.getRanks());
        System.out.println(a.getMedian());
        System.exit(0);
    }
       
    public ASCLGolf(int normal)
    {
        normalPar = normal; 
        loadScores();
        buildMaps();
    }

    public String getPar()
    {
        Character player = (Character) JOptionPane.showInputDialog(null, "Select a player:",
                "Get the par of ...", JOptionPane.QUESTION_MESSAGE, null, players, players[0]);
        switch(player)
        {
            default :   {return a.getCoursePar();}
            case 'B':   {return b.getCoursePar();}
            case 'C':   {return c.getCoursePar();}
            case 'D':   {return d.getCoursePar();}
        }
    }
    
    public int topCumulativeHoles()
    {
        return cumulativeHoles.get(cumulativeHoles.firstKey());
    }
    
    public String getRanks()
    {
        String output = "";
        TreeMap<Integer,String> ranks = new TreeMap<Integer,String>();
        ranks.put(a.getCumulative(), "A"); ranks.put(b.getCumulative(), "B");
        ranks.put(c.getCumulative(), "C"); ranks.put(d.getCumulative(), "D");
        for(int i: ranks.keySet())
        {
            output += ranks.get(i) + ',';
        }
        return output.substring(0, output.length()-1);
    }
    
    public int getMedian()
    {
        return (allScores[17] + allScores[18]) / 2;
    }
    
    private int[] strArrayToIntArray(String[] strArray)
    {
        int[] output = new int[strArray.length];
        for (int i = 0; i < strArray.length; i++)
        {
            output[i] = Integer.parseInt(strArray[i]);
        }
        return output;
    }
    
    private int[] getScores(char player)
    {
        String promptPlayer = "Enter " + player + "'s scores";
        String tryAgain = player + ": Please input integers in the format of #,#,#,# ... (9 digits)";
        String score = JOptionPane.showInputDialog(null, promptPlayer).replaceAll(" ", "");
        while (!score.matches("(\\d,){8}\\d"))
        {
            score = JOptionPane.showInputDialog(null, tryAgain).replaceAll(" ", "");
        }
        compiledScores += score + ',';
        return strArrayToIntArray(score.split(","));
    }
    
    private void loadScores()
    {
        a = new GolfPlayer(getScores('A'), normalPar);
        b = new GolfPlayer(getScores('B'), normalPar);
        c = new GolfPlayer(getScores('C'), normalPar);
        d = new GolfPlayer(getScores('D'), normalPar);
        allScores = strArrayToIntArray(compiledScores.split(","));
        Arrays.sort(allScores);
    }
    
    private void buildMaps()
    {
        cumulativeHoles = new TreeMap<Integer, Integer>();
        int ac = a.getCumulative(); int bc = b.getCumulative();
        int cc = c.getCumulative(); int dc = d.getCumulative();
        cumulativeHoles.put(ac, 0); cumulativeHoles.put(bc, 0);
        cumulativeHoles.put(cc, 0); cumulativeHoles.put(dc, 0);
        int[] ar = a.getParScore(); int[] br = b.getParScore();
        int[] cr = c.getParScore(); int[] dr = d.getParScore();
        for(int i = 0; i < 9; i++)
        {
            TreeMap<Integer, Character> t = new TreeMap<Integer, Character>();
            t.put(ar[i], 'A');
            t.put(br[i], 'B');
            t.put(cr[i], 'C');
            t.put(dr[i], 'D');
            switch(t.get(t.firstKey()))
            {
                case 'A': cumulativeHoles.put(ac, cumulativeHoles.get(ac) + 1); break;
                case 'B': cumulativeHoles.put(bc, cumulativeHoles.get(bc) + 1); break;
                case 'C': cumulativeHoles.put(cc, cumulativeHoles.get(cc) + 1); break;
                case 'D': cumulativeHoles.put(dc, cumulativeHoles.get(dc) + 1); break;
            }
        }
    }
}
