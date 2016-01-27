import static java.lang.System.out;
import static java.lang.System.nanoTime;
import java.util.ArrayList;
import java.util.Collections;

// Used to record time stamps
class TimeStamp implements Comparable<TimeStamp> {
    String id;
    int seq;
    long timeStamp;
    TimeStamp( String id, int seq, long timeStamp ) {
        this.id = id;
        this.seq = seq;
        this.timeStamp = timeStamp;
    }
    // compare time using only timeStamp
    public int compareTo(TimeStamp o) {
        return (int)(timeStamp - o.timeStamp);
    }
    public String toString() {
        return id + seq + " " + timeStamp;
    }
}

public class Yield3 {
    public static void main( String[] args ) throws InterruptedException {
        final ArrayList<TimeStamp> al1 = new ArrayList<TimeStamp>();
        final ArrayList<TimeStamp> al2 = new ArrayList<TimeStamp>();
        final long start = nanoTime();
        Thread t1 = new Thread( new Runnable() {
            public void run() {
                for(int i = 0 ; i < 5; i++ ) {
                    al1.add( new TimeStamp( "A", i, nanoTime()-start) );
                    Thread.yield();
                }
            }
        });
        Thread t2 = new Thread( new Runnable() {
            public void run() {
                for(int i = 0 ; i < 5; i++ ) {
                    al2.add( new TimeStamp( "B", i, nanoTime()-start) );
                    Thread.yield();
                }
            }
        });
        t1.start(); // create a new thread
        t2.run(); // use the main thread
        t1.join();

        ArrayList<TimeStamp> combined = new ArrayList<TimeStamp>();
        combined.addAll( al1 );
        combined.addAll( al2 );
        // sort combined timeStamps by time
        Collections.sort( combined );
        // output the time stamps in order
        for( TimeStamp ts : combined ) {
            out.println( ts );
        }
    }
}
