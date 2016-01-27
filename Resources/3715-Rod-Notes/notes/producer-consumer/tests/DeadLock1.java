public class DeadLock1 extends Thread {
    private Object res1, res2;

    public DeadLock1( String name, Object res1, Object res2 ) {
        super( name );
        this.res1 = res1;
        this.res2 = res2;
    }

    public void run() {
        // lock res1
        synchronized( res1 ) {
            System.out.println(getName() + ": locked " + res1);
            // now try to get res2
            synchronized( res2 ) {
                System.out.println(getName() + ": locked " + res2);
            }
        }
    }

    public static void main( String[] args ) throws Exception {
        int count = 0;
        while( true ) {
            Object r1 = new Object();
            Object r2 = new Object();
            DeadLock1 t1 = new DeadLock1("t1", r1, r2);
            DeadLock1 t2 = new DeadLock1("t2", r2, r1);
            t1.start();
            t2.start();

            // wait for t1 and t2 to die before restarting
            t1.join();
            t2.join();
            System.out.println("loop = " + count );
            count++;
        }
    }
}
