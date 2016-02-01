import java.util.ArrayList;
public class Assignment {
	private Course course; 
	private TestSuite tests;
	private ArrayList<AssignmentSubmission> submissions;
	private AssignmentSolution solution;
	private ArrayList<UserFile> assignmentFiles;
    public Assignment(Course course,TestSuite tests,ArrayList<AssignmentSubmission> submissions,
    		AssignmentSolution solution,ArrayList<UserFile> assignmentFiles){
    	this.course=course;
    	this.tests=tests;
    	this.submissions=submissions;
    	this.solution= solution;
    	this.assignmentFiles=assignmentFiles;
    }
}
