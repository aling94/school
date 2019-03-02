/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package textanalyzerapp;

import java.util.ArrayList;

/**
 *
 * @author Alvin Ling
 */
public class ArticleConjun
{
    private final ArrayList<String>  artConjun;
    
    public ArticleConjun()
    {
        artConjun = new ArrayList<String>();
        artConjun.add("a");
        artConjun.add("an");
        artConjun.add("the");
        artConjun.add("though");
        artConjun.add("nor");
        artConjun.add("for");
        artConjun.add("but");
        artConjun.add("so");
        artConjun.add("yet");
        artConjun.add("after");
        artConjun.add("although");
        artConjun.add("as");
        artConjun.add("because");
        artConjun.add("before");
        artConjun.add("if");
        artConjun.add("once");
        artConjun.add("since");
        artConjun.add("than");
        artConjun.add("that");
        artConjun.add("though");
        artConjun.add("till");
        artConjun.add("until");
        artConjun.add("when");
        artConjun.add("where");
        artConjun.add("whether");
        artConjun.add("while");
        artConjun.add("both");
        artConjun.add("an");
        artConjun.add("either");
        artConjun.add("neither");
        artConjun.add("whether");
        artConjun.add("also");
    }
    
    public boolean contains(String str)
    {
        return artConjun.contains(str);
    }
    
}
