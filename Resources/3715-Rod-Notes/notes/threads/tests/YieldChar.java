import static java.lang.System.out;

public class YieldChar {
    public static void main( String[] args )
        throws InterruptedException
    {
        // static inner class declaration
        Thread t1 = new Thread( new Runnable() {
            public void run() {
                try {
                    for(int i = 0 ; i < 5; i++ ) {
                        out.print('A');
                        out.flush();
                        Thread.sleep(1); // sleep for 1 ms
                        out.print((char)('0'+i));
                        out.flush();
                        Thread.sleep(1);
                    }
                }
                catch( InterruptedException ex ) {
                }
            }
        });
        Thread t2 = new Thread( new Runnable() {
            public void run() {
                try {
                    for(int i = 0 ; i < 5; i++ ) {
                        out.print('B');
                        out.flush();
                        Thread.sleep(1); // sleep for 1 ms
                        out.print((char)('a'+i));
                        out.flush();
                        Thread.sleep(1);
                    }
                }
                catch( InterruptedException ex ) {
                }
            }
        });
        t1.start(); // create a new thread
        t2.run(); // use the main thread
        t1.join();
        out.println();
    }
}
