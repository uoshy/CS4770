package cloudCoding;

import java.util.Collection;

import files.UserFile;
import utility.HTMLDisplayable;

/**
 * An encapsulation of the output of a compiler to be returned to the client.
 * 
 * @author Alex Brandt
 *
 */
public class CompilerReturn implements HTMLDisplayable 
{
	/** 
	 * The output of a compiler stored as a string.
	 */
	public String compilerMessage;
	
	/** 
	 * The collection of compiled files. 
	 * May be object files or library files depending on compiler options.
	 */
	public Collection<UserFile> returnedFiles;
	
	/**
	 * Encode the compiler message as an HTML string to be displayed
	 * on the client.
	 */
	@Override
	public String displayAsHTML()
	{
		String toReturn = "<div class=\"CompilerReturn\">"
				+ "<pre class=\"CompilerReturn\">"
				+ compilerMessage
				+ "</pre></div>";
		return toReturn;
	}
	
}
