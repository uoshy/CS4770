import java.io.PrintStream;

class MyCounter {
    int count;
    MyCounter() {
        count = 0;
    }
}

class T1 extends Thread {
    protected PrintStream out;
    protected MyCounter counter;

    public T1( String name, MyCounter counter,  PrintStream out ) {
        super( name );
        this.counter = counter;
        this.out = out;
    }

    public void run() {
        out.println(getName() + ": running ");
        synchronized( out ) {
            out.println(getName() + ": locked " + out);
            // now try to get res2
            synchronized( counter ) {
                out.println(getName() + ": locked " + counter);
                counter.count++;
                out.println(getName() + " c=" + counter.count);
            }
        }
    }
}

// this is an example of bad inheritance
class T2 extends T1 {

    public T2( String name, MyCounter counter,  PrintStream out ) {
        super( name, counter, out );
    }

    public void run() {
        out.println(getName() + ": running ");
        synchronized( counter ) {
            out.println(getName() + ": locked " + counter);
            counter.count++;
            synchronized( out ) {
                out.println(getName() + ": locked " + out);
                out.println(getName() + " c=" + counter.count);
            }
        }
    }
}

public class RealisticDeadLock {
    public static void main( String[] args ) throws Exception {
        MyCounter c = new MyCounter();
        while( true ) {
            T1 t1 = new T1("t1", c, System.out);
            T2 t2 = new T2("t2", c, System.out);
            t1.start();
            t2.start();

            // wait for t1 and t2 to die before restarting
            t1.join();
            t2.join();
        }
    }
}
