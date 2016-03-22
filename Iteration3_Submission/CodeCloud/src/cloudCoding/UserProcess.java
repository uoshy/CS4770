package cloudCoding;

import java.io.*;
import java.util.concurrent.*;

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
	 * The execution timeout of a running program. Programs which run longer than this amount
	 * will be terminated by the server. Measured in seconds.
	 */
	public static final int EXECUTION_TIMEOUT = 10;
	
	/**
	 * The underlying process of this UserProcess
	 */
	private Process process;
	
	/**
	 * The input stream to the process, out of this java thread.
	 */
	private OutputStream procInput;
	
	/**
	 * The writer of the output stream.
	 */
	private BufferedWriter writer;
	
	/**
	 * The output stream of the process, in to this java thread.
	 */
	private InputStream procOutput;
	
	/**
	 * The ProcessBuidler used build up the process command and start the process.
	 */
	private ProcessBuilder processBuilder;
	
	/**
	 * The UserProcess's unique process ID.
	 */
	private long processID;
	
	private StreamGobbler gobbler;

	private boolean processEnded;
	
	/**
	 * Instantiate a new UserProcess (thread) with a ProcessBuilder so that the start 
	 * of the thread starts the process built up by the ProcessBuilder.
	 * @param p_lProcessID the unique process ID.
	 * @param p_processBuilder the ProcessBuilder to start when the thread starts
	 */
	public UserProcess(long p_lProcessID, ProcessBuilder p_processBuilder)
	{
		this.processID = p_lProcessID;
		processBuilder = p_processBuilder;
		processEnded = false;
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
			System.out.println("Exception in UserProcess run start");
			//TODO handle exception
		}
		procInput = process.getOutputStream();
		procOutput = process.getInputStream();
		gobbler = new StreamGobbler(procOutput);
		gobbler.start();
		writer = new BufferedWriter(new OutputStreamWriter(procInput));
		
		try {
			process.waitFor(EXECUTION_TIMEOUT, TimeUnit.SECONDS);
		} catch(InterruptedException ie) {}
		
		if(!isProcessEnded())
		{
			timeoutProcess();
		}
	}
	
	/**
	 * Get this UserProcess's unique process ID.
	 * @return the unique process ID.
	 */
	public long getProcessID()
	{
		return processID;
	}
	
	/**
	 * Check to see if the process has ended. 
	 * @return true iff the process has ended
	 */
	public boolean isProcessEnded()
	{
		if(!process.isAlive() && !gobbler.isAlive())
			setProcessEnded();
		return processEnded;
	}
	
	/**
	 * Set the UserProcess as ended. Only successful if the underlying process has actually
	 * ended. 
	 * @return true iff successfully setting the process as ended.
	 */
	private boolean setProcessEnded()
	{
		if(!process.isAlive() && !gobbler.isAlive())
		{
			processEnded = true;
			Console.getInstance().processEnded(processID);
			return true;
		}
		else
			return false;
		
	}
	
	/**
	 * Get this process's exit status. If the process has not yet exited, -1 is returned.
	 * @return the process's exit status or -1 if has not yet exited.
	 */
	public int getExitStatus()
	{
		if(isProcessEnded())
			return process.exitValue();
		else
			return -1;
	}
	
	/**
	 * Write a string to the process's stdin. 
	 * Only one write request should be issued per expectation of input for the program. 
	 * After each invocation of this method the stream is flushed and thus the process may get
	 * fragmented input if this method is called multiple times.
	 * @param output the string to write
	 */
	public void writeToProcess(String output)
	{
		try{
			if(writer == null) //process has yet to be created
			{
				try{
					Thread.sleep(100);
				} catch(InterruptedException e) {}
			}
			writer.write(output);
			writer.write("\n");
			writer.flush();
		} catch (IOException ioe) {
			//error in writing to process. Probably because it's already closed.
		}
	}
	
	/**
	 * Attempts to read from the proccess's stdout. Through the use of a StreamGobbler,
	 * returns all output from the process since the previous call to readFromProcess.
	 * 
	 * @see StreamGobbler
	 * 
	 * @return the current available output from the process
	 */
	public String readFromProcess()
	{
		return gobbler.getOutput();
	}
	
	/**
	 * Kill the process. Attempt to kill it first by writing SIGINT, the interrupt signal ctrl+C, 
	 * to the program. If the process does not terminate after 1 second then the process is forcibly
	 * destroyed. After the process is destroyed, return its exit status.
	 * @return the program's exit status after termination.
	 */
	public int killProcess()
	{
		gobbler.setStreamClosed();
		this.writeToProcess("\003");
		try{
        	Thread.sleep(1000);
        } catch(InterruptedException e) {}
		
		int exitStatus = getExitStatus();
        if(exitStatus == -1)
        {
			process.destroyForcibly();
			try {
				process.waitFor(1000, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) { 
				System.out.println("Killing process took longer than 1 second..."); 
			}	
		}
        return getExitStatus();
	}
	
	public int timeoutProcess()
	{
		gobbler.setTimedOut();
		return killProcess();
	}
	
	

}