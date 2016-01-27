import static java.lang.System.out;
import java.util.ArrayList;

public class Yield1 {
    public static void main( String[] args ) throws InterruptedException {
        // al must be final since in appears in t1/t2
        final ArrayList<String> al = new ArrayList<String>();
        Thread t1 = new Thread( new Runnable() {
            public void run() {
                for(int i = 0 ; i < 5; i++ ) {
                    synchronized(al) {
                        al.add( "A" + i );
                    }
                    Thread.yield();
                }
            }
        });
        Thread t2 = new Thread( new Runnable() {
            public void run() {
                for(int i = 0 ; i < 5; i++ ) {
                    synchronized(al) {
                        al.add( "B" + i );
                    }
                    Thread.yield();
                }
            }
        });
        t1.start(); // create a new thread
        t2.run(); // use the main thread
        t1.join();
        // output list
        for( String s : al ) {
            out.println(s);
        }
    }
}
