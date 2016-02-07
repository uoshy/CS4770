package cloudCoding;

import files.UserFile;

import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

public class Console 
{
	/** Singleton reference */
	private static Console _console;
	
	/** Static unique processes IDs */
	private static long processID = 1l;
	
	/** A map from proccessID to currently running subprocesses */
	private Map<Long, UserProcess> activeProcesses;
	
	/**
	 * Private constructor for singleton.
	 */
	private Console()
	{
		activeProcesses = new HashMap<Long, UserProcess>();
	}
	
	/**
	 * Initialize the Console singleton.
	 */
	private static void initialize()
	{
		if(_console == null)
		{
			_console = new Console();
		}
	}
	
	/**
	 * Get a reference to a Console object for creating UserProcesses
	 * @return a reference to a Console object
	 */
	public static Console getInstance()
	{
		if(_console == null)
			initialize();
		
		return _console;
	}
	
	/**
	 * Execute the given command and command arguments using file as the working directory.
	 * Returns a UserProcess object which encapsulates the execution thread of the process.
	 * 
	 * @param file a file object representing the working directory
	 * @param commands a variable list of command arguments for execution
	 * @return a UserProcess encapsulating the execution thread
	 */
	public UserProcess execute(File file, String...commands)
	{
		ProcessBuilder builder = new ProcessBuilder();
		builder.command(Arrays.asList(commands));
		UserProcess userProc = new UserProcess(builder);
		try{
			userProc.run();
			activeProcesses.put(processID++, userProc);
		}
		catch(Exception e)
		{
			return null;
		}
		
		return userProc;
		
	}
}
