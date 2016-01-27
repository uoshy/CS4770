import static java.lang.System.out;

public class Yield {
    public static void main( String[] args ) {
        // static inner anonymou class declaration
        Thread t1 = new Thread( new Runnable() {
            public void run() {
                for(int i = 0 ; i < 5; i++ ) {
                    out.println("A" + i );
                    Thread.yield();
                }
            }
        });
        Thread t2 = new Thread( new Runnable() {
            public void run() {
                for(int i = 0 ; i < 5; i++ ) {
                    out.println("B" + i );
                    Thread.yield();
                }
            }
        });
        t1.start(); // create a new thread
        t2.run(); // use the main thread
    }
}
