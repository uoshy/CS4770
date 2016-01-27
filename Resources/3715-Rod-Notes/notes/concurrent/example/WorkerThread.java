public class WorkerThread extends Thread {
    private ThreadPoolManager manager;

    public WorkerThread( ThreadPoolManager manager ) {
        this.manager = manager;
    }

    public void run() {
        Runnable nextRunnable;
        while ( (nextRunnable=manager.nextRunnable()) != null ) {
            try {
                nextRunnable.run();
            }
            catch( Exception ex ) {
                // ignore exception from runnable
            }
        }
    }
}
