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
	private Collection<UserFile> assignmentFiles;
	
	/** Optional tests for testing assignment submissions */
	private TestSuite tests;
	
	/** A collection of submission made towards this assignment */
	private Collection<AssignmentSubmission> submissions;
	
	/** An optional solution set for this assignment */
	private AssignmentSolution solution;
	
	/**
	 * Create a new Assignment for a particular course offering by supplying a 
	 * collection of files for the Assignment.
	 * @param course the particular course offering for this Assignment
	 * @param assignmentFiles a collection of files detailing this Assignment
	 */
    public Assignment(Course course, Collection<UserFile> assignmentFiles)
    {
    	this.course = course;
    	this.assignmentFiles = assignmentFiles;
    	submissions = new ArrayList<AssignmentSubmission>();
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
    public TestReport addSubmission(AssignmentSubmission submission)
    {
    	submissions.add(submission);
    	//TODO the testing?
    	return null;
    }
}
