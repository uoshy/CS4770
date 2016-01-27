import static java.lang.System.out;

public class LambdaYield {
    public static void main( String[] args ) {
        // lambda pass as argument, Thread expects a Runnable
        Thread t1 = new Thread(
            () -> {
                for(int i = 0 ; i < 5; i++ ) {
                    out.println("A" + i );
                    Thread.yield();
                }
            }
        );
        // lambda assigned to a Runnable
        Runnable r = () -> {
            for(int i = 0 ; i < 5; i++ ) {
                out.println("B" + i );
                Thread.yield();
            }
        };
        Thread t2 = new Thread( r );
        t1.start(); // create a new thread
        t2.run(); // use the main thread
    }
}
