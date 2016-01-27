public class Consumer extends Thread {
    private Link link;

    public Consumer( Link link ) {
        this.link = link;
    }

    public void run() {
        while( true ) {
            do {
                System.out.println("wait to recv" );
                Object o = link.recv();
                if ( o == null ) return;
                Integer i = (Integer)o;
                System.out.println("recv: " + i );
            } while ( link.getSize() != 0 );
            try {
                Thread.sleep( 10 );
            }
            catch( InterruptedException e ) {
            }
        }
    }
}
