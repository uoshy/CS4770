import java.util.concurrent.*;

public class JavaAPIThreadPoolTest {
    public static void main(String[] args) {
	if ( args.length != 2 ) System.exit( 1 ); // XXX
        int numWorkers = Integer.parseInt(args[0]);
        int threadPoolSize = Integer.parseInt(args[1]);
        ExecutorService tpes = Executors.newFixedThreadPool(threadPoolSize);
        for (int i = 0; i < numWorkers; i++) {
            tpes.execute(new LimitedBabble("t"+i, 500, 10));
        }
        tpes.shutdown();
    }
}
