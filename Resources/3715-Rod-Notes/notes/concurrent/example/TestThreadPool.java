public class TestThreadPool {
    public static void main( String[] args ) {
        int tasks = Integer.parseInt( args[0] );
        int poolSz = Integer.parseInt( args[1] );
        ThreadPoolManager tp = new ThreadPoolManager(poolSz);
        for( int i = 0 ; i < tasks; i++ ) {
            tp.addRunnable( new LimitedBabble( "t"+i, 500, 10) );
        }
    }
}
