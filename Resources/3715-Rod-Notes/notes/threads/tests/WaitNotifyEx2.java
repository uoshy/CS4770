import static java.lang.System.out;

public class WaitNotifyEx2 {
    public static void main( String[] args ) throws InterruptedException {
        // static inner class declaration
        final Runnable r1 = new Runnable() {
            public void run() {
                try {
                    out.println("r1 waiting");
                    synchronized( this ) {
                        wait();
                    }
                    out.println("r1 done");
                }
                catch( InterruptedException ex ) {
                    out.println("r1 interrupted");
                }
            }
        };
        final Thread t1 = new Thread( r1 );
        final Thread t2 = new Thread( new Runnable() {
            public void run() {
                try {
                    out.println("t2 sleeping");
                    // ensure notify happens after wait
                    Thread.sleep( 2000 );
                    out.println("t2 notify r1");
                    synchronized( r1 ) {
                        r1.notify();
                    }
                    out.println("t2 done");
                }
                catch( InterruptedException ex ) {
                    out.println("t2 interrupted");
                }
            }
        });
        t1.start(); // create a new thread
        t2.start(); // create a new thread

        t1.join();
        t2.join();
        out.println("main done");
    }
}
