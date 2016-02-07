package cloudCoding;

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
