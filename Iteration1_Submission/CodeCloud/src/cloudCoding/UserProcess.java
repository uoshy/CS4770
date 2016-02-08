package cloudCoding;

import java.io.*;

/**
 * An abstraction of a running process on the server. A UserProcess encapsulates the 
 * operating system's process, the process's I/O, as well as a separate Java thread 
 * containing a reference to the process.
 * 
 * The UserProcess is created when a client requests that the server executes a program
 * and maintains a continuous reference to the process so the client can make discrete
 * requests to the server to be directed to this process.
 * 
 * @author Alex Brandt
 *
 */
public class UserProcess extends Thread
{
	/**
	 * The underlying process of this UserProcess
	 */
	public Process process;
	
	/**
	 * The input stream to the process, out of this java thread.
	 */
	public OutputStream input;
	
	/**
	 * The output stream of the process, in to this java thread.
	 */
	public InputStream output;
	
	/**
	 * The ProcessBuidler used build up the process command and start the process.
	 */
	private ProcessBuilder processBuilder;
	
	/**
	 * Instantiate a new UserProcess (thread) with a ProcessBuilder so that the start 
	 * of the thread starts the process built up by the ProcessBuilder.
	 * @param p_processBuilder the ProcessBuilder to start when the thread starts
	 */
	public UserProcess(ProcessBuilder p_processBuilder)
	{
		processBuilder = p_processBuilder;
	}
	
	/**
	 * Start the thread and the underlying process. 
	 * 
	 * @exception NullPointerException if an element in the command list is null
	 * @exception IndexOutOfBoundsException if the command list is empty
	 * @exception SecurityException if the security manager does not allow subprocesses or certain file interactions
	 */
	public void run() throws NullPointerException, IndexOutOfBoundsException, SecurityException
	{
		try {
			process = processBuilder.start();
		} catch (IOException e) {
			//TODO handle exception
		}
		input = process.getOutputStream();
		output = process.getInputStream();
	}

}
