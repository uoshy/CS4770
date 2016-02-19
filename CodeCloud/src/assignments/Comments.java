package assignments;

import java.util.Collection;

import files.UserFile;
import users.User;
import utility.HTMLDisplayable;

/**
 * Comments that can optionally be submitted with the Grade evaluation of 
 * an Assignment Submission. Comments encapsulate direct, prose feedback
 * as well as files that can be associated with the comments. Such files 
 * may be source code files or files containing code snippets as examples. 
 * 
 * Comments are associated with a particular Assignment Submission due to their 
 * reference within a single Grade object. A direct link to the Assignment Submission
 * is not needed by semantically the relationship is 1-1.
 * @see Grade
 * 
 * @author Alex Brandt
 *
 */
public class Comments implements HTMLDisplayable {
	
	/** The collection of optional files. */
	private Collection<UserFile> commentFiles;
	
	/** The written comments about the submission*/
	private String feedback;

	/* The user being given the feedback */
	private User recipient;

	private AssignmentSubmission relatedSubmission;
	
	/**
	 * Create a comments encapsulating the feedback given to an Assignment
	 * Submission. This instance does not have any extra associated files.
	 * @param feedback the prose feedback for the assignment submission
	 */
	public Comments(String feedback, User recipient, AssignmentSubmission relatedSubmission)
	{
		this.feedback = feedback;
		this.recipient = recipient;
		this.relatedSubmission = relatedSubmission;
	}
	
	/**
	 * Create a Comments object encapsulating the feedback given to an Assignment
	 * Submission. Extra associated files are supplied for the Student receiving
	 * these comments to review. 
	 * 
	 * @param feedback the prose feedback for the assignment submission
	 * @param commentFiles a collection of UserFiles to be included with these Comments
	 */
	public Comments(String feedback, Collection<UserFile> commentFiles, User recipient, AssignmentSubmission relatedSubmission)
	{
		this.feedback = feedback;
		this.commentFiles = commentFiles;
		this.recipient = recipient;
		this.relatedSubmission = relatedSubmission;
	}
	
	public User getRecipient(){
		return recipient;
	}

	public String getFeedback(){
		return feedback;
	}

	public AssignmentSubmission getRelatedSubmission(){
		return relatedSubmission;
	}

    public String displayAsHTML(){
    	//TODO
    	return null;
    }
}
