import static java.lang.System.nanoTime;

public class SafeRaceBlock extends Thread {
    private int count;
    private SafeAccumulateBlock acc;

    public SafeRaceBlock( int count, SafeAccumulateBlock acc ) {
        this.count = count;
        this.acc = acc;
    }
    public void run() {
        for( int i = 0; i < count ; i++ ) {
            acc.inc();
        }
    }
    public static void main( String[] args )  {
        long t = nanoTime();
        SafeAccumulateBlock acc = new SafeAccumulateBlock();
        Thread t1 = new SafeRaceBlock( 9000, acc );
        Thread t2 = new SafeRaceBlock( 8000, acc );
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        }
        catch( InterruptedException ex ) {
        }
        System.out.println( acc.getCount() + " == " + 17000 );
        t = nanoTime() - t;
        System.out.println( "elapsed = " + t );
    }
}

class SafeAccumulateBlock {
    private int sum;
    private long loopCount;

    public SafeAccumulateBlock() {
        sum = 0;
    }
    // the only difference is synchronized block
    public void inc() {
        synchronized (this ) {
            int s = sum;
            for( int i = 0 ; i < 10000 ; i++ ) 
                loopCount++; // do nothing
            sum = s + 1;
        }
    }
    public int getCount() {
        return sum;
    }
}
