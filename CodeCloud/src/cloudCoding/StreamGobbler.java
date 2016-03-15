package cloudCoding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * A StreamGobbler is used to accumulate all data received from an InputStream. 
 * The gobbler runs on a separate thread so that it can block waiting for a read and not affect 
 * the remaining program. Simply construct a new StreamGobbler using a reference to an InputStream and 
 * then start the thread. 
 * 
 * Note: It is assumed the InputStream encodes characters and not binary.
 * 
 * @author Alex Brandt
 *
 */
public class StreamGobbler extends Thread {
	
	/** The InputStream from which to receive data. */
	private InputStream is;
	
	/** The StringBuilder user to accumulate read data. */
	private StringBuilder builder;
	
	/** A flag to determine if the stream was closed externally. */
	private boolean streamClosed;
	
	/** A flag to determine if the stream source timed out and has ended. */
	private boolean timedOut;
	
	/**
	 * Construct a new StreamGobbler by supplying the input stream it should read data from.
	 * @param is The input stream to read data from 
	 */
	public StreamGobbler(InputStream is)
	{
		this.is = is;
		builder = new StringBuilder();
	}

	/**
	 * The method executed when a thread is started. This method is executed asynchronously to 
	 * other threads. StreamGobbler's implementation is to simply read all input the is
	 * available from it's input stream. The thread terminates when the stream is closed externally 
	 * or when the stream returns EOF as the other end of the stream is closed.
	 */
	@Override
	public void run()
	{
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while( (line = br.readLine()) != null) //keep reading while not EOF
			{	
				System.out.println(line);
				synchronized(this) //make sure you don't append to builder while
				{                  //it is being retrieved from elsewhere
					builder.append(line);
					builder.append("\n");
				}
			}
		} catch(IOException ioe) 
		{
			if(!streamClosed) //will throw an exception if the stream was externally closed
				ioe.printStackTrace();
		}
		if(timedOut)
		{
			builder.append("\nProcess Timed Out!");
		}
	}

	/**
	 * Get the contents of the InputStream which have been read so far.
	 * Note that this operation is a one-time operation. The buffer of read data 
	 * is cleared after this method returns.
	 * @return the data read from the input stream since the last call to this method
	 */
	public String getOutput()
	{
		synchronized(this) //ensure no data is being put in the builder when we extract the string
		{
			String toRet = builder.toString();
			builder.setLength(0);
			
			return toRet;
		}
	}
	
	/**
	 * Sets the stream to be closed so that the thread it was intentionally closed and was no an error.
	 */
	public void setStreamClosed()
	{
		streamClosed = true;
	}
	
	public void setTimedOut()
	{
		timedOut = true;
	}
	
}

