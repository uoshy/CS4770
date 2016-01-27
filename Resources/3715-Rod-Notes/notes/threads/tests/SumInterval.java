import static java.lang.System.out;
import static java.lang.System.nanoTime;
import java.util.ArrayList;
import java.util.Collections;

class SumThread extends Thread {
    private long start, end;
    private long sum;
    private long startT, endT;

    public SumThread( long s, long e ) {
        start = s;
        end = e;
        sum = 0;
    }

    public void run() {
        startT = nanoTime();
        long x = start;
        while( x < end ) {
            sum += x;
            x++;
        }
        endT = nanoTime();
    }

    public long getElapsed() { return endT-startT; }
    public long getStartT() { return startT; }
    public long getEndT() { return endT; }
    public long getSum() { return sum; }
}

public class SumInterval {
    public static void main( String[] args ) throws Exception {
        long range = Long.parseLong( args[0] );
        int threads = Integer.parseInt( args[1] );
        long interval = range / threads;
        ArrayList<SumThread> sums = new ArrayList<SumThread>();
        long si = 0;
        for( int i = 0; i < (threads-1); i++ ) {
            // using si as arguments is very bad style, why?
            sums.add( new SumThread(si, si += interval));
        }
        sums.add( new SumThread(si, range));

        long begin = nanoTime();
        for( SumThread t : sums ) {
            t.start();
        }
        long total = range; // take care of the last sum
        for( SumThread t : sums ) {
            t.join();
            total += t.getSum();
        }
        long end = nanoTime();
        out.println("total = " + total );
        out.printf("tt %11d %11d %11d%n",
            begin-begin, end-begin, end-begin );
	out.println("--------------------------------------");
        for(int i = 0 ; i < sums.size(); i++ ) {
            SumThread t = sums.get(i);
            out.printf("t%d %11d %11d %11d%n",
                i, t.getStartT()-begin,
                t.getEndT()-begin, t.getElapsed() );
        }
    }
}
