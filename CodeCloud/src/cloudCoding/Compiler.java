package cloudCoding;

import files.UserFile;

import java.util.Arrays;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * A singleton class used to compile files. It executes the input commands needed to compile the
 * files and returns the compiler output as well as references to the compiled files
 * if the compilation was successful.
 * @author Alex Brandt
 *
 */
public class Compiler
{
	/** Singleton reference */
	private static Compiler _compiler;	
	
	/**
	 * Private constructor for singleton.
	 */
	private Compiler()
	{
		
	}
	
	/**
	 * Initialize the Compiler singleton.
	 */
	private static void initialize()
	{
		if(_compiler == null)
		{
			_compiler = new Compiler();
		}
	}
	
	/**
	 * Get a reference to a Compiler object for compiling source code files
	 * @return a reference to a Compiler object
	 */
	public static Compiler getInstance()
	{
		if(_compiler == null)
			initialize();
		
		return _compiler;
	}
	
	/**
	 * Compile the given UserFiles using the specified command and command options.
	 * The implementation of the Language interface calling this method
	 * should be aware of the proper compiler commands. This method simply
	 * executes the specified commands and returns the a CompilerReturn object
	 * which encapsulates the compiler messages and output files.
	 * 
	 * The UserFile represents the working directory from which to compile. The variable list
	 * of command strings should always finish with the files names to compile and
	 * therefore begin with the command and all arguments.
	 * 
	 * Note: The CompilerReturn does have its value of classFiles set when this method returns.
	 * It is the job of the Language implementation to fill in the class files as appropriate.
	 * 
	 * @param file a file representing the working directory
	 * @param commands a variable list of command arguments for execution
	 * @return a CompilerReturn object encapsulating compiler output
	 */
	public CompilerReturn compile(UserFile file, String... commands)
	{
        ProcessBuilder builder = new ProcessBuilder();
        builder.redirectErrorStream(true);
		builder.command(commands);
        System.out.println("Compile Command: " + String.join(" ", builder.command()));
        builder.directory(file.getFile());
        try{
            Process proc = builder.start();
            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(proc.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                output.append(line);
                output.append("\n");
            }
            reader.close();
            try{
            	proc.waitFor();
            	System.out.println(proc.exitValue());
            }
            catch(InterruptedException ie){}
            
            CompilerReturn toReturn = new CompilerReturn();
            String outStr = output.toString();
            if(outStr.length() > 0)
            	toReturn.compilerMessage = output.toString();
            else
            	toReturn.compilerMessage = "Compilation Successful!";
            
            toReturn.compilerExitStatus = proc.exitValue();
            return toReturn;
        }
		catch(NullPointerException npe)
        {
			System.out.println("Null Pointer");
            //one of the strings in commands where null
            return null;
        }
        catch(IndexOutOfBoundsException iobe)
        {
        	System.out.println("Out of bounds");
            //commands had no elements in it
            return null;
        }
        catch(SecurityException se)
		{
        	System.out.println("Security!");
            //Security does not allow subprocesses or read/write access
			return null;
		}
		catch(IOException ioe)
        {
			System.out.println("IOException");
            //error in process I/O
            return null;
        }
	}
}
