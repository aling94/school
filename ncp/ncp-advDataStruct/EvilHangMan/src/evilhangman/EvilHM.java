package evilhangman;
import javax.swing.JOptionPane;

public class EvilHM
{
   private LetterBank lb;
   private WordBank wb;

   public static void main(String[] args)
   {
      EvilHM e = new EvilHM();
      e.run();
   }

   /**
    * The Evil Hangman game. The objective of the object is to make it close to impossible to win.
    * See https://docs.google.com/document/d/1tRLCr3mAKU2iQqQ_W-Vd6Srjyr1YcUAF94azlw8V7w0/edit
    * for the full description of the assignment.
    */
   public EvilHM()
   {
      wb = new WordBank(promptLength());
      lb = new LetterBank(promptGuesses());
   }

   /**
    * Utility function for prompting the user for the word-length he/she wishes to play with.
    * @return : The user's selection.
    */
   private int promptLength()
   {
      String wordLength = "";
      int wLength = 0;
      while (!wordLength.matches("(^0*[2-9]$)|(^0*1\\d$)|(^0*2[^3567]$)"))
         wordLength = JOptionPane.showInputDialog("Choose a word-length between 2 and 29\n(inclusive, excluding 23, 25, 26, and 27");
      wLength = Integer.parseInt(wordLength);
      return wLength;
   }

   /**
    * Utility function for prompting the user for the number of guesses he/she wishes to play with.
    * @return : The user's selection.
    */
   private int promptGuesses()
   {
      int nGuesses = 0;
      String prompt = "How many guesses do you want?";
      while (nGuesses <= 0)
      {
         try
         {
            String response = JOptionPane.showInputDialog(prompt);
            nGuesses = Integer.parseInt(response);
         } catch (NumberFormatException e)
         {
            nGuesses = 0;
            prompt = "This is not a valid amount.\nChoose a number greater than 0.";
         }
      }
      return nGuesses;
   }

   /**
    * Utility function for printing the messages pertaining to:
    * Whether the user was right or wrong in his/her guess.
    * The number of guesses he/she has left.
    * His/her current progress towards revealing the word.
    */
   private void displayMessages()
   {
      String rightOrWrong = wb.message();
      String guessesLeft = lb.message();
      System.out.println(String.format("%s %s", rightOrWrong, guessesLeft));
      System.out.println(wb.getCurrentFam().toUpperCase());
   }

   /**
    * Runs the game.
    */
   public void run()
   {
      String endMessage = "You have lost the game.";
      runGame:
         while (lb.isValid())
         {
            String selection = lb.promptLetter();
            wb.updateFam(selection);
            lb.subtractGuess(wb.wasRight());
            displayMessages();
            if (!wb.getCurrentFam().contains("."))
            {
               endMessage = "Congratulations! You have won the game!";
               break runGame;
            }
         }
      System.out.println(endMessage);
   }
}
