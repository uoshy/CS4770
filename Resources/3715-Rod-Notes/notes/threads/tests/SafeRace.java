import static java.lang.System.nanoTime;

public class SafeRace extends Thread {
    private int count;
    private SafeAccumulate acc;

    public SafeRace( int count, SafeAccumulate acc ) {
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
            System.out.println("java SafeRace loops");
            System.exit( 1 );
        }
        int loops = Integer.parseInt( args[0] );
        long t = nanoTime();
        SafeAccumulate acc = new SafeAccumulate( loops );
        Thread t1 = new SafeRace( 19000, acc );
        Thread t2 = new SafeRace( 18000, acc );
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        }
        catch( InterruptedException ex ) {
        }
        System.out.println( acc.getCount() + " == " + 37000 );
        t = nanoTime() - t;
        System.out.println( "elapsed = " + t );
    }
}

class SafeAccumulate {
    private int sum;
    private int loops;
    private long loopCount;

    public SafeAccumulate(int loops ) {
        this.sum = 0;
        this.loops = loops;
    }
    // the only difference is the synchronized keyword
    public synchronized void inc() {
        int s = sum;
        for( int i = 0 ; i < loops ; i++ ) 
            loopCount++; // do nothing
        sum = s + 1;
    }
    public int getCount() {
        return sum;
    }
}
