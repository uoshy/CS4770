package cloudCoding;

import java.util.List;

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
	 * The list of compiled files. 
	 * May be object files or library files depending on compiler options.
	 */
	public List<UserFile> returnedFiles;

	/**
	 * The exit status of the compiler. 0 indicates success and non-zero indicates errors.
	 */
	public int compilerExitStatus;

	
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
				+ "</pre>";
		for(int i = 0; i < returnedFiles.size(); i++)
		{
			toReturn += "<a href=\"" + returnedFiles.get(i).getPath() + "\">File " + i + "</a><br>";
		}
		toReturn += "</div>";
		return toReturn;
	}
	
}
