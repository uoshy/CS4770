package utility;

/**
 * The HTMLDisplable interface declares a contract such that the implementing object
 * can be converted into a string, which encodes HTML, to describe the state of the object.
 * @author Alex Brandt
 */
public interface HTMLDisplayable 
{
	/**
	 * Convert this object to a String encoding HTML to display the 
	 * current state of this object
	 * @return the string encoding HTML
	 */
	public String displayAsHTML();
}
