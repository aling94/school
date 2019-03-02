import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import javax.swing.JOptionPane;

/*** Designed by Alvin Ling ***/
public final class Assembler
{

   private static Parser parser;
   private static SymbolTable table;

   public static void assemble(File asm) throws FileNotFoundException
   {
      parser = new Parser(asm);
      table = new SymbolTable(asm);
      String fileName = asm.getName().replace(".asm", ".hack");
      FileOutputStream output = new FileOutputStream(fileName);
      PrintStream hackFile = new PrintStream(output);
      while (parser.hasMoreCommands())
      {
         parser.advance();
         assemble: switch (parser.type())
         {
            case 'C':
            {
               String cInstruction = parser.cInstruction();
               hackFile.println(cInstruction);
               break assemble;
            }
            case 'A':
            {
               Integer address = table.getAddress(parser.symbol());
               String aInstruction = parser.aInstruction(address);
               hackFile.println(aInstruction);
               break assemble;
            }
            case 'L': break assemble;
         }
      }
   }

   public static void main(String args[]) throws FileNotFoundException
   {
      String directory = "C:\\Users\\Alvin\\Dropbox\\CS II - Nguyen\\tecs-software-suite-2.5\\project 06/";
      FileChooser.setMediaPath(directory);
      String fileName = FileChooser.pickAFile();
      if (fileName.endsWith(".asm"))
      {
         File file = new File(fileName);
         assemble(file);
         System.exit(0);
      } else
      {
         String fileExt = fileName.substring(fileName.indexOf("."));
         String warning = "Thought you could screw with my assembler with " + fileExt
                 + " files, eh? Pick an .asm file!";
         JOptionPane.showMessageDialog(null, warning, "Error", JOptionPane.WARNING_MESSAGE);
         System.exit(0);
      }
   }
}
