class Ticking extends Thread {
    private Ticking[] threads;
    private int id;
    private int limit;
    private int interval;
    private int inc;

    public Ticking( 
        Ticking[] threads, int id,
        int limit, int interval, int pri )
    {
        this.threads = threads;
        this.id = id;
        this.limit = limit;
        this.interval = interval;
        setPriority( pri );
    }

    public int getInc() { 
        return inc;
    }

    private int[]  spin() {
        int loops = 10000;
        int[] d = new int[loops];

        while ( loops > 0 ) {
           loops--;
           d[loops] = loops;
        }
        return d;
    }

    public void run() {
        for( inc = 0 ; inc < limit; inc++ ) {
            if ( ( inc % interval) == 0 ) {
                synchronized( System.out ) {
                    System.out.print( "thread: " + id + " :");
                    for( int i = 0 ; i < threads.length; i++ ) {
                        System.out.print( " " + threads[i].getInc() );
                    }
                    System.out.println();
                }
            }
            int[] d = spin();
        }
    }
}

/**
 * MAX_PRIORITY = 10
 * MIN_PRIORITY = 1
 * NORM_PRIORITY = 5
 *
 * The effect of priority is implementation specific.
 */
public class Scheduling {
    public static void main( String[] args ) {
        final int limit = 100000;
        final int interval = 10000;
        Ticking[] threads = new Ticking[ args.length ];

        for( int i = 0 ; i < args.length; i++ ) {
            int pri = Integer.parseInt( args[i] );
            Ticking t =
                new Ticking(threads, i, limit, interval, pri );
            System.out.println( i + "/" + t.getPriority() );
            threads[i] = t;
        }
        for( int i = 0 ; i < args.length; i++ ) {
            threads[i].start();
        }
    }
}
