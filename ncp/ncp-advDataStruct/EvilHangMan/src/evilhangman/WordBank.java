package evilhangman;

import java.io.FileNotFoundException;
import java.util.*;

public class WordBank
{
   private List<Word> wordList;  // List of words available.
   private String currentFam;    // The family (position string) that represents the current list of words.
   private boolean rightGuess;   // Boolean to indicate if the users last guess was correct (true), or wrong (false).
   public final String blank;    // Blank position string for comparison.

   public WordBank(int wordLength)
   {
      wordList = new ArrayList<>();
      blank = new String(new char[wordLength]).replace('\0', '.');
      currentFam = blank;
      try
      {
         Scanner scanner = new Scanner(Dictionary.get(wordLength));
         while (scanner.hasNextLine()) wordList.add(new Word(scanner.nextLine()));
      } catch (FileNotFoundException e)
      {
         System.out.println(String.format("Missing dictionary_length%d.dat", wordLength));
         System.exit(0);
      }
   }

   /**
    * Utility function for resorting the words into buckets based on position of a char.
    * Output is to be passed into WordBank.getLargest family to retrieve the largest family.
    * @param selection : The char the user has selected as his/her guess.
    * @return : A Map containing the families of words. Keys are the position string,
    * values are a List containing each Word that matches that string.
    */
   private Map<String, List<Word>> regroupWords(String selection)
   {
      Map<String, List<Word>> resortedMap = new HashMap<>();
      for (Word w : wordList)
      {
         String key = w.getPositionOf(selection);
         if (!resortedMap.containsKey(key))
         {
            List<Word> newList = new ArrayList<>();
            newList.add(w);
            resortedMap.put(key, newList);
         } else resortedMap.get(key).add(w);
      }
      return resortedMap;
   }

   /**
    * Utility function for analyzing the Map of families and getting the largest word family.
    * The wordList is updated to point to this largest family.
    * It also makes use of the mergeFamily() function to update the currentFam variable.
    * @param m : The Map containing the families grouped by position string.
    */
   private void getLargestFam(Map<String,List<Word>> m)
   {
      Set<String> keySet = m.keySet();
      List<Word> largestFam = new ArrayList<>();
      String largestFamKey = "";
      for (String key : keySet)
      {
         if (m.get(key).size() > largestFam.size())
         {
            largestFamKey = key;
            largestFam = m.get(largestFamKey);
         }
      }
      wordList = largestFam;
      if (!largestFamKey.equals(blank)) mergeFamily(largestFamKey);
   }

   /**
    * Utility function for merging a new position string with the current one.
    * e.g. If the current family of the WordBank is a...., and new family is
    * .pp.., the function will merge the string into app.. and update currentFam.
    * @param family : The new family to be merged with the current.
    */
   private void mergeFamily(String family)
   {
      char[] currentFamArray = currentFam.toCharArray();
      char[] newFamArray = family.toCharArray();
      for (int i = 0; i < currentFamArray.length; i++)
         if (currentFamArray[i] != '.') newFamArray[i] = currentFamArray[i];
      currentFam = new String(newFamArray);
   }

   /**
    * Updates the list of words based on user selection, choosing the largest family
    * and merging the current position string with the newly determined one.
    * Has an additional effect of updating guessCorrect to indicate whether the user guessed
    * correctly.
    * @param selection : User's character guess.
    */
   public void updateFam(String selection)
   {
      String temp = currentFam;
      if (wordList.size() == 1)
      {
         Word lastWord = new Word(wordList.get(0).toString());
         mergeFamily(lastWord.getPositionOf(selection));
      }
      else getLargestFam(regroupWords(selection));
      rightGuess = (temp.equals(currentFam))? false : true;
   }

   /**
    * @return : True if the user's guess was right. To be passed into LetterBank's subtractGuess() function
    * to determine whether to decrement number of guesses remaining.
    */
   public boolean wasRight()
   {
      return rightGuess;
   }

   /**
    * @return : A string message stating whether the user was correct in his guess or not.
    */
   public String message()
   {
      return rightGuess? "You got it right!" : "Too bad. That wasn't right.";
   }

   /**
    * @return The current position string representing the list of words available.
    */
   public String getCurrentFam()
   {
      return currentFam;
   }
}
