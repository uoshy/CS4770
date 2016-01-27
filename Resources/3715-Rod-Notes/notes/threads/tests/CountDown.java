/**
 * Count down to termination unless stopped.
 */
public class CountDown implements Runnable {

    private int count;
    private boolean stop;

    public CountDown( int count ) {
        this.count = count;
        this.stop = false;
    }

    public void stop() {
        stop = true;
    }

    public void run() {
        try {
            for( int i = 0; i < count ; i++ ) {
                System.out.println( "count = " + i );
                if ( stop ) return;
                Thread.sleep( 1000 );
            }
            System.out.println("too late");
            System.exit( 0 );
        }
        catch( InterruptedException ex ) {
        }
    }

    /**
     * The initial thread starts with main.
     */
    public static void main( String[] args ) throws java.io.IOException {
        if ( args.length != 1 ) {
            System.out.println("java CountDown limit");
            System.exit( 1 );
        }
        int limit = Integer.parseInt( args[0] );
        CountDown cd = new CountDown( limit );
        Thread t = new Thread( cd );
        // start the second thread.
        t.start();
        int ch;
        while ( (ch=System.in.read()) != -1) {
            char c = (char)ch;
            if ( c == 'x' ) {
                cd.stop();
                System.out.println("stop requested");
                break;
            }
            else if ( c == 'q' ) {
                System.out.println("quit main");
                break;
            }
            else {
                System.out.println("what?");
            }
        }
        System.out.println("main done.");
    }
}
