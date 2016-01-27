import static java.lang.System.out;
import static java.lang.Thread.sleep;
/**
 * Babble with lambdas
 */
class LambdaBabble {

    public static void main( String[] args ) {
        final String b1 = "hi";
        final int d1 = 200;
        Thread t1 = new Thread( () -> {
            try {
                for(;;){out.println(b1);sleep(d1);}
            }
            catch( InterruptedException ex ) {}
        } );
        t1.start();

        final String b2 = "hello";
        final int d2 = 300;
        Thread t2 = new Thread( () -> {
            try {
                for(;;){out.println(b2);sleep(d2);}
            }
            catch( InterruptedException ex ) {}
        } );
        t2.start();
    }
}
