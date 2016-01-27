/**
 * Babble is an example of thread creation,
 * each Babble thread prints a messages, waits
 * a fixed time, and repeats forever.
 */
class Babble extends Thread {
    private String msg;
    private int delay;

    public Babble( String msg, int delay ) {
        this.msg = msg;
        this.delay = delay;
    }

    public void run() {
        try {
            for( ;; ) { // common infinte loop idiom
                System.out.println( msg );
                sleep( delay );
            }
        }
        catch( InterruptedException e ) {
            return; // end of this thread
        }
    }

    /**
     * Creates two babblng threads, the initial
     * thread exits (terminates).
     */
    public static void main( String[] args )  {
        Thread t1 = new Babble( "hi", 250 );
        Thread t2 = new Babble( "hello", 500 );
        t1.start();
        t2.start();
        // original thread is finished
        System.out.println("main done");
    }
}
