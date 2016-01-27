import static  java.lang.System.nanoTime;

/**
 * Count the overlaps
 */
public class CountRace extends Thread {
    private int count;
    private CountAccumulate acc;
    int id;

    public CountRace( int count, CountAccumulate acc, int id ) {
        this.count = count;
        this.acc = acc;
        this.id = id;
    }

    public void run() {
        for( int i = 0; i < count ; i++ ) {
            acc.inc( id );
        }
    }

    public static void main( String[] args )  {
        if ( args.length != 1 ) {
            System.out.println("java CountRace loops");
            System.exit( 1 );
        }
        long t = nanoTime();
        int loops = Integer.parseInt( args[0] );
        CountAccumulate acc = new CountAccumulate(loops, 2);
        Thread t1 = new CountRace( 19000, acc, 0 );
        Thread t2 = new CountRace( 18000, acc, 1 );
        t1.start();
        t2.start();
        try {
            t1.join(); // wait for t1 to die
            t2.join(); // wait for t2 to die
        }
        catch( InterruptedException ex ) {
        }
        System.out.println( acc.getCount() + " == " + 37000 );
        System.out.println( "overlaps = " + acc.getOverlap() );
        t = nanoTime() - t;
        System.out.println( "elapsed = " + t );
    }
}

class CountAccumulate {
    private int sum;
    private int overlap;
    private int loops;
    private long loopCount;
    private boolean[] busy;

    public CountAccumulate( int loops, int threads ) {
        this.sum = 0;
        this.overlap = 0;
        this.loops = loops;
        this.busy = new boolean[threads];

        for ( int i = 0; i < busy.length; i++ ) {
            this.busy[i] = false;
        }
    }

    public synchronized void startOverlap(int id){
        boolean over = false;
        for( boolean b : busy ) {
            if ( b ) { over = true; break; }
        }
        busy[id] = true;
        if ( over ) {
            overlap++;
        }
    }

    public synchronized void endOverlap(int id){
        busy[id] = false;
    }

    public void inc(int id) {
        startOverlap(id);
        int s = sum;
        for( int i = 0 ; i < loops ; i++ ) 
            loopCount++; // do nothing
        sum = s + 1;
        endOverlap(id);
    }

    public int getCount() {
        return sum;
    }

    public int getOverlap() {
        return overlap;
    }
}
