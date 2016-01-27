public class StepControl {
    private int stepNumber;

    public StepControl() {
        stepNumber = 0;
    }

    public synchronized void waitForStep( int step )
        throws InterruptedException
    {
        while ( step >= stepNumber ) {
            wait();
        }
    }

    public synchronized void advanceStep() {
        stepNumber++;
        notifyAll();
    }
}
