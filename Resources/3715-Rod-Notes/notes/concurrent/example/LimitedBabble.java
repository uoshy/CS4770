class LimitedBabble implements Runnable {
    private String msg;
    private int delay;
    private int limit;

    public LimitedBabble( String msg, int delay, int limit ) {
	this.msg = msg;
	this.delay = delay;
        this.limit = limit;
    }

    public void run() {
	try {
	    for(int i = 0; i < limit; i++) {
                double per = 100.0*i/limit;
		System.out.printf("%s %6.1f%n",msg, per );
		Thread.sleep( delay );
	    }
	}
	catch( InterruptedException e ) {
	    return; // end of this thread
	}
    }
}
