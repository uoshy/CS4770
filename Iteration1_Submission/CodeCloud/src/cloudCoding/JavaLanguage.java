package cloudCoding;

import files.UserFile;

/**
 * An implementation of the Lanuage interface for the Java language. 
 * @see Language
 * @author Alex Brandt
 *
 */
public class JavaLanguage implements Language 
{
	/** Singleton reference to the JavaLanguage object */
	private static JavaLanguage _java;
	
	/**
	 * Private constructor for singleton.
	 */
	private JavaLanguage()
	{
		
	}
	
	/**
	 * Initialize the static singleton reference.
	 */
	private static void initialize()
	{
		if(_java == null)
			_java = new JavaLanguage();
	}
	
	/**
	 * Get a reference to a JavaLanguage object.
	 * @return a JavaLanguage object
	 */
	public static JavaLanguage getInstance()
	{
		if(_java == null)
			initialize();
		
		return _java;
	}
	
	/**
	 * Compile a list of UserFiles for this language. 
	 * Note that files[0] should represent the directory from which to compile
	 * @param files a list of files to compile given this language.
	 */
	@Override
	public CompilerReturn compile(UserFile[] files)
	{
		return null;
	}
	
	@Override
	public long execute(UserFile file) {
		// TODO Auto-generated method stub
		return 0;
	}
}
