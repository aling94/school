package evilhangman;

import java.io.*;
import java.util.Scanner;

/**
 * This is a utility class for analyzing the dictionary, dividing it, and choosing
 * the files can contain words of n - length.
 */
public class Dictionary
{
   public static final File SOURCE = new File("src\\dictionary\\dictionary_source.dat");     // Constant storing path to source dictionary.

   /**
    * @param length Selected word length you want to play EvilHangman with.
    * @return : returns the file containing words of @param length length or null
    * if the file does not exist.
    */
   public static File get(int length)
   {

      String fileName = String.format("src\\dictionary\\dictionary_length%d.dat", length);
      File dictionary = new File(fileName);
      if (dictionary.exists()) return dictionary;
      else
      {
         System.out.println("Sorry, there are no words of that length.");
         return null;
      }
   }

   /**
    * Counts the frequency of words of each length from 1 to 50.
    */
   public static void countWords() throws FileNotFoundException
   {
      try
      {
         int[] freq = new int[50];
         Scanner s = new Scanner(SOURCE);
         while(s.hasNextLine())
         {
            String word = s.nextLine();
            freq[word.length()]++;
         }
         for(int i = 1; i < freq.length; i++)
            System.out.println(String.format("%d: %d", i, freq[i]));
      } catch (FileNotFoundException e) {System.out.println("Source Dictionary is missing.");}
   }

   /**
    * Reads the source dictionary and divides it into smaller files that contain
    * words only of a particular length.
    */
   public static void diviUp()
   {
      try
      {
         for (int wordLength = 2; wordLength < 30; wordLength++)
         {
            FileOutputStream output = new FileOutputStream(get(wordLength));
            PrintStream outfile = new PrintStream(output);
            Scanner s = new Scanner(SOURCE);
            while (s.hasNextLine())
            {
               String content = s.nextLine();
               if (content.length() == wordLength) outfile.println(content);
            }
         }
      } catch (IOException e) {System.out.println("Source Dictionary is missing.");}
   }
}
