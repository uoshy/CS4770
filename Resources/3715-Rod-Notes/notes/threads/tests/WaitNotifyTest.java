import java.util.Timer;
import java.util.TimerTask;

public class WaitNotifyTest {
    private int stepNo = 0;
    private int maxStep = 0;

    // check if the thread can proceed
    public synchronized void canProceed() {
        // idiom that ensure no false alarms
        // waiting should always depend on a condition
        while ( stepNo >= maxStep ) {
            try {
                wait(); // lock is relased by wait
            }
            catch( InterruptedException ex ) {
                // ignore interruption
            }
        }
        stepNo++;
    }

    // allow a blocked thread to proceed
    public synchronized void allowNext() {
        maxStep++;
        notify(); // notifies only one waiting thread
    }

    /**
     * Demonstrate thread coordination by allowing
     * the main thread to proceed when allowed
     * by the timer task.
     */
    public static void main( String[] args ) {
        WaitNotifyTest stepper = new WaitNotifyTest();
        NextStepTask advance = new NextStepTask( stepper );
        Timer timer = new Timer( true ); // mark as Daemon
        timer.schedule( advance, 1000, 2000 );

        System.out.println("starting steps");
        for( int step = 0; step < 10; step++ ) {
            System.out.println("waiting: " + step );
            stepper.canProceed();
            System.out.println("step: " + step );
        }
    }

    static class NextStepTask extends TimerTask {
        private WaitNotifyTest stepper;

        public NextStepTask( WaitNotifyTest stepper ) {
            this.stepper = stepper;
        }

        public void run() {
            System.out.println("allowing next");
            stepper.allowNext();
        }
    }
}
