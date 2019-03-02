import java.io.*;

public class CodeWriter
{
	Parser p;
	FileOutputStream out;
	PrintStream VM_file;
	int sp;
	int stack[];
	int y;

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException
	{
		String fileName = FileChooser.pickAFile();
		File file = new File(fileName);
		CodeWriter c = new CodeWriter(file);
		while(c.p.hasMoreCommands())
		{
			c.p.advance();
			if(c.p.commandType().equals("C_ARITHMETIC"))
			{
				c.writeArithmetic();
			}
			if(c.p.commandType().equals("C_PUSH"))
			{
				c.writePushPop();
			}
		}

	}
	
	public CodeWriter(File f) throws FileNotFoundException
	{
		p = new Parser(f);
		String fileName = f.getName().replace(".vm", ".asm");	
		out = new FileOutputStream(fileName);
		VM_file = new PrintStream(out);
		sp = 255;
		stack = new int[2048];
	}
	
	public void writeArithmetic()
	{
		if(p.commandType().equals("C_ARITHMETIC"))
		{
			if(p.arg1().equals("add")) 
			{
				y = stack[sp];
				sp--;
				stack[sp] = stack[sp] + y;
				VM_file.println("@" + sp);
				VM_file.println("M=D+M");
			}
			if(p.arg1().equals("sub")) 
			{
				y = stack[sp];
				sp--;
				stack[sp] = stack[sp] - y;
				VM_file.println("@" + sp);
				VM_file.println("M=M-D");
			}
			if(p.arg1().equals("neg")) 
			{
				stack[sp] = -stack[sp];
				VM_file.println("@" + sp);
				VM_file.println("M=-M");
				VM_file.println("D=M");
			}
			if(p.arg1().equals("eq")) 
			{
				y = stack[sp];
				sp--;
				boolean result = stack[sp] == y;
				if(result)
				{
					stack[sp] = -1;
					VM_file.println("@" + sp);
					VM_file.println("M=-1");
				}
				else
				{
					stack[sp] = 0;
					VM_file.println("@" + sp);
					VM_file.println("M=0");
				}
			}
			if(p.arg1().equals("gt")) 
			{
				y = stack[sp];
				sp--;
				boolean result = stack[sp] > y;
				if(result)
				{
					stack[sp] = -1;
					VM_file.println("@" + sp);
					VM_file.println("M=-1");
				}
				else
				{
					stack[sp] = 0;
					VM_file.println("@" + sp);
					VM_file.println("M=0");
				}
			}
			if(p.arg1().equals("lt")) 
			{
				y = stack[sp];
				sp--;
				boolean result = stack[sp] < y;
				if(result)
				{
					stack[sp] = -1;
					VM_file.println("@" + sp);
					VM_file.println("M=-1");
				}
				else
				{
					stack[sp] = 0;
					VM_file.println("@" + sp);
					VM_file.println("M=0");
				}
			}
			if(p.arg1().equals("and")) 
			{
				y = stack[sp];
				sp--;
				stack[sp] = stack[sp] & y;
				VM_file.println("@" + sp);
				VM_file.println("M=D&M");
			}
			if(p.arg1().equals("or")) 
			{
				y = stack[sp];
				sp--;
				stack[sp] = stack[sp] | y;
				VM_file.println("@" + sp);
				VM_file.println("M=D|M");
			}
			if(p.arg1().equals("not")) 
			{
				stack[sp] = ~stack[sp];
				VM_file.println("@" + sp);
				VM_file.println("M=!M");
			}
		}
	}


	
	public void writePushPop()
	{
		if(p.commandType().equals("C_PUSH") & p.arg1().equals("constant"))
		{
			sp++;
			stack[sp] = Integer.parseInt(p.arg2());
			VM_file.println("@" + p.arg2());
			VM_file.println("D=A");
			VM_file.println("@" + sp);
			VM_file.println("M=D");
		}
	}

}
