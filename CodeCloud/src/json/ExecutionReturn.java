package json;

/**
 * A JSON object encapsulating all necessary data to return from a request to read from the executing 
 * program. It holds the exitStatus, a unique processID, and any output text from the program
 * since the last request to read from the program.
 * @author Alex Brandt
 *
 */
public class ExecutionReturn 
{
	/** The program's exit status. If the process has not exited yet, this will be -1. */
	public int exitStatus;
	
	/** The unique processID for the process. */
	public long processID;
	
	/** The text output from the process since the last request. */
	public String outputText;
}
