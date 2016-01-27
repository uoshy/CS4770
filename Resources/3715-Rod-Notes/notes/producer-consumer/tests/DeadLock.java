public class DeadLock extends Thread {
    private Object res1, res2;

    public DeadLock( String name, Object res1, Object res2 ) {
        super( name );
        this.res1 = res1;
        this.res2 = res2;
    }

    public void run() {
        // lock res1
        synchronized( res1 ) {
            System.out.println( getName() + ": locked " + res1 );
            try {
                Thread.sleep( 100 );
            }
            catch( InterruptedException ex ) {
            }
            // now try to get res2
            synchronized( res2 ) {
                System.out.println( getName() + ": locked " + res2 );
            }
        }
    }

    public static void main( String[] args ) {
        Object r1 = new Object();
        Object r2 = new Object();
        DeadLock t1 = new DeadLock("t1", r1, r2);
        DeadLock t2 = new DeadLock("t2", r2, r1);
        t1.start();
        t2.start();

        Thread[] threads = new Thread[10];
        int num = Thread.enumerate( threads );
        for( int i = 0 ; i < num; i++ ) {
            System.out.println( threads[i] );
        }
    }
}
