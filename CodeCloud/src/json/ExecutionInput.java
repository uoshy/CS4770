package json;

/**
 * A JSON object to receive from the client to determine which file to execute. 
 * For example: the class name for java or the executable file name for C/C++.
 * @author Alex Brandt
 *
 */
public class ExecutionInput 
{
	/** The name of the file to execute */
	public String fileName;
	
	/** The path to the working directory from which to execute the file */
	public String workingDirPath;
}
