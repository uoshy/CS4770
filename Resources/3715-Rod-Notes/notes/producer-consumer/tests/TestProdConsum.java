public class TestProdConsum {
    public static void main( String[] args ) {
        if ( args.length != 3 ) {
            System.out.println("usage: java TestProdConsum prods buf flush");
            System.exit( 1 );
        }
        try {
            int products = Integer.parseInt( args[0] );
            int bufSize =  Integer.parseInt( args[1] );
            boolean flush = args[2].equals("flush");
            Link link = new Bounded( bufSize );

            Producer prod = new Producer( link, products, flush );
            Consumer consum = new Consumer( link );

            consum.start();
            prod.start();
        }
        catch( Exception e ) {
            e.printStackTrace();
        }
    }
}
