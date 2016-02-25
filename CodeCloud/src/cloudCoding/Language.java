package cloudCoding;

import files.UserFile;
import json.ExecutionReturn;

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
	 * in the represented by mainFileName and should exist in the directory represented by 
	 * workingDir. It is assumed that the file to be executed has already be compiled. 
	 * 
	 * @param workingDir the file representing the working directory.
	 * @param mainFileName the name of the file to execute
	 * @return an object encapsulating the encapsulated process's status
	 * @see ExecutionReturn
	 */
	public ExecutionReturn execute(UserFile workingDir, String mainFileName);
}
