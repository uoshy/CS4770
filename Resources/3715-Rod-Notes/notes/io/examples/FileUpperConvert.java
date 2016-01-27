import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

public class FileUpperConvert {
    public static void main( String[] args ) throws IOException {
        BufferedReader rd =
            new BufferedReader( new FileReader( args[0] ));
        String line = null;
        while( (line=rd.readLine()) != null ) {
            System.out.println( line.toUpperCase() );
        }
        rd.close();
    }
}
