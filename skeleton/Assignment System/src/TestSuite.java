
public abstract class TestSuite {
    private Assignment relatedAssignment;
    private TestSuite(Assignment relatedAssignment){
    	this.relatedAssignment = relatedAssignment;
    }
    public abstract TestReport testSubmission(Assignment sub);
}
