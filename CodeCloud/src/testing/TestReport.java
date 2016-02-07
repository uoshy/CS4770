package testing;

import utility.HTMLDisplayable;

/**
 * A report returned after running a test suite on an Assignment Submission. 
 * The report should contain information about the passing and failing 
 * unit tests.
 * 
 * @author Alex Brandt
 *
 */
public class TestReport implements HTMLDisplayable {
    
	/** The text of the report */
	private String reportText;
	
	/**
	 * Initialize a test report with the supplied report text.
	 * 
	 * @param reportText the text of test report to be returned.
	 */
	public TestReport(String reportText)
	{
		this.reportText = reportText;
	}
	
	/**
	 * Display the test report's text as HTML
	 * 
	 * @return the test report encoded as HTML
	 */
	public String displayAsHTML(){
    	return reportText;
    }
}
