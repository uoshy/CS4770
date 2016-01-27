public class Producer extends Thread {
    private Link link;
    private int totalItems;
    private boolean flush;

    public Producer( Link link, int totalItems, boolean flush ) {
        this.link = link;
        this.totalItems = totalItems;
        this.flush = flush;
    }

    public void run() {
        for( int items = 0 ; items < totalItems; items++ ) {
            Integer i = new Integer( items);
            System.out.println("sending: " + i );
            if ( flush ) System.out.flush();
            link.send( i );
            System.out.println( "sent: " + i + " size: "
                    + link.getSize() );
            if ( flush ) System.out.flush();
        }
        link.close();
    }
}
