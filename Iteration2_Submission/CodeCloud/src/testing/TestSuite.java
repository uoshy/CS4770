package testing;

import assignments.Assignment;
import assignments.AssignmentSubmission;
import files.UserFile;

/**
 * An abstraction of a collection of unit tests. Running a test suite should
 * run all of the tests collected into the suite.
 * 
 * The actual implementation may be JUnit for example.
 * 
 * @author Alex Brandt
 *
 */
public abstract class TestSuite {
	
	/** The assignment associated to this test suite */
    private Assignment relatedAssignment;
    
    /** The main file containing the test suite  */
    private UserFile suiteFile;
    
    /**
     * Initialize a test suite with a main file containing the test suite implementation.
     * @param suiteFile the main test suite file
     */
    public TestSuite(UserFile suiteFile){
    	this.suiteFile = suiteFile;
    	this.relatedAssignment = null;
    }
    
    /**
     * Initialize a test suite with a main file containing the test suite implementation
     * and the assignment associated with this suite.
     * @param suiteFile the main suite file
     * @param relatedAssignment the assignment to associate this test suite with
     */
    public TestSuite(UserFile suiteFile, Assignment relatedAssignment)
    {
    	this.suiteFile = suiteFile;
    	this.relatedAssignment = relatedAssignment;
    }
    
    /**
     * Set the Assignment which this test suite is associated with.
     * @param relatedAssignment the assignment to associate with
     */
    public void setRelatedAssignment(Assignment relatedAssignment)
    {
    	this.relatedAssignment = relatedAssignment;
    }

    /**
     * Run the test suite on a assignment submissions. 
     * @param sub the assignment submission
     * @return a test report indicating the results of the test suite
     */
    public abstract TestReport testSubmission(AssignmentSubmission sub);
}
