import static java.lang.System.out;
import static java.lang.System.nanoTime;
import java.util.ArrayList;

class SumAccumulate {
    private long sum;

    public SumAccumulate() {
        this.sum = 0;
    }
    public synchronized void add( long x) {
        sum += x;
    }
    public synchronized long getSum() {
        return sum;
    }
}

class SumThreadAcc extends Thread {
    private long start, end;
    private long startT, endT;
    private SumAccumulate acc;

    public SumThreadAcc( long s, long e,  SumAccumulate a ) {
        start = s;
        end = e;
        acc = a;
    }

    public void run() {
        startT = nanoTime();
        long x = start;
        while( x < end ) {
            acc.add( x );
            x++;
        }
        endT = nanoTime();
    }

    public long getElapsed() { return endT-startT; }
    public long getStartT() { return startT; }
    public long getEndT() { return endT; }
}

public class SumIntervalAcc {
    public static void main( String[] args ) throws Exception {
        long range = Long.parseLong( args[0] );
        int threads = Integer.parseInt( args[1] );
	SumAccumulate acc = new SumAccumulate();
	acc.add( range );

        long interval = range / threads;
        ArrayList<SumThreadAcc> sums = new ArrayList<SumThreadAcc>();
        long si = 0;
        for( int i = 0; i < (threads-1); i++ ) {
            sums.add( new SumThreadAcc(si, si + interval, acc ));
	    si += interval;
        }
        sums.add( new SumThreadAcc(si, range, acc));

        long begin = nanoTime();
        for( SumThreadAcc t : sums ) {
            t.start();
        }
        for( SumThreadAcc t : sums ) {
            t.join();
        }
        long end = nanoTime();
        out.println("total = " + acc.getSum() );
        out.printf("rel start = %11d rel end = %11d (%7.2e)%n",
            begin-begin, end-begin, (double)(end-begin) );
        for(int i = 0 ; i < sums.size(); i++ ) {
            SumThreadAcc t = sums.get(i);
            out.printf("t%d %11d %11d %11d (%7.2e)%n",
                i, t.getStartT()-begin,
                t.getEndT()-begin, t.getElapsed(), 
		(double) t.getElapsed());
        }
    }
}
