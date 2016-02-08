package cloudCoding;

import files.UserFile;

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
	 * The first UserFile in files should be a file pointer to the working directory
	 * to compile from. The remaining files should be the files to compile.
	 * 
	 * @param files an array of files representing the working directory and files to compile
	 * @param commands a variable list of command arguments for execution
	 * @return a CompilerReturn object encapsulating compiler output
	 */
	public CompilerReturn compile(UserFile[] files, String... commands)
	{
		return null;
		//TODO actual compiling
	}
}
