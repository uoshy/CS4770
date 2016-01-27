import static  java.lang.System.nanoTime;

/**
 * Concurrency can cause confusion
 */
public class Race extends Thread {
    private int count;
    private Accumulate acc;

    public Race( int count, Accumulate acc ) {
        this.count = count;
        this.acc = acc;
    }
    public void run() {
        for( int i = 0; i < count ; i++ ) {
            acc.inc();
        }
    }
    public static void main( String[] args )  {
        if ( args.length != 1 ) {
            System.out.println("java Race loops");
            System.exit( 1 );
        }
        long loops = Long.parseLong( args[0] );
        long t = nanoTime();
        Accumulate acc = new Accumulate( loops );
        Thread t1 = new Race( 19000, acc );
        Thread t2 = new Race( 18000, acc );
        t1.start();
        t2.start();
        try {
            t1.join(); // wait for t1 to die
            t2.join(); // wait for t2 to die
        }
        catch( InterruptedException ex ) {
        }
        int expected = 19000 + 18000;
        System.out.println( acc.getCount() + " == " + expected );
        t = nanoTime() - t;
        System.out.println( "elapsed = " + t );

    }
}

class Accumulate {
    private int sum;
    private long loops;
    private long loopCount;

    public Accumulate( long loops ) {
        this.loops = loops;
        this.sum = 0;
    }
    public void inc() {
        int s = sum;
        // provide a stopping point for the thread
        for( int i = 0 ; i < loops ; i++ ) {
            loopCount++ ; // do nothing really,
           // required to ensure execution
        }
        sum = s + 1;
    }
    public int getCount() {
        return sum;
    }
}
