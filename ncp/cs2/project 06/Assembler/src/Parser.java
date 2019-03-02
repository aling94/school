import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import javax.swing.JOptionPane;

/*** Designed by Alvin Ling ***/
public final class Parser
{

   private Scanner scan;
   private String current;
   private char type;
   private int lineNum;
   private Code code;

   private static final String
           A_SYNTAX = "@.+",
           L_SYNTAX = "\\(.+\\)",
           C_SYNTAX = "((M|D|MD|A|AM|AD|AMD)\\={1})?"
           + "(0|1|\\-1|D|A|!D|!A|\\-D|\\-A|D\\+1|A\\+1|D\\-1|A\\-1|D\\+A|D\\-A|A\\-D|D\\&A|D\\|A|M|!M|\\-M|M\\+1|M\\-1|D\\+M|D\\-M|M\\-D|D\\&M|D\\|M){1}"
           + "(;{1}(JGT|JEQ|JGE|JLT|JNE|JLE|JMP))?";

   public Parser(File asm)
   {
      try
      {
         scan = new Scanner(asm);
         code = new Code();
         current = "";
         type = '\0';
         lineNum = 0;
      } catch (FileNotFoundException e)
      {
         e.printStackTrace();
      }
   }

   public boolean hasMoreCommands() { return scan.hasNextLine(); }

   public void advance()
   {
      if (hasMoreCommands())
      {
         current = scan.nextLine();
         lineNum++;
         while (current.isEmpty() || current.matches("\\s*(//).*"))
         {
            current = scan.nextLine();
            lineNum++;
         }
         if (current.matches(".+(//).*")) current = current.substring(0, current.indexOf("//"));
         current = current.replaceAll("\\s", "").toUpperCase();
         if (current.matches(C_SYNTAX)) type = 'C';
         else if (current.matches(A_SYNTAX)) type = 'A';
         else if (current.matches(L_SYNTAX)) type = 'L';
         else
         {
            JOptionPane.showMessageDialog(null, "Please check line: " + lineNum + "\n" + current, "Assembly Syntax Error", JOptionPane.WARNING_MESSAGE);
            System.exit(0);
         }
      }
   }

   public char type() { return type; }

   public String symbol()
   {
      switch (type)
      {
         default: return "";
         case 'A': return current.substring(1);
         case 'L': return current.substring(1, current.length() - 1);
      }
   }

   public String dest()
   {
      return (type == 'C' && current.contains("=")) ?
              current.substring(0, current.indexOf("=")) : "";
   }

   public String comp()
   {
      if (type == 'C')
      {
         int start, end;
         start = (current.contains("=")) ? current.indexOf("=") + 1 : 0;
         end = (current.contains(";")) ? current.indexOf(";") : current.length();
         return current.substring(start, end);
      } else
         return "";
   }

   public String jump()
   {
      return (type == 'C' && current.contains(";")) ?
              current.substring(current.indexOf(";") + 1) : "";
   }

   public String cInstruction() {  return code.translateC(); }

   public String aInstruction(int address) { return code.translateA(address); }

   private class Code
   {
      private final HashMap<String, String> DESTS, COMPS, JUMPS;

      public Code()
      {
         DESTS = new HashMap<String, String>();
         COMPS = new HashMap<String, String>();
         JUMPS = new HashMap<String, String>();
         DESTS.put("M", "001");        DESTS.put("D", "010");        DESTS.put("MD", "011");
         DESTS.put("A", "100");        DESTS.put("AM", "101");       DESTS.put("AD", "110");
         DESTS.put("AMD", "111");
         COMPS.put("0", "0101010"); 	COMPS.put("1", "0111111"); 	COMPS.put("-1", "0111010");
         COMPS.put("D", "0001100"); 	COMPS.put("A", "0110000"); 	COMPS.put("!D", "0001101");
         COMPS.put("!A", "0110001");  	COMPS.put("-D", "0001111"); 	COMPS.put("-A", "0110011");
         COMPS.put("D+1", "0011111"); 	COMPS.put("A+1", "0110111");	COMPS.put("D-1", "0001110");
         COMPS.put("A-1", "0110010"); 	COMPS.put("D+A", "0000010");	COMPS.put("D-A", "0010011");
         COMPS.put("A-D", "0000111");  COMPS.put("D&A", "0000000"); 	COMPS.put("D|A", "0010101");
         COMPS.put("M", "1110000");   	COMPS.put("!M", "1110001");  	COMPS.put("-M", "1110011");
         COMPS.put("M+1", "1110111"); 	COMPS.put("M-1", "1110010"); 	COMPS.put("D+M", "1000010");
         COMPS.put("D-M", "1010011"); 	COMPS.put("M-D", "1000111"); 	COMPS.put("D&M", "1000000");
         COMPS.put("D|M", "1010101");
         JUMPS.put("JGT", "001"); 		JUMPS.put("JEQ", "010"); 	JUMPS.put("JGE", "011");
         JUMPS.put("JLT", "100"); 		JUMPS.put("JNE", "101"); 	JUMPS.put("JLE", "110");
         JUMPS.put("JMP", "111");
      }
      
      public String translateC()
      {
         return (type == 'C') ?
                 "111" + transComp() + transDest() + transJump() : "";
      }

      public String translateA(int address)
      {
         String binaryForm = Integer.toBinaryString(address);
         while (binaryForm.length() < 16) binaryForm = "0" + binaryForm;
         return binaryForm;
      }

      private String transDest() { return (dest().isEmpty()) ? "000" : DESTS.get(dest()); }
      private String transComp() { return COMPS.get(comp()); }
      private String transJump() { return (jump().isEmpty()) ? "000" : JUMPS.get(jump()); }
   }
}
