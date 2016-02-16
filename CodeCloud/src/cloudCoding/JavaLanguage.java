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
        if(files.length < 2)
        {
            //Not enough files to compile. 
            return null;
        }
        String[] commands = new String[files.length];
        commands[0] = "javac";
        String workingDir = files[0].getPath();
        for(int i = 1; i < commands.length; i++)
        {
        	String path = files[i].getPath();
        	int index = path.indexOf(workingDir);
        	path = path.substring(index+workingDir.length());
        	if(path.indexOf("/") == 0)
        		path = path.substring(1);
            commands[i] = path;
        }
        System.out.println(workingDir);
        for(int i = 0; i < commands.length; i++)
        	System.out.println(commands[i]);
        return Compiler.getInstance().compile(files[0], files.length-1, commands);
	}
	
	@Override
	public long execute(UserFile file) {
		// TODO Auto-generated method stub
		return 0;
	}
}
