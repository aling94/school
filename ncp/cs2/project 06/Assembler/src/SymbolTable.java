import java.io.File;
import java.util.HashMap;

/*** Designed by Alvin Ling ***/
public final class SymbolTable
{

   private Parser parser;
   private int lineCount, symAddress;
   private HashMap<String, Integer> table;

   public SymbolTable(File asm)
   {
      table = new HashMap<String, Integer>();
      table.put("R0", 0); 	 table.put("SP", 0); 		  table.put("LCL", 1);
      table.put("R1", 1); 	 table.put("R2", 2); 		  table.put("ARG", 2);
      table.put("THIS", 3); table.put("R3", 3); 		  table.put("THAT", 4);
      table.put("R4", 4); 	 table.put("R5", 5); 		  table.put("R6", 6);
      table.put("R7", 7); 	 table.put("R8", 8); 		  table.put("R8", 9);
	   table.put("R9", 9); 	 table.put("R10", 10); 		  table.put("R11", 11);
	   table.put("R12", 12); table.put("R13", 13); 		  table.put("R14", 14);
	   table.put("R15", 15); table.put("SCREEN", 16384); table.put("KBD", 24576);
      lineCount = -1;
      symAddress = 16;
      addLabelsSymbols(asm);
   }

   public int getAddress(String symbol)
   {
      return (!symbol.matches("\\d+") && table.containsKey(symbol))
              ? table.get(symbol) : Integer.parseInt(symbol);
   }

   private void addLabelsSymbols(File asm)
   {
      parser = new Parser(asm);
      addLabels: while (parser.hasMoreCommands())
      {
         parser.advance();
         lineCount++;
         if (parser.type() == 'L')
         {
            String label = parser.symbol();
            table.put(label, lineCount);
            lineCount--;
         }
      }
      parser = new Parser(asm);
      addSymbols: while (parser.hasMoreCommands())
      {
         parser.advance();
         if (parser.type() == 'A')
         {
            String symbol = parser.symbol();
            if (!symbol.matches("\\d+") && !table.containsKey(symbol))
            {
               table.put(symbol, symAddress);
               symAddress++;
            }
         }
      }
   }
}
