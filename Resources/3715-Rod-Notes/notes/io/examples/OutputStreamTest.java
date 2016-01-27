import java.io.IOException;
import java.io.OutputStream;

public class OutputStreamTest {

    public static void main( String[] args ) {
        for( int i = 48 ; i < 90; i++ ) {
            System.out.write( i );
        }
        System.out.write( 10 );
        System.out.flush();
    }

}
