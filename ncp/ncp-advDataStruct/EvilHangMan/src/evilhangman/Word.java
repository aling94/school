package evilhangman;

import java.util.HashMap;

/**
 * A string encapsulated in a special class that manages the positions of characters.
 * Character positions are represented by a string in which the characters
 * other than the character in question is replaced by a '.'
 * e.g. the position string of a in the word "yanek" is ".a..."
 */
public class Word
{
   private String word = "";                       // The string this object represents.
   private HashMap<String, String> positionMap;    // HashMap of character positions
   public final String blank;                      // A blank character position string.

   /**
    * @param word : The string that this word will represent.
    */
   public Word(String word)
   {
      this.word = word;
      blank = new String(new char[word.length()]).replace('\0', '.');
      positionMap = new HashMap<>();
      buildPositionMaps:
         for (int position = 0; position < word.length(); position++)
         {
            String key = "" + word.charAt(position);
            String value = word.replaceAll(String.format("[^%s]", key), ".");
            positionMap.put(key, value);
         }
   }

   /**
    * @param letter : The letter you want to see the position of in the word.
    * @return : Position string of @param letter in the word.
    */
   public String getPositionOf(String letter)
   {
      return (positionMap.containsKey(letter)) ? positionMap.get(letter) : blank;
   }

   @Override
   public String toString()
   {
      return word;
   }
}
