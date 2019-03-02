import java.io.*;
import java.util.*;

/**
 * Encapsulates access to the input code. Reads an VM language command,
 * parses it, and provides convenient access to the command’s components.
 * In addition, removes all white space and comments.
 * @author Alvin Ling
 */
public class Parser
{
	Scanner asmScan;			//Create a reference to the scanner containing the input file
	String currentC;			//Create a reference to a string containing the current command
	
	
	public static void main(String[] args) throws FileNotFoundException
	{
		String fileName = FileChooser.pickAFile();
		File file = new File(fileName);
		Parser p = new Parser(file);
		while(p.hasMoreCommands())
		{
			p.advance();
			System.out.println(p.currentC);
			System.out.println(p.commandType());
			System.out.println(p.arg1());
			System.out.println(p.arg2());
		}
	}

	/**
	 * Constructor
	 * @param file: asm file to be parsed
	 */
	public Parser(File file) throws FileNotFoundException
	{
		asmScan = new Scanner(file);
	}
	
	/**
	 * Returns true if there are more lines to read, else false
	 */
	public boolean hasMoreCommands()
	{
		return asmScan.hasNextLine();
	}
	
	/**
	 * @return the next line of the asm file, with all white space removed
	 */
	public String nextLine()
	{
		return asmScan.nextLine().replaceAll(" ","");
	}
	
	/**
	 * Reads the next command from the input and makes it the current command
	 * Trim all the white space and skips blank and commented lines
	 */
	public void advance()
	{
		if(hasMoreCommands())									//If there are more commands
		{	
			currentC = nextLine();								//Retrieve the line
			int commentIndex = currentC.indexOf("//");			//int containing index of "//"
			while(currentC.isEmpty() || commentIndex == 0)		//Check if starts with "//" or empty
			{
				currentC = nextLine();							//Skip if blank or comment
				commentIndex = currentC.indexOf("//");
			}
			if (commentIndex > 0)								//Index of "//" > 0 = trailing comment
	            {
				//Set the current line to be from the begining till before the comment and trim it
				currentC = currentC.substring(0, commentIndex);
	            }
		}
		else currentC = null;
	}
	
	public String commandType()
	{
		if(currentC.contains("push"))		{return "C_PUSH";}
		if(currentC.contains("pop"))		{return "C_POP";}
		if(currentC.contains("label"))		{return "C_LABEL";}
		if(currentC.contains("goto"))		{return "C_GOTO";}
		if(currentC.contains("if-goto"))	{return "C_IF";}
		if(currentC.contains("function"))	{return "C_FUNCTION";}
		if(currentC.contains("call"))		{return "C_CALL";}
		if(currentC.contains("return"))		{return "C_RETURN";}
		else 								{return "C_ARITHMETIC";}
	}
	
	public String arg1()
	{
		if(commandType().equals("C_ARITHMETIC"))	{return currentC;}
		if(commandType().equals("C_RETURN"))		{return "";}
		if(currentC.contains("argument"))			{return "argument";}
		if(currentC.contains("local"))				{return "local";}
		if(currentC.contains("static"))				{return "static";}
		if(currentC.contains("this"))				{return "this";}
		if(currentC.contains("that"))				{return "that";}
		if(currentC.contains("constant"))			{return "constant";}
		if(currentC.contains("pointer"))			{return "pointer";}
		else										{return "temp";}	
	}
	
	public String arg2()
	{
		boolean a = commandType().equals("C_PUSH") | commandType().equals("C_POP");
		boolean b = commandType().equals("C_FUNCTION") | commandType().equals("C_CALL");
		boolean validC = a | b;
		if(validC)
		{
			int indexArg1 = currentC.indexOf(arg1()) + arg1().length();
			return currentC.substring(indexArg1);
		}
		else return "";
	}
}





