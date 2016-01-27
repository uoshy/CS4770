import java.net.InetAddress;
import java.net.UnknownHostException;

public class NSLookup {
    public static void main( String[] args ) {
        if ( args.length != 1 ) {
            System.out.println( "usage: java NSLookup host-name" );
            System.exit( 1 ); // indicate an error
        }
        try {
            InetAddress[] addrs =
                InetAddress.getAllByName( args[0] );
            for ( InetAddress addr : addrs ) {
                System.out.println( addr.getHostAddress() );
            }
        }
        catch(  UnknownHostException ex ) {
            System.out.println( ex.getMessage() );
            System.exit( 1 ); 
        }
    }
}
