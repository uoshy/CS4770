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
	 * The reader of the input stream.
	 */
	private BufferedReader reader;
	
	/**
	 * The ProcessBuidler used build up the process command and start the process.
	 */
	private ProcessBuilder processBuilder;
	
	/**
	 * The UserProcess's unique process ID.
	 */
	private long processID;
	
	private class StreamGobbler extends Thread {
		InputStream is;
		StringBuilder builder;
		boolean streamClosed;
		StreamGobbler(InputStream is)
		{
			this.is = is;
			builder = new StringBuilder();
		}

		public void run()
		{
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				String line = null;
				while( (line = br.readLine()) != null)
				{	
					System.out.println(line);
					synchronized(this)
					{
						builder.append(line);
						builder.append("\n");
					}
				}
			} catch(IOException ioe)
			{
				if(!streamClosed) 
				    ioe.printStackTrace();
			}
		}

		public String getOutput()
		{
			synchronized(this)
			{
				String toRet = builder.toString();
				builder.setLength(0);
				return toRet;
			}
		}
	}


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
		//reader = new BufferedReader(new InputStreamReader(procOutput));
		/*
		while(!isProcessEnded())
		{
			System.out.println("sleeping");
			try{
			Thread.sleep(100);
			}
			catch(InterruptedException e)
			{
				continue;
			}
		}
		System.out.println("Done sleeping.. ");
		*/
	}
	
	/**
	 * Get this UserProcess's unique process ID.
	 * @return the unique process ID.
	 */
	public long getProcessID()
	{
		return processID;
	}
	
	public boolean isProcessEnded()
	{
		if(!process.isAlive())
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
		if(!process.isAlive())
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
	 * Attempts to read from the proccess's stdout. Using threading and timeouts
	 * this method will return if reading from the process blocks for too long. 
	 * This likely signifies the process is waiting for input. Returns the empty
	 * string if no output is available from the process.
	 * 
	 * @return the current available output from the process
	 */
	public synchronized String readFromProcess()
	{
		return gobbler.getOutput();
		/*
		//Inspired by http://www.programering.com/q/MjN4MzNwATc.html
		ExecutorService executor = Executors.newFixedThreadPool(2);
		Callable<String> readTask = new Callable<String>() {
			public String call() throws Exception {
				return reader.readLine();
			}
		};
		
		StringBuilder builder = new StringBuilder();
		while(true)
		{
			Future<String> future = executor.submit(readTask);
			try {
				String readLine = future.get(1000, TimeUnit.MILLISECONDS);
				if(readLine == null) //reader.readLine() found EOF (proc. terminated)
				{
					System.out.println("Read null from process");
					setProcessEnded();
					break;
				}
				else
				{
					System.out.println("Read from process in read method: " + readLine);
				}
				
				builder.append(readLine);
				builder.append("\n");
			}
			catch(CancellationException e) { //future was cancelled 
				//should never really happen
				continue;
			} 
			catch (InterruptedException e) { //current thread interrupted while waiting
				continue; //try to read again
			} 
			catch (ExecutionException e) { //if the callable threw an exception
				continue;
			} 
			catch (TimeoutException e) { //if the future timed out
				//process's stdout is not currently printing anything
				//so probably waiting for input. So let's return and do that.
				System.out.println("Read op timed out!");
				if(!future.cancel(false)) //future could not be cancelled so it probably did something
				{
					try {
						String readLine = future.get();
						builder.append(readLine);
						builder.append("\n");
					}
					catch(CancellationException | InterruptedException | ExecutionException ex) {

					}
				}
				break;
			}
		}
		executor.shutdown();
		return builder.toString(); //if nothing was read, empty string returned
		*/
	}
	
	public void killProcess()
	{
		gobbler.streamClosed = true;
		this.writeToProcess("\003");
		try{
        	Thread.sleep(1000);
        } catch(InterruptedException e) {}
        if(getExitStatus() == -1)
        {
			process.destroyForcibly();
			try {
				process.waitFor(1000, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) { 
				System.out.println("Killing process took longer than 1 second..."); 
			}	
		}
	}
	
	
	

}