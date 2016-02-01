import java.util.ArrayList;
public class AssignmentSubmission {
    private User owner;
    private Assignment relatedAssignment;
    private ArrayList<UserFile> files;
    private SubmissionReceipt submission;
    public AssignmentSubmission(User owner,Assignment relatedAssignment,
    		ArrayList<UserFile> files, SubmissionReceipt submission){
    	this.owner=owner;
    	this.relatedAssignment=relatedAssignment;
    	this.files=files;
    	this.submission=submission;
    }
    
}
