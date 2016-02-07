package cloudCoding;

import files.UserFile;

/**
 * An abstraction of a programming language for compiling and executing file 
 * and programs of the language type.
 * 
 * Implementing languages should internally specify the required compiler and execution
 * commands. 
 * 
 * @author Alex Brandt
 *
 */
public interface Language
{
	/**
	 * Compile the list of UserFiles for the specified language. The first file in the list should
	 * represent the working directory from which to compile.
	 * @param files the list of files to compile
	 * @return a CompilerReturn object encapsulating the compiler message
	 */
	public CompilerReturn compile(UserFile[] files);
	
	/**
	 * Execute the file containing main method for the program. The main method should be contained
	 * in file.
	 * @param file the file containing the main method
	 * @return the processID associated with the execution instance created
	 */
	public long execute(UserFile file);
}
