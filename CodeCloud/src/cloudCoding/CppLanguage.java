package cloudCoding;

import files.UserFile;
import json.ExecutionReturn;

/**
 * An implementation of the Lanuage interface for the C++ language. 
 * @see Language
 * @author Alex Brandt
 *
 */
public class CPPLanguage implements Language 
{
	/** Singleton reference to the CPPLanguage object */
	private static CPPLanguage _cpp;
	
	/**
	 * Private constructor for singleton.
	 */
	private CPPLanguage()
	{
		
	}
	
	/**
	 * Initialize the static singleton reference.
	 */
	private static void initialize()
	{
		if( _cpp == null)
			_cpp = new CPPLanguage();
	}
	
	/**
	 * Get a reference to a CPPLanguage object.
	 * @return a CPPLanguage object
	 */
	public static CPPLanguage getInstance()
	{
		if(_cpp == null)
			initialize();
		
		return _cpp;
	}
	
	/**
	 * Compile a list of UserFiles for this language. 
	 * Note that files[0] should represent the directory from which to compile
	 * @param files a list of files to compile given this language.
	 */
	@Override
	public CompilerReturn compile(UserFile[] files, String outputFileName)
	{
        if(files.length < 2)
        {
            //Not enough files to compile. 
            return null;
        }
        String[] commands = new String[files.length + 3];
        commands[0] = "g++";
        commands[1] = "-Wall";
        String workingDir = files[0].getPath();
        for(int i = 2; i < commands.length-2 ; i++)
        {
        	String path = files[i].getPath();
        	int index = path.indexOf(workingDir);
        	path = path.substring(index+workingDir.length());
        	if(path.indexOf("/") == 0)
        		path = path.substring(1);
            commands[i] = path;
        }
        commands[commands.length-2] = "-o ";
        commands[commands.length-1] = outputFileName;
        System.out.println(workingDir);
        for(int i = 0; i < commands.length; i++)
        	System.out.println(commands[i]);
        return Compiler.getInstance().compile(files[0], files.length-1, commands);
	}
	
	@Override
	public ExecutionReturn execute(UserFile workingDir, String mainFileName) {
		
		Console console = Console.getInstance();
		UserProcess uProc = console.execute(workingDir.getFile(), "./" + mainFileName);
		System.out.println("got user process");
		//TODO support package-declared class names
		ExecutionReturn execRet = new ExecutionReturn();
		
		String processOutput = uProc.readFromProcess();
		System.out.println("read from process!: " + processOutput);
		execRet.outputText = processOutput;
		execRet.exitStatus = uProc.getExitStatus();
		execRet.processID = uProc.getProcessID();
	
		return execRet;
	}
}
