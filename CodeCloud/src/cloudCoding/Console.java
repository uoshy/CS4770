
package cloudCoding;


import java.io.File;
import java.util.Map;
import java.util.HashMap;

/**
 * A singleton object used to execute arbitrary code as requested from the client.
 * This class is responsible for creating and storing UserProcess objects for static access
 * to them from anywhere in the system. 
 * 
 * The class is also responsible for setting up the sandbox in which client code is executed.
 * @author Alex Brandt
 *
 */
public class Console 
{
	/** Singleton reference */
	private static Console _console;

	/** Static unique processes IDs */
	private static long processID = 1l;

	/** A map from proccessID to currently running subprocesses */
	private Map<Long, UserProcess> activeProcesses;
	
	private Map<Long, UserProcess> exitedProcesses;
	
	/** 
	 * The commands to run from /bin/bash to set up the docker container and execute a program.
	 * the command, and its arguments, are to be specified by inserting between part1 and part2. 
	 * Note the format specified in part1. One should insert a string here using String.format that
	 * resembles the server directory to mount to the docker container. 
	 */
	private static final String dockerCommandPart1 = "docker run -i -v %s:/home/user -w /home/user --rm alexgbrandt/sandbox su - user -c \'";
	private static final String dockerCommandPart2 = "\'; exit;";

	/**
	 * Private constructor for singleton.
	 */
	private Console()
	{
		activeProcesses = new HashMap<Long, UserProcess>();
		exitedProcesses = new HashMap<Long, UserProcess>();
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
	 * Gets a process by its unique ID. This will return the process regardless if it 
	 * has already stopped. If no process with such a processID exists, null is returned.
	 * 
	 * @param processID the unique ID of the user process
	 * @return the associated UserProcess, or null if no such process.
	 */
	public UserProcess getProcess(long processID)
	{
		if(activeProcesses.containsKey(processID))
			return activeProcesses.get(processID);
		else if(exitedProcesses.containsKey(processID))
			return exitedProcesses.get(processID);
		else
			return null;
	}
	
	/**
	 * Notify that a process has ended.
	 * @param processID the ID of the ended process.
	 */
	public void processEnded(long processID)
	{
		if(activeProcesses.containsKey(processID))
			exitedProcesses.put(processID, activeProcesses.remove(processID));
	}
	
	
	/**
	 * Execute the given command and command arguments using file as the working directory.
	 * Returns a UserProcess object which encapsulates the execution thread of the process.
	 * The working directory should be the root directory which the requesting user can access.
	 * For example: The user ab1637 should only access /static/users/ab1637/ as that user's "root" 
	 * directory.
	 * 
	 * @param file a file object representing the working directory. 
	 * @param command a string containing the command and arguments to execute
	 * @return a UserProcess encapsulating the execution thread
	 */
	public UserProcess execute(File file, String command)
	{
		ProcessBuilder builder = new ProcessBuilder();
		builder.command("/bin/bash");
		builder.redirectErrorStream(true);
		builder.directory(file);
		String dockerCommand = String.format(dockerCommandPart1, file.getAbsolutePath());
		dockerCommand += " " + command + " " + dockerCommandPart2;
		
		System.out.println("about to run user process");
		UserProcess userProc = new UserProcess(processID++, builder);
		try{
			userProc.start();
			System.out.println("process started");
			userProc.writeToProcess(dockerCommand);
			System.out.println("wrote to process: " + dockerCommand);
			activeProcesses.put(userProc.getProcessID(), userProc);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}

		return userProc;
	}
}
