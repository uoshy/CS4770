import java.io.File;
import java.io.IOException;

public class DirList {
    public static void main( String[] args ) throws IOException {
        if ( args.length != 1 ) {
            System.out.println("usage: java DirList dir");
            System.exit( 1 );
        }
        String dirname = args[0];
        File dir = new File( dirname );
        if( !dir.isDirectory() ) {
            System.out.println(dirname + " is not a directory");
            System.exit( 1 );
        }
        String[] list = dir.list();
        for( int i = 0 ; i < list.length; i++ ) {
            System.out.println( list[i] );
        }
    }
}
