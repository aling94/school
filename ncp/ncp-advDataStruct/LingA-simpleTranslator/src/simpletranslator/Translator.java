/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simpletranslator;

/**
 *
 * @author Alvin Ling
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;



public class Translator
{
    private Map<String, String> dictionary, pairs;

    public Translator()
    {
        dictionary = new HashMap<String, String>();
        File f = new File("SpanishEnglish.dat");
        try
        {
        Scanner scan = new Scanner(f);
        while(scan.hasNext())
        {
            dictionary.put(scan.next(), scan.next());
        }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public String translate(String sent)
    {
        pairs = new HashMap<String, String>();
        String output = "";
        String[] list = sent.split(" ");
        for(String word: list)
        {
            String translation = dictionary.get(word);
            pairs.put(word, translation);
            output += translation + " ";
        }
        return output.trim();
    }

    public String toString()
    {
        return pairs.toString();
    }
}
