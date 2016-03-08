package assignments;

import utility.HTMLDisplayable;

import java.util.Date;

/**
 * A submission confirmation to be returned to the client when an AssignmentSubmission
 * is submitted. 
 * 
 * @author Alex Brandt
 *
 */
public class SubmissionReceipt implements HTMLDisplayable
{
	/** The text to display to the client */
	private String submissionResponse;
	
	/** The date of the submission */
	private Date submissionDate;
	
	/**
	 * Create a submission receipt for a submitted assignment. 
	 * @param submissionResponse the text for the receipt
	 * @param submissionDate the date of the submission
	 */
	public SubmissionReceipt(String submissionResponse, Date submissionDate)
	{
		this.submissionResponse = submissionResponse;
		this.submissionDate = submissionDate;
	}
	
    public String displayAsHTML(){
    	return submissionResponse;
    }
    
    /**
     * Get the date when the assignment submission was made.
     * @return the date of submission
     */
    public Date getAssignmentSubmissiondate(){
    	return submissionDate;
    }
}
