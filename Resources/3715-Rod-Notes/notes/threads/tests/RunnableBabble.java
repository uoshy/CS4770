/**
 * Babble is an example of thread creation,
 * each Babble thread prints a messages waits
 * a fixed time, and repeats forever.
 */
class RunnableBabble implements Runnable {
    private String msg;
    private int delay;

    public RunnableBabble( String msg, int delay ) {
        this.msg = msg;
        this.delay = delay;
    }

    public void run() {
        try {
            for( ;; ) { // common infinte loop idiom
                System.out.println( msg );
                Thread.sleep( delay );
            }
        }
        catch( InterruptedException e ) {
            return; // end of this thread
        }
    }

    /**
     * Creates two babble threads, the initial
     * thread exists.
     */
    public static void main( String[] args )  {
        Thread t1 = new Thread( new Babble( "hi", 200 ));
        Thread t2 = new Thread( new Babble( "hello", 600 ));
        t1.start();
        t2.start();
    }
}
