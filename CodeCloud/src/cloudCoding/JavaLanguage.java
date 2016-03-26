package cloudCoding;

import java.util.Arrays;

import files.UserFile;
import json.ExecutionReturn;

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
	
	@Override
	public CompilerReturn compile(UserFile[] files, String outputFileName)
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
        CompilerReturn compileRet = Compiler.getInstance().compile(files[0], commands);
        
        UserFile[] classFiles = new UserFile[files.length-1];
        String dir = files[0].getPath();
    	int index = dir.indexOf("static/");
    	if(index >= 0) 
    		dir = dir.substring(index + 7);
        for(int i = 1; i <  commands.length; i++)
        {
        	String fileName = commands[i];
        	int dotIndex = fileName.indexOf(".");
        	if(dotIndex > 0)
        		fileName = fileName.substring(0, dotIndex) + ".class";
        	classFiles[i-1] = new UserFile(null, dir +fileName);
        }
        System.out.println("Java Commands: ");
        System.out.println(Arrays.toString(commands));
        System.out.println("Java files:");
        System.out.println(Arrays.toString(files));
        compileRet.returnedFiles = Arrays.asList(classFiles);
        return compileRet;
	}
	
	@Override
	public ExecutionReturn execute(UserFile workingDir, String mainFileName) {
		
		Console console = Console.getInstance();
		UserProcess uProc = console.execute(workingDir.getFile(), "java " + mainFileName);
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
