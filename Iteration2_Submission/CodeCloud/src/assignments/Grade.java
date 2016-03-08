package assignments;

/**
 * A grade given to a particular Assignment Submission. This encapsulates all of 
 * the feedback for the Student's assignment submission. Transitively, through
 * the Assignment Submission, this grade corresponds to a single Student for 
 * particular Assignment. The Grade has option comments which can be used as feedback
 * for the assignment submission.
 * 
 * @author Alex
 *
 */
public class Grade {
    private AssignmentSubmission assign;
    private float grade;
    private Comments comments;
    
    /**
     * Create a Grade object associating a grade value, as a float, with a 
     * particular Assignment Submission. No Comments are supplied and thus 
     * only a numerical grade is associated with this Grade.
     * @param assign the Assignment Submission receiving this Grade
     * @param grade the numerical value indicating quality of the Assignment Submission
     */
    public Grade(AssignmentSubmission assign, float grade)
    {
    	this.assign = assign;
    	this.grade = grade;
    	comments = null;
    }
    
    /**
     * Create a Grade object associating a grade value, as a float, with a 
     * particular Assignment Submission. Comments are supplied with this grade.
     * @param assign the Assignment Submission receiving this Grade
     * @param grade the numerical value indicating quality of the Assignment Submission
     * @param comments the Comments associated to the marking of this Assignment Submission
     * @see Comments
     */
    public Grade(AssignmentSubmission assign,float grade,Comments comments){
    	this.assign=assign;
    	this.grade=grade;
    	this.comments=comments;
    }

    public AssignmentSubmission getAssignmentSubmission(){
	return assign;
    }

    public float getGrade(){
	return grade;
    }

    public Comments getComments(){
	return comments;
    }
}
