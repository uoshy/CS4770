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
	 * The UserFile represents the working directory from which to compile.
	 * The fileCount is the number of files to compile. The variable list
	 * of command strings should always finish with the files names to compile and
	 * therefore begin with the command and all arguments.
	 * 
	 * @param file a file representing the working directory
	 * @param fileCount the number of files to compile
	 * @param commands a variable list of command arguments for execution
	 * @return a CompilerReturn object encapsulating compiler output
	 */
	public CompilerReturn compile(UserFile file, int fileCount, String... commands)
	{
        ProcessBuilder builder = new ProcessBuilder();
        builder.redirectErrorStream(true);
		builder.command(commands);
        
        builder.directory(file.getFile());
        try{
            Process proc = builder.start();
            System.out.println("Started");
            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(proc.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
            	System.out.println("Read: " + line);
                output.append(line);
                output.append("\n");
            }
            reader.close();
            try{
            	System.out.println("Waiting");
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
            
            UserFile[] classFiles = new UserFile[fileCount];
            String dir = file.getPath();
        	int index = dir.indexOf("static/");
        	if(index >= 0) 
        		dir = dir.substring(index + 7);
            for(int i = 0; i < fileCount; i++)
            {
            	String fileName = commands[commands.length - fileCount + i];
            	int dotIndex = fileName.indexOf(".");
            	if(dotIndex > 0)
            		fileName = fileName.substring(0, dotIndex) + ".class";
            	classFiles[i] = new UserFile(null, dir+"/"+fileName);
            }
            toReturn.returnedFiles = Arrays.asList(classFiles);
            return toReturn;
        }
		catch(NullPointerException npe)
        {
            //one of the strings in commands where null
            return null;
        }
        catch(IndexOutOfBoundsException iobe)
        {
            //commands had no elements in it
            return null;
        }
        catch(SecurityException se)
		{
            //Security does not allow subprocesses or read/write access
			return null;
		}
		catch(IOException ioe)
        {
            //error in process I/O
            return null;
        }
	}
}
