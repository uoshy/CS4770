package assignments;

import java.util.Collection;

import files.UserFile;
import users.User;

/**
 * A set of answers created by a Student for a particular Assignment and submitted 
 * for evaluation. This object encapsulates the files to be submitted, and references
 * to the owner of the submission and the associated Assignment.
 * 
 * @author Alex Brandt
 *
 */
public class AssignmentSubmission 
{
	/** The owner of the Assignment Submission */
    private User owner;
    
    /** The particular assignment which this submission attempts to solve */
    private Assignment relatedAssignment;
    
    /** The collection of files this submission is comprised of */
    private UserFile files;
    
    /** The confirmation of submission of the assignment to the system */
    private SubmissionReceipt submissionRec;

    private int submissionNum;
    
    /**
     * Create an AssignmentSubmission, supplying the owner of the submission, 
     * the related assignment, and the collection of files which make up the submission.
     * @param owner the User who is submitting the AssingmentSubmission
     * @param relatedAssignment the Assignment for which this submission is being made
     * @param files the files which this submission consists of
     */
    public AssignmentSubmission(User owner,Assignment relatedAssignment,
    		UserFile files, int submissionNum){
    	this.owner=owner;
    	this.relatedAssignment=relatedAssignment;
    	this.files=files;
	this.submissionNum = submissionNum;
    	//TODO create submission receipt
    }
    
	public Assignment getRelatedAssignment(){
		return relatedAssignment;
	}

	public User getOwner(){
		return owner;
	}

	public int getSubmissionNum(){
		return submissionNum;
	}
}
