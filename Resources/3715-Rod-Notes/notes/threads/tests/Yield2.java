import static java.lang.System.out;
import static java.lang.System.nanoTime;
import java.util.ArrayList;

public class Yield2 {
    public static void main( String[] args ) throws InterruptedException {
        // al1, al2, and start appear in the inner classes
        final ArrayList<Long> al1 = new ArrayList<Long>();
        final ArrayList<Long> al2 = new ArrayList<Long>();
        final long start = nanoTime();
        Thread t1 = new Thread( new Runnable() {
            public void run() {
                for(int i = 0 ; i < 5; i++ ) {
                    al1.add( nanoTime() - start );
                    Thread.yield();
                }
            }
        });
        Thread t2 = new Thread( new Runnable() {
            public void run() {
                for(int i = 0 ; i < 5; i++ ) {
                    al2.add( nanoTime() - start );
                    Thread.yield();
                }
            }
        });
        t1.start(); // create a new thread
        t2.run(); // use the main thread
        t1.join();
        // output time stamps
        for( int i = 0 ; i < al1.size(); i++ ) {
            out.printf("A%d=%d B%d=%d%n", i, al1.get(i), i, al2.get(i) );
        }
    }
}
