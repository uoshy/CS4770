package assignments;

import java.util.Collection;

import files.UserFile;
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
	
	/**
	 * Create a comments encapsulating the feedback given to an Assignment
	 * Submission. This instance does not have any extra associated files.
	 * @param feedback the prose feedback for the assignment submission
	 */
	public Comments(String feedback)
	{
		this.feedback = feedback;
	}
	
	/**
	 * Create a Comments object encapsulating the feedback given to an Assignment
	 * Submission. Extra associated files are supplied for the Student receiving
	 * these comments to review. 
	 * 
	 * @param feedback the prose feedback for the assignment submission
	 * @param commentFiles a collection of UserFiles to be included with these Comments
	 */
	public Comments(String feedback, Collection<UserFile> commentFiles)
	{
		this.feedback = feedback;
		this.commentFiles = commentFiles;
	}
	
    public String displayAsHTML(){
    	//TODO
    	return null;
    }
}
