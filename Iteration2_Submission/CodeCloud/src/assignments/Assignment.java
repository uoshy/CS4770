package assignments;

import java.util.ArrayList;
import java.util.Collection;

import testing.TestReport;
import testing.TestSuite;
import files.UserFile;

/**
 * Assignment is a set of files representing a method of evaluation for a course. 
 * An assignment may include files with questions (.doc, .pdf, .md, etc.) and files
 * with source code. 
 * 
 * @author Alex Brandt
 */
public class Assignment 
{
	/** The course which this assignment evaluates */
	private Course course; 
	
	/** A collection of files containing the details of this assignment */
	private UserFile assignmentDir;
	
	/** Optional tests for testing assignment submissions */
	private TestSuite tests;
	
	/** A collection of submission made towards this assignment */
	private UserFile submissions;
	
	/** An optional solution set for this assignment */
	private AssignmentSolution solution;

	/** The assignment's number in its course */
	private int number;

	/**The number of submissions a student is allowed to make for this assignment */
	private int submissionLimit;
	
	/**
	 * Create a new Assignment for a particular course offering by supplying a 
	 * collection of files for the Assignment.
	 * @param course the particular course offering for this Assignment
	 * @param assignmentFiles a collection of files detailing this Assignment
	 */
	/**
	 * Constructor requires AssignmentSolution as parameter but AssignmentSolution requires Assignment. This seems unlikely (but not impossible) to be useful in practice.
	 * Ideally the database would use the fullest constructor but in this case that isn't possible since the database requires an AssignmentSolution to initialize this Assignment.
	 * I've left this constructor up in case this isn't actually a problem for reasons I don't understand but have added other, less "full" constructors.
	 * -Tim 21/2/16
	 */
    public Assignment(Course course, UserFile assignmentDir, TestSuite tests, UserFile submissions,  AssignmentSolution solution, int number, int submissionLimit)
    {
    	this.course = course;
    	this.assignmentDir = assignmentDir;
	this.tests = tests;
    	this.submissions = submissions;
	this.solution = solution;
	this.number = number;
	this.submissionLimit = submissionLimit;
    }

    public Assignment(Course course, UserFile assignmentDir, TestSuite tests, UserFile submissions, int number, int submissionLimit)
    {
    	this.course = course;
    	this.assignmentDir = assignmentDir;
	this.tests = tests;
    	this.submissions = submissions;
	this.number = number;
	this.submissionLimit = submissionLimit;
    }


    public Assignment(Course course, UserFile assignmentDir, UserFile submissions, int number, int submissionLimit)
    {
    	this.course = course;
    	this.assignmentDir = assignmentDir;
    	this.submissions = submissions;
	this.number = number;
	this.submissionLimit = submissionLimit;
    }
    
    /**
     * Set the Test Suite for this Assignment to be used to 
     * test submissions.
     * @param test the Test Suite to associate with this Assignment
     */
    public void setAssignmentTestSuite(TestSuite test)
    {
    	this.tests = test;
    }
    
    /**
     * Set the Assignment Solutions to this Assignment.
     * @param solution the solutions for this Assignment.
     */
    public void setAssignmentSolutions(AssignmentSolution solution)
    {
    	this.solution = solution;
    }
    
    /**
     * Add (submit) an Assignment Submission towards this Assignment.
     * If a Test Suite exists for this Assignment the submission will be tested
     * and a TestReport returned. Otherwise the return will be null.
     * @param submission the Assignment Submission to add.
     * @return a TestReport if a Test Suite exists to test the submission. 
     */
//TODO: addSubmissionLogic and testing/TestReport creation
/*    public TestReport addSubmission(AssignmentSubmission submission)
    {
    	submissions.add(submission);
    	return null;
    }**/

	public Course getCourse(){
		return course;
	}

	public int getNumber(){
		return number;
	}

	public int getSubmissionLimit(){
		return submissionLimit;
	}
}
