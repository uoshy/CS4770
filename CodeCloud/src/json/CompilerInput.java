package json;

/**
 * The input received from the client to be used as input to the Compiler. 
 * This is a JSON object transformed into Java.
 * 
 * @author Alex Brandt
 *
 */
public class CompilerInput {
	
	/**
	 * The String containing the working directory.
	 */
	public String workingDir;
	
	/**
	 * The String containing the file name.
	 */
	public String fileName;
}
