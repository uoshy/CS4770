import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

public class SumFile {
    public static void main( String[] args ) throws IOException {
        if( args.length != 1 ) {
            System.out.println("usage: java SumFile file");
            System.exit( 1 );
        }
        BufferedReader rd =
            new BufferedReader( new FileReader( args[0] ));
        int sum = 0;
        String line = null;
        while( (line=rd.readLine()) != null ) {
            sum += Integer.parseInt( line );
        }
        rd.close();
        System.out.println( sum );
    }
}
