import java.io.OutputStream;
import java.io.IOException;

public class InputStreamTest {
    final static int LINE_WRAP = 20;

    public static void main( String[] args ) throws IOException {
        int b;
        int count = 0;
        while ( (b=System.in.read()) != -1 ) {
            System.out.print( b + " " );
            count++;
            if ( count > LINE_WRAP ) {
                System.out.println();
                count = 0;
            }
        }
        System.out.println();
    }

}
