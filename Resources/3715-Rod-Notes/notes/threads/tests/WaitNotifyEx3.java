import static java.lang.System.out;

class WaitForNotify implements Runnable {
    private String id;
    private boolean notified = false;

    public WaitForNotify( String id ) {
        this.id = id;
    }

    public void run() {
        try {
            out.println(id + " waiting");
            synchronized( this ) {
                /* It is common to test for a condition
                 * to see if the waiting is no longer required.
                 * This prevents false  wake ups.
                 */
                while ( !notified ) {
                    wait();
                }
                notified = false; // reset condition
            }
            out.println(id + " done");
        }
        catch( InterruptedException ex ) {
            out.println(id + " interrupted");
        }
    }

    // notifyRequest must be synchronized for the notify()
    public synchronized void notifyRequest() {
        notified = true;
        notify();
    }
}

public class WaitNotifyEx3 {
    public static void main( String[] args ) throws InterruptedException {
        final WaitForNotify w = new WaitForNotify( "w1" );
        final Thread t1 = new Thread( w );
        final Thread t2 = new Thread( new Runnable() {
            public void run() {
                try {
                    out.println("t2 sleeping");
                    Thread.sleep( 2000 );
                    out.println("t2 notify r1");
                    w.notifyRequest();
                    out.println("t2 done");
                }
                catch( InterruptedException ex ) {
                    out.println("t2 interrupted");
                }
            }
        });
        t1.start(); // create a new thread
        t2.start(); // use the main thread

        t1.join();
        t2.join();
        out.println("main done");
    }
}
