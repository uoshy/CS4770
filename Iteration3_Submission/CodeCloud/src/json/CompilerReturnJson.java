package json;

/**
 * An object to excapsulate the returning information from a compilation. To be sent to the client
 * as a JSON object.
 * @author Alex Brandt
 *
 */
public class CompilerReturnJson 
{
	/**
	 * The message of the compiler to display.
	 */
	public String compilerMessageToDisplay;
	
	/**
	 * The process exit status of the compiler.
	 */
	public int compilerExitStatus;
}
