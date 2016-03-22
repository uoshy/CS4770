package assignments;

import java.util.Collection;
import files.UserFile;

/**
 * A solution set for a particular Assignment. The instructor of a Course can optionally
 * supply the solutions for an Assignment. 
 * @author Alex
 *
 */
public class AssignmentSolution {
	
	/** Collection of files containing the solutions */
	private UserFile files;
	
	/** The assignment for which these files are solutions */
	private Assignment relatedAssignment;
	
    public AssignmentSolution(Assignment relatedAssignment, UserFile files)
    {
    	this.files = files;
    	this.relatedAssignment = relatedAssignment;
    }

	public Assignment getAssignment(){
		return relatedAssignment;
	}

	public UserFile getFiles(){
		return files;
	}
}
