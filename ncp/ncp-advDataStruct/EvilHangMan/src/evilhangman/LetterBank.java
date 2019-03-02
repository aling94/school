package evilhangman;

import javax.swing.JOptionPane;

/**
 * This class is for handling the character input.
 */
public class LetterBank
{
   private String selection, letters;  // Strings that store the last selected char and remaining letters.
   private String [] letterArray;      // The array that stores the input options for the option pane used to selected chars.
   private int nGuesses;               // Int storing the number of guesses remaining.

   /**
    * @param nGuesses : Number of guesses you want. More than 26 guesses is pointless.
    */
   public LetterBank(int nGuesses)
   {
      selection = "";
      letters = "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z";
      letterArray = letters.split("\\s");
      this.nGuesses = nGuesses;
   }

   /**
    * Utility function for removing the selected char from the remaining char options.
    * @param index : Index in the letter array of the char to be removed.
    */
   private void remove(int index)
   {
      String target = String.format("%s[\\s]*", letterArray[index]);
      selection = letterArray[index];
      letters = letters.replaceFirst(target, "");
      letterArray = letters.split("\\s");
   }

   /**
    * Prompts the user for the next letter they want to guess.
    * @return : The letter that they guessed.
    */
   public String promptLetter()
   {
      if (isValid())
      {
         int response = JOptionPane.showOptionDialog(
              null, "Choose a letter.", "Your guess",
              JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
              null, letterArray, 0);
         remove(response);
         return selection.toLowerCase();
      } else
      {
         System.out.println("You're out of guesses and/or letters");
         return "";
      }
   }

   /**
    * @param right Boolean value denoting whether the guess was right, to be passed by the WordBank's wasRight() function.
    * Decrement the number of remaining guesses the player guessed wrong.
    */
   public void subtractGuess(boolean right)
   {
      if (!right) nGuesses--;
   }

   /**
    * @return : A message stating how many guesses are left.
    */
   public String message()
   {
      return String.format("You have %d guesses left.", nGuesses);
   }

   /**
    * @return : True if there are still letters left and more than zero guesses left,
    * else false.
    */
   public boolean isValid()
   {
      return letters.length() > 0 && nGuesses > 0;
   }
}
