import java.util.*;
import java.util.TimerTask;

/**
 * Demonstrates the effect of priorities and
 * difference between notify and notifyALL.
 * The difference must be studied carefully.
 */
public class MultipleThreads {
    private int stepNo = 0;
    private int maxStep = 0;
    private boolean all = false;

    public MultipleThreads( boolean all ) {
        this.all = all;
    }

    // check if the thread can proceed
    public synchronized void canProceed() {
        // idiom that ensure no false alarms
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
        if ( all ) {
            notifyAll();
        }
        else {
            notify();
        }
    }

    public static void main( String[] args ) {
        boolean allFlag = false;
        int aPriority = 0;
        int bPriority = 0;
        if ( args.length  >= 1 ) {
            allFlag = args[0].equals("t");
        }
        if ( args.length >= 2 ) {
            try {
                aPriority = Integer.parseInt( args[1] );
            }
            catch( NumberFormatException ex ) {
                // ignore
            }
        }
        if ( args.length >= 3 ) {
            try {
                bPriority = Integer.parseInt( args[2] );
            }
            catch( NumberFormatException ex ) {
                // ignore
            }
        }

        MultipleThreads stepper = new MultipleThreads( allFlag );
        NextStep advance = new NextStep( stepper );
        Timer timer = new Timer( true ); // mark as Daemon
        timer.schedule( advance, 500, 1000 );

        CanStep c1 = new CanStep("A", stepper, 3, aPriority );
        CanStep c2 = new CanStep("B", stepper, 5, bPriority );
        System.out.println("started A and B");
    }

    // static inner class, prevents pollution
    private static class NextStep extends TimerTask {
        private MultipleThreads stepper;

        public NextStep( MultipleThreads stepper ) {
            this.stepper = stepper;
        }

        public void run() {
            System.out.println("Allow next");
            stepper.allowNext();
        }
    }

    // static inner class, prevents pollution
    private static class CanStep extends Thread {
        private MultipleThreads stepper;
        private int limit;

        public CanStep(
            String name,
            MultipleThreads stepper,
            int limit,
            int priority )
        {
            super( name );
            this.stepper = stepper;
            this.limit = limit;
            setPriority( NORM_PRIORITY + priority );
            start();
        }

        public void run() {
            for( int step = 0; step < limit; step++ ) {
                stepper.canProceed();
                System.out.println( getName() + " : " + step );
            }
        }
    }
}
