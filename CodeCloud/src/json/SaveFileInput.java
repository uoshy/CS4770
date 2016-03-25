package json;

/**
 * The input received from the client to be used to save a file.
 * This is a JSON object transformed into Java.
 * 
 * @author Alex Brandt
 *
 */
public class SaveFileInput {
	
	/**
	 * The String containing the content of the file
	 */
	public String fileContent;
	
	/**
	 * The String containing the file name.
	 */
	public String fileName;
}
