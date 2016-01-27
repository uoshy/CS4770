import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class UpperStringConvert {
    public static void main( String[] args ) throws IOException {
        InputStreamReader isd = new InputStreamReader(System.in);
        BufferedReader rd = new BufferedReader( isd );
        String line = null;
        while( (line=rd.readLine()) != null ) {
            System.out.println( line.toUpperCase() );
        }
    }
}
