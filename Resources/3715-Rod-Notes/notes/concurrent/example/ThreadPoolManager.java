import java.util.LinkedList;

public class ThreadPoolManager {
    private LinkedList<Runnable> task = new LinkedList<Runnable>();
    private WorkerThread[] workers;

    public ThreadPoolManager( int size ) {
        workers = new WorkerThread[ size ];
        for( int i = 0 ; i < size; i++ ) {
            workers[i] = new WorkerThread( this );
            workers[i].start();
        }
    }

    public synchronized void addRunnable( Runnable r) {
        task.add( r );
        notify();
    }

    public synchronized Runnable nextRunnable() {
        while( task.size() <= 0 ) {
            try {
                wait();
            }
            catch( InterruptedException ex ) {
            }
        }
        return task.remove( 0 );
    }
}
