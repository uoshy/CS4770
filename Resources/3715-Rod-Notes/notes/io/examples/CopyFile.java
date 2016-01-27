import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class CopyFile {
    public static void main( String[] args ) throws IOException {
        if( args.length != 2 ) {
            System.out.println("usage: java CopyFile infile outfile");
            System.exit( 1 );
        }
        BufferedReader rd =
            new BufferedReader( new FileReader( args[0] ));
        BufferedWriter wt =
            new BufferedWriter( new FileWriter( args[1] ));
        String line = null;
        while( (line=rd.readLine()) != null ) {
            wt.write( line );
            wt.newLine();
        }
        rd.close();
        wt.close();
    }
}
